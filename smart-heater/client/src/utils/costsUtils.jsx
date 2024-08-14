import axios from "axios";

export const fetchCosts = async (startDate, simulatedDate, setHeaterDatas) => {
    const response = await axios.post("http://localhost:8080/heater_datas/range", {
        "startDate": startDate.toISOString().replace('Z', ''), "endDate": simulatedDate.toISOString().replace('Z', '')}
    );
    setHeaterDatas(
        response.data.map(heaterdata => ({
        ...heaterdata,
        gasUsage: heaterdata.gasUsageStd + heaterdata.gasUsageStdPlus + heaterdata.gasUsageDeluxe + heaterdata.gasUsageSuperDeluxe,
        costs: (heaterdata.gasUsageStd + heaterdata.gasUsageStdPlus + heaterdata.gasUsageDeluxe + heaterdata.gasUsageSuperDeluxe) * 1.50
        }))
    );
}

export const determineStartDate = async (setSimulatedDate, setStartDate) => {
    try {
        const response = await axios.get("http://localhost:8080/heaters");
        const newSimulatedDate = new Date(response.data[0].dateGenerated);
        setSimulatedDate(newSimulatedDate);

        const startDate = new Date(newSimulatedDate);
        startDate.setDate(newSimulatedDate.getDate() - 364);
        setStartDate(startDate);
    }
    catch (error) {
        console.log(error);
    }
}

export const calculateMonthlySummaries = (heaterDatas) => {
    const monthNames = [
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    ];

    const monthlySums = monthNames.map(monthName => ({
        date: monthName,
        gasUsageStd: 0,
        gasUsageStdPlus: 0,
        gasUsageDeluxe: 0,
        gasUsageSuperDeluxe: 0,
        costs: 0
    }));

    heaterDatas.forEach(data => {
        const month = new Date(data.date).getMonth();
        monthlySums[month].gasUsageStd += data.gasUsageStd;
        monthlySums[month].gasUsageStdPlus += data.gasUsageStdPlus;
        monthlySums[month].gasUsageDeluxe += data.gasUsageDeluxe;
        monthlySums[month].gasUsageSuperDeluxe += data.gasUsageSuperDeluxe;
        monthlySums[month].costs += data.costs;
    });

    const currentMonth = new Date(heaterDatas[heaterDatas.length -1].date).getMonth();
    const startingIndex = (currentMonth + 1) % 12;
    const reorderedSums = monthlySums.slice(startingIndex).concat(monthlySums.slice(0, startingIndex));

    const roundedSums = reorderedSums.map(summary => ({
        ...summary,
        costs: parseFloat(summary.costs.toFixed(2)),
        gasUsage: parseFloat(summary.gasUsageStd + summary.gasUsageStdPlus + summary.gasUsageDeluxe + summary.gasUsageSuperDeluxe).toFixed(0),
        gasUsageStd: parseFloat(summary.gasUsageStd.toFixed(2)),
        gasUsageStdPlus: parseFloat(summary.gasUsageStdPlus.toFixed(2)),
        gasUsageDeluxe: parseFloat(summary.gasUsageDeluxe.toFixed(2)),
        gasUsageSuperDeluxe: parseFloat(summary.gasUsageSuperDeluxe.toFixed(2))
    }));

    return roundedSums;
}

export const getTotalCostsAndUsage = (heaterDatas, setTotal) => {
    let totalCosts = 0;
    let totalUsage = 0;
    heaterDatas.forEach(data => {
      totalCosts += data.costs;
      totalUsage += data.gasUsage;
    });
    setTotal([totalCosts.toFixed(2), totalUsage.toFixed(2)]);
}

export const CustomTooltip = ({ active, payload, label }) => {

    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip">
          <p className="label">Date: {label}</p>
          <div>
            {payload.map((pld) => (
              <div style={{ display: "inline-block", padding: 10 }}>
                <div>{pld.dataKey}</div>
                <div>{pld.value}</div>
              </div>
            ))}
          </div>
        </div>
      );
    }
  
    return null;
  };
