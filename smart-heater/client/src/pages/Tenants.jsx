import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Menu from '../components/Menu';
import AddTenant from '../components/AddTenant';
import EditTenant from '../components/EditTenant';
import DeleteTenant from '../components/DeleteTenant';
import "./tenants.css";

export default function Tenants() {
    const [tenants, setTenants] = useState([]);
    const [filterValue, setFilterValue] = useState('');

    const fetchTenants = async () => {
        try {
            const response = await axios.get('http://localhost:8080/users');
            console.log(response);
            setTenants(response.data);
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        fetchTenants();
    }, []);

    const filteredTenants = tenants.filter((tenant) => {
        const searchValue = filterValue.toLowerCase();
        return tenant.role === 'TENANT' &&
            (tenant.email.toLowerCase().includes(searchValue) || tenant.name.toLowerCase().includes(searchValue));
    });

    const handleFilterChange = (e) => {
        setFilterValue(e.target.value);
    };

    return (
        <div>
            <Menu />
            
            <div className='container-fluid justify-space-between'>
                <div className="filter-panel-and-button">
                    <div className="row">
                        <div className="col">
                        <div className="filter-panel-tenants">
                                <input
                                    type="text"
                                    className="form-control blue-background"
                                    value={filterValue}
                                    onChange={handleFilterChange}
                                    placeholder="Filter by tenant email or name"
                                    required=""
                                />
                            </div>
                        </div>
                        <div className="col d-flex justify-content-end">
                            <AddTenant />
                        </div>
                    </div>
                </div>

                <div className="tenant-table">
                    <table className="table table-bordered table-hover table-sm">
                        <thead className='blue-background'>
                            <tr>
                                <th>Email</th>
                                <th>Name</th>
                                <th>Occupies</th>
                            </tr>   
                        </thead>
                        <tbody>
                            {filteredTenants.map((tenant) => (
                                <tr key={tenant.id}>
                                    <td>{tenant.email}</td>
                                    <td className='row-and-buttons'>
                                        <span>{tenant.name}</span>                                   
                                        <div className='table-buttons'>
                                            <EditTenant tenantData={tenant} className="edit-button"/>
                                            <DeleteTenant tenantData={tenant} className="delete-button"/>
                                        </div>
                                    </td>
                                    <td>
                                        {tenant.bungalow !== null ? 
                                            tenant.bungalow.name
                                        :
                                            <i>No bungalow</i>
                                        }
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div> 
    );
}

