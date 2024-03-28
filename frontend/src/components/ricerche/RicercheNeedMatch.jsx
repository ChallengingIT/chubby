import React, { useState } from 'react';
import { Button, Box, Grid, Select, MenuItem, FormControl, InputLabel, IconButton, Drawer, Typography, TextField, InputAdornment, Autocomplete } from '@mui/material';
import CloseIcon                                        from '@mui/icons-material/Close';
import SearchIcon                                       from '@mui/icons-material/Search';
import { useNavigate  }                                 from 'react-router-dom';


function RicercheNeedMatch({ filtri, onFilterChange, onReset, tipologiaOptions, tipoOptions, seniorityOptions, onRicerche, onGoBack}) {

    const navigate = useNavigate();

    const [ openFiltri, setOpenFiltri ] = useState(false);

    const handleOpenFiltri = () => setOpenFiltri(true);
    const handleCloseFiltri = () => setOpenFiltri(false);

    const handleReset = (filter) => {
        onFilterChange(filter)('');
    };

 


    
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
        onClick={onGoBack}
        style={{
            backgroundColor: '#00853C',
            color: 'white', 
            border: 'none', 
            fontSize: '0.8rem', 
            cursor: 'pointer', 
            display: 'flex', 
            alignItems: 'center', 
            padding: '0.5rem 1rem', 
            outline: 'none', 
            borderRadius: '10px',

        }}
        >
        <span style={{ marginRight: '0.5rem' }}>{"<"}</span> 
        Indietro
        </Button>


        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
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
                                id="tipo-combo-box"
                                options={tipoOptions}
                                getOptionLabel={(option) => option.label}
                                value={tipoOptions.find(option => option.value === filtri.tipo) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('tipo')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Tipologia" />}
                            />
                        {/* <InputLabel id="tipo-label">Tipologia</InputLabel>
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
                            <MenuItem key={option.value} value={option.value}
                            >
                                {option.label}
                            </MenuItem>
                            ))}
                        </Select> */}
                        </FormControl>

                        <FormControl fullWidth sx={{ mb: 2 }}>
                        <Autocomplete
                                id="tipologia-combo-box"
                                options={tipologiaOptions}
                                getOptionLabel={(option) => option.label}
                                value={tipologiaOptions.find(option => option.value === filtri.tipologia) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('tipologia')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Jobtitle" />}
                            />
                        {/* <InputLabel id="tipologia-label">JobTitle</InputLabel>
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
                            <MenuItem key={option.value} value={option.value}
                            >
                                {option.label}
                            </MenuItem>
                            ))}
                        </Select> */}
                        </FormControl>

                        <FormControl fullWidth sx={{ mb: 2 }}>
                        <Autocomplete
                                id="seniority-combo-box"
                                options={seniorityOptions}
                                getOptionLabel={(option) => option.label}
                                value={seniorityOptions.find(option => option.value === filtri.seniority) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('seniority')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Seniority" />}
                            />
                        {/* <InputLabel id="seniority-label">Seniority</InputLabel>
                        <Select
                            labelId="seniority-label"
                            displayEmpty
                            value={filtri.seniority || ''} 
                            onChange={onFilterChange('seniority')}
                            renderValue={(selected) => {
                                if (selected === '') {
                                    return <em></em>;
                                }
                                const selectedLabel = seniorityOptions.find(option => option.value === selected)?.label;
                                return selectedLabel || selected;
                            }}
                        >
                            
                            {seniorityOptions.map((option) => (
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
export default RicercheNeedMatch;