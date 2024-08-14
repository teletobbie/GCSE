package com.teama.server.services;

import com.teama.server.dto.daterange.DateRangeRequest;
import com.teama.server.dto.daterange.DateRangeResponse;
import com.teama.server.enums.BungalowType;
import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.models.Bungalow;
import com.teama.server.models.HeaterData;
import com.teama.server.repositories.BungalowRepository;
import com.teama.server.repositories.HeaterDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeaterDataService {
    @Autowired
    private HeaterDataRepository heaterDataRepository;
    @Autowired
    private BungalowRepository bungalowRepository;

    public HeaterData createHeaterDataEntry(HeaterData heaterData, Long bungalowId) throws EntityNotFoundException {
        Optional<Bungalow> bungalow = bungalowRepository.findById(bungalowId);
        if (bungalow.isEmpty()) throw new EntityNotFoundException(Bungalow.class.getName(), bungalowId);

        heaterData.setBungalow(bungalow.get());

        return heaterDataRepository.save(heaterData);
    }

    public List<HeaterData> getAllHeaterData() {
        return heaterDataRepository.findAll();
    }

    public HeaterData getHeaterDataById(Long id) throws EntityNotFoundException {
        Optional<HeaterData> heaterData = heaterDataRepository.findById(id);
        if (heaterData.isEmpty()) throw new EntityNotFoundException(HeaterData.class.getName(), id);

        return heaterData.get();
    }

    public List<DateRangeResponse> getGasUsageByDateRange(DateRangeRequest dateRange) {
        List<DateRangeResponse> response = new ArrayList<>();
        List<HeaterData> heaterDataBetweenDates = new ArrayList<>();
        List<LocalDate> datesInBetween = dateRange.getStartDate().toLocalDate().datesUntil(dateRange.getEndDate().toLocalDate().plusDays(1)).toList();

        for (Bungalow bungalow : bungalowRepository.findAll()) {
            addLastEntryBeforeStartDateToList(dateRange, heaterDataBetweenDates, bungalow); // Needed for calculating the gas usage increments
            appendEODHeaterDataInRange(datesInBetween, heaterDataBetweenDates, bungalow);
            addGasUsageIncrementToList(heaterDataBetweenDates, response, bungalow);
            setMissingGasUsagesToZero(datesInBetween, response);
        }

        Map<LocalDate, DateRangeResponse> map = new HashMap<>();

        for (DateRangeResponse entry : response) {
            if (map.containsKey(entry.getDate())) {
                DateRangeResponse existingEntry = map.get(entry.getDate());
                existingEntry.setGasUsageStd(existingEntry.getGasUsageStd() + entry.getGasUsageStd());
                existingEntry.setGasUsageStdPlus(existingEntry.getGasUsageStdPlus() + entry.getGasUsageStdPlus());
                existingEntry.setGasUsageDeluxe(existingEntry.getGasUsageDeluxe() + entry.getGasUsageDeluxe());
                existingEntry.setGasUsageSuperDeluxe(existingEntry.getGasUsageSuperDeluxe() + entry.getGasUsageSuperDeluxe());
            } else {
                map.put(entry.getDate(), entry);
            }
        }

        return map.values().stream().sorted(Comparator.comparing(DateRangeResponse::getDate)).toList();
    }

    private void setMissingGasUsagesToZero(List<LocalDate> datesInBetween, List<DateRangeResponse> response) {
        // Set gasUsage to 0 for all other days within the daterange.
        for (LocalDate localDate : datesInBetween) {
            if(response.stream().noneMatch(resp -> resp.getDate().isEqual(localDate))) {
                response.add(new DateRangeResponse(localDate, 0, 0, 0, 0));
            }
        }
    }

    private void addGasUsageIncrementToList(List<HeaterData> heaterDataBetweenDates, List<DateRangeResponse> response, Bungalow bungalow) {
        List<HeaterData> heaterDataOfBungalow = heaterDataBetweenDates.stream().filter(hd -> Objects.equals(hd.getBungalow().getId(), bungalow.getId())).toList();
        // Calculate gasUsage for each day within the list created in the previous for loop.
        BungalowType bungalowType = bungalow.getType();
        
        for (int i = 1; i < heaterDataOfBungalow.size(); i++) {
            HeaterData heaterDataLastDay = heaterDataOfBungalow.get(i-1);
            HeaterData heaterDataCurrentDay = heaterDataOfBungalow.get(i);
            switch (bungalowType) {
                case STANDARD -> response.add(new DateRangeResponse(heaterDataCurrentDay.getDateGenerated().toLocalDate(),
                                getGasUsageDifference(heaterDataCurrentDay, heaterDataLastDay),
                                0, 0, 0));
                case STANDARD_PLUS -> response.add(new DateRangeResponse(heaterDataCurrentDay.getDateGenerated().toLocalDate(),
                                0, getGasUsageDifference(heaterDataCurrentDay, heaterDataLastDay),
                                0, 0));
                case DELUXE -> response.add(new DateRangeResponse(heaterDataCurrentDay.getDateGenerated().toLocalDate(),
                        0, 0, getGasUsageDifference(heaterDataCurrentDay, heaterDataLastDay),
                        0));
                case SUPER_DELUXE -> response.add(new DateRangeResponse(heaterDataCurrentDay.getDateGenerated().toLocalDate(),
                                0, 0, 0,
                                getGasUsageDifference(heaterDataCurrentDay, heaterDataLastDay)));
            }
        }
    }

    private double getGasUsageDifference(HeaterData heaterDataCurrentDay, HeaterData heaterDataLastDay) {
        double gasUsageDifference;
        if (heaterDataCurrentDay.getGasUsage() < heaterDataLastDay.getGasUsage()) {
            //simulator has restarted, setting the gas usage back to zero, therefore, there is no difference to calculate
            gasUsageDifference = heaterDataCurrentDay.getGasUsage();
        } else {
            // else calculate the difference like normal
            gasUsageDifference = heaterDataCurrentDay.getGasUsage() - heaterDataLastDay.getGasUsage();
        }
        return gasUsageDifference;
    }

    private void appendEODHeaterDataInRange(List<LocalDate> datesInBetween, List<HeaterData> heaterDataBetweenDates, Bungalow bungalow) {
        // Obtain all EndOfDay database entries WITHIN the daterange and add to list.
        List<HeaterData> heaterData = heaterDataRepository.findAll().stream().filter(hd -> Objects.equals(hd.getBungalow().getId(), bungalow.getId())).toList();
        for (LocalDate date : datesInBetween) {
            List<HeaterData> heaterDataPerDate = heaterData
                    .stream().filter(hd ->  hd.getDateGenerated().toLocalDate().isEqual(date))
                    .sorted(Comparator.comparing(HeaterData::getDateGenerated).reversed()).toList();
            if (!heaterDataPerDate.isEmpty()) {
                HeaterData lastHeaterDataEntry = heaterDataPerDate.get(0);
                heaterDataBetweenDates.add(lastHeaterDataEntry);
            }
        }
    }

    private void addLastEntryBeforeStartDateToList(DateRangeRequest dateRange, List<HeaterData> heaterDataBetweenDates, Bungalow bungalow) {
        // Obtain last database entry BEFORE the startdate of the daterange, which is necessary for calculating the gasUsage increments.
        LocalDate dateToCheck = dateRange.getStartDate().toLocalDate();
        List<HeaterData> heaterDataBeforeDate = heaterDataRepository.findAll()
                .stream().filter(hd -> Objects.equals(hd.getBungalow().getId(), bungalow.getId()) && hd.getDateGenerated().toLocalDate().isBefore(dateToCheck))
                .sorted(Comparator.comparing(HeaterData::getDateGenerated).reversed()).toList();

        if (!heaterDataBeforeDate.isEmpty()) {
            heaterDataBetweenDates.add(heaterDataBeforeDate.get(0));
        } else {
            HeaterData syntheticData = new HeaterData(0, 0, 0, true, dateRange.getStartDate().minusDays(1), 0, dateRange.getStartDate().minusDays(1));
            syntheticData.setBungalow(bungalow);
            heaterDataBetweenDates.add(syntheticData);
        }
    }
}