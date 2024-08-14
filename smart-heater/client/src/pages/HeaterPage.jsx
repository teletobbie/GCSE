import { useState, useEffect } from 'react';
import Menu from '../components/Menu';
import UpdateTenantName from '../components/UpdateTenantName';
import TimeDateComponent from '../components/TimeDateComponent';
import EditScheduler from '../components/EditScheduler';
import useAuth from '../hooks/useAuth';
import axios from 'axios';
import { CircleSlider } from "react-circle-slider";
import BungalowSchedules from '../components/BungalowSchedules';

export default function HeaterPage() {
    const [tenant, setTenant] = useState([]);
    const [loggedInUser, setLoggedInUser] = useState({});
    const [bungalowId, setBungalowId] = useState(0);  
    const [temperatureRequest, setTemperatureRequest] = useState({temperature: 18.0});
    const [isDisabled, setIsDisabled] = useState(false);

    const auth = useAuth();

    useEffect(() => {
        const fetchAuthTenant = () => {
            if(auth.userDetails) {
                setLoggedInUser(auth.userDetails);
            }
        };
        fetchAuthTenant();
    }, [auth.userDetails]);

    useEffect(() => {
      const fetchTenant = async () => {
        if (loggedInUser.id) {
          try {
            const response = await axios.get(
              `http://localhost:8080/users/${loggedInUser.id}`
            );
            setTenant(response.data);
          } catch (error) {
            console.log(error);
          }
        }
      };

      fetchTenant();
    }, [loggedInUser.id]); 

    useEffect(() => {
      if(tenant && tenant.bungalow && tenant.bungalow.id) {
        setBungalowId(tenant.bungalow.id);
      }
      if(tenant && tenant.bungalow && tenant.bungalow.targetTemperature) {
        setTemperatureRequest({temperature: tenant.bungalow.targetTemperature})
      }
    }, [tenant]);

   

    const updateTargetTemperature = async (newTemperature) => {
      try {
          setTemperatureRequest({ temperature: newTemperature });
          const response = await axios.patch('http://localhost:8080/bungalows/temp/' + bungalowId, {
              temperature: newTemperature
          });
          setIsDisabled(true);

          const interval = setInterval(() => {
            setIsDisabled(false);
            clearInterval(interval);
        }, 5000);

      } catch (error) {
          console.log(error);
      }
  };
  
  return (
    <div>
      <Menu />

      <div className="container text-center py-2">
        <TimeDateComponent temperatureRequest={temperatureRequest} />
      </div>

      <div className="container text-center py-2">
        <h3 className='text-shadow fw-semibold'>
          Welcome, {tenant.name}! <UpdateTenantName tenantData={tenant} />
        </h3>
        <div className="container text-center py-4">
          <div className="slider-container py-2">
            <CircleSlider
              value={temperatureRequest.temperature}
              size={220}
              stepSize={0.5}
              knobRadius={12}
              shadow={true}
              progressWidth={20}
              circleWidth={7}
              knobColor="#FFFFFF"
              min={15}
              max={23.5}
              gradientColorFrom={temperatureRequest.temperature <= 18 ? "#43A4D4" : "#FFC600"}
              gradientColorTo={temperatureRequest.temperature <= 18 ? "#0069B4" : "#FB7800"}
              onChange={(newValue) => {updateTargetTemperature(newValue)}}
              disabled={isDisabled}
            />
            <div className="slider-value">{temperatureRequest.temperature}°C</div>
          </div>
        </div>
        <div>
         
        </div>
        <p className='py-2' style={{color: "darkgray"}}>Drag circle to set temperature</p>
        <h5 className="py-2">
          Set schedule <EditScheduler temperatureRequest={temperatureRequest} bungalowId={bungalowId} tenantData={tenant} />
        </h5>
        <BungalowSchedules className="me-2" bungalow={tenant.bungalow}/>
      </div>
    </div>
  );
}