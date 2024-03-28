import React, { useState, useEffect }               from "react";
import { useNavigate, useLocation, useParams }      from "react-router-dom";
import { Button, Box, Typography }                                   from "@mui/material";
import IntervistaBox                                from "../../components/IntervistaBox";
import axios from "axios";

const DettaglioIntervista = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const rowData  = location.state;
  const candidatoID = rowData.candidato.id;

  const [ isDataLoaded,               setIsDataLoaded                 ] = useState(false);
  const [ candidato, setCandidato ] = useState([]);
  const [ loading, setLoading ] = useState(false);


  const user = JSON.parse(localStorage.getItem("user"));
  const accessToken = user?.accessToken;

  const headers = {
    Authorization: `Bearer ${accessToken}`
  };


  const fetchData = async () => {
    try{
      const responseCandidato                      = await axios.get(`/staffing/react/${candidatoID}`            , { headers: headers }); //questo è il candidato
      if (responseCandidato.data && typeof responseCandidato.data === 'object' && !Array.isArray(responseCandidato.data)) {
        setCandidato(responseCandidato.data);
    } else {
        console.error("I dati ottenuti non sono in formato oggetto:", responseCandidato.data);
    }
    setIsDataLoaded(true);
    } catch(error) {
      console.error("Errore durante il recupero dei dati: ", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);



  const handleGoBack = () => {
    navigate(-1); 
  };

  const fields = [
    { type: "titleGroups",                label: "Informazioni candidato", xs: 12, sm: 12             },
    { label: "Tipologia Incontro",        name: "stato",                  type: "text", xs: 12, sm: 4 },
    { label: "Nome",                      name: "nome",                   type: "text", xs: 12, sm: 4},
    { label: "Cognome",                   name: "cognome",                type: "text", xs: 12, sm: 4},
    { label: "Data di Nasciata",          name: "dataNascita",            type: "date", xs: 12, sm: 4},
    { label: "Location",                  name: "location",               type: "text", xs: 12, sm: 4},
    { label: "Job Title",                 name: "tipologia",              type: "text", xs: 12, sm: 4 },
    { label: "Anni di Esperienza",        name: "anniEsperienza",         type: "text", xs: 12, sm: 4},
    { label: "Data Incontro*",            name: "dataColloquio",          type: "date", xs: 12, sm: 4},
    { label: "Recapiti",                  name: "cellulare",              type: "text", xs: 12, sm: 4},
    { label: "Intervistatore",            name: "idOwner",                type: "text",xs: 12, sm: 4 },




    { type: "titleGroups",                label: "Soft Skills", xs: 12, sm: 12                          },
    { label: "Aderenza Posizione",        name: "aderenza",                type: "text", xs: 12, sm: 4},
    { label: "Coerenza Percorso",         name: "coerenza",                type: "text", xs: 12, sm: 4},
    { label: "Motivazione Posizione",     name: "motivazione",             type: "text", xs: 12, sm: 4},
    { label: "Standing",                  name: "standing",                type: "text", xs: 12, sm: 4},
    { label: "Energia",                   name: "energia",                 type: "text", xs: 12, sm: 4},
    { label: "Comunicazione",             name: "comunicazione",           type: "text", xs: 12, sm: 4},
    { label: "Livello di Inglese",        name: "inglese",                 type: "text", xs: 12, sm: 4},



    { type: "titleGroups",                label: "Hard Skills", xs: 12, sm: 12                          },
    { label: "Competenze vs ruolo",       name: "competenze",              type: "text", xs: 12, sm: 6},
    { label: "Valutazione",               name: "valutazione",             type: "text", xs: 12, sm: 6},



    { type: "titleGroups",                label: "Ultime Osservazioni", xs: 12, sm: 12                 },
    { label: "One word",                  name: "descrizioneCandidatoUna", type: "text", xs: 12, sm: 6},
    { label: "Lo vorresti nel tuo team?", name: "teamSiNo",                type: "text", xs: 12, sm: 6},
    { label: "Descrizione Candidato",     name: "note",                    type: "note", xs: 12, sm: 12},
    


    { type: "titleGroups",                label: "Next Steps", xs: 12, sm: 12},
    { label: "Disponibilità",             name: "disponibilita",           type: "text", xs: 12, sm: 4},
    { label: "RAL Attuale",               name: "attuale",                 type: "text", xs: 12, sm: 4},
    { label: "RAL Desiderata",            name: "desiderata",              type: "text", xs: 12, sm: 4},
    { label: "Proposta economica",        name: "proposta",                type: "text", xs: 12, sm: 4},
    { label: "Follow Up",                 name: "tipo",                    type: "text",  xs: 12, sm: 4 },
    { label: "Preavviso",                 name: "preavviso",               type: "text", xs: 12, sm: 4},
    { label: "Next Deadline",             name: "dataAggiornamento",       type: "dateOra", xs: 12, sm: 4},
    { label: "Owner next Deadline",       name: "idNextOwner",             type: "text",  xs: 12, sm: 4 },

];




const initialValues = {
  stato:                            rowData.stato?.descrizione                || "",
  nome:                             rowData.candidato?.nome                   || "",
  cognome:                          rowData.candidato?.cognome                || "",
  dataNascita:                      rowData.dataNascita                       || "",
  location:                         candidato.citta                           || "", 
  tipologia:                        candidato?.tipologia?.descrizione         || "",
  anniEsperienza:                   rowData.anniEsperienza                    || "",
  dataColloquio:                    rowData.dataColloquio                     || "",
  cellulare:                        candidato?.cellulare                      || "",
  idOwner:                          rowData.owner?.descrizione                || "",
  aderenza:                         rowData.aderenza                          || "",
  coerenza:                         rowData.coerenza                          || "",
  motivazione:                      rowData.motivazione                       || "",
  standing:                         rowData.standing?.toString()              || "",
  energia:                          rowData.energia?.toString()               || "",
  comunicazione:                    rowData.comunicazione?.toString()         || "",
  inglese:                          rowData.inglese?.toString()               || "",
  competenze:                       rowData.competenze                        || "",
  valutazione:                      rowData.valutazione                       || "",
  descrizioneCandidatoUna:          rowData.descrizioneCandidatoUna           || "",
  teamSiNo:                         rowData.teamSiNo                          || "",
  note:                             candidato?.note                           || "",
  disponibilita:                    rowData.disponibilita                     || "",
  attuale:                          rowData.attuale                           || "",
  desiderata:                       rowData.desiderata                        || "",
  proposta:                         rowData.proposta                          || "",
  tipo:                             rowData.tipo?.descrizione                 || "",
  preavviso:                        rowData.preavviso                         || "",
  dataAggiornamento:                rowData.dataAggiornamento                 || "", 
  idNextOwner:                      rowData.nextOwner?.descrizione            || ""
};




const disableFields = {
  stato:                      true,
  nome:                       true,
  cognome:                    true,
  dataNascita:                true,
  location:                   true,
  tipologia:                  true,
  anniEsperienza:             true,
  dataColloquio:              true,
  cellulare:                  true,
  owner:                      true,
  aderenza:                   true,
  coerenza:                   true,
  motivazione:                true,
  standing:                   true,
  energia:                    true,
  comunicazione:              true,
  inglese:                    true,
  competenze:                 true,
  valutazione:                true,
  descrizioneCandidatoUna:    true,
  teamSiNo:                   true,
  note:                       true,
  disponibilita:              true,
  attuale:                    true,
  desiderata:                 true,
  proposta:                   true,
  preavviso:                  true,
  dataAggiornamento:          true,
  idNextOwner:                true,
  tipo:                       true,
  idOwner:                    true

};



return (
    <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: '100vh', width: '100vw', flexDirection: 'row' }}>
            <Box sx={{ flexGrow: 1, p: 3, marginLeft: '12.2em', marginTop: '0.5em', marginBottom: '0.8em', marginRight: '0.8em', backgroundColor: '#FEFCFD', borderRadius: '10px', display: 'flex', justifyContent: 'center', flexDirection: 'column' }}>
        <Typography variant="h4" component="h1" sx={{ mt: 3, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>Dettaglio Intervista</Typography>
            
    
        {isDataLoaded ? (
        <IntervistaBox 
        fields={fields} 
        initialValues={initialValues}  
        disableFields={disableFields} 
        title="" 
        showSaveButton={false}
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

export default DettaglioIntervista;
