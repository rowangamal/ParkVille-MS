import React, { useState } from 'react';
import { Car, Lock, User } from 'lucide-react';

const Login = () => {
  const [formData, setFormData] = useState({ username: '', password: '', role: '' });
  const [message, setMessage] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!formData.username || !formData.password || !formData.role) {
      setMessage({ text: 'Username, password, and role are required', type: 'error' });
      return;
    }
  
    let apiUrl = '';
    if (formData.role === 'driver') {
      apiUrl = 'http://localhost:8080/api/drivers/login';
    } else if (formData.role === 'admin') {
      apiUrl = 'http://localhost:8080/api/admins/login';
    } else if (formData.role === 'manager') {
      apiUrl = 'http://localhost:8080/api/managers/login';
    } else {
      setMessage({ text: 'Invalid role selected', type: 'error' });
      return;
    }
  
    const requestData = {
      username: formData.username,
      password: formData.password,
    };
  
    try {
      const response = await fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
      });
  
      if (response.ok) {
        const successData = await response.json();
        setMessage({ 
          text: `Welcome, ${successData.username}!`, 
          type: 'success' 
        });

        localStorage.setItem('role', successData.role);
        localStorage.setItem('jwtToken', successData.jwtToken);
      } else {
        const errorText = await response.text();
        setMessage({ text: errorText || 'Login failed', type: 'error' });
      }
    } catch (error) {
      setMessage({ text: 'Network error, please try again', type: 'error' });
    }
  
    setFormData({ username: '', password: '', role: '' });
  };
  

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#ffffff',
        position: 'relative',
        overflow: 'hidden',
        fontFamily: 'Roboto, sans-serif',
      }}
    >
      {/* Login Container */}
      <div
        style={{
          width: '100%',
          maxWidth: '350px',
          padding: '2rem',
          margin: '1rem',
          backgroundColor: '#2d3748',
          borderRadius: '1rem',
          boxShadow: '0 8px 20px rgba(0, 0, 0, 0.2)',
          zIndex: 10,
        }}
      >
        {/* Logo and Title */}
        <div style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
          <div
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              justifyContent: 'center',
              width: '3.5rem',
              height: '3.5rem',
              borderRadius: '50%',
              backgroundColor: '#2563eb',
              marginBottom: '1rem',
            }}
          >
            <Car style={{ width: '1.75rem', height: '1.75rem', color: 'white' }} />
          </div>
          <h2 style={{ fontSize: '1.75rem', fontWeight: 'bold', color: '#edf2f7' }}>Welcome Back</h2>
          <p style={{ color: '#a0aec0', marginTop: '0.5rem' }}>Sign in to your account</p>
        </div>

        {/* Notification Message */}
        {message && (
          <div
            style={{
              marginBottom: '1rem',
              padding: '0.75rem',
              borderRadius: '0.5rem',
              backgroundColor: message.type === 'error' ? '#e53e3e' : '#38a169',
              color: 'white',
              textAlign: 'center',
            }}
          >
            {message.text}
          </div>
        )}

        {/* Login Form */}
        <form onSubmit={handleSubmit} style={{ display: 'grid', gap: '1rem' }}>
          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label htmlFor="username" style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}>
              Username
            </label>
            <div style={{ position: 'relative' }}>
              <User
                style={{
                  position: 'absolute',
                  top: '50%',
                  left: '0.75rem',
                  transform: 'translateY(-50%)',
                  color: '#718096',
                  width: '1.25rem',
                  height: '1.25rem',
                }}
              />
              <input
                type="text"
                name="username"
                id="username"
                value={formData.username}
                onChange={handleChange}
                required
                style={{
                  width: '100%',
                  padding: '0.75rem 0.75rem 0.75rem 2.5rem',
                  border: '1px solid #4a5568',
                  maxWidth: '300px',
                  borderRadius: '0.5rem',
                  outline: 'none',
                  fontSize: '0.95rem',
                  backgroundColor: '#1a202c',
                  color: '#edf2f7',
                }}
                placeholder="Enter your username"
              />
            </div>
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label htmlFor="password" style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}>
              Password
            </label>
            <div style={{ position: 'relative' }}>
              <Lock
                style={{
                  position: 'absolute',
                  top: '50%',
                  left: '0.75rem',
                  transform: 'translateY(-50%)',
                  color: '#718096',
                  width: '1.25rem',
                  height: '1.25rem',
                }}
              />
              <input
                type="password"
                name="password"
                id="password"
                value={formData.password}
                onChange={handleChange}
                required
                style={{
                  width: '100%',
                  maxWidth: '300px',
                  padding: '0.75rem 0.75rem 0.75rem 2.5rem',
                  border: '1px solid #4a5568',
                  borderRadius: '0.5rem',
                  outline: 'none',
                  fontSize: '0.95rem',
                  backgroundColor: '#1a202c',
                  color: '#edf2f7',
                }}
                placeholder="Enter your password"
              />
            </div>
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label htmlFor="role" style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}>
              Role
            </label>
            <select
              name="role"
              value={formData.role}
              onChange={handleChange}
              required
              style={{
                width: '100%',
                padding: '0.75rem',
                borderRadius: '0.5rem',
                border: '1px solid #4a5568',
                backgroundColor: '#1a202c',
                color: '#edf2f7',
              }}
            >
              <option value="">Select your role</option>
              <option value="driver">Driver</option>
              <option value="admin">Administrator</option>
              <option value="manager">Lot Manager</option>
            </select>
          </div>

          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <input
                id="remember-me"
                name="remember-me"
                type="checkbox"
                style={{ width: '1rem', height: '1rem', marginRight: '0.5rem' }}
              />
              <label htmlFor="remember-me" style={{ fontSize: '0.85rem', color: '#a0aec0' }}>
                Remember me
              </label>
            </div>
            <a href="#" style={{ fontSize: '0.85rem', fontWeight: '500', color: '#63b3ed' }}>
              Forgot password?
            </a>
          </div>

          <button
            type="submit"
            style={{
              width: '100%',
              padding: '0.75rem',
              border: 'none',
              borderRadius: '0.5rem',
              backgroundColor: '#2563eb',
              color: 'white',
              fontSize: '0.95rem',
              fontWeight: '500',
              cursor: 'pointer',
              transition: 'background-color 0.2s ease',
            }}
            onMouseEnter={(e) => (e.target.style.backgroundColor = '#1d4ed8')}
            onMouseLeave={(e) => (e.target.style.backgroundColor = '#2563eb')}
          >
            Sign In
          </button>
        </form>

        <p style={{ marginTop: '1rem', textAlign: 'center', fontSize: '0.85rem', color: '#a0aec0' }}>
          Don't have an account?{' '}
          <a href="/signup" style={{ fontWeight: '500', color: '#63b3ed' }}>
            Sign up now
          </a>
        </p>
      </div>
    </div>
  );
};

export default Login;
