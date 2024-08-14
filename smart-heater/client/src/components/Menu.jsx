import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Navbar, Nav, Row, Col } from 'react-bootstrap';
import Logo_Small from '../assets/Logo_smaller.png';
import useAuth from '../hooks/useAuth';

export default function Menu() {
  const [tenant, setTenant] = useState({});
  const auth = useAuth();

  const logout = () => {
    auth.logout();
  }

    useEffect(() => {
        const fetchTenant = () => {
            if(auth.userDetails) {
                setTenant(auth.userDetails);
            }
        };
        fetchTenant();
    }, [auth.userDetails]);

  
  return (
    <Navbar className="nav">
      <img src={Logo_Small} className="logo-menu ps-2" alt="Logo" />
      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse className="responsive-navbar-nav">
        <Nav className="mr-auto">
          <Row className="row">
            {auth.isLoggedIn && (tenant.role === "ADMIN" || tenant.role === "OWNER") && (
              <>
                <Col>
                  <Nav.Link as={Link} to="/bungalows" className="nav-text ms-2">
                    Bungalows
                  </Nav.Link>
                </Col>
                <Col>
                  <Nav.Link as={Link} to="/tenants" className="nav-text ms-2">
                    Tenants
                  </Nav.Link>
                </Col>
              </>
            )}
            {auth.isLoggedIn && (tenant.role === "OWNER") && (
              <>
              <Col>
                  <Nav.Link as={Link} to="/costs" className="nav-text ms-2">
                    Costs
                  </Nav.Link>
                </Col>
              </>
            )}
          </Row>
        </Nav>
      </Navbar.Collapse>
      <Col xs="auto">
        <Nav.Link
          as={Link}
          to="/"
          className="nav-text nav-end"
          onClick={logout}
        >
          Logout
        </Nav.Link>
      </Col>
    </Navbar>
  );
}
