import React from 'react';
import { useNavigate } from 'react-router-dom';
import loginTorchy from '../images/loginTorchy.svg';
import { Box } from '@mui/material';
import LogoTorchySVG from '../images/LogoTorchy.svg';
import LoginComponent from '../components/LoginComponent';

const LoginPage = () => {
    const navigate = useNavigate();
    return (
        <Box sx={{
        display: 'flex',
        flexDirection: 'column',
        width: '100%',
        height: '100vh',
        alignItems: 'center', 
        justifyContent: 'center', 
        position: 'relative', 
        owerflow: 'hidden'
        }}>
        <img
            alt="Login background"
            src={loginTorchy}
            style={{
            width: '100%',
            height: '100%',
            objectFit: 'cover',
            position: 'absolute', 
            top: 0,
            left: 0,
            zIndex: -1, 
            }}
        />
        <Box sx={{
            position: 'absolute', 
            top: '5%', 
            left: '5%', 
        }}>
            <img
            alt="Torchy logo"
            src={LogoTorchySVG}
            style={{
                height: '100%',
                width: '45%', 
            }}
            />
        </Box>

        <Box sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: '40%',
            height: '70vh',
        }}>
            <LoginComponent
            navigate={navigate}
            />
        </Box>
        </Box>
    );
    }

export default LoginPage;
