import React, { useState } from 'react';
import { Button, Box, Grid, Select, MenuItem, FormControl, InputLabel, IconButton, Drawer, Typography, TextField, InputAdornment, Autocomplete } from '@mui/material';
import CloseIcon                                        from '@mui/icons-material/Close';
import SearchIcon                                       from '@mui/icons-material/Search';
import { useNavigate  }                                 from 'react-router-dom';

function RicercheRecruiting({ filtri, onFilterChange, onReset, tipologiaOptions, statoOptions, tipoOptions, onRicerche}) {

    const navigate = useNavigate();

    const [ openFiltri, setOpenFiltri ] = useState(false);

    const handleOpenFiltri = () => setOpenFiltri(true);
    const handleCloseFiltri = () => setOpenFiltri(false);

    const navigateToAggiungi = () => {
        navigate('/recruiting/aggiungi');
    }


    
    return (
        <Box
        sx={{
            backgroundColor: '#FEFCFD', 
            display: 'flex', 
            flexDirection: 'row', 
            alignItems: 'center', 
            justifyContent: 'space-between', 
            borderRadius: '10px',  
            marginBottom: '4rem',
            width: '100%',
            overflow: 'hidden'
        }}
        >
        <Button 
                    variant="contained" 
                    color="primary" 
                    onClick={navigateToAggiungi} 
                    sx={{
                        minWidth: '12em',
                        backgroundColor: '#00853C',
                        borderRadius: '10px',
                        textTransform: 'none',
                        '&:hover': {
                        backgroundColor: '#00853C',
                        transform: 'scale(1.05)',
                    },
                }}>
                    + Aggiungi Candidato
                    </Button>

                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        {/* Barra di ricerca */}
                        <TextField
                        id="search-bar"
                        variant="outlined"
                        placeholder="Nome"
                        size="small"
                        value={filtri.nome}
                        onChange={onFilterChange('nome')}
                        onKeyDown={(event) => {
                            if (event.key === 'Enter') {
                                event.preventDefault();
                                onRicerche();
                            }
                        }}
                        InputProps={{
                            startAdornment: (
                                <InputAdornment position="start">
                                    <SearchIcon sx={{ color: '#00853C' }} />
                                </InputAdornment>
                            ),
                        }}
                        sx={{
                            width: '20em',
                            minWidth: '10em', 
                            mb: 0.5,
                            '& .MuiOutlinedInput-root': {
                                borderRadius: '0px', 
                                '& fieldset': {
                                    borderColor: '#00853C', 
                                    borderRadius: '4px 0 0 4px', 
                                    // borderRight: 'none',
                                },
                                '&:hover fieldset': {
                                    borderColor: '#00853C', 
                                },
                                '&.Mui-focused fieldset': {
                                    borderColor: '#00853C', 
                                },
                            },
                        }}
                    />


                    <TextField
                    id="search-location"
                    variant="outlined"
                    placeholder="Cognome"
                    size="small"
                    value={filtri.cognome}
                    onChange={onFilterChange('cognome')}
                    onKeyDown={(event) => {
                        if (event.key === 'Enter') {
                            event.preventDefault();
                            onRicerche();
                        }
                    }}
                    sx={{
                        borderTopLeftRadius: 0,
                        borderBottomLeftRadius: 0,
                        mb: 0.5,
                        width: '20em',
                        minWidth: '10em',

                        '& .MuiOutlinedInput-root': {
                        borderTopLeftRadius: 0,
                        borderBottomLeftRadius: 0,
                        borderLeft: 0,
                        borderColor: '#00853C',
                        '&:hover fieldset': {
                            borderColor: '#00853C', 
                        },
                        '&.Mui-focused': {
                            borderColor: '#00853C',
                        },
                        },
                        '& .MuiInputLabel-root': {
                        color: 'black', 
                        },
                        '& .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#00853C',
                        },
                        '& .MuiOutlinedInput-input': {
                        color: 'black', 
                        },
                        '& .MuiPlaceholder-root': {
                        color: 'black', 
                        },
                        '& .Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#00853C', 
                        },
                    }}
                    />
                    </Box>
                    <Button variant="contained" color="primary" onClick={handleOpenFiltri} sx={{
                    minWidth: '12em',
                    backgroundColor: '#00853C',
                    borderRadius: '10px',
                    textTransform: 'none',
                    '&:hover': {
                    backgroundColor: '#00853C',
                    transform: 'scale(1.05)',
                    },
                }}>
                    Filtra per: 
                </Button>  

        
                <Drawer
                anchor='right'
                open={openFiltri}
                onClose={handleCloseFiltri}
                sx={{ '& .MuiDrawer-paper': { width: '250px' } }}
            >
                <Box
                    sx={{ width: 250, p: 2 }}
                    role="presentation"
                >
                    <Box sx={{
                        display: 'flex',
                        flexDirection: 'row',
                        justifyContent: 'space-between'
                    }}>
                    <Typography variant="h6" sx={{ mb: 2, color: 'black', fontWeight: 'bold' }}>
                        Filtri
                    </Typography>
                    <IconButton
                        onClick={handleCloseFiltri}
                        sx={{ color: 'black', mb: 2 }}
                    >
                        <CloseIcon />
                    </IconButton>
                    </Box>
                </Box>

                <Grid container spacing={2} direction="column" sx={{ p: 2}}>
                    <Grid item>


                    <FormControl fullWidth sx={{ mb: 2 }}>
                    <Autocomplete
                                id="tipologia-combo-box"
                                options={tipologiaOptions}
                                getOptionLabel={(option) => option.label}
                                value={tipologiaOptions.find(option => option.value === filtri.tipologia) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('tipologia')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Tipologia" />}
                            />
                        {/* <InputLabel id="tipologia-label">Tipologia</InputLabel>
                        <Select
                            labelId="tipologia-label"
                            displayEmpty
                            value={filtri.tipologia || ''} 
                            onChange={onFilterChange('tipologia')}
                            renderValue={(selected) => {
                                if (selected === '') {
                                    return <em></em>;
                                }
                                const selectedLabel = tipologiaOptions.find(option => option.value === selected)?.label;
                                return selectedLabel || selected;
                            }}
                        >
                            
                            {tipologiaOptions.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                            ))}
                        </Select> */}
                        </FormControl>

                        <FormControl fullWidth sx={{ mb: 2 }}>
                        <Autocomplete
                                id="stato-combo-box"
                                options={statoOptions}
                                getOptionLabel={(option) => option.label}
                                value={statoOptions.find(option => option.value === filtri.stato) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('stato')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Stato" />}
                            />
                        {/* <InputLabel id="stato-label">Stato</InputLabel>
                        <Select
                            labelId="stato-label"
                            displayEmpty
                            value={filtri.stato || ''} 
                            onChange={onFilterChange('stato')}
                            renderValue={(selected) => {
                                if (selected === '') {
                                    return <em></em>;
                                }
                                const selectedLabel = statoOptions.find(option => option.value === selected)?.label;
                                return selectedLabel || selected;
                            }}
                        >
                            
                            {statoOptions.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                            ))}
                        </Select> */}
                        </FormControl>

                        <FormControl fullWidth sx={{ mb: 2 }}>
                        <Autocomplete
                                id="tipo-combo-box"
                                options={tipoOptions}
                                getOptionLabel={(option) => option.label}
                                value={tipoOptions.find(option => option.value === filtri.tipo) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('tipo')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Tipo" />}
                            />
                        {/* <InputLabel id="tipo-label">Tipo</InputLabel>
                        <Select
                            labelId="tipo-label"
                            displayEmpty
                            value={filtri.tipo || ''} 
                            onChange={onFilterChange('tipo')}
                            renderValue={(selected) => {
                                if (selected === '') {
                                    return <em></em>;
                                }
                                const selectedLabel = tipoOptions.find(option => option.value === selected)?.label;
                                return selectedLabel || selected;
                            }}
                        >
                            
                            {tipoOptions.map((option) => (
                            <MenuItem key={option.value} value={option.label}>
                                {option.label}
                            </MenuItem>
                            ))}
                        </Select> */}
                        </FormControl>
                        <Box sx={{ display: 'flex', justifyContent: 'flex-end'}}>
                        <Button 
                        onClick={onReset}
                        sx={{
                            backgroundColor: 'black',
                            color: 'white',
                            textTransform: 'lowercase',
                            fontWeight: 'bold',
                            '&:hover': {
                                backgroundColor: 'black',
                                color: 'white',
                                trasform: '1.05'
                            },
                        }}>
                            Reset
                        </Button>
                        </Box>
                    </Grid>
                </Grid>
                </Drawer>
    </Box>


    );





};
export default RicercheRecruiting;