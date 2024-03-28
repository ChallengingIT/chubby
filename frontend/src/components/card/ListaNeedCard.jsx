import React                                from 'react';
import { useNavigate }                      from 'react-router-dom';
import AutoModeIcon                         from '@mui/icons-material/AutoMode'; //stato

import { 
    Card, 
    CardContent, 
    Box,
    Typography,
    Button,
    CardActions,
    } from '@mui/material';


    const calculateDataDifference = (weekString) => {
        const [ year, week ] = weekString.split('-W');
        const firstDayOfYear = new Date(year, 0, 1);
        const weekDate = new Date(firstDayOfYear.setDate(firstDayOfYear.getDate() + (week - 1) * 7));
        weekDate.setDate(weekDate.getDate() - weekDate.getDay());
        const currentDate = new Date();
        const diffInMs = currentDate - weekDate;
        const diffInDays = diffInMs / (1000 * 60 * 60 * 24);

        if (diffInDays < 7) {
            return `${Math.round(diffInDays)} giorno${Math.round(diffInDays) !== 1 ? 'i' : ''} fa`;
        } else {
            const diffInWeeks = diffInDays / 7;
            return `${Math.round(diffInWeeks)} settiman${Math.round(diffInWeeks) !== 1 ? 'e' : ''} fa`;
        }
    };

    const ListaNeedCard = ({valori}) => {

        const navigate = useNavigate();

        const timeDifference = calculateDataDifference(valori.week);


        const navigateToAggiorna = (id, event) => {
            event.stopPropagation();
            navigate(`/need/modifica/${valori.id}`, { state: {...valori }});
        };


        return (
            <Card
                raised
                onClick={(event) => navigateToAggiorna(valori.id, event)}
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
            }>
            <CardContent>
                {/* Contenuto della Card */}
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
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
                    width: '100%',
                    p: 1
                    }}
                >
                    {valori.descrizione}
                </Typography>
                </Box>


                <Typography variant="body2" color="text.primary" sx={{ mb: 0.5, color: 'black', pl: 1 }}>
                {timeDifference}
                </Typography>

                {/* <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5, color: 'black' }}>
                {valori.stato && valori.stato.descrizione}
                </Typography> */}

                <Typography variant="body2" color="text.primary"  sx={{  color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1, pl: 1 }}>
                    <AutoModeIcon sx={{ color: '#00853C', mr: 1 }} />
                    {valori.stato && valori.stato.descrizione}
            </Typography>

            </CardContent>
            <CardActions>
            <Box
            sx={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'flex-start',
                alignItems: 'center',
                width: '100%',
                mr:2
            }} >
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
                    }}>Modifica</Button> */}
            </Box>

            </CardActions>
            </Card>
        );
    };

    export default ListaNeedCard;