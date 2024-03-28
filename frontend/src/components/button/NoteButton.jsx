import React from 'react';
import SpeakerNotesIcon from '@mui/icons-material/SpeakerNotes';
import Button from "@mui/material/Button";

function NoteButton({onClick}) {


return (
    <Button 
    variant="contained" 
    size="medium" 
    startIcon={<SpeakerNotesIcon />} 
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
};

export default NoteButton;