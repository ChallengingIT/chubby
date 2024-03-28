import React, {useState, useEffect}                 from 'react';
import { useNavigate }                      from 'react-router-dom';
import CloseIcon                            from '@mui/icons-material/Close';
import FactoryIcon                          from '@mui/icons-material/Factory';
import PlaceIcon                            from '@mui/icons-material/Place';
import { 
    Card, 
    CardContent, 
    Box,
    Typography,
    Button,
    CardActions,
    Modal
    } from '@mui/material';


const AziendeCard = ({valori, onDelete}) => {


    const [ modalDelete,       setModalDelete     ] = useState(false);


    const navigate = useNavigate();





    const getCardStyle = (tipologia) => {
        switch (tipologia) {

            case 'PROSPECT':
                return {
                    borderRadius: '20px', 
                    maxWidth: '80%', 
                    justifyContent: 'center', 
                    margin: 'auto', 
                    cursor: 'pointer', 
                    height: 'auto',
                    borde: '2px solid #ffae44', // Blu navy
                    transition: 'transform 0.3s ease, border-width 0.3s ease', 

                    '&:hover': {
                        transform: 'scale(1.05)', 
                        border: '4px solid #ffae44'
                    }
                };
                case 'Prospect':
                return {
                    borderRadius: '20px', 
                    maxWidth: '80%', 
                    justifyContent: 'center', 
                    margin: 'auto', 
                    cursor: 'pointer', 
                    height: 'auto',
                    border: '2px solid #ffae44', // Blu navy
                    transition: 'transform 0.3s ease, border-width 0.3s ease', 

                    '&:hover': {
                        transform: 'scale(1.05)', 
                        border: '4px solid #ffae44'
                    }
                };
                
            case 'EXCLIENTE':
                return {
                    backgroundColor: '#f0f0f0', // Grigio Chiaro
                    borderColor: '#f0f0f0'
                    // pointerEvents: 'none', // Disabilita interazioni
                    // opacity: 0.5 
                };
                case 'Ex cliente':
                return {
                    backgroundColor: '#f0f0f0', // Grigio Chiaro
                    borderColor: '#f0f0f0'
                    // pointerEvents: 'none', // Disabilita interazioni
                    // opacity: 0.5 
                };
            default:
                return { 
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
                }};
        }
    };

    const cardStyle = {
        borderRadius: '20px',
        maxWidth: '80%',
        justifyContent: 'center',
        margin: 'auto',
        cursor: 'pointer',
        height: 'auto',
        border: '2px solid', 
        transition: 'transform 0.3s ease, border-width 0.3s ease',
        ...getCardStyle(valori.tipologia) 
    };


    const navigateToAssocia = (id) => {
        navigate(`/need/${valori.id}`, { state: {...valori}});
    };
    

    const navigateToAggiorna = (id, event) => {
        event.stopPropagation();
        navigate(`/aziende/modifica/${valori.id}`, { state: { ...valori } });
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
            raised 
            sx={cardStyle}
            onClick={() => navigateToAssocia(valori.id)}
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
                {valori.denominazione}
            </Typography>
            <Button onClick={handleOpenModalDelete} variant="outlined" sx={{backgroundColor: 'transparent', border: 'none', color: '#898989', '&:hover': { border: 'none', color: 'red', transform: 'scale(1.1)'}}}startIcon={<CloseIcon sx={{backgroundColor: 'transparent'}}/>}/>
            </Box>

            <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5, color: 'black' }}>
                {valori.citta}
            </Typography>

            {/* <Typography variant="body2" color="text.secondary" sx={{ mb: 2, color: 'black' }}>
                {valori.sedeOperativa}
            </Typography> */}


            {/* <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5, color: 'black' }}>
                {valori.sedeOperativa}
            </Typography> */}

            <Typography variant="body2" color="text.primary"  sx={{  color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1 }}>
                    <PlaceIcon sx={{ color: '#00853C', mr: 1 }} />
                    {valori.sedeOperativa}
            </Typography>

            <Typography variant='body2' color='text.secondary' sx={{ color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1 }}>
                <FactoryIcon sx={{ color: '#00853C', mr: 1 }} />
                {valori.settoreMercato}
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
                            Sei sicuro di voler eliminare l'azienda?
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
            <Button 
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
                }}>Aggiorna</Button>
                    </Box>
                    </Box>
        </CardActions>
    </Card>
    );
};

export default AziendeCard;