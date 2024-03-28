import React from 'react';
import AttachmentIcon from '@mui/icons-material/Attachment';
import Button from "@mui/material/Button";

function ClipButton({ idFile, fileDescrizione, onClick }) {
    const handleDownload = () => {
        if (onClick && idFile && fileDescrizione) {
            onClick();
        } else {
            console.warn("La funzione di download non è definita");
        }
    };



    return (
        <Button 
            variant="contained" 
            size="medium" 
            startIcon={<AttachmentIcon />} 
            onClick={handleDownload} 
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
                backgroundColor: 'balck',
                transform:'scale(1.05)',
                color: 'white',
                cursor: 'pointer',
                borderRadius: '50%',
                borderStyle: 'none',
              }
            }}
        />
    );
};

export default ClipButton;
