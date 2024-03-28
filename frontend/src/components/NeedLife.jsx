import React, { useState } from 'react';
import { Box, Stepper, Step, StepLabel, Typography, Popover, List, ListItem, ListItemText } from '@mui/material';

const steps = [
  'Need', 'Annuncio', 'Screening', 'Pre Call', 
  'Itw', 'Invio CF', 'QM', 'Follow Up', 
  'Proposta', 'Firma', 'Ingresso'
];

const NeedLife = () => {
  const [activeStep, setActiveStep] = useState(null);
  const [anchorEl, setAnchorEl] = useState(null);

  const handleClick = (step, event) => {
    setActiveStep(step);
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? 'simple-popover' : undefined;

  return (
    <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: '100vh', width: '100vw', flexDirection: 'column' }}>
      <Box sx={{ flexGrow: 1, p: 3, marginTop: '2em', marginBottom: '0.8em', backgroundColor: '#EEEDEE', borderRadius: '10px' }}>
        <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-around', mb: 4 }}>
          <Typography variant="h6">Azienda</Typography>
          <Typography variant="h6">Need</Typography>
        </Box>

        <Box
        sx={{ flexGrow: 1, p: 3, ml: 14 }}>

        <Stepper alternativeLabel>
            {steps.map((label, index) => (
            <Step key={label} completed={false}>
                <StepLabel
                StepIconProps={{
                    onClick: (e) => handleClick(index, e),
                  style: { cursor: 'pointer' } // Add pointer style here
                }}
                >
                {label}
                </StepLabel> 
            </Step>
          ))}
        </Stepper>
        <Popover
          id={id}
          open={open}
          anchorEl={anchorEl}
          onClose={handleClose}
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'left',
          }}
        >
          <List>
            {['Name 1', 'Name 2', 'Name 3'].map((name) => (
              <ListItem button key={name}>
                <ListItemText primary={name} />
              </ListItem>
            ))}
          </List>
        </Popover>
        </Box>
      </Box>
    </Box>
  );
};

export default NeedLife;
