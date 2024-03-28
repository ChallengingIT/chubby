import React                 from 'react';
import Sfondo                               from '../images/loginTorchy.svg';
import { Box } from '@mui/material';


const Homepage = () => {





    return (
    <Box
    sx={{
        display: 'flex',
        width: '100vw',
        height: '100vh'
    }}
    >
        <img
        alt='Login background'
        src={Sfondo}
        style={{
            width: '100vw',
            height: '100vh',
            objectFit: 'cover',
            position: 'absolute',
            top: 0,
            left: 0,
            zIndex: -1
        }}
        />
    </Box>
    );
};

export default Homepage;