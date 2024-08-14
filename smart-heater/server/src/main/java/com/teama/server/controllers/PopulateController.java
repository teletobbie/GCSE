package com.teama.server.controllers;

import com.teama.server.enums.BungalowType;
import com.teama.server.enums.Role;
import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.exceptions.TenantAlreadyHasBungalowException;
import com.teama.server.models.Bungalow;
import com.teama.server.models.HeaterData;
import com.teama.server.models.User;
import com.teama.server.services.BungalowService;
import com.teama.server.services.HeaterDataService;
import com.teama.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.random.RandomGenerator;

@RestController
@RequestMapping("/populate")
public class PopulateController {
    @Autowired
    private UserService userService;

    @Autowired
    private BungalowService bungalowService;

    @Autowired
    private HeaterDataService heaterDataService;

    @PostMapping
    public void populate() {
        populateUsers();
        populateBungalows();
        try {
            assignUsersToBungalows();
        } catch (TenantAlreadyHasBungalowException ignore) { }
        try {
            populateHeaterData();
        } catch (EntityNotFoundException ignore) { }
    }

    private void populateUsers() {
        // Owner
        userService.createUser(new User(null, "jasper.schouten@cgi.com", "Jasper Schouten", Role.OWNER, "1234"));
        // Admins
        userService.createUser(new User(null, "martijn.vast@cgi.com", "Martijn Vast", Role.ADMIN, "1234"));
        userService.createUser(new User(null, "richard.stuit@cgi.com", "Richard Stuit", Role.ADMIN, "1234"));
        userService.createUser(new User(null, "irina.kulikova@cgi.com", "Irina Kulikova", Role.ADMIN, "1234"));
        userService.createUser(new User(null, "tobias.schiphorst@cgi.com", "Tobias Schiphorst", Role.ADMIN, "1234"));
        // Standard Tenants (ln 55)
        userService.createUser(new User(null, "leila.albrecht@cgi.com", "Leila Albrecht", Role.TENANT, "1234"));
        userService.createUser(new User(null, "gerard.heuvelman@cgi.com", "Gerard Heuvelman", Role.TENANT, "1234"));
        userService.createUser(new User(null, "nick.arema@cgi.com", "Nick Arema", Role.TENANT, "1234"));
        userService.createUser(new User(null, "koen.vander.marel@cgi.com", "Koen van der Marel", Role.TENANT, "1234"));
        userService.createUser(new User(null, "davidalexandre.baptista@cgi.com", "David Alexandre Baptista", Role.TENANT, "1234"));
        userService.createUser(new User(null, "edwin.rodriguezvalle@cgi.com", "Edwin Rodriguez Valle", Role.TENANT, "1234"));
        userService.createUser(new User(null, "ruben.sinnige@cgi.com", "Ruben Sinnige", Role.TENANT, "1234"));
        userService.createUser(new User(null, "jeremy.trotereau@cgi.com", "Jeremy Trotereau", Role.TENANT, "1234"));
        userService.createUser(new User(null, "charissa.oudejans@cgi.com", "Charissa Oudejans", Role.TENANT, "1234"));
        userService.createUser(new User(null, "raphael.ashruf@cgi.com", "Raphaël Ashruf", Role.TENANT, "1234"));
        userService.createUser(new User(null, "shelby.hendrickx@cgi.com", "Shelby Hendrickx", Role.TENANT, "1234"));
        userService.createUser(new User(null, "beatriz.garciadequevedosuero@cgi.com", "Beatriz Garcia de Quevedo Suero", Role.TENANT, "1234"));
        // Plus Tenants (ln 68)
        userService.createUser(new User(null, "bas.kranenbarg@cgi.com", "Bas Kranenbarg", Role.TENANT, "1234"));
        userService.createUser(new User(null, "remco.verweij@cgi.com", "Remco Verweij", Role.TENANT, "1234"));
        userService.createUser(new User(null, "paulina.swiatek@cgi.com", "Paulina Świątek", Role.TENANT, "1234"));
        userService.createUser(new User(null, "waleed.akhtar@cgi.com", "Waleed Akhtar", Role.TENANT, "1234"));
        userService.createUser(new User(null, "emma.purcell@cgi.com", "Emma Purcell", Role.TENANT, "1234"));
        // Deluxe Tenants (ln 74)
        userService.createUser(new User(null, "mourad.nasser@cgi.com", "Mourad Nasser", Role.TENANT, "1234"));
        userService.createUser(new User(null, "thomas.buck@cgi.com", "Thomas Buck", Role.TENANT, "1234"));
        userService.createUser(new User(null, "konstantinos.malamas@cgi.com", "Konstantinos Malamas", Role.TENANT, "1234"));
        userService.createUser(new User(null, "natan.trosman@cgi.com", "Natan Trosman", Role.TENANT, "1234"));
        // Super Tenants (ln 79)
        userService.createUser(new User(null, "antonio.bento@cgi.com", "António Bento", Role.TENANT, "1234"));
        userService.createUser(new User(null, "cipo.impellizzeri@cgi.com", "Cipo Impellizzeri", Role.TENANT, "1234"));
    }

