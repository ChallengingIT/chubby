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

const IntervistaBox = ({
  fields,
  initialValues,
  onSave,
  title = 'Form',
  disableFields,
  campiObbligatori,
  showSaveButton = true,
  statoOptions,
  tipologiaOptions,
  ownerOptions,
  tipoIntervistaOptions
  
}) => {
  const [currentGroupIndex, setCurrentGroupIndex] = useState(0);
  const [errors, setErrors] = useState({});
  const [values, setValues] = useState({ ...initialValues, dateOra: new Date().toISOString().slice(0, 16) });
  const navigate = useNavigate();


const getDisabledStyles = (isDisabled) => {
  return isDisabled ? { color: 'black' } : {};
};


const validate = () => {
  let tempErrors = {};
  campiObbligatori.forEach(field => {
    if (!values[field]) {
      tempErrors[field] = 'Campo obbligatorio';
    }
  });
  setErrors(tempErrors);
  return !Object.keys(tempErrors).length;
};


  const handleInputChange = (e) => {
    const { name, value, type, options } = e.target;
    if (type === 'select') {
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
      setErrors({ ...errors, [name]: '' });
    }
  };


  const handleSubmit = (e) => {
    e.preventDefault();
    if (onSave) {
      onSave(values);
    }
  };
  const handleNext = () => {
    if (currentGroupIndex < groupedFields.length - 1) {
      setCurrentGroupIndex(currentGroupIndex + 1);
    }
  };

  const handleBack = () => {
    if (currentGroupIndex > 0) {
      setCurrentGroupIndex(currentGroupIndex - 1);
    }
  };


  const groupFields = (fields) => {
    const groupedFields = [];
    let currentGroup = [];
    fields.forEach((field) => {
      if (field.type === 'titleGroups') {
        if (currentGroup.length > 0) {
          groupedFields.push(currentGroup);
        }
        currentGroup = [field];
      } else {
        currentGroup.push(field);
      }
    });
    if (currentGroup.length > 0) {
      groupedFields.push(currentGroup);
    }
    return groupedFields;
  };

  const renderField = (field, groupLength) => {


    const { xs, sm, md, lg, xl } = field;


    const isDisabled = disableFields[field.name];
    
    switch (field.type) {

      case 'select':
        
        return (
          <Grid item xs={xs} sm={12} md={md} lg={lg} xl={xl} key={field.name}>

          <FormControl fullWidth key={field.name} sx={{ marginBottom: '1.5em', marginTop: '1.5em', flexGrow: 1 }}>
            <InputLabel>{field.label}</InputLabel>
            <Select
              name={field.name}
              value={values[field.name] || []}
              onChange={handleInputChange}
              disabled={isDisabled}
              sx={{ width: '100%', textAlign: 'left' }}
              
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
              marginBottom: '60px',
              textAlign: 'center',
              borderBottom: '1px solid #000',
              paddingBottom: '5px',
              marginTop: '80px',
              gridColumn: '1 / -1',
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
            disabled={isDisabled}
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
                disabled={isDisabled}
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
              disabled={isDisabled}
              multiline={field.type === 'note'}
              fullWidth
              InputLabelProps={field.type === 'date' ? { shrink: true } : undefined}
              type={field.type === 'date' ? 'date' : 'text'}
              sx={{  marginBottom: '1.5em', marginTop: '1.5em'}}
              />
          </Grid>
          
        );

        case 'number':
          return (
          <Grid item xs={xs} sm={12} md={md} lg={lg} xl={xl} key={field.name}>
          <TextField
            label={field.label}
            name={field.name}
            value={values[field.name] || ''}
            onChange={handleInputChange}
            disabled={isDisabled}
            multiline={field.type === 'note'}
            fullWidth
            InputLabelProps={field.type === 'date' ? { shrink: true } : undefined}
            type={field.type === 'date' ? 'date' : 'number'}
            sx={{  marginBottom: '1.5em', marginTop: '1.5em'}}
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
                disabled={isDisabled}
                multiline
                fullWidth
                InputLabelProps={field.type === 'date' ? { shrink: true } : undefined}
                type={field.type === 'date' ? 'date' : 'text'}
                sx={{ width: '100%', marginBottom: '16px', marginTop: '16px', display: 'flex', flexGrow: 1 }}
              />
            </Grid>
          );



      default:
        return null;
    }
  };

  const groupedFields = groupFields(fields);
  const isLastGroup = currentGroupIndex === groupedFields.length - 1;

  return (
    <Box
    // sx={{ flexDirection: 'column',
    // width: '90%',
    // height: 'auto',
    // margin: 'auto',
    // padding: '30px',
    // backgroundColor: 'white',
    // borderRadius: '20px',
    // justifyItems: 'center',
    // boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.5)', }}
    sx={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      margin: 'auto'
  }}>
      <form onSubmit={handleSubmit}>
        {/* Renderizza qui il titolo del gruppo corrente */}
        {groupedFields[currentGroupIndex][0].type === 'titleGroups' && (
          <Typography
            variant="h6"
            sx={{
              textAlign: 'center',
              borderBottom: '1px solid #000',
              paddingBottom: '5px',
              marginTop: '20px',
              marginBottom: '20px',
            }}
          >
            {groupedFields[currentGroupIndex][0].label}
          </Typography>
        )}
        {/* Renderizza i campi del gruppo corrente, escludendo il primo elemento se è un titleGroup */}
        <Grid container spacing={3}>
          {groupedFields[currentGroupIndex].slice(groupedFields[currentGroupIndex][0].type === 'titleGroups' ? 1 : 0).map((field, index) => (
            <Grid item xs={field.xs} sm={field.sm} md={field.md} lg={field.lg} xl={field.xl} key={index}>
                <Box sx={{ display: 'flex', justifyContent: 'center', gap: 3, width: '100%' }}>
              {renderField(field)}
              </Box>
            </Grid>
          ))}
        </Grid>
        <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '2%', gap: 6 }}>
        {currentGroupIndex > 0 && (
            <Button onClick={() => setCurrentGroupIndex(currentGroupIndex - 1)} sx={{
                width: '250px',
                backgroundColor: "black",
                color: "white",
                fontWeight:"bold",
                boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)',
                borderRadius: '10px',
                "&:hover": {
                  backgroundColor: "black",
                  transform: "scale(1.05)",
                  boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)',
                  borderRadius: '10px',
                },
            }}>Indietro</Button>
          )}
          {currentGroupIndex < groupedFields.length - 1 && (
            <Button onClick={() => setCurrentGroupIndex(currentGroupIndex + 1)} sx={{ width: '250px',
                  backgroundColor: "#00853C",
                  color: "white",
                  fontWeight:"bold",
                  boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)',
                  borderRadius: '10px',
                  
                  "&:hover": {
                    backgroundColor: "#00853C",
                    color: "white",
                    transform: "scale(1.05)",
                    boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)',
                    borderRadius: '10px',
                  },}}>Avanti</Button>
          )}
          {currentGroupIndex === groupedFields.length - 1 && showSaveButton && (
            <Button type="submit" sx={{
                width: '250px',
                backgroundColor: "#00853C",
                color: "white",
                fontWeight:"bold",
                boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)',
                borderRadius: '10px',
                
                "&:hover": {
                  backgroundColor: "#00853C",
                  color: "white",
                  transform: "scale(1.05)",
                  boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)',
                  borderRadius: '10px',
                },
            }}>Salva</Button>
          )}
        </Box>
      </form>
    </Box>
  );
};

export default IntervistaBox;
