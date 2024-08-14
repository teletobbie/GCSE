import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';


export default function UnassignTenant({bungalow}) {
  const [show, setShow] = useState(false);
  const [formData, setFormData] = useState({
    id: bungalow.user.id,
    email: bungalow.user.email,
    name: bungalow.user.name,
    role: 'TENANT',
    password: bungalow.user.password
  });

  const unassignTenant = async () => {
    try {
        const response = await axios.patch('http://localhost:8080/bungalows/unassign/'+bungalow.id, formData)
        console.log(response)
        handleClose();
        //window.location.reload();
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
      <div className="table-buttons"> 
        <i className="fa-solid fa-user-minus" onClick={handleShow}></i>
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
          <Modal.Title>Are you sure you want to unassign this tenant from {bungalow.name}?</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                name="name"
                placeholder="Enter name"
                value={bungalow.user.name}
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
                value={bungalow.user.email}
                onChange={handleInputChange}
                disabled
                readOnly
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={unassignTenant}>Unassign <i className="fa-solid fa-arrow-right"></i></Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}