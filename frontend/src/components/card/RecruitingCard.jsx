import React                                from 'react';
import { useNavigate }                      from 'react-router-dom';
import ChecklistIcon                        from '@mui/icons-material/Checklist';
import WorkIcon                             from '@mui/icons-material/Work';
import EmailIcon                            from '@mui/icons-material/Email';

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


    const RecruitingCard = ({ valori }) => {

        const navigate = useNavigate();

        const navigateToIntervista = (id, event) => {
            event.stopPropagation();
            navigate(`/recruiting/intervista/${valori.id}`, { state: { ...valori}});
        };


        const navigateToAggiorna = (id, event) => {
            event.stopPropagation();
            navigate(`/recruiting/modifica/${valori.id}`, { state: { ...valori}});
        };

        const timeDifference = calculateDataDifference(valori.week);
        const skillsToShow = valori.skills.slice(0, 3).map(skill => skill.descrizione).join(', ');
        const additionalSkillsCount = valori.skills.length > 3 ? `e altre ${valori.skills.length - 3}` : '';
    

        return (
            <Card
            // onClick={handleCardClick}
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
                </Box>
                <Typography variant="body2" color="text.primary" sx={{ mb: 0.5, color: 'black' }}>
                    {timeDifference}
                </Typography>

                <Typography variant="body2" color="text.primary"  sx={{  color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1 }}>
                    <EmailIcon sx={{ color: '#00853C', mr: 1 }} />
                    {valori.email}
                </Typography>
    
                <Typography variant="body2" color="text.primary"  sx={{  color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1 }}>
                        <WorkIcon sx={{ color: '#00853C', mr: 1 }} />
                        {valori.tipologia && valori.tipologia.descrizione
                        ? valori.tipologia.descrizione
                        : "N/A"}
                        </Typography>

    
                <Typography variant="body2" color="text.primary"  sx={{  color: 'black', display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-end', mt: 1, mb: 1 }}>
                        <ChecklistIcon sx={{ color: '#00853C', mr: 1 }} />
                        Competenze: {skillsToShow} {additionalSkillsCount}
                </Typography>
            </CardContent>
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
                    backgroundColor: '#00853C',
                    color: 'white',
                    ml: 1,
                    '&:hover': {
                        backgroundColor: '#00853C',
                        transform: 'scale(1.05)',
                        },
                    }}>Aggiorna</Button>
                    <Button
                        size="small"
                        onClick={(event) => navigateToIntervista(valori.id, event)}
                        sx={{
                        backgroundColor: '#000000',
                        color: 'white',
                        '&:hover': {
                            backgroundColor: '#000000',
                            transform: 'scale(1.05)',
                        },
                        }}>Intervista</Button>
                        </Box>
                        <Typography
                        gutterBottom
                        variant="h6"
                        component="div"
                        sx={{
                        // color: 'black',
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
    
    export default RecruitingCard;

