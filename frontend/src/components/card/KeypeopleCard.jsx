import React, {useState, useEffect}                 from 'react';
import { useNavigate }                      from 'react-router-dom';
import EmailIcon                            from '@mui/icons-material/Email';
import BusinessCenterIcon                   from '@mui/icons-material/BusinessCenter';
import LocalPhoneIcon                       from '@mui/icons-material/LocalPhone';
import CloseIcon                            from '@mui/icons-material/Close';

import { 
    Card, 
    CardContent, 
    Box,
    Typography,
    Button,
    CardActions,
    Modal
    } from '@mui/material';


const KeypeopleCard = ({valori, onDelete}) => {

    const navigate = useNavigate();




    const [ modalDelete,       setModalDelete     ] = useState(false);


    const navigateToAggiorna = (id, event) => {
        event.stopPropagation();
        navigate(`/keypeople/modifica/${valori.id}`, { state: { ...valori } });
    };


    const handleOpenModalDelete = (event) => {
        event.stopPropagation();
        setModalDelete(true);
    };

    const handleCloseModalDelete = (event) => {
        setModalDelete(false);
    };
    


    const confirmDelete = (id, event) => {
        onDelete();
        handleCloseModalDelete(true);
    };


    return (
        <Card
        onClick={(event) => navigateToAggiorna(valori.id, event)}
        raised
        sx={{
            borderRadius: '20px', 
            maxWidth: '80%', 
            justifyContent: 'center', 
            margin: 'auto', 
            cursor: 'pointer', 
            height: 'auto', 
            border: '2px solid #00853C', 
            transition: 'transform 0.3s ease, border-width 0.3s ease', 
            '&:hover': {
            transform: 'scale(1.05)', 
            border: '4px solid #00853C' 
        }}
    }
    >
        <CardContent>
            {/* Contenuto della Card */}
            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <Typography
                gutterBottom
                variant="h5"
                component="div"
                sx={{
                color: 'black',
                fontWeight: 'bold',
                overflow: 'hidden',
                whiteSpace: 'nowrap',
                textOverflow: 'ellipsis',
                width: '100%' 
                }}
            >
                {valori.nome} {valori.cognome}
            </Typography>

            <Button onClick={handleOpenModalDelete} variant="outlined" sx={{backgroundColor: 'transparent', border: 'none', color: '#898989', '&:hover': { border: 'none', color: 'red', transform: 'scale(1.1)'}}}startIcon={<CloseIcon sx={{backgroundColor: 'transparent'}}/>}/>

            </Box>

            <Typography variant='body2' color='text.secondary' sx={{ color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1 }}>
                <BusinessCenterIcon sx={{ color: '#00853C', mr: 1 }} />
                {valori.ruolo}
            </Typography>

            <Typography variant="body2" color="text.primary"  sx={{  color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1 }}>
                    <EmailIcon sx={{ color: '#00853C', mr: 1 }} />
                    {valori.email}
            </Typography>

            <Typography variant='body2' color='text.secondary' sx={{ color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1}}>
                <LocalPhoneIcon sx={{ color: '#00853C', mr: 1}} />
                {valori.cellulare}
            </Typography>

        </CardContent>

        <Modal
                open={modalDelete}
                onClose={handleCloseModalDelete}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
                onClick={(event) => event.stopPropagation()}
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                }}
                >
                    <Box
                            sx={{
                            backgroundColor: 'white',
                            p: 4,
                            borderRadius: 2,
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            flexDirection: 'column',
                            gap: 2,
                            width: '40vw',
                            }}
                            >
                            <Typography id="modal-modal-title" variant="h6" component="h2">
                            Sei sicuro di voler eliminare il contatto?
                            </Typography>
                            <Box 
                            sx={{
                                display: 'flex',
                                flexDirection: 'row',
                                justifyContent: 'center',
                                gap: 3
                            }}>
                                <Button
                                onClick={handleCloseModalDelete}
                                sx={{
                                    backgroundColor: 'black',
                                    color: 'white',
                                    borderRadius: '5px',
                                    '&:hover': {
                                        backgroundColor: 'black',
                                        color: 'white',
                                        transform: 'scale(1.05)'
                                    },
                                }}>
                                Indietro
                            </Button>
                            <Button
                            onClick={confirmDelete}
                            sx={{
                                backgroundColor: '#00853C',
                                color: 'white',
                                borderRadius: '5px',
                                '&:hover': {
                                    backgroundColor: '#00853C',
                                    color: 'white',
                                    transform: 'scale(1.05)'
                                },
                            }}>
                                Conferma
                            </Button>
                            </Box>
                            </Box>
                </Modal>
        <CardActions>
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'space-between',
                alignItems: 'center',
                width: '100%',
                mr:2
            }} >
                <Box
                sx={{
                    display: 'flex',
                    gap: 3,
                    mr:3
                    
                }}>
            {/* <Button 
            size="small"
            onClick={(event) => navigateToAggiorna(valori.id, event)}
            sx={{
                backgroundColor: 'black',
                color: 'white',
                ml: 1,
                mb: 1,
                textTransform: 'lowercase',
                fontWeight: 'bold',
                '&:hover': {
                    backgroundColor: 'black',
                    transform: 'scale(1.05)',
                    },
                }}>Aggiorna</Button> */}
                    </Box>
                    <Typography
                    gutterBottom
                    variant="h6"
                    component="div"
                    sx={{
                    fontWeight: 'bold',
                    overflow: 'hidden',
                    whiteSpace: 'nowrap',
                    textOverflow: 'ellipsis',
                    maxWidth: '70%',
                    color: '#00853C',
                    }}
                >
                    {valori.cliente.denominazione}
                </Typography>
                    </Box>
        </CardActions>
    </Card>
    );
};

export default KeypeopleCard;