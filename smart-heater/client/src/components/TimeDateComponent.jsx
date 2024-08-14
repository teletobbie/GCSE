import { useState, useEffect } from 'react';
import axios from 'axios';

export default function TimeDateComponent({temperatureRequest}) {
    const [date, setDate] = useState(new Date());
    const [heaterDate, setHeaterDate] = useState("");
    const [temperature, setTemperature] = useState([]);

    useEffect(() => {
      const getHeaterData = async () => {
        try {
          const response = await axios.get('http://localhost:8080/heaters');
          const dataObject = response.data;
          const newValue = dataObject[0].insideTemperature.toFixed(1);
          const newDate = dataObject[0].dateGenerated;
          setTemperature(newValue);
          setHeaterDate(newDate);
        } catch (error) {
          console.log(error);
        }
      };
      getHeaterData();
  
      const interval = setInterval(getHeaterData, 1000);
      return () => {
        clearInterval(interval);
      };
    }, []);

    useEffect(() => {
        setInterval(() => setDate(new Date()), 30000);
    }, []);

    const parsedDate = new Date(heaterDate);

  return (
    <div className='container py-2'>
          <div className="row">
          <div className="col-6 col-md-5 col-lg-6 text-end"></div>
          <div className="col-6 col-md-5 col-lg-6 text-end fw-bold">
          <i className="fa-solid fa-clock pe-1"></i>
    
            {parsedDate.toLocaleTimeString('en-GB', {
                hour: 'numeric',
                minute: 'numeric',
                hour12: true
            })}
          </div>
          <div className="col-6 col-md-5 col-lg-6 text-end"></div>
          <div className="col-6 col-md-5 col-lg-6 text-end py-1 fw-bold">
          <i className="fa-solid fa-calendar pe-1"></i>
            {parsedDate.toLocaleDateString('en-GB', {
                day: 'numeric',
                month: 'short',
                year: 'numeric'
            })}
          </div>

          <div className="col-6 col-md-5 col-lg-6 text-end"></div>
          <div className="col-6 col-md-5 col-lg-6 text-end py-1">
          {temperature&&(
            temperatureRequest.temperature >= temperature ?  <h4><i className="fa-solid fa-fire fa-beat pe-2 color-orange"></i>
            {temperature} °C</h4> : <h4><i className="fa-solid fa-snowflake fa-beat pe-2 color-blue"></i>
            {temperature} °C</h4>
          )}
         
          </div>
          </div>
    </div>
  )
}
