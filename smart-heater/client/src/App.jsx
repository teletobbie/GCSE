import { Routes, Route, Navigate } from 'react-router-dom';
import './App.css';

import AuthProvider from './components/AuthProvider';
import PrivateRoute from './components/PrivateRoute';
import PrivateTenantRoute from './components/PrivateTenantRoute';
import Login from './pages/Login';
import Bungalows from './pages/Bungalows';
import Tenants from './pages/Tenants';
import HeaterPage from './pages/HeaterPage';
import Costs from './pages/Costs';
import Unauthorized from './pages/Unauthorized';

function App() {

  return (
    <AuthProvider>
      <Routes>
        <Route exact path='/' element={<Login/>}/>
        <Route exact path='/overview' element={<PrivateRoute><HeaterPage/></PrivateRoute>}/>
        <Route exact path='/bungalows' element={<PrivateTenantRoute allowedRoles={["ADMIN", "OWNER"]}><Bungalows/></PrivateTenantRoute>}/>
        <Route exact path='/tenants' element={<PrivateTenantRoute allowedRoles={["ADMIN", "OWNER"]}><Tenants/></PrivateTenantRoute>}/>
        <Route exact path='/costs' element={<PrivateTenantRoute allowedRoles={["OWNER"]}><Costs/></PrivateTenantRoute>}/>
        <Route exact path='/unauthorized' element={<PrivateTenantRoute allowedRoles={["ADMIN", "OWNER"]}><Unauthorized/></PrivateTenantRoute>}/>
      </Routes>
    </AuthProvider>
  )
}

export default App
