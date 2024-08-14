import { useState, useEffect } from 'react';
import axios from 'axios';
import Menu from '../components/Menu';
import './bungalows.css';
import { Button, ToggleButton } from 'react-bootstrap';
import AssignTenant from '../components/AssignTenant';
import UnassignTenant from '../components/UnassignTenant';
import AdminSetTemperature from '../components/AdminSetTemperature';
import BungalowSchedules from '../components/BungalowSchedules';

export default function Bungalows() {
  const [bungalows, setBungalows] = useState([]);
  const [standard, setStandard] = useState(false);
  const [standardPlus, setStandardPlus] = useState(false);
  const [deluxe, setDeluxe] = useState(false);
  const [superDeluxe, setSuperDeluxe] = useState(false);
  const [filterValue, setFilterValue] = useState('');

  const fetchBungalows = async () => {
    try {
      const response = await axios.get('http://localhost:8080/bungalows');
      setBungalows(response.data);
    } catch(error) {
      console.log(error);
    }
  }

  useEffect(() => {
    fetchBungalows();
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      fetchBungalows();
    }, 1000)
    return () => clearInterval(interval)
  }, []);

  const filteredBungalows = bungalows.filter((bungalow) => {
    const searchValue = filterValue.toLowerCase();
    return bungalow.name.toLowerCase().includes(searchValue) && (
         bungalow.type === "STANDARD" && standard
      || bungalow.type === "STANDARD_PLUS" && standardPlus
      || bungalow.type === "DELUXE" && deluxe
      || bungalow.type === "SUPER_DELUXE" && superDeluxe
      || (!standard && !standardPlus && !deluxe && !superDeluxe));
  }).sort((a, b) => {
    if (a.name < b.name) return -1;
    if (a.name > b.name) return 1;
    return 0;
  });

  const handleFilterChange = (e) => {
    setFilterValue(e.target.value);
  };

  const renderBungalowType = (bungalowType) => {
    switch (bungalowType) {
      case "STANDARD":      return <i className="fa-solid fa-house-chimney fa-lg std-color"/>;
      case "STANDARD_PLUS": return <i className="fa-solid fa-house-chimney-medical fa-lg plus-color"/>;
      case "DELUXE":        return <i className="fa-solid fa-house-chimney-window fa-lg delux-color"/>;
      case "SUPER_DELUXE":  return <i className="fa-solid fa-house-chimney-user fa-lg super-color"/>;
      default: return "";
    }  
  }

