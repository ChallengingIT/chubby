import React, { useState, useEffect }     from "react";
import { useNavigate }                    from "react-router-dom";
import axios                              from "axios";
import { Box, Typography }                from "@mui/material";
import FieldBoxFile                       from '../../components/FieldBoxFile.jsx';


const AggiungiNeed = () => {

  const navigate   = useNavigate();

  const [ aziendeOptions,       setAziendeOptions     ] = useState([]);
  const [ skillsOptions,        setSkillsOptions      ] = useState([]);
  const [ ownerOptions,         setOwnerOptions       ] = useState([]);
  const [ tipologiaOptions,     setTipologiaOptions   ] = useState([]);
  const [ statoOptions,         setStatoOptions       ] = useState([]);

  const user = JSON.parse(localStorage.getItem("user"));
  const accessToken = user?.accessToken;

  const headers = {
    Authorization: `Bearer ${accessToken}`
  };



  useEffect(() => {
    const fetchNeedOptions = async () => {
      try {
        const responseAziende       = await axios.get("/aziende/react/select", { headers: headers });
        const responseSkill         = await axios.get("/staffing/react/skill", { headers: headers });
        const ownerResponse         = await axios.get("/aziende/react/owner" , { headers: headers });
        const tipologiaResponse     = await axios.get("/need/react/tipologia", { headers: headers });
        const statoResponse         = await axios.get("/need/react/stato"    , { headers: headers});


        if (Array.isArray(statoResponse.data)) {
          const statoOptions = statoResponse.data.map((stato) => ({
            label: stato.descrizione,
            value: stato.id,
          }));
          setStatoOptions(statoOptions);
        }



        if (Array.isArray(tipologiaResponse.data)) {
          const tipologiaOptions = tipologiaResponse.data.map((tipologia) => ({
            label: tipologia.descrizione,
            value: tipologia.id,
          }));
          setTipologiaOptions(tipologiaOptions);
        }



        if (Array.isArray(ownerResponse.data)) {
          const ownerOptions = ownerResponse.data.map((owner) => ({
            label: owner.descrizione,
            value: owner.id,
          }));
          setOwnerOptions(ownerOptions);
        }


      if (Array.isArray(responseSkill.data)) {
        const skillsOptions = responseSkill.data.map((skill) => ({
          value: skill.id,
          label: skill.descrizione
        }));
        setSkillsOptions(skillsOptions);
      }


        if (Array.isArray(responseAziende.data)) {
          const ownerOptions = responseAziende.data.map((aziende) => ({
            label: aziende.denominazione,
            value: aziende.id,
          }));
          setAziendeOptions(ownerOptions);
        }
      } catch (error) {
        console.error("Errore durante il recupero delle aziende:", error);
      }
    };

    fetchNeedOptions();
  }, []);


  const pubblicazioneOptions = [
    { value: 1, label: 'To Do' },
    { value: 2, label: 'Done'  }
  ];

  const screeningOptions = [
    { value: 1, label: 'To Do' },
    { value: 2, label: 'In progress' },
    { value: 3, label: 'Done' }
  ];

  const campiObbligatori = [ "idAzienda", "descrizione", "priorita", "week", "pubblicazione", "screening", "tipologia", "stato", "idOwner"];

  const fields = [
    { label: "Azienda*",            name: "idAzienda",                    type: "select",               options: aziendeOptions    },
    { label: "Descrizione Need*",   name: "descrizione",                  type: "text"                                             },
    { label: "Priorità*",           name: "priorita",                     type: "number"                                           },
    { label: "Week*",               name: "week",                         type: "weekPicker"                                       },
    { label: "Tipologia*",           name: "tipologia",                    type: "select",               options: tipologiaOptions  },
    { label: "Tipologia Azienda",   name: "tipo",                         type: "select",               options: [
    { value: 1,                   label: "Cliente" },
    { value: 2,                   label: "Consulenza" },
    { value: 3,                   label: "Prospect" }
  ] },
    { label: "Owner*",                     name: "idOwner",                      type: "select",                 options: ownerOptions         },
    { label: "Stato*",                     name: "stato",                        type: "select",                 options: statoOptions         },
    { label: "Headcount",                 name: "numeroRisorse",                type: "number"                                                },
    { label: "Location",                  name: "location",                     type: "text"                                                  },
    { label: "Skills 1",                  name: "skills",                       type: "multipleSelectSkill",    options: skillsOptions        },
    { label: "Seniority",                 name: "anniEsperienza",               type: "decimalNumber"                                         },
    { label: 'Pubblicazione Annuncio*',   name: 'pubblicazione',                type: 'select',                 options: pubblicazioneOptions },
    { label: 'Screening*',                name: 'screening',                    type: 'select',                 options: screeningOptions     },
    { label: "Note",                      name: "note",                         type: "note"                                                  },
  ];




  const handleSubmit = async (values) => {
    const errors = validateFields(values);
    const hasErrors = Object.keys(errors).length > 0;

    if(!hasErrors) {
      try {
        Object.keys(values).forEach(key => {
          if (!campiObbligatori.includes(key) && !values[key]) {
            values[key] = null;
          }
        });
        const skills = values.skills ? values.skills.join(',') : '';
        delete values.skills;

        if (!accessToken) {
          console.error("nessun token di accesso disponibile");
          return;
        }

        const responseSaveNeed = await axios.post("/need/react/salva", values, { params: { skill1: skills }, headers: headers});
        navigate('/need');
      } catch(error) {
        console.error("Errore durante il salvataggio", error);
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
          <Typography variant="h4" component="h1" sx={{ mt:3,  fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>Aggiungi Need</Typography>

          <FieldBoxFile
          fields={fields}
          campiObbligatori={campiObbligatori}
          onSubmit={handleSubmit}
          title=""
          skillsOptions={skillsOptions} 
          />
          </Box>
    </Box>
  );
};

export default AggiungiNeed;
