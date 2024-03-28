import React, { useState, useEffect }                                   from "react";
import { useNavigate, useLocation }                                     from "react-router-dom";
import axios                                                            from "axios";
import { Button, Box, Typography, Alert, Snackbar }                     from "@mui/material";
import IntervistaBox                                                    from "../../components/IntervistaBox";

const AggiungiIntervista = () => {

const navigate      = useNavigate();
const location      = useLocation();
const candidatoID   = location.state?.candidatoID;


const user = JSON.parse(localStorage.getItem("user"));
const accessToken = user?.accessToken;

const headers = {
  Authorization: `Bearer ${accessToken}`
};


  const [ tipologiaOptions,           setTipologiaOptions             ] = useState([]); //jobtile
  const [ ownerOptions,               setOwnerOptions                 ] = useState([]);
  const [ statoOptions,               setStatoOptions                 ] = useState([]); //tipologiaIncontro
  const [ tipoIntervistaOptions,      setTipoIntervistaOptions        ] = useState([]); //follow up
  const [ interviste,                 setInterviste                   ] = useState([]);
  const [ candidato,                  setCandidato                    ] = useState([]);
  const [ isDataLoaded,               setIsDataLoaded                 ] = useState(false);
  const [ alert,                      setAlert                        ] = useState(false);


  //stati per la paginazione
  const [ pagina, setPagina ] = useState([]);
  const quantita = 10;



useEffect(() => {
const fetchData = async () => {
  const paginazione = {
    pagina: 0,
    quantita: 10,
  }
    try {
      //jobtitle = tipologia, tipologiaIncontro = stato, owner = owner
    const responseTipologia                      = await axios.get("/aziende/react/tipologia"                  , { headers: headers });
    const ownerResponse                          = await axios.get("/aziende/react/owner"                      , { headers: headers });
    const responseStato                          = await axios.get("/staffing/react/stato/candidato"           , { headers: headers });
    const responseTipoIntervista                 = await axios.get("/intervista/react/tipointervista"          , { headers: headers });
    const responseIntervista                     = await axios.get(`/intervista/react/mod/${candidatoID}`      , { headers: headers, params: paginazione }); //questa è la lista delle interviste di cui devo prendere sempre l'ultima
    const responseCandidato                      = await axios.get(`/staffing/react/${candidatoID}`            , { headers: headers }); //questo è il candidato



    if (Array.isArray(responseIntervista.data) && responseIntervista.data.length > 0) {
        // Prendo l'ultima intervista per data
        const ultimaIntervista = responseIntervista.data[responseIntervista.data.length - 1];
        setInterviste(ultimaIntervista);
    } else if (responseIntervista.data.length === 0) {

    } else {
        console.error("I dati ottenuti non sono nel formato Array:", responseIntervista.data);
    }
    
    if (responseCandidato.data && typeof responseCandidato.data === 'object' && !Array.isArray(responseCandidato.data)) {
        setCandidato(responseCandidato.data);
    } else {
        console.error("I dati ottenuti non sono in formato oggetto:", responseCandidato.data);
    }


    if (Array.isArray(responseTipoIntervista.data)) {
        const tipoIntervistaOptions = responseTipoIntervista.data.map((tipoIntervista) => ({
        label: tipoIntervista.descrizione,
        value: tipoIntervista.id,
        }));
        setTipoIntervistaOptions(tipoIntervistaOptions);


    if (Array.isArray(responseStato.data)) {
        const statoOptions = responseStato.data.map((stato) => ({
        label: stato.descrizione,
        value: stato.id,
        }));
        setStatoOptions(statoOptions);

    if (Array.isArray(ownerResponse.data)) {
        const ownerOptions = ownerResponse.data.map((owner) => ({
        label: owner.descrizione,
        value: owner.id,
        }));
        setOwnerOptions(ownerOptions);

    if (Array.isArray(responseTipologia.data)) {
        const tipologiaOptions = responseTipologia.data.map((tipologia) => ({
        label: tipologia.descrizione,
        value: tipologia.id,
        }));
        setTipologiaOptions(tipologiaOptions);
    }
    }
}
}

setIsDataLoaded(true);
    } catch (error) {
    console.error("Errore durante il recupero delle province:", error);
    }
};

fetchData();
}, []);

const handleGoBack = () => {
    navigate(-1); 
  };

  const handleCloseAlert = (event, reason) => {
    if (reason === 'clickaway') {
        return;
    }
    setAlert({ ...alert, open: false });
};


const campiObbligatori = [ "dataColloquio"];

const fields = [
    { type: "titleGroups",                label: "Informazioni candidato", xs: 12, sm: 12             },
    { label: "Tipologia Incontro",        name: "stato",                  type: "select", options: statoOptions, xs: 12, sm: 4 },
    { label: "Nome",                      name: "nome",                   type: "text", xs: 12, sm: 4},
    { label: "Cognome",                   name: "cognome",                type: "text", xs: 12, sm: 4},
    { label: "Data di Nascita",           name: "dataNascita",            type: "date", xs: 12, sm: 4},
    { label: "Location",                  name: "location",               type: "text", xs: 12, sm: 4},
    { label: "Job Title",                 name: "tipologia",              type: "select", options: tipologiaOptions, xs: 12, sm: 4 },
    { label: "Anni di Esperienza",        name: "anniEsperienza",         type: "text", xs: 12, sm: 4},
    { label: "Data Incontro*",            name: "dataColloquio",          type: "date", xs: 12, sm: 4},
    { label: "Recapiti",                  name: "cellulare",              type: "text", xs: 12, sm: 4},
    { label: "Intervistatore",            name: "idOwner",                type: "select", options: ownerOptions, xs: 12, sm: 4 },




    { type: "titleGroups",                label: "Soft Skills", xs: 12, sm: 12                          },
    { label: "Aderenza Posizione",        name: "aderenza",                type: "number", xs: 12, sm: 4},
    { label: "Coerenza Percorso",         name: "coerenza",                type: "number", xs: 12, sm: 4},
    { label: "Motivazione Posizione",     name: "motivazione",             type: "number", xs: 12, sm: 4},
    { label: "Standing",                  name: "standing",                type: "number", xs: 12, sm: 4},
    { label: "Energia",                   name: "energia",                 type: "number", xs: 12, sm: 4},
    { label: "Comunicazione",             name: "comunicazione",           type: "number", xs: 12, sm: 4},
    { label: "Livello di Inglese",        name: "inglese",                 type: "number", xs: 12, sm: 4},



    { type: "titleGroups",                label: "Hard Skills", xs: 12, sm: 12                          },
    { label: "Competenze vs ruolo",       name: "competenze",              type: "number", xs: 12, sm: 6},
    { label: "Valutazione",               name: "valutazione",             type: "number", xs: 12, sm: 6},



    { type: "titleGroups",                label: "Ultime Osservazioni", xs: 12, sm: 12                 },
    { label: "One word",                  name: "descrizioneCandidatoUna", type: "text", xs: 12, sm: 6},
    { label: "Lo vorresti nel tuo team?", name: "teamSiNo",                type: "text", xs: 12, sm: 6},
    { label: "Descrizione Candidato",     name: "note",                    type: "note", xs: 12, sm: 12},
    


    { type: "titleGroups",                label: "Next Steps", xs: 12, sm: 12},
    { label: "Disponibilità",             name: "disponibilita",           type: "text", xs: 12, sm: 4},
    { label: "RAL Attuale",               name: "attuale",                 type: "text", xs: 12, sm: 4},
    { label: "RAL Desiderata",            name: "desiderata",              type: "text", xs: 12, sm: 4},
    { label: "Proposta economica",        name: "proposta",                type: "text", xs: 12, sm: 4},
    { label: "Follow Up",                 name: "tipo",                    type: "select", options: tipoIntervistaOptions, xs: 12, sm: 4 },
    { label: "Preavviso",                 name: "preavviso",               type: "text", xs: 12, sm: 4},
    { label: "Next Deadline",             name: "dataAggiornamento",       type: "dateOra", xs: 12, sm: 4},
    { label: "Owner next Deadline",       name: "idNextOwner",             type: "select", options: ownerOptions, xs: 12, sm: 4 },

];



const initialValues = {
    stato:                            candidato.stato?.id                           || null,
    nome:                             candidato?.nome                               || null,
    cognome:                          candidato.cognome                             || null,
    dataNascita:                      candidato.dataNascita                         || null,
    location:                         candidato.citta                               || null, 
    tipologia:                        candidato.tipologia?.id                       || null,
    anniEsperienza:                   candidato.anniEsperienza                      || null,
    dataColloquio:                    interviste.dataColloquio                      || null,
    cellulare:                        candidato.cellulare                           || null,
    idOwner:                          interviste.owner?.id                          || null,
    aderenza:                         interviste.aderenza                           || null,
    coerenza:                         interviste.coerenza                           || null,
    motivazione:                      interviste.motivazione                        || null,
    standing:                         interviste.standing                           || null,
    energia:                          interviste.energia                            || null,
    comunicazione:                    interviste.comunicazione                      || null,
    inglese:                          interviste.inglese                            || null,
    competenze:                       interviste.competenze                         || null,
    valutazione:                      interviste.valutazione                        || null,
    descrizioneCandidatoUna:          interviste.descrizioneCandidatoUna            || null,
    teamSiNo:                         interviste.teamSiNo                           || null,
    note:                             interviste.note                               || null,
    disponibilita:                    interviste.disponibilita                      || null,
    attuale:                          interviste.attuale                            || null,
    desiderata:                       interviste.desiderata                         || null,
    proposta:                         interviste.proposta                           || null,
    tipo:                             interviste.tipo?.id                           || null,
    preavviso:                        interviste.preavviso                          || null,
    dataAggiornamento:                interviste.dataAggiornamento                  || null,
    idNextOwner:                      interviste.nextOwner?.id                      || null
};


const disableFields = {
nome:               true,
cognome:            true,
dataNascita:        true,
tipologia:          true,
location:           true,
anniEsperienza:     true,
cellulare:          true,
stato:              true
};

const handleSubmit = async (values) => {
  const errors    = validateFields(values);
  const hasErrors = Object.keys(errors).length > 0;

  if (hasErrors) {
    setAlert({
      open: true,
      message: 'Per favore, inserire la data del colloquio.'
    });
    return; 
  }

    try {
    const note = values.note;
    const modifica = 0; 
    const response = await axios.post("/intervista/react/salva",  values, {
      params: {
        idCandidato: candidatoID,
        note: note,
        modifica: modifica
      },
      headers: headers
    });
    navigate(`/recruiting/intervista/${candidatoID}`);

    } catch (error) {
    console.error("Errore durante il salvataggio:", error);
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
    <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: '100vh', width: '100vw', flexDirection: 'row' }}>
            <Box sx={{ 
              flexGrow: 1, 
              p: 3, 
              marginLeft: '12.2em', 
              marginTop: '0.5em', 
              marginBottom: '0.8em', 
              marginRight: '0.8em', 
              backgroundColor: '#FEFCFD', 
              borderRadius: '10px', 
              display: 'flex', 
              justifyContent: 'center', 
              flexDirection: 'column' 
            }}
            >
        <Typography variant="h4" component="h1" sx={{ mt: 3, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>Aggiungi Intervista</Typography>
            
                <Snackbar open={alert.open} autoHideDuration={6000} onClose={handleCloseAlert} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
                <Alert onClose={handleCloseAlert} severity="error" sx={{ width: '100%' }}>
                    {alert.message}
                </Alert>
            </Snackbar>
        {isDataLoaded ? (
        <IntervistaBox 
        fields={fields} 
        initialValues={initialValues}  
        disableFields={disableFields} 
        onSave={handleSubmit} 
        title="" 
        campiObbligatori={campiObbligatori}
        />
        ) : (
            <div>Caricamento in corso...</div>
        )}

        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <Button
                color="primary"
                onClick={handleGoBack}
                sx={{
                    backgroundColor: "black",
                    borderRadius: '40px',
                    color: "white",
                    width: '250px',
                    height: '30px', 
                    mt: 2,
                    "&:hover": {
                    backgroundColor: "black",
                    transform: "scale(1.05)",
                    },
                }}
                >
                Torna ad Interviste
                </Button>
        </Box>
        </Box>
</Box>
);
};

export default AggiungiIntervista;
