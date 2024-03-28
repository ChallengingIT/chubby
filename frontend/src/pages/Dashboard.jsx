import React from 'react';
import { Box } from '@mui/material';
import DashboardImg from '../images/dashboard.png';

function Dashboard() {
  return (
    <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: '100vh', width: '100vw', overflow: 'hidden' }}>
        <Box sx={{ 
                flexGrow: 1, 
                p: 3, 
                marginLeft: '13.2em', 
                marginTop: '0.5em', 
                marginBottom: '0.8em', 
                marginRight: '0.8em', 
                backgroundColor: '#FEFCFD', 
                borderRadius: '20px', 
                minHeight: '98vh',
                mt: 1.5,
                // overflow: 'auto'
                
            }}>
        <img src={DashboardImg} alt="Dashboard" style={{ width: '100%', height: '100%' }} />
      </Box>
    </Box>
  );
}

export default Dashboard;
