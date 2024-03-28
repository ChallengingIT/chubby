import React                    from 'react';
import DeleteIcon               from '@mui/icons-material/Delete';
import Button                   from "@mui/material/Button";

function DeleteButton({ onClick, id }) {
  return (
    <Button
      variant="contained"
      size="medium"
      startIcon={<DeleteIcon />}
      sx={{
        marginRight: '10%',
        backgroundColor: '#00853C',
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
          backgroundColor: 'red',
          transform:'scale(1.05)',
          color: 'white',
          cursor: 'pointer',
          borderRadius: '50%',
          borderStyle: 'none',
        }
      }}
      onClick={() => onClick(id)}
    />
  );
}

export default DeleteButton;
