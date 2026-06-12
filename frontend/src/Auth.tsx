import React, { useState } from 'react';
import axios from 'axios';
import './Auth.css';

const Auth: React.FC = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setMessage('');
        try {
            const url = isLogin ? '/api/auth/login' : '/api/auth/register';
            const response = await axios.post(url, { username, password });
            setMessage(response.data.message || 'Success!');
            if (isLogin && response.data.token) {
                localStorage.setItem('token', response.data.token);
                // Redirect or update UI for logged-in state
            }
        } catch (error: any) {
            setMessage(error.response?.data || 'An error occurred.');
        }
    };

    return (
        <div className="auth-container">
            <h2>{isLogin ? 'Login' : 'Register'}</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">{isLogin ? 'Login' : 'Register'}</button>
            </form>
            <button onClick={() => setIsLogin(!isLogin)}>
                {isLogin ? 'Need an account? Register' : 'Have an account? Login'}
            </button>
            {message && <p>{message}</p>}
        </div>
    );
};

export default Auth;