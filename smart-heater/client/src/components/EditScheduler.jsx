import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import { CircleSlider } from "react-circle-slider";


export default function EditScheduler({temperatureRequest, tenantData}) {
  const [show, setShow] = useState(false);
  const [id, setId] = useState(0);
  const [formData, setFormData] = useState({
    startTime: "",
    temperature: temperatureRequest.temperature,
    bungalow: {
      id: 0
  }
  });

  
  useEffect(() => {
    if(tenantData && tenantData.bungalow && tenantData.bungalow.id) {
      setId(tenantData.bungalow.id);
    }
  }, [tenantData]);

  useEffect(() => {
    setFormData({
      startTime: "",
      temperature: temperatureRequest.temperature,
      bungalow: {
        id: id
      }
    });
  }, [id, temperatureRequest.temperature]);


  const editScheduler = async () => {
    try {
        const response = await axios.post('http://localhost:8080/schedules', formData);
        console.log(response);
        handleClose();
        window.location.reload();
    } catch (error) {
        console.log(error)
    }
  }

  const handleClose = () => {
    setShow(false);
    setFormData({
      startTime: "",
      temperature: temperatureRequest.temperature,
      bungalow: {
        id: id
    }
    });
  }

  const handleShow = () => setShow(true);

  const handleInputChange = (event, input) => {
    const { value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [input]: value,
    }));
  };
  

  return (
    <>
      <i
        className="fa-solid fa-calendar-days"
        style={{ cursor: "pointer" }}
        onClick={handleShow}
      ></i>
      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="lg"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title className="text-center">
            Set the schedule to change temperature
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="container text-center py-4">
            <div className="slider-container py-2">
                <CircleSlider
                  value={formData.temperature}
                  size={220}
                  stepSize={0.5}
                  knobRadius={12}
                  progressWidth={20}
                  circleWidth={7}
                  progressColor={formData.temperature <= 18 ? "#0069B4" : "#FB7800"}
                  knobColor="#FFFFFF"
                  shadow={true}
                  min={15}
                  max={23.5}
                  onChange={(newValue) => {
                    const event = {
                      target: {
                        value: newValue
                      }
                    };
                    handleInputChange(event, "temperature");
                  }}
                />
                <div className="slider-value">{formData.temperature}°C</div>
              </div>
          </div>
          <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Schedule Date</Form.Label>
              <Form.Control
                type="datetime-local"
                name="startTime"
                placeholder="Schedule date"
                value={formData.startTime}
                onChange={(event) => handleInputChange(event, "startTime")}
                autoFocus
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <div className="text-center">
          <Modal.Footer>
            <Button variant="primary" onClick={editScheduler}>
              Submit <i className="fa-solid fa-arrow-right"></i>
            </Button>
          </Modal.Footer>
        </div>
      </Modal>
    </>
  );
}