    private void populateBungalows() {
        bungalowService.createBungalow(new Bungalow(null, "Std101", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std102", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std103", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std104", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std105", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std202", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std203", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std204", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std205", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std206", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std207", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "Std208", BungalowType.STANDARD));

        bungalowService.createBungalow(new Bungalow(null, "Plus201", BungalowType.STANDARD_PLUS));
        bungalowService.createBungalow(new Bungalow(null, "Plus106", BungalowType.STANDARD_PLUS));
        bungalowService.createBungalow(new Bungalow(null, "Plus106", BungalowType.STANDARD_PLUS));
        bungalowService.createBungalow(new Bungalow(null, "Plus106", BungalowType.STANDARD_PLUS));
        bungalowService.createBungalow(new Bungalow(null, "Plus106", BungalowType.STANDARD_PLUS));

        bungalowService.createBungalow(new Bungalow(null, "Deluxe108", BungalowType.DELUXE));
        bungalowService.createBungalow(new Bungalow(null, "Deluxe109", BungalowType.DELUXE));
        bungalowService.createBungalow(new Bungalow(null, "Deluxe110", BungalowType.DELUXE));
        bungalowService.createBungalow(new Bungalow(null, "Deluxe111", BungalowType.DELUXE));

        bungalowService.createBungalow(new Bungalow(null, "Super107", BungalowType.SUPER_DELUXE));
        bungalowService.createBungalow(new Bungalow(null, "Super209", BungalowType.SUPER_DELUXE));

        // Extra Bungalows without Tenants
        bungalowService.createBungalow(new Bungalow(null, "No Tenant", BungalowType.STANDARD));
        bungalowService.createBungalow(new Bungalow(null, "No Heater", BungalowType.STANDARD));
    }

    private void assignUsersToBungalows() throws TenantAlreadyHasBungalowException {
        bungalowService.assignUser(1L,6L);
        bungalowService.assignUser(2L,7L);
        bungalowService.assignUser(3L,8L);
        bungalowService.assignUser(4L,9L);
        bungalowService.assignUser(5L,10L);
        bungalowService.assignUser(6L,11L);
        bungalowService.assignUser(7L,12L);
        bungalowService.assignUser(8L,13L);
        bungalowService.assignUser(9L,14L);
        bungalowService.assignUser(10L,15L);
        bungalowService.assignUser(11L,16L);
        bungalowService.assignUser(12L,17L);
        bungalowService.assignUser(13L,18L);
        bungalowService.assignUser(14L,19L);
        bungalowService.assignUser(15L,20L);
        bungalowService.assignUser(16L,21L);
        bungalowService.assignUser(17L,22L);
        bungalowService.assignUser(18L,23L);
        bungalowService.assignUser(19L,24L);
        bungalowService.assignUser(20L,25L);
        bungalowService.assignUser(21L,26L);
        bungalowService.assignUser(22L,27L);
        bungalowService.assignUser(23L,28L);
        bungalowService.assignUser(24L,29L);
        bungalowService.assignUser(25L,30L);
    }

    private void populateHeaterData() throws EntityNotFoundException {
        Random random = new Random();
        LocalDateTime time = LocalDateTime.of(2022, 1, 1, 0, 0);
        float stdGas = 0f;
        float plsGas = 0f;
        float delGas = 0f;
        float sprGas = 0f;

        // December 2022
        float seasonMulti = 1.7f;
        int month = 11;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time, stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time, plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time, delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time, sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);

        // January 2023
        seasonMulti = 1.9f;
        month = 0;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // February 2023
        seasonMulti = 2f;
        month = 1;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // March 2023
        seasonMulti = 1.7f;
        month = 2;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // April 2023
        seasonMulti = 1.5f;
        month = 3;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // May 2023
        seasonMulti = 1.3f;
        month = 4;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // June 2023
        seasonMulti = 1f;
        month = 5;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // July 2023
        seasonMulti = 0.7f;
        month = 6;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // August 2023
        seasonMulti = 0.8f;
        month = 7;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // September 2023
        seasonMulti = 1.1f;
        month = 8;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // October 2023
        seasonMulti = 1.3f;
        month = 9;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
        // November 2023
        seasonMulti = 1.5f;
        month = 10;
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), stdGas += 1.0f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 1L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), plsGas += 0.8f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 13L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), delGas += 1.4f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 18L);
        heaterDataService.createHeaterDataEntry(new HeaterData(random.nextFloat(1.5f, 2f), random.nextFloat(10f, 13f) / seasonMulti, random.nextFloat(15f, 23.5f), random.nextBoolean(), time.plusYears(1), sprGas += 1.2f * random.nextFloat(7, 12) * seasonMulti, time.plusYears(1).plusMonths(month).plusDays(random.nextInt(0, 29)).plusHours(random.nextInt(9, 24)).plusMinutes(random.nextInt(0, 60))), 22L);
    }
}