
import React, { useEffect, useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  FormControl,
  Autocomplete,
  Box,
  Typography,
  
} from '@mui/material';
import CloseIcon                            from '@mui/icons-material/Close';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'; 
import { LocalizationProvider } from '@mui/x-date-pickers'; 
import axios from 'axios';
import dayjs from 'dayjs';




function EmailModal({ open, handleClose }) {


    const user = JSON.parse(localStorage.getItem("user"));
    const accessToken = user?.accessToken;
  
    const headers = {
      Authorization: `Bearer ${accessToken}`
    };


    const [formData, setFormData] = useState({
    destinatari: '',
    oggetto: '',
    note: '',
  });

  const handleChange = (name) => (event) => {
    const newValue = event.target.value;
    setFormData({...formData, [name]:newValue});
};

const onClose = () => {
    setFormData({
    destinatari: '',
    oggetto: '',
    note: '',
    });
    handleClose();
};




  const handleSubmitEmail = async () => {


      const datiDaInviare = {
        oggetto: formData.oggetto ,
        note: formData.note ,
        destinatari: formData.destinatari ,
      };
      try {
        const responseInviaEmail = await axios.post("/email/send", datiDaInviare, { headers: headers });
        if (responseInviaEmail.data === 'OK') {
          onClose();
        }
      } catch (error) {
        console.error("Errore durante l'invio dell'email: ", error);
      }
  };
  


  return (
    <Dialog 
    open={open} 
    onClose={handleClose} 
    sx={{ 
        '& .MuiDialog-paper': {
        width: '70%',
        maxWidth: '70%',
        },
    }}
    >
    <LocalizationProvider dateAdapter={AdapterDayjs}>

        <Box sx={{ display:'flex', justifyContent: 'flex-end'}}>
        <Button onClick={onClose} variant="outlined" sx={{mt: 3, mr: 3 ,backgroundColor: 'transparent', border: 'none', color: '#898989', '&:hover': { border: 'none', color: 'red', transform: 'scale(1.1)'}}}startIcon={<CloseIcon sx={{backgroundColor: 'transparent'}}/>}/>

        </Box>
      <DialogTitle sx={{ mb: 2, display: 'flex', justifyContent: 'center', mt: 0.5, fontWeight: '600'}}>Invia Email</DialogTitle>
      <DialogContent sx={{ p: 12}}  >
        <Box>
        <TextField
          id="destinatari-box"
          label="Destinatari*"
          fullWidth
          variant='filled'
          name="destinatari"
          value={formData.destinatari}
          onChange={handleChange('destinatari')}
          sx={{
            p: 1,
            borderRadius: '40px', 
            backgroundColor: '#EDEDED', 
            '& .MuiFilledInput-root': {
                backgroundColor: 'transparent',
            },
            '& .MuiFilledInput-underline:after': {
                borderBottomColor: 'transparent',
            },
            '& .MuiFilledInput-root::before': {
                borderBottom: 'none', 
            },
            '&:hover .MuiFilledInput-root::before': {
                borderBottom: 'none', 
            } 
            }} 
        />
        <Typography variant="h6" sx={{ mb: 3, mt: 0.3, ml: 1, color: '#666565', fontSize: '1em'}}>* Inserire i destinatari separati da " ; "</Typography>


        <TextField
        id='oggetto-box'
          label="Oggetto"
          fullWidth
          variant='filled'
          name="oggetto"
          value={formData.oggetto}
          onChange={handleChange('oggetto')}
          sx={{
            mb: 8,
            p: 1,
            borderRadius: '40px', 
            backgroundColor: '#EDEDED', 
            '& .MuiFilledInput-root': {
                backgroundColor: 'transparent',
            },
            '& .MuiFilledInput-underline:after': {
                borderBottomColor: 'transparent',
            },
            '& .MuiFilledInput-root::before': {
                borderBottom: 'none', 
            },
            '&:hover .MuiFilledInput-root::before': {
                borderBottom: 'none', 
            } 
            }} 
        />


        <TextField
        id='messaggio-box'
                label="Testo del messaggio"
                name="note"
                multiline
                rows={4}
                onChange={handleChange('note')}
                value={formData.note}
                fullWidth
                variant='filled'
                sx={{
                    mb: 2,
                    p: 1,
                    borderRadius: '40px', 
                    backgroundColor: '#EDEDED', 
                    '& .MuiFilledInput-root': {
                        backgroundColor: 'transparent',
                    },
                    '& .MuiFilledInput-underline:after': {
                        borderBottomColor: 'transparent',
                    },
                    '& .MuiFilledInput-root::before': {
                        borderBottom: 'none', 
                    },
                    '&:hover .MuiFilledInput-root::before': {
                        borderBottom: 'none', 
                    } 
                    }}             />
            <Box sx={{ display: 'flex', justifyContent: 'center'}}>
        <Button onClick={handleSubmitEmail} variant="contained" sx={{ borderRadius: '5px', backgroundColor: '#00853C', '&:hover': { backgroundColor: '#00853C', transform: 'scale(1.1)'}}}>
          Invia
        </Button>
        </Box>

            </Box>
      </DialogContent>
      <DialogActions>
        
      </DialogActions>
      </LocalizationProvider>

    </Dialog>
  );
}

export default EmailModal;
