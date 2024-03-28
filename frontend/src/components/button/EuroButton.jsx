import React from 'react';
import EuroIcon from '@mui/icons-material/Euro';
import Button from "@mui/material/Button";

function EuroButton({onClick}) {
 
    return (
        <Button 
        variant="contained" 
        size="medium" 
        startIcon={<EuroIcon />} 
        onClick={onClick}
        sx={{
            marginRight: '10%',
            backgroundColor: 'black',
            color: 'white',
            borderRadius: '50%',
            minWidth: '2em',
            width: '2em',
            height: '2em',
            padding: '0',
            '& .MuiButton-startIcon': {
              margin: '0',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            },
            '&:hover': {
              backgroundColor: 'black',
              transform:'scale(1.05)',
              color: 'white',
              cursor: 'pointer',
              borderRadius: '50%',
              borderStyle: 'none',
            }
          }}
  
            />
    );
}

export default EuroButton