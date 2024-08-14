import { Button } from "react-bootstrap"
import { useState } from "react";
import axios from 'axios';

export default function AdminSetTemperature({bungalow}) {
    const [temperatureRequest, setTemperatureRequest] = useState({temperature: 20.0});
    
    const handleInput = (event) => setTemperatureRequest({temperature: event.target.value});

    const updateTargetTemperature = async () => {
        try {
            console.log(temperatureRequest);
            const response = await axios.patch('http://localhost:8080/bungalows/temp/'+bungalow.id, temperatureRequest);
            console.log(response);
        } catch (error) {
            console.log(error);
        }
    }
    
    return (
        <>
            <input
                className="tempInput mx-1"
                type="number"
                step="0.5"
                defaultValue={20}
                onChange={handleInput}
                min="15"
                max="23.5"
                />
                <Button 
                    className="controlButton mx-1"
                    onClick={updateTargetTemperature}
                >
                Set Temperature
                </Button>
        </>
    )
}