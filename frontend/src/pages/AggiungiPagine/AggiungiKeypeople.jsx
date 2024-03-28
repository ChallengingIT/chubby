import React, { useState, useEffect }   from "react";
import { useNavigate }                  from "react-router-dom";
import axios                            from "axios";
import { Box, Typography, Alert, Snackbar }              from "@mui/material";
import FieldBoxFile                     from "../../components/FieldBoxFile";


const AggiungiKeypeople = () => {

  const navigate = useNavigate();

  const [ aziendeOptions, setAziendeOptions] = useState([]);
  const [ ownerOptions,   setOwnerOptions  ] = useState([]);
  const [ alert,           setAlert          ] = useState({ open: false, message: '' });

  const user = JSON.parse(localStorage.getItem("user"));
      const accessToken = user?.accessToken;
  
      const headers = {
        Authorization: `Bearer ${accessToken}`
      };

  useEffect(() => {
    const fetchAziendeOptions = async () => {
      try {
        const aziendeResponse = await axios.get("/aziende/react/select",       { headers: headers });
        const ownerResponse   = await axios.get("/aziende/react/owner",        { headers: headers });
        if (Array.isArray(ownerResponse.data)) {
          const ownerOptions = ownerResponse.data.map((owner) => ({
            label: owner.descrizione,
            value: owner.id,
          }));
          setOwnerOptions(ownerOptions);
        } else {
          console.error("I dati ottenuti non sono nel formato Array:", ownerResponse.data);
        }


        if (Array.isArray(aziendeResponse.data)) {
          const aziendeOptions = aziendeResponse.data.map((aziende) => ({
            label: aziende.denominazione,
            value: aziende.id,
          }));
          setAziendeOptions(aziendeOptions);
        } else {
          console.error("I dati ottenuti non sono nel formato Array:", aziendeResponse.data);
        }
      } catch (error) {
        console.error("Errore durante il recupero delle province:", error);
      }
    };

    fetchAziendeOptions();
  }, []);


  const handleCloseAlert = (event, reason) => {
    if (reason === 'clickaway') {
        return;
    }
    setAlert({ ...alert, open: false });
};


  const campiObbligatori = ["nome", "idAzienda", "email", "idOwner", "status", "ruolo", "dataCreazione"];

  const fields = [
    { label: "Nome Contatto*",        name: "nome",                 type: "text" },
    { label: "Azienda*",              name: "idAzienda",            type: "select",      options: aziendeOptions },
    { label: "Email*",                name: "email",                type: "text" },
    { label: "Cellulare",             name: "cellulare",            type: "text" },
    { label: "Owner*",                name: "idOwner",              type: "select",      options: ownerOptions},
    { label: "Stato*",                name: "status",               type: "select",      options: [
      { value: 1, label: "Verde" },
      { value: 2, label: "Giallo" },
      { value: 3, label: "Rosso" },
    ] },
    
    { label: "Ruolo*",                name: "ruolo",               type: "text" },
    { label: "Data di Creazione*",    name: "dataCreazione",       type: "date" },
    { label: 'Ultima attività',       name: 'dataUltimaAttivita',  type: 'date' },
    { label: "Note",                  name: "note",                type: "note" },
  ];


  const handleSubmit = async (values) => {
    const errors = validateFields(values);
    const hasErrors = Object.keys(errors).length > 0;
    if (!hasErrors) {
    try {
      const response = await axios.post("/keypeople/react/salva", values, {
        headers: headers
      });
      if (response.data === "DUPLICATO") {
        setAlert({ open: true, message: "Email già utilizzata!" });
        console.error("L'email fornita è già in uso.");
        return; 
      }

      navigate("/keyPeople");
    } catch (error) {
      console.error("Errore durante il salvataggio:", error);
    }
  }
  };

  const validateFields = (values) => {
    let errors = {};
    campiObbligatori.forEach(field => {
      if (!values[field]) {
        errors[field] = 'Questo campo è obbligatorio';
      }
    });
    return errors;
  };
  

  return (
    <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: 'auto',minHeight: '100vh', width: '100vw', flexDirection: 'column' }}>
      <Box sx={{ flexGrow: 1, p: 3, marginLeft: '12.2em', marginTop: '0.5em', marginBottom: '0.8em', marginRight: '0.8em', backgroundColor: '#FEFCFD', borderRadius: '10px', display: 'flex', justifyContent: 'center', flexDirection: 'column' }}>
      <Snackbar open={alert.open} autoHideDuration={6000} onClose={handleCloseAlert} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
                <Alert onClose={handleCloseAlert} severity="error" sx={{ width: '100%' }}>
                    {alert.message}
                </Alert>
            </Snackbar>
          <Typography variant="h4" component="h1" sx={{ mt: 3, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>Aggiungi contatto</Typography>

          <FieldBoxFile
          fields={fields}
          campiObbligatori={campiObbligatori}
          onSubmit={handleSubmit}
          title=""
          />
          </Box>
    </Box>
  );
};

export default AggiungiKeypeople;
