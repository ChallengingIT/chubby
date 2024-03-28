import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Grid,
  TextField,
  Button,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
  Typography,
  Input,
  Box
} from '@mui/material';

const AggiungiBox = ({
    fields,
    initialValues,
    onSave,
    title,
    disableFields,
    campiObbligatori,
    // showSaveButton = true,
}) => {


    const [ values,             setValues               ] = useState({ ...initialValues, dataOra: new Date().toISOString().slice(0, 16)});
    const [ errors,             setErrors               ] = useState({});
    // const [ currentGroupInde,   setCurrentGroupIndex    ] = useState(0);




    const handleInputChange = (event) => {
        const { name, value, type, options } = event.target;
        if ( type === 'select' ) {
            const selectedOptions = Array.from(options)
            .filter((option) => option.selected)
            .map((option) => option.value);
            setValues((prevValues) => ({
                ...prevValues,
                [name]: selectedOptions,
            }));
        } else {
            setValues((prevValues) => ({
                ...prevValues,
                [name]: value,
            }));
        }
        if (errors[name]) {
            setErrors({...errors, [name]: ''});
        }
    };


    // const handleSubmit = (event) => {
    //     event.preventDefault();
    //     if (onSave) {
    //         onSave(values);
    //     }
    // };

    const handleSaveClick = () => {
        if(onSave) {
            onSave(values); 
        }
    };

    
    const renderField = (field) => {

        const { xs, md, lg, xl } = field;

        // const isDisabled = disableFields[field.name];

            switch (field.type) {
                case 'select':
                return (
                    <Grid item xs={xs} sm={12} md={md} lg={lg} xl={xl} key={field.name}>
        
        <FormControl fullWidth key={field.name} sx={{ mr: 3, ml: 3 }}>
        <InputLabel
            sx={{
            textAlign: 'center', 
            }}
        >
            {field.label}
        </InputLabel>
        <Select
            name={field.name}
            value={values[field.name] || []}
            onChange={handleInputChange}
            displayEmpty
            sx={{
                display: 'flex',
                width:'93.5%',
            textAlign: 'left', 
            '.MuiSelect-select': {
                paddingLeft: '50%', 
                transform: 'translateX(-50%)', 
            }
            }}
        >
            {field.options.map((option) => (
            <MenuItem key={option.value || option} value={option.value || option}>
                {option.label || option}
            </MenuItem>
            ))}
        </Select>
        </FormControl>

                    </Grid>
        
                );
            
                case 'titleGroups':
                return (
                    <Typography
                    variant="h6"
                    sx={{
                        mt: 2,
                        mb: 1,
                        textAlign: 'center',
                        gridColumn: '1 / -1',
                        fontWeight: 'normal'
                    }}
                    >
                    {field.label}
                    </Typography>
                );
                case 'dateOra':
                return (
                    <Grid item xs={xs} sm={12} md={md} lg={lg} xl={xl} key={field.name}>
        
                    <TextField
                    type="datetime-local"
                    label={field.label}
                    name={field.name}
                    value={values[field.name] || ''}
                    onChange={handleInputChange}
                    InputLabelProps={{ shrink: true }}
                    // disabled={isDisabled}
                    fullWidth
                    sx={{  marginBottom: '1.5em', marginTop: '1.5em'}}
                    />
                    </Grid>
                );
        
        
                case 'date':
                    return (
                        <Grid item xs={xs} sm={12} md={md} lg={lg} xl={xl} key={field.name}>
            
                        <TextField
                        type="date"
                        label={field.label}
                        name={field.name}
                        value={values[field.name] || ''}
                        onChange={handleInputChange}
                        InputLabelProps={{ shrink: true }}
                        // disabled={isDisabled}
                        fullWidth
                        sx={{  marginBottom: '1.5em', marginTop: '1.5em'}}
                        />
                        </Grid>
                    );
        
                case 'text':
                    return (
                    <Grid item xs={xs} sm={12} md={md} lg={lg} xl={xl} key={field.name}>
                    <TextField
                        label={field.label}
                        name={field.name}
                        value={values[field.name] || ''}
                        onChange={handleInputChange}
                        // disabled={isDisabled}
                        multiline={field.type === 'note'}
                        // fullWidth
                        InputLabelProps={field.type === 'date' ? { shrink: true } : undefined}
                        type={field.type === 'date' ? 'date' : 'text'}
                        sx={{  mr: 3, mb: 0.5, ml: 3, display: 'flex', justifyContent: 'center'}}
                        />
                    </Grid>
                    
                );
        
                case 'note':
                    return (
                    <Grid item  key={field.name} sx={{ width: '100%' }}>
                        <TextField
                        label={field.label}
                        name={field.name}
                        value={values[field.name] || ''}
                        onChange={handleInputChange}
                        // disabled={isDisabled}
                        multiline
                        fullWidth
                        InputLabelProps={field.type === 'date' ? { shrink: true } : undefined}
                        type={field.type === 'date' ? 'date' : 'text'}
                        sx={{ mr: 3, ml: 3, width: '93.5%' }}
                        />
                    </Grid>
                    );
        
        
        
                default:
                return null;
            }
            };

            return (
                <Box sx={{ width: '100%' }}>
                    <Grid container spacing={1} direction="column">
                        {fields.map((field) => (
                            <Grid item key={field.name || field.label} xs={12}>
                                {renderField(field)}
                            </Grid>
                        ))}
                    </Grid>
                    <Box sx={{
                                display: 'flex',
                                justifyContent: 'flex-end',
                                alignItems: 'center',
                                m:1,
                            }}>
                                <Button 
                                onClick={handleSaveClick}
                                sx={{
                                    mt: 4,
                                    mr:3,
                                    mb:4,
                                    backgroundColor: '#00853C',
                                    color: 'white',
                                    fontWeight: 'bold',
                                    '&:hover': {
                                        backgroundColor: '#00853C',
                                        color: 'white',
                                    },
                                }}>
                                    Salva
                                </Button>
                                </Box>
                </Box>
            );


    







};
export default AggiungiBox;