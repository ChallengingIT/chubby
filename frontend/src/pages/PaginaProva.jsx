import React, { useState } from 'react';
import { Checkbox, FormControlLabel, Grid, Box } from '@mui/material';



const PaginaProva = () => {
//   const classes = useStyles();
  const [competences, setCompetences] = useState([
    { id: 1, name: 'Competenza 1', selected: false },
    { id: 2, name: 'Competenza 2', selected: false },
    { id: 3, name: 'Competenza 3', selected: false },
    { id: 1, name: 'Competenza 1', selected: false },
    { id: 2, name: 'Competenza 2', selected: false },
    { id: 3, name: 'Competenza 3', selected: false },
    { id: 1, name: 'Competenza 1', selected: false },
    { id: 2, name: 'Competenza 2', selected: false },
    { id: 3, name: 'Competenza 3', selected: false },
    { id: 1, name: 'Competenza 1', selected: false },
    { id: 2, name: 'Competenza 2', selected: false },
    { id: 3, name: 'Competenza 3', selected: false },
    // Aggiungi altre competenze secondo necessità
  ]);

  const handleCompetenceToggle = (index) => {
    const updatedCompetences = [...competences];
    updatedCompetences[index].selected = !updatedCompetences[index].selected;
    setCompetences(updatedCompetences);
  };

  return (
    <Box sx={{ maxHeight: '300px', overflowY: 'scroll', display:'flex', justifyContent: 'center' }}>
      <Grid container spacing={2} >
        {competences.map((competence, index) => (
          <Grid item key={competence.id} sm={4}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={competence.selected}
                  onChange={() => handleCompetenceToggle(index)}
                  color="primary"
                //   sx={{ backgroundColor: 'red'}}
                />
              }
              label={competence.name}
            />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default PaginaProva;
