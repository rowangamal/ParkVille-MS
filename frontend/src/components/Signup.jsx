import React, { useState } from 'react';
import { Car, Lock, Mail } from 'lucide-react';
import axios from 'axios';

const Signup = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    licensePlate: '',
    paymentMethod: '',
    role: 'Driver',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/api/signup', formData);
      alert('Signup successful!');
    } catch (error) {
      alert('Error during signup: ' + error.response?.data?.message || error.message);
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
      {/* Signup Container */}
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
          <h2 style={{ fontSize: '1.75rem', fontWeight: 'bold', color: '#edf2f7' }}>Sign Up</h2>
          <p style={{ color: '#a0aec0', marginTop: '0.5rem' }}>Create your rental account</p>
        </div>

        {/* Signup Form */}
        <form onSubmit={handleSubmit} style={{ display: 'grid', gap: '1rem' }}>
          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label
              htmlFor="firstName"
              style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
            >
              First Name
            </label>
            <input
              type="text"
              name="firstName"
              id="firstName"
              value={formData.firstName}
              onChange={handleChange}
              required
              style={{
                width: '100%',
                maxWidth: '470px',
                padding: '0.75rem',
                border: '1px solid #4a5568',
                borderRadius: '0.5rem',
                outline: 'none',
                fontSize: '0.95rem',
                backgroundColor: '#1a202c',
                color: '#edf2f7',
              }}
              placeholder="Enter your first name"
            />
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label
              htmlFor="lastName"
              style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
            >
              Last Name
            </label>
            <input
              type="text"
              name="lastName"
              id="lastName"
              value={formData.lastName}
              onChange={handleChange}
              required
              style={{
                width: '100%',
                maxWidth: '470px',
                padding: '0.75rem',
                border: '1px solid #4a5568',
                borderRadius: '0.5rem',
                outline: 'none',
                fontSize: '0.95rem',
                backgroundColor: '#1a202c',
                color: '#edf2f7',
              }}
              placeholder="Enter your last name"
            />
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label
              htmlFor="email"
              style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
            >
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
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label
              htmlFor="password"
              style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
            >
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
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label
              htmlFor="licensePlate"
              style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
            >
              License Plate
            </label>
            <input
              type="text"
              name="licensePlate"
              id="licensePlate"
              value={formData.licensePlate}
              onChange={handleChange}
              style={{
                width: '100%',
                maxWidth: '470px',
                padding: '0.75rem',
                border: '1px solid #4a5568',
                borderRadius: '0.5rem',
                outline: 'none',
                fontSize: '0.95rem',
                backgroundColor: '#1a202c',
                color: '#edf2f7',
              }}
              placeholder="Enter your license plate"
            />
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label
              htmlFor="paymentMethod"
              style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
            >
              Payment Method
            </label>
            <input
              type="text"
              name="paymentMethod"
              id="paymentMethod"
              value={formData.paymentMethod}
              onChange={handleChange}
              style={{
                width: '100%',
                maxWidth: '470px',
                padding: '0.75rem',
                border: '1px solid #4a5568',
                borderRadius: '0.5rem',
                outline: 'none',
                fontSize: '0.95rem',
                backgroundColor: '#1a202c',
                color: '#edf2f7',
              }}
              placeholder="Enter payment method"
            />
          </div>

          <div style={{ display: 'grid', gap: '0.5rem' }}>
            <label
              htmlFor="role"
              style={{ fontSize: '0.9rem', fontWeight: '500', color: '#a0aec0' }}
            >
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
                maxWidth: '470px',
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

          <button
            type="submit"
            style={{
              width: '100%',
              maxWidth: '470px',
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
            Sign Up
          </button>
        </form>

        <p style={{ marginTop: '1rem', textAlign: 'center', fontSize: '0.85rem', color: '#a0aec0' }}>
          Already have an account?{' '}
          <a href="/login" style={{ fontWeight: '500', color: '#63b3ed' }}>
            Sign in now
          </a>
        </p>
      </div>
    </div>
  );
};

export default Signup;
