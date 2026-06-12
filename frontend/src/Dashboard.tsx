import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Dashboard.css';

interface Dataset {
    id: string;
    name: string;
    status: string;
}

const Dashboard: React.FC = () => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [datasets, setDatasets] = useState<Dataset[]>([]);
    const [message, setMessage] = useState('');

    const token = localStorage.getItem('token');

    useEffect(() => {
        if (token) {
            fetchDatasets();
        }
    }, [token]);

    const fetchDatasets = async () => {
        try {
            const response = await axios.get('/api/datasets', {
                headers: { Authorization: `Bearer ${token}` }
            });
            setDatasets(response.data);
        } catch (error: any) {
            setMessage(error.response?.data || 'Failed to fetch datasets.');
        }
    };

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            setSelectedFile(event.target.files[0]);
        }
    };

    const handleFileUpload = async () => {
        if (!selectedFile) {
            setMessage('Please select a file first!');
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            setMessage('Uploading...');
            await axios.post('/api/datasets/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`
                }
            });
            setMessage('File uploaded successfully!');
            setSelectedFile(null);
            fetchDatasets(); // Refresh dataset list
        } catch (error: any) {
            setMessage(error.response?.data || 'File upload failed.');
        }
    };

    return (
        <div className="dashboard-container">
            <h2>Dashboard</h2>
            <div className="upload-section">
                <input type="file" onChange={handleFileChange} />
                <button onClick={handleFileUpload}>Upload Dataset</button>
            </div>
            {message && <p>{message}</p>}

            <h3>Your Datasets</h3>
            <div className="datasets-list">
                {datasets.length === 0 ? (
                    <p>No datasets uploaded yet.</p>
                ) : (
                    <ul>
                        {datasets.map((dataset) => (
                            <li key={dataset.id}>
                                {dataset.name} - Status: {dataset.status}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};

export default Dashboard;