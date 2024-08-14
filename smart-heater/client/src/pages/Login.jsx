import "./login.css";
import Logo from '../assets/Logo.png';
import { useState } from 'react';
import useAuth from '../hooks/useAuth';

export default function Login() {
  const [credentials, setCredentials] = useState({
    email: "",
    password: ""
  });
  const [errorMessage, setErrorMessage] = useState("");
  const {email, password} = credentials;

  const auth = useAuth();

  const handleEmailChange = (e) => {
    setCredentials({...credentials, email: e.target.value});
  }

  const handlePasswordChange = (e) => {
    setCredentials({...credentials, password: e.target.value});
  }

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!email || !password) {
      setErrorMessage("Please provide valid email and password to login.")
    } else {
      try {
        await auth.login(credentials);
        setCredentials({email: "", password: ""});
      } catch (error) {
        if (error) {
          setErrorMessage(error.response.data.message);
        } else {
          setErrorMessage("An error occurred");
        }
      }
    } 
   
  }

  return (
    <div className='bg-image pt-4'>
       <div className='container text-center pt-4'>
       <div className='row pt-4' style={{minHeight: "10vh"}}></div>
        <img src={Logo} alt="Logo" className='logo py-3' />
        <div className='row py-2'>
        <form onSubmit={handleLogin}>
          <div className="col-sm-4 offset-sm-4 mt-2 mb-4">
            <div className="form-floating">
              <input
              onChange={handleEmailChange}
                className="form-control"
                style={{fontSize: "15px"}}
                value={email}
                name="email"
                type="text"
                placeholder="email"
              />
              <label className="form-label" style={{color: "grey"}}>Email</label>
            </div>
          </div>
          <div className="col-sm-4 offset-sm-4 my-3">
            <div className="form-floating">
              <input
              onChange={handlePasswordChange}
                className="form-control"
                style={{fontSize: "15px"}}
                value={password}
                name="password"
                type="password"
                placeholder="password"
              />
              <label className="form-label"  style={{color: "grey"}}>Password</label>
            </div>
          </div>

          <button
            className="login-btn my-2 py-2 col-12 col-sm-4 text-white"
            onClick={handleLogin}
          >
            Sign in<i className="fa-solid fa-arrow-right ms-3"></i>
          </button>

          {errorMessage && <p className='py-2 text-white'>{errorMessage}</p>}

          <p className='mt-3'>
            <a className="text-white" href="#">
              Forgot password?
            </a>
          </p>
        </form>
       
        </div>
        </div>
        </div>
     

  )
}
