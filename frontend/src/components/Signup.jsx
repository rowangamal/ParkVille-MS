import React, { useState } from 'react';
import { Car, Lock, Mail, User } from 'lucide-react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Signup = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    licensePlateNumber: '',
    paymentMethod: '',
    role: 'Driver',
  });

  const [errors, setErrors] = useState({
    username: '',
    email: '',
    password: '',
    licensePlateNumber: '',
    paymentMethod: '',
  });

  const [message, setMessage] = useState({ text: '', type: '' });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setErrors({ ...errors, [name]: '' });
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.username) {
      newErrors.username = 'Username is required';
    }
    if (!formData.email) {
      newErrors.email = 'Email is required';
    }
    if (!formData.password) {
      newErrors.password = 'Password is required';
    }
    if (formData.role === 'Driver') {
      if (!formData.licensePlateNumber) {
        newErrors.licensePlateNumber = 'License plate number is required';
      }
      if (!formData.paymentMethod) {
        newErrors.paymentMethod = 'Payment method is required';
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0; 
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!validate()) return;
  
    try {
      let endpoint = '';
      const dataToSend = {
        username: formData.username,
        email: formData.email,
        password: formData.password,
        role: formData.role,
      };
  
      if (formData.role === 'Driver') {
        endpoint = 'http://localhost:8080/api/drivers/signup';
        dataToSend.licensePlateNumber = formData.licensePlateNumber;
        dataToSend.paymentMethod = formData.paymentMethod;
      } else if (formData.role === 'ParkingLotManager') {
        endpoint = 'http://localhost:8080/api/managers/signup';
      } else if (formData.role === 'SystemAdmin') {
        endpoint = 'http://localhost:8080/api/admins/signup';
      }
  
      const response = await axios.post(endpoint, dataToSend);
      navigate('/login');
      setMessage({ text: 'Signup successful!', type: 'success' });
      setFormData({
        username: '',
        email: '',
        password: '',
        licensePlateNumber: '',
        paymentMethod: '',
        role: 'Driver',
      });
    } catch (error) {
      setMessage({
        text: error.response?.data?.message || 'Error during signup. Please try again.',
        type: 'error',
      });
    }
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
      <div
        style={{
          width: '100%',
          maxWidth: '500px',
          padding: '2rem',
          margin: '1rem',
          backgroundColor: '#2d3748',
          borderRadius: '1rem',
          boxShadow: '0 8px 20px rgba(0, 0, 0, 0.2)',
          zIndex: 10,
        }}
      >
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
          <h2 style={{ fontSize: '1.75rem', fontWeight: 'bold', color: '#edf2f7' }}>Sign Up</h2>
          <p style={{ color: '#a0aec0', marginTop: '0.5rem' }}>Create your rental account</p>
        </div>

        {message.text && (
          <div
            style={{
              marginBottom: '1rem',
              padding: '1rem',
              backgroundColor: message.type === 'success' ? '#38a169' : '#e53e3e',
              color: '#fff',
              borderRadius: '0.5rem',
              textAlign: 'center',
            }}
          >
            {message.text}
          </div>
        )}

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
                  maxWidth: '440px',
                  padding: '0.75rem 0.75rem 0.75rem 2.5rem',
                  border: '1px solid #4a5568',
                  borderRadius: '0.5rem',
                  outline: 'none',
                  fontSize: '0.95rem',
                  backgroundColor: '#1a202c',
                  color: '#edf2f7',
                }}
                placeholder="Enter your username"
              />
            </div>
            {errors.username && <span style={{ color: 'red', fontSize: '0.8rem' }}>{errors.username}</span>}
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label htmlFor="role" style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}>
              Role
            </label>
            <select
              name="role"
              id="role"
              value={formData.role}
              onChange={handleChange}
              required
              style={{
                width: '100%',
                maxWidth: '490px',
                padding: '0.75rem',
                border: '1px solid #4a5568',
                borderRadius: '0.5rem',
                outline: 'none',
                fontSize: '0.95rem',
                backgroundColor: '#1a202c',
                color: '#edf2f7',
              }}
            >
              <option value="Driver">Driver</option>
              <option value="ParkingLotManager">Parking Lot Manager</option>
              <option value="SystemAdmin">System Administrator</option>
            </select>
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label htmlFor="email" style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}>
              Email Address
            </label>
            <div style={{ position: 'relative' }}>
              <Mail
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
                type="email"
                name="email"
                id="email"
                value={formData.email}
                onChange={handleChange}
                required
                style={{
                  width: '100%',
                  maxWidth: '440px',
                  padding: '0.75rem 0.75rem 0.75rem 2.5rem',
                  border: '1px solid #4a5568',
                  borderRadius: '0.5rem',
                  outline: 'none',
                  fontSize: '0.95rem',
                  backgroundColor: '#1a202c',
                  color: '#edf2f7',
                }}
                placeholder="Enter your email"
              />
            </div>
            {errors.email && <span style={{ color: 'red', fontSize: '0.8rem' }}>{errors.email}</span>}
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
                  maxWidth: '440px',
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
            {errors.password && <span style={{ color: 'red', fontSize: '0.8rem' }}>{errors.password}</span>}
          </div>

          {/* Driver-specific fields */}
          {formData.role === 'Driver' && (
            <>
              <div style={{ display: 'grid', gap: '0.5rem' }}>
                <label
                  htmlFor="licensePlateNumber"
                  style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
                >
                  License Plate Number
                </label>
                <input
                  type="text"
                  name="licensePlateNumber"
                  id="licensePlateNumber"
                  value={formData.licensePlateNumber}
                  onChange={handleChange}
                  required
                  style={{
                    width: '100%',
                    maxWidth: '440px',
                    padding: '0.75rem',
                    border: '1px solid #4a5568',
                    borderRadius: '0.5rem',
                    outline: 'none',
                    fontSize: '0.95rem',
                    backgroundColor: '#1a202c',
                    color: '#edf2f7',
                  }}
                  placeholder="Enter your license plate number"
                />
                {errors.licensePlateNumber && (
                  <span style={{ color: 'red', fontSize: '0.8rem' }}>
                    {errors.licensePlateNumber}
                  </span>
                )}
              </div>

              <div style={{ display: 'grid', gap: '0.5rem' }}>
                <label htmlFor="paymentMethod" style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}>
                  Payment Method
                </label>
                <input
                  type="text"
                  name="paymentMethod"
                  id="paymentMethod"
                  value={formData.paymentMethod}
                  onChange={handleChange}
                  required
                  style={{
                    width: '100%',
                    maxWidth: '440px',
                    padding: '0.75rem',
                    border: '1px solid #4a5568',
                    borderRadius: '0.5rem',
                    outline: 'none',
                    fontSize: '0.95rem',
                    backgroundColor: '#1a202c',
                    color: '#edf2f7',
                  }}
                  placeholder="Enter your payment method"
                />
                {errors.paymentMethod && (
                  <span style={{ color: 'red', fontSize: '0.8rem' }}>
                    {errors.paymentMethod}
                  </span>
                )}
              </div>
            </>
          )}

          <button
            type="submit"
            style={{
              width: '100%',
              padding: '1rem',
              fontSize: '1rem',
              fontWeight: '600',
              color: '#fff',
              backgroundColor: '#2563eb',
              border: 'none',
              borderRadius: '0.5rem',
              cursor: 'pointer',
              transition: 'background-color 0.2s',
            }}
          >
            Sign Up
          </button>
          <p style={{ marginTop: '1rem', textAlign: 'center', fontSize: '0.85rem', color: '#a0aec0' }}>
            Already have an account?{' '}
            <a href="/login" style={{ fontWeight: '500', color: '#63b3ed' }}>
              Sign In
            </a>
          </p>
        </form>
      </div>
    </div>
  );
};

export default Signup;
