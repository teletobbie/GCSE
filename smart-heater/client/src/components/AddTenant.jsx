import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';


export default function AddTenant() {
  const [show, setShow] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    name: '',
    role: 'TENANT',
    password: ''
  });

  const createTenant = async () => {
    try {
        const response = await axios.post('http://localhost:8080/users', formData)
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
      <Button variant="primary" onClick={handleShow}>
        New Tenant
      </Button>

      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="lg"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Add New Tenant</Modal.Title>
        </Modal.Header>
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
                autoFocus
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
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="text"
                name="password"
                placeholder="Enter password"
                value={formData.password}
                onChange={handleInputChange}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={createTenant}>Submit <i className="fa-solid fa-arrow-right"></i></Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}