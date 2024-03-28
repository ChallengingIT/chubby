import React, { useState, useEffect } from "react";
import { useNavigate }                from "react-router-dom";
import axios                          from "axios";
import FieldBoxFile from "../../components/FieldBoxFile";
import { Box,Typography, Alert, Snackbar } from "@mui/material";


const AggiungiRecruiting = () => {
const navigate = useNavigate();

const [ statoOptions,             setStatoOptions             ] = useState([]);
const [ fornitoriOptions,         setFornitoriOptions         ] = useState([]);
const [ jobTitleOptions,          setJobTitleOptions          ] = useState([]);
const [ tipologiaOptions,         setTipologiaOptions         ] = useState([]);
const [ skillsOptions,            setSkillsOptions            ] = useState([]);
const [ ownerOptions,             setOwnerOptions             ] = useState([]);
const [ facoltaOptions,           setFacoltaOptions           ] = useState([]);
const [ livelloScolasticoOptions, setLivelloScolasticoOptions ] = useState([]);
const [ alert,           setAlert          ] = useState({ open: false, message: '' });


const user = JSON.parse(localStorage.getItem("user"));
const accessToken = user?.accessToken;
const headers = {
    Authorization: `Bearer ${accessToken}`
};

useEffect(() => {
    const fetchAziendeOptions = async () => {
    try {
        const responseStato               = await axios.get("/staffing/react/stato/candidato", { headers: headers });
        const responseFornitori           = await axios.get("/fornitori/react"               , { headers: headers });
        const responseJobTitle            = await axios.get("/aziende/react/tipologia"       , { headers: headers });
        const responseTipologia           = await axios.get("/staffing/react/tipo"           , { headers: headers });
        const responseNeedSkills          = await axios.get("/staffing/react/skill"          , { headers: headers });
        const ownerResponse               = await axios.get("/aziende/react/owner"           , { headers: headers });
        const facoltaResponse             = await axios.get("/staffing/react/facolta"        , { headers: headers });
        const livelloScolasticoResponse   = await axios.get("/staffing/react/livello"        , { headers: headers });

        if (Array.isArray(livelloScolasticoResponse.data)) {
        const livelloScolasticoOptions = livelloScolasticoResponse.data.map((livelloScolastico) => ({
            label: livelloScolastico.descrizione,
            value: livelloScolastico.id,
        }));
        setLivelloScolasticoOptions(livelloScolasticoOptions);
    }


        if (Array.isArray(facoltaResponse.data)) {
        const facoltaOptions = facoltaResponse.data.map((facolta) => ({
            label: facolta.descrizione,
            value: facolta.id,
        }));
        setFacoltaOptions(facoltaOptions);
    }

        if (Array.isArray(ownerResponse.data)) {
    const ownerOptions = ownerResponse.data.map((owner) => ({
            label: owner.descrizione,
            value: owner.id,
        }));
        setOwnerOptions(ownerOptions);
    }
        
        if (Array.isArray(responseNeedSkills.data)) {
        const skillsOptions = responseNeedSkills.data.map((skills) => ({
            label: skills.descrizione,
            value: skills.id,
        }));
        setSkillsOptions(skillsOptions);
    }

        if (Array.isArray(responseTipologia.data)) {
            const tipologiaOptions = responseTipologia.data.map((tipologia) => ({
            label: tipologia.descrizione,
            value: tipologia.id,
            }));
            setTipologiaOptions(tipologiaOptions);
        }


            if (Array.isArray(responseJobTitle.data)) {
            const jobTitleOptions = responseJobTitle.data.map((jobTitle) => ({
                label: jobTitle.descrizione,
                value: jobTitle.id,
            }));
            setJobTitleOptions(jobTitleOptions);
        }


            if (Array.isArray(responseFornitori.data)) {
                const fornitoriOptions = responseFornitori.data.map((fornitori) => ({
                label: fornitori.denominazione,
                value: fornitori.id,
                }));
                setFornitoriOptions(fornitoriOptions);
            }



                if (Array.isArray(responseStato.data)) {
                const statoOptions = responseStato.data.map((stato) => ({
                    label: stato.descrizione,
                    value: stato.id,
                }));
                setStatoOptions(statoOptions);
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


const campiObbligatori = ["nome", "cognome", "email", "anniEsperienzaRuolo", "tipologia", "dataUltimoContatto", "tipo", "stato", "livelloScolastico" ];

const fields = [
    { label: "Tipologia*",                     name: "tipo",                     type: "select",          options: tipologiaOptions                      },
    { label: "Fornitore",                     name: "fornitore",                type: "select",          options: fornitoriOptions                      },
    { label: "Tipo Ricerca",                  name: "ricerca",                  type: "select",          options: [
        { value: "C", label: "C"},
        { value: "R", label: "R" },
        { value: "C-R", label: "C-R" },
    ]},
    { label: "Nome*",                         name: "nome",                     type: "text"                                                            },
    { label: "Cognome*",                      name: "cognome",                  type: "text"                                                            },
    { label: "Data di Nascita",               name: "dataNascita",              type: "date"                                                            },
    { label: "Email*",                        name: "email",                    type: "text"                                                            },
    { label: "Cellulare",                     name: "cellulare",                type: "text"                                                            },
    { label: "Anni di Esperienza",            name: "anniEsperienza",           type: "decimalNumber"                                                            },
    { label: "Residenza",                     name: "citta",                    type: "text"                                                            },
    { label: "Modalità di lavoro",            name: "modalita",                 type: "select",          options: [ 
        { value: 1, label: "Full Remote" },
        { value: 2, label: "Ibrido" },
        { value: 3, label: "On Site"},
    ] },
    { label: "Anni di Esperienza nel Ruolo*",   name: "anniEsperienzaRuolo",      type: "decimalNumber"                                                            },
    { label: "Livello Scolastico*",              name: "livelloScolastico",        type: "select",               options: livelloScolasticoOptions         },
    { label: "Facoltà",                         name: "facolta",                  type: "select",               options: facoltaOptions                   },
    { label: "Job Title*",                      name: "tipologia",                type: "select",               options: jobTitleOptions                  },
    { label: "Data Inserimento*",               name: "dataUltimoContatto",       type: "date"                                                            },
    { label: "Stato*",                           name: "stato",                    type: "select",               options: statoOptions                     },
    { label: "Owner",                           name: "owner",                    type: "select",               options: ownerOptions                     },
    { label: "Seleziona le Skills",             name: "skills",                   type: "multipleSelectSkill",  options: skillsOptions                    },
    { label: "RAL/Tariffa",                     name: "ral",                      type: "text"                                                            },
    { label: "Disponibilità",                   name: "disponibilita",            type: "text"                                                            },
    { label: "Note",                            name: "note",                     type: "note"                                                            },
    { label: "Curriculim Vitae",                name: "cv",                       type: "modificaFileCV"                                                  },
    { label: "Consultant File",                 name: "cf",                       type: "modificaFileCF"                                                  },
];


const handleSubmit = async (values, fileCV, fileCF, fileMultipli, fileAllegati) => {
    const errors = validateFields(values);
    const hasErrors = Object.keys(errors).length > 0;

    if (!hasErrors) {
        try {
            Object.keys(values).forEach(key => {
                if (!campiObbligatori.includes(key) && !values[key]) {
                values[key] = null;
                }
            });
        const skills = values.skills ? values.skills.join(',') : '';

        delete values.skills;

        // const cv = values.cv;
        // const cf = values.cf;

        delete values.cv;
        delete values.cf;

        const datiResponse = await axios.post("/staffing/salva", values, {
        params: { skill: skills },
        headers: headers,
        });
        if (datiResponse.data === "DUPLICATO") {
            setAlert({ open: true, message: "Email già utilizzata!" });
            console.error("L'email fornita è già in uso.");
            return; 
        }
        const candidatoId = datiResponse.data;

    try{
    if (fileCV) {
        const formDataCV = new FormData();
        formDataCV.append('file', fileCV);
        formDataCV.append('tipo', 1);

        const responseCV = await axios.post(`/staffing/react/staff/salva/file/${candidatoId}`, formDataCV,
        {headers: headers});
    } 
} catch(error) {
        console.error("Errore nell'invio del CV", error);
    }

    try{

      // Invio del file "cf", se presente
    if (fileCF) {
        const formDataCF = new FormData();
        formDataCF.append('file', fileCF);
        formDataCF.append('tipo', 2);
        const responseCF = await axios.post(`/staffing/react/staff/salva/file/${candidatoId}`, formDataCF, {headers: headers});
    }
} catch(error) {
    console.error("errore nell'invio del CF", error);
}
    navigate("/recruiting");
} catch(error) {
    console.error("Errore nella chiamata axios: ", error);
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
        <Typography variant="h4" component="h1" sx={{  mb: 4, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>Aggiungi candidato</Typography>

        
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

export default AggiungiRecruiting;