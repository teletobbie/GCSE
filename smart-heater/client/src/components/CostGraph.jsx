import {
  ComposedChart,
  Line,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  Label
} from "recharts";
import '../pages/costs.css';

export default function CostGraph({ data, toggleStackID }) {

  const stackIdBars = toggleStackID
  ? ["gasUsage", "gasUsage", "gasUsage", "gasUsage"]
  : ["unstack1", "unstack2", "unstack3", "unstack4"];

  const isStackedChart = stackIdBars.every((stackId) => stackId === 'gasUsage');

  const gasUsageValues = data.map((item) => item.gasUsage);
  const maxGasUsage = Math.ceil(Math.max(...gasUsageValues) * 1.2);
  const yAxisDomain = isStackedChart ? [0, maxGasUsage] : undefined;

  return (
    <ResponsiveContainer className="background-color-light-blue" width="100%" height={600}>
      <ComposedChart
          data={data}
          margin={{
            top: 50,
            right: 70,
            bottom: 20,
            left: 70
          }}
        >
        <CartesianGrid stroke="#E5F3FF" />
        <XAxis dataKey="date" type="category"/>
        <YAxis yAxisId="costs" dataKey='costs' orientation="left" type="number">
          <Label value={"Gas costs (€)"} angle={-90} position="insideLeft"/>
        </YAxis>
        <YAxis yAxisId="usage" datakey='gasUsage' domain={yAxisDomain} orientation="right" type="number">
          <Label value={"Gas usage (m3)"} angle={-270} position="insideRight"/>
        </YAxis>
        <Tooltip content={<CustomTooltip />} contentStyle={{backgroundColor:"#FFFFFF", color: "#000000"}}  itemStyle={{color: ""}} cursor={{strokeDasharray:"4 4", stroke: "#0069B4"}}/>
        <Legend />
        <Bar name="Standard" yAxisId="usage" dataKey="gasUsageStd" stackId={stackIdBars[0]} barSize={50} fill="#0069B4" />
        <Bar name="Standard Plus" yAxisId="usage" dataKey="gasUsageStdPlus" stackId={stackIdBars[1]} barSize={50} fill="#005999" />
        <Bar name="Deluxe" yAxisId="usage" dataKey="gasUsageDeluxe" stackId={stackIdBars[2]} barSize={50} fill="#1F355E" />
        <Bar name="Super Deluxe" yAxisId="usage" dataKey="gasUsageSuperDeluxe" stackId={stackIdBars[3]} barSize={50} fill="#000" />
        <Line name="Costs" yAxisId="costs" type="monotone" strokeWidth={3} dataKey="costs" stroke="#0069B4" activeDot={{ r: 10}}/>
      </ComposedChart>
    </ResponsiveContainer>
  );
}

function CustomTooltip({active, payload, label}) {
  if (active) {
    return (
      <div className="custom-tooltip">
          <p className="tooltip-label">{label}</p>
          <div>
              <div style={{ display: "inline-block", padding: 10 }}>
                <div className="tooltip-divs">
                  <div>Standard: </div>
                  <div className="tooltip-value">{parseFloat(payload[0].value).toFixed(2)} m<sup>3</sup></div>
                </div>
                <div className="tooltip-divs">
                  <div>Standard Plus: </div>
                  <div className="tooltip-value">{parseFloat(payload[1].value).toFixed(2)} m<sup>3</sup></div>
                </div>
                <div className="tooltip-divs">
                  <div>Deluxe: </div>
                  <div className="tooltip-value">{parseFloat(payload[2].value).toFixed(2)} m<sup>3</sup></div>
                </div>
                <div className="tooltip-divs">
                  <div>Super Deluxe: </div>
                  <div className="tooltip-value">{parseFloat(payload[3].value).toFixed(2)} m<sup>3</sup></div>
                </div>                         
                <div className="tooltip-divs">
                  <div>Total Costs: </div>
                  <div className="tooltip-value">€{parseFloat(payload[4].value).toFixed(2)},-</div>
                </div>                            
              </div>
          </div>
      </div>
    );
  };
  return null;
}