import React from 'react';
import { useNavigate } from 'react-router-dom';

const LandingPage = () => {
  const navigate = useNavigate();

  return (
    <div style={styles.container}>
      <header style={styles.header}>
        <h1 style={styles.title}>Welcome to ParkEase</h1>
        <p style={styles.subtitle}>Your ultimate car parking solution</p>
      </header>
      <div style={styles.buttonContainer}>
        <button style={styles.button} onClick={() => navigate('/login')}>Login</button>
        <button style={styles.button} onClick={() => navigate('/signup')}>Signup</button>
      </div>
    </div>
  );
};


const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
    backgroundImage: 'url(\'https://example.com/car-theme-background.jpg\')', // Replace with a car-themed background image URL
    backgroundSize: 'cover',
    backgroundPosition: 'center',
  },
  header: {
    textAlign: 'center',
    marginBottom: '20px',
  },
  title: {
    fontSize: '3rem',
    color: '#fff',
  },
  subtitle: {
    fontSize: '1.5rem',
    color: '#ddd',
  },
  buttonContainer: {
    display: 'flex',
    gap: '20px',
  },
  button: {
    padding: '10px 20px',
    fontSize: '1rem',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    backgroundColor: '#007bff',
    color: '#fff',
    transition: 'background-color 0.3s ease',
  },
  buttonHover: {
    backgroundColor: '#0056b3',
  },
};

export default LandingPage;
