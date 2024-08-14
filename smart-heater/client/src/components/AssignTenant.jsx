import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';


export default function AssignTenant({bungalow}) {
  const [show, setShow] = useState(false);
  const [tenants, setTenants] = useState([]);
  const [formData, setFormData] = useState({
    id: 0,
    email: '',
    name: '',
    role: 'TENANT',
  });

  const fetchTenants = async () => {
    try {
        const response = await axios.get('http://localhost:8080/users');
        const data = response.data.filter(t => t.role === 'TENANT' && t.bungalow === null);
        console.log(data);
        setTenants(data);
      } catch (error) {
        console.log(error);
      }
  }

  const assignTenant = async () => {
    try {
      if (formData.id === 0) formData.id = tenants[0].id;
      const response = await axios.patch('http://localhost:8080/bungalows/assign/'+bungalow.id, formData);
      console.log(response);
      handleClose();
    } catch (error) {
      console.log(error);
    }
  }

  const handleClose = () => {
    setShow(false);
  }

  const handleShow = () => {
    fetchTenants();
    setShow(true);
  }

  const handleInputChange = (event) => {
    setFormData({id: event.target.value});
    console.log(event.target.value);
  }

  return (
    <>
      <div className="table-buttons"> 
        <i className="fa-solid fa-user-plus" onClick={handleShow}></i>
      </div>

      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="lg"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Select a Tenant to assign to {bungalow.name}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            {tenants.length > 0 ? 
            <>
              <Form.Select defaultValue={tenants[0].id} onChange={handleInputChange}>
                {tenants.map(t => (
                  <option key={t.id} value={t.id}>{t.name}</option>
                ))}
              </Form.Select>
            </> : 
            <>
            <Form.Control
              placeholder="No tenants available!"
              disabled
            />
            </>}
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={assignTenant}>Assign <i className="fa-solid fa-arrow-right"></i></Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}