import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import axios from 'axios';


export default function BungalowSchedules({bungalow}) {
  const [show, setShow] = useState(false);
  const [schedules, setSchedules] = useState([]);
  const [userName, setUserName] = useState([]);

  const fetchSchedules = async () => {
    try {
      const response = await axios.get('http://localhost:8080/schedules/bungalow/'+bungalow.id);
      setSchedules(response.data);
      const dataObj = response.data;
      setUserName(dataObj[0].bungalow.user);
    } catch(error) {
      console.log(error);
    }
  }

  const deleteSchedule = async (id) => {
    try {
      const response = await axios.delete('http://localhost:8080/schedules/'+id);
      console.log(response.data);
      setSchedules(response.data);
    } catch (error) {
      console.log(error);
    }
  }

  const handleClose = () => {
    setShow(false);
  }

  const handleShow = () => {
      fetchSchedules();
      setShow(true);
  }

  return (
    <>
      <Button className="controlButton mx-1" onClick={handleShow}>Schedules</Button>

      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="lg"
        centered
      >
        <Modal.Header closeButton>
            <Modal.Title>Showing Schedules for {userName.name}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            {schedules.map((schedule) => {
              const parsedDate = new Date(schedule.startTime);
                return( 
                <ul key={schedule.id} >
                    Scheduled at {`${parsedDate.toLocaleDateString('en-GB')} ${parsedDate.toLocaleTimeString('en-GB')}`} to set the target temperature to {schedule.temperature} °C
                    <i className="fa-regular fa-trash-can mx-2" style={{cursor: "pointer"}} onClick={() => deleteSchedule(schedule.id)}></i>
                </ul>)
            })}
        </Modal.Body>
      </Modal>
    </>
  );
}