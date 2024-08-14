import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import useAuth from '../hooks/useAuth';


export default function UpdateTenantName({tenantData}) {
  const [show, setShow] = useState(false);
  const [formData, setFormData] = useState({
    id: tenantData.id,
    name: tenantData.name,
  });
  const [loggedInUser, setLoggedInUser] = useState({});

  const editTenant = async (loggedInUser) => {
    try {
        await axios.patch('http://localhost:8080/users/'+loggedInUser.id, formData)
        handleClose();
        window.location.reload();
    } catch (error) {
        console.log(error)
    }
  }

  const handleClose = () => {
    setShow(false);
    setFormData({
      name: ''
    });
  }

  const handleShow = () => setShow(true);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  }

  const auth = useAuth();

  useEffect(() => {
      const fetchUser = () => {
          if (auth.userDetails) {
              setLoggedInUser(auth.userDetails);
          }
      };
      fetchUser();
  }, [auth.userDetails]);

  const findUserById = async () => {
    try {
        await editTenant(loggedInUser);
        if (formData.id === loggedInUser.id) {
        console.log("Name update successfull");
        }
    } catch (error) {
        console.log(error)
    }
  }

  return (
    <>
      <i className="fa-solid fa-pencil" style={{cursor: 'pointer'}} onClick={handleShow}></i>
      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="lg"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Change your name</Modal.Title>
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
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={findUserById}>Submit <i className="fa-solid fa-arrow-right"></i></Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}