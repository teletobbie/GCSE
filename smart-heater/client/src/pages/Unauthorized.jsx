import "./login.css";
import Logo from '../assets/Logo.png';
import { useNavigate } from 'react-router-dom';

export default function Unauthorized() {
    const navigate = useNavigate()

    const handleClick = () => {
        navigate("/bungalows");
    }

  return (
    <div className='bg-image pt-4'>
       <div className='container text-center pt-4'>
       <div className='row pt-4' style={{minHeight: "10vh"}}></div>
        <img src={Logo} alt="Logo" className='logo py-3' />
        <h1 style={{color: "white"}}>401 Unauthorized</h1>
        <button
            className="login-btn my-2 py-2 col-12 col-sm-4 text-white"
            onClick={handleClick}
          >
            Go back to bungalow overview<i className="fa-solid fa-arrow-right ms-3"></i>
          </button>
        </div>
        </div>
  )
}
