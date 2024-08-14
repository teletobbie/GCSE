import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';


export default function DeleteTenant({tenantData}) {
  const [show, setShow] = useState(false);
  const [formData, setFormData] = useState({
    id: tenantData.id,
    email: tenantData.email,
    name: tenantData.name,
    role: 'TENANT',
  });

  const deleteTenant = async () => {
    try {
        const response = await axios.delete('http://localhost:8080/users/'+tenantData.id, formData)
        console.log(response)
        handleClose();
        window.location.reload();
    } catch (error) {
        console.log(error)
    }
  }

  const handleClose = () => {
    setShow(false);
    // Clear the form fields when the modal is closed
    setFormData({
      email: '',
      name: '',
      role: 'TENANT',
      password: '',
    });
  }

  const handleShow = () => setShow(true);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  }

  return (
    <>
      <i className="fa-solid fa-trash" onClick={handleShow}></i>

      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="lg"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Are you sure you want to delete this tenant?</Modal.Title>
        </Modal.Header>
        { tenantData.bungalow !== null ? 
          <Modal.Header>
            <Modal.Title>WARNING! {tenantData.name} is assigned to a bungalow!</Modal.Title>
          </Modal.Header> :
          <></>
        }
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                name="name"
                placeholder="Enter name"
                value={formData.name}
                onChange={handleInputChange}
                disabled
                readOnly
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                name="email"
                placeholder="name@example.com"
                value={formData.email}
                onChange={handleInputChange}
                disabled
                readOnly
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="text"
                name="password"
                placeholder="Enter password"
                value=""
                onChange={handleInputChange}
                disabled
                readOnly
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={deleteTenant}>Delete <i className="fa-solid fa-arrow-right"></i></Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}