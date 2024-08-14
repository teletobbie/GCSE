import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import jwt from "jwt-decode";

export default function useProvideAuth() {
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
    const [userDetails, setUserDetails] = useState({
        name: ""
    });
 
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const decodedUser = jwt(token);
            setIsLoggedIn(true);
            setUserDetails(decodedUser);
        }
    }, []);

    const login = async (user) => {
        try {
        const response = await axios("http://localhost:8080/auth/login", {
            method: "POST",
            data: user,
        }
        )
        const token = response.data.token;
        const newUser = jwt(token);
        localStorage.setItem('token', token);
        setIsLoggedIn(true);
        setUserDetails(newUser);
        newUser.role === "TENANT" ? navigate("/overview") : navigate("/bungalows");
        } catch (error) {
            console.log(error);
        }
    }

    const logout = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
    }

    return {
        isLoggedIn,
        login,
        logout,
        userDetails
    };
}