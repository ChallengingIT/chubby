import React, { useState } from 'react';
import { Button, Box, Grid, FormControl, InputLabel, IconButton, Drawer, Typography, TextField, InputAdornment, Autocomplete } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import SearchIcon from '@mui/icons-material/Search';
import { useNavigate } from 'react-router-dom';

function RicercheKeypeople({ filtri, onFilterChange, onReset, aziendaOptions, statoOptions, ownerOptions, onRicerche }) {

    const navigate = useNavigate();

    const [openFiltri, setOpenFiltri] = useState(false);

    const handleOpenFiltri = () => setOpenFiltri(true);
    const handleCloseFiltri = () => setOpenFiltri(false);

    const navigateToAggiungi = () => {
        navigate('/keypeople/aggiungi');
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
                marginBottom: '4rem'
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
                    mt: 2,
                    '&:hover': {
                        backgroundColor: '#00853C',
                        transform: 'scale(1.05)',
                    },
                }}
            >
                + Aggiungi Contatto
            </Button>

            <Box sx={{ display: 'flex', alignItems: 'center', mt: 2 }}>
                <TextField
                    id="search-bar"
                    variant="outlined"
                    placeholder="Cerca Contatto"
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
                        width: '25em',
                        mb: 0.5,
                        '& .MuiOutlinedInput-root': {
                            borderRadius: '0px',
                            '& fieldset': {
                                borderColor: '#00853C',
                                borderRadius: '4px 0 0 4px',
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

            </Box>
            <Button variant="contained" color="primary" onClick={handleOpenFiltri} sx={{
                backgroundColor: '#00853C',
                minWidth: '12em',
                borderRadius: '10px',
                textTransform: 'none',
                mt: 2,
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
                    <Box
                        sx={{
                            display: 'flex',
                            flexDirection: 'row',
                            justifyContent: 'space-between',
                        }}
                    >
                        <Typography variant="h6" sx={{ mb: 2, color: 'black', fontWeight: 'bold' }}>
                            Filtri
                        </Typography>
                        <IconButton
                            onClick={handleCloseFiltri}
                            sx={{ mb: 2 }}
                        >
                            <CloseIcon />
                        </IconButton>
                    </Box>
                </Box>
                <Grid container spacing={2} direction="column" sx={{ p: 2 }}>
                    <Grid item>
                        <FormControl fullWidth sx={{ mb: 2 }}>
                            <Autocomplete
                                id="azienda-combo-box"
                                options={aziendaOptions}
                                getOptionLabel={(option) => option.label}
                                value={aziendaOptions.find(option => option.value === filtri.azienda) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('azienda')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Azienda" />}
                            />
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
                        </FormControl>

                        <FormControl fullWidth sx={{ mb: 2 }}>
                            <Autocomplete
                                id="owner-combo-box"
                                options={ownerOptions}
                                getOptionLabel={(option) => option.label}
                                value={ownerOptions.find(option => option.value === filtri.owner) || null}
                                onChange={(event, newValue) => {
                                    onFilterChange('owner')({ target: { value: newValue?.value || null } });
                                }}
                                renderInput={(params) => <TextField {...params} label="Owner" />}
                            />
                        </FormControl>

                        <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
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

export default RicercheKeypeople;