/*   const parseDate = (value) => {
    return new Date(value);
  } */

  return (
    <div>
      <Menu />
      <div className="container-fluid justify-space-between">
        <div className="filter-panel-and-button">
          <div className="row">
              <div className="col">
                <div className="filter-panel-bungalows">
                  <input
                      type="text"
                      className="form-control blue-background"
                      value={filterValue}
                      onChange={handleFilterChange}
                      placeholder="Filter by bungalow name or occupant"
                      required=""
                  />
                </div>
              </div>
              <div className="col d-flex justify-content-end">
                <ToggleButton
                  id="standard"
                  className="filterButton mx-2"
                  type="checkbox"
                  checked={standard}
                  value="1"
                  onChange={(e) => setStandard(e.currentTarget.checked)}
                >
                  <div className="row justify-content-between">
                    <span className="col-8 text-start">
                      Standard
                    </span>
                    <span className="col-3 text-end">
                      {
                        bungalows.filter(b => b.type === "STANDARD").some(b => b.heater !== null && b.heater.insideTemperature > 20 && !b.isMovement) ?
                        <i className="fa-solid fa-triangle-exclamation text-warning fa-fade"/> :
                        <i className="fa-solid fa-triangle-exclamation"/>
                      }
                    </span>
                  </div>
                </ToggleButton>
                <ToggleButton
                  id="standardPlus"
                  className="filterButton mx-2"
                  type="checkbox"
                  checked={standardPlus}
                  value="1"
                  onChange={(e) => setStandardPlus(e.currentTarget.checked)}
                >
                  <div className="row justify-content-between">
                    <span className="col-8 text-start">
                      Standard Plus
                    </span>
                    <span className="col-3">
                      <i className="fa-solid fa-triangle-exclamation"/>
                    </span>
                  </div>
                </ToggleButton>
                <ToggleButton
                  id="deluxe"
                  className="filterButton mx-2"
                  type="checkbox"
                  checked={deluxe}
                  value="1"
                  onChange={(e) => setDeluxe(e.currentTarget.checked)}
                >
                  <div className="row justify-content-between">
                    <span className="col-8 text-start">
                      Deluxe
                    </span>
                    <span className="col-3">
                      <i className="fa-solid fa-triangle-exclamation"/>
                    </span>
                  </div>
                </ToggleButton>
                <ToggleButton
                  id="superDeluxe"
                  className="filterButton mx-2"
                  type="checkbox"
                  checked={superDeluxe}
                  value="1"
                  onChange={(e) => setSuperDeluxe(e.currentTarget.checked)}
                >
                  <div className="row justify-content-between">
                    <span className="col-8 text-start">
                      Super Deluxe
                    </span>
                    <span className="col-3">
                      <i className="fa-solid fa-triangle-exclamation"/>
                    </span>
                  </div>
                </ToggleButton>
              </div>
            </div>
          </div>

        <div className="bungalow-table">
          <table className="table table-bordered table-hover table-sm">
            <thead className="blue-background">
              <tr className="tr-size">
                <th style={{width: "50px"}}>Type</th>
                <th style={{width: "150px"}}>Name</th>
                <th style={{width: "250px"}}>Occupied by</th>
                <th style={{width: "300px"}}>Movement</th>
                <th style={{width: "100px"}}>Door</th>
                <th style={{width: "120px"}}>Temperature</th>
                <th style={{width: "120px"}}>Target</th>
                <th>Controls</th>
                <th style={{width: "50px"}}></th>{/* Icons */}
              </tr>
            </thead>
            <tbody>
              {filteredBungalows.map((bungalow) => (
                <tr className="tr-size" key={bungalow.id}>
                  <td className="text-center"> {/* Bungalow Type */}
                    {renderBungalowType(bungalow.type)}
                  </td>

                  <td> {/* Bungalow Name */}
                    {bungalow.name}
                  </td>

                  {bungalow.user !== null ? 
                    <td className="row-and-buttons"> {/* Occupied by some */}
                      <span className="text-start">
                        {bungalow.user.name}
                      </span>
                      <span>
                      <UnassignTenant bungalow={bungalow} />
                      </span>
                    </td>:
                    <td className="row-and-buttons"> {/* Occupied by none */}
                      <span className="text-start">
                        <i>No tenant</i>
                      </span>
                      <span>
                        <AssignTenant bungalow={bungalow} />
                      </span>
                    </td>
                  }
                  {bungalow.heater !== null ?
                  <>
                    <td> {/* Movement */}
                      {bungalow.heater.movementSensor.slice(11)} - {bungalow.isMovement ? "Recent Movement" : "No Movement"}
                    </td>

                    <td> {/* Door */}
                      {bungalow.heater.doorSensor ? "Open" : "Closed" }
                    </td>

                    <td> {/* Temperature */}
                      {bungalow.heater.insideTemperature} °C
                    </td>

                    { bungalow.user !== null ?
                    <>
                      <td> {/* Target */}
                        {bungalow.targetTemperature} °C
                      </td>

                      <td> {/* Controls */}
                        <AdminSetTemperature bungalow={bungalow} />
                        <BungalowSchedules bungalow={bungalow} />
                      </td>

                      <td> {/* Icons */}
                        <div className="text-center">
                        {bungalow.heater.insideTemperature > 20 && !bungalow.isMovement ? 
                          <i className="heater-alarm fa-solid fa-triangle-exclamation fa-lg text-warning fa-fade"/> :
                          <i className="heater-alarm fa-solid fa-triangle-exclamation fa-lg text-secondary"/>
                        }
                        </div>
                      </td>
                    </> : <>
                      <td>
                        None
                      </td>
                      <td>
                          This Bungalow does not have a tenant; The Heater is turned off.
                      </td>
                    </>
                    }
                  </>:
                  <>
                    <td>
                      No Heater Connection!
                    </td>
                  </>
                  }
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

