import { useState, useEffect } from "react";
import BootstrapSwitchButton from 'bootstrap-switch-button-react'
import Menu from '../components/Menu';
import { Stack, Container, Row, Col, Spinner } from "react-bootstrap";
import './costs.css';
import CostGraph from "../components/CostGraph";
import { calculateMonthlySummaries, determineStartDate, fetchCosts, getTotalCostsAndUsage } from "../utils/costsUtils";

export default function Costs() {
  const [toggleYearOrWeek, setToggleYearOrWeek] = useState(true); //week is true, year is false
  const [toggleStackID, setToggleStackID] = useState(true); //Stacked is true
  const [heaterDatas, setHeaterDatas] = useState([]);
  const [simulatedDate, setSimulatedDate] = useState(new Date());
  const [startDate, setStartDate] = useState(new Date());
  const [dataSlice, setDataSlice] = useState([]);
  const [total, setTotal] = useState([0,0]);

  useEffect(() => {
    determineStartDate(setSimulatedDate, setStartDate);
  }, []);

  useEffect(() => {
    fetchCosts(startDate, simulatedDate, setHeaterDatas);
  }, [startDate])

  useEffect(() => {
    setDataSlice(toggleYearOrWeek ? heaterDatas.slice(-7) : heaterDatas);
  }, [toggleYearOrWeek, heaterDatas]);

  useEffect(() => {
    getTotalCostsAndUsage(dataSlice, setTotal);
  }, [dataSlice]);

  return (
    <div>
      <Menu />
      {heaterDatas.length > 1 ? 
        <div>
          <Container className="info-cost-container">
            <Row>
                <Col className="total-cost-row background-color-light-blue" md={3}>
                  <Stack direction="horizontal" gap={3} >
                    <div className="p-2 text-end"><b>Total Gas Costs: </b>€{total[0]},-</div>
                    <div className="p-2 text-end"><b>Total Gas Usage: </b>{total[1]} m<sup>3</sup></div>
                  </Stack>
                </Col>

                <Col className = "d-flex flex-row align-items-end justify-content-end" >
                  <div style={{marginRight: '10px'}}>
                  <BootstrapSwitchButton
                    checked={toggleYearOrWeek}
                    onlabel="Week"
                    onstyle="primary"
                    offlabel="Year"
                    offstyle="primary"
                    onChange={() => { setToggleYearOrWeek(!toggleYearOrWeek) }}
                    width={110}
                    style='small-switch-button'
                  />
                  </div>
                  <BootstrapSwitchButton
                    checked={toggleStackID}
                    onlabel="Stacked"
                    onstyle="primary"
                    offlabel="Unstacked"
                    offstyle="primary"
                    onChange={() => { setToggleStackID(!toggleStackID) }}
                    width={110}
                    style='small-switch-button'
                  />
                </Col>
            </Row>
            </Container>
            <CostGraph data={toggleYearOrWeek ? dataSlice : calculateMonthlySummaries(dataSlice)} toggleStackID={toggleStackID}/>
            <div className="outer-container">
              <div className="gas-price-div">
                <p className="gas-price">Gas price per m<sup>3</sup></p>
                <p>€1.50,-</p>
              </div> 
            </div>            
          </div>
        : 
        <Container className="spinner-container">
          <Row>
          <Col className = "d-flex align-items-center justify-content-center">
            <Spinner className="spinner" animation="border" />
          </Col>
          </Row>
          <Row >
            <Col className = "d-flex align-items-center justify-content-center"><b className="loading-text">Loading</b></Col>
          </Row>
        </Container>
        }
      
    </div>
  );
}


