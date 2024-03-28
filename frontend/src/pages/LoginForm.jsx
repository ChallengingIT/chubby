import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [csrfToken, setCsrfToken] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCsrfToken = async () => {
      try {
        const response = await fetch('/csrf');
        const data = await response.json();
        setCsrfToken(data['XSRF-TOKEN']); 
      } catch (error) {
        console.error('Errore durante il recupero del token CSRF', error);
      }
    };

    fetchCsrfToken();
  }, []);

  const handleLogin = async () => {
    try {
      const response = await fetch('/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'XSRF-TOKEN': csrfToken,
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Errore durante il login');
      }

      const data = await response.json();

      await fetch('/home', {
        method: 'GET', 
        headers: {
          'XSRF-TOKEN': csrfToken,
        },
      });

      navigate('/login');
    } catch (error) {
      console.error('Errore durante il login', error);
    }
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>Login</button>
    </div>
  );
};

export default LoginForm;
