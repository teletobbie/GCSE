import { useState, useEffect } from 'react';
import useAuth from '../hooks/useAuth';
import { Navigate } from 'react-router-dom';

export default function PrivateTenantRoute({ children, allowedRoles }) {
    const [user, setUser] = useState({});

    const auth = useAuth();

    if (!auth.isLoggedIn) return <Navigate to="/"/>

    useEffect(() => {
        const fetchUser = () => {
            if (auth.userDetails) {
                setUser(auth.userDetails);
            }
        };
        fetchUser();
    }, [auth.userDetails]);

    if (allowedRoles.includes(user.role)) {
        return children;
    } else if (user.role === "TENANT") {
        return <Navigate to="/overview"/> 
    } else if (user.role === "ADMIN" && window.location.pathname === "/costs") {
        return <Navigate to="/unauthorized"/>
    }
}
