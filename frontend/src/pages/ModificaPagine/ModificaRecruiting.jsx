import React, { useState, useEffect }          from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import axios                                   from "axios";
import FieldBoxFile from "../../components/FieldBoxFile";
import { Box, Typography, CircularProgress} from "@mui/material";

const ModificaStaffing = () => {

  const navigate = useNavigate();
  const location = useLocation();

  const {id}                    = useParams();
  const { recruitingData = {} } = location.state || {};
  const idCandidato             = recruitingData.id;
  const nomeCandidato           = recruitingData.nome;
  const cognomeCandidato        = recruitingData.cognome;

  const [statoOptions,              setStatoOptions              ] = useState([]);
  const [staffing,                  setStaffing                  ] = useState([]);
  const [jobTitleOptions,           setJobTitleOptions           ] = useState([]);
  const [tipologiaOptions,          setTipologiaOptions          ] = useState([]);
  const [skillsOptions,             setSkillsOptions             ] = useState([]); 
  const [livelloScolasticoOptions,  setLivelloScolasticoOptions  ] = useState([]);
  const [ownerOptions,              setOwnerOptions              ] = useState([]);
  const [facoltaOptions,            setFacoltaOptions            ] = useState([]);
  const [cv,                        setCV                        ] = useState(null);
  const [cf,                        setCF                        ] = useState(null);  
  const [newCVUploaded,             setNewCVUploaded             ] = useState(false);
  const [newCFUploaded,             setNewCFUploaded             ] = useState(false);    
  const [loading,                   setLoading                   ] = useState(true);






  // Recupera l'accessToken da localStorage
  const user = JSON.parse(localStorage.getItem("user"));
  const accessToken = user?.accessToken;

  // Configura gli headers della richiesta con l'Authorization token
  const headers = {
    Authorization: `Bearer ${accessToken}`
  };



  useEffect(() => {
    const fetchData = async () => {
      try {
        const responseStaffing            = await axios.get(`/staffing/react/${id}`, { headers: headers });
        const responseStato               = await axios.get("/staffing/react/stato/candidato", { headers: headers });
        const responseJobTitle            = await axios.get("/aziende/react/tipologia", { headers: headers });
        const responseTipologia           = await axios.get("/staffing/react/tipo", { headers: headers });
        const responseNeedSkill           = await axios.get("/staffing/react/skill", { headers: headers });
        const livelloScolasticoResponse   = await axios.get("/staffing/react/livello", { headers: headers });
        const ownerResponse               = await axios.get("/aziende/react/owner", { headers: headers });
        const facoltaResponse             = await axios.get("/staffing/react/facolta", { headers: headers });

        setStaffing(responseStaffing.data);

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
        if (Array.isArray(livelloScolasticoResponse.data)) {
          const livelloScolasticoOptions = livelloScolasticoResponse.data.map((livelloScolastico) => ({
            label: livelloScolastico.descrizione,
            value: livelloScolastico.id,
          }));
          setLivelloScolasticoOptions(livelloScolasticoOptions);

        }


        if (Array.isArray(responseNeedSkill.data)) {
          const skillsOptions = responseNeedSkill.data.map((skills) => ({
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

              if (Array.isArray(responseStato.data)) {
                const statoOptions = responseStato.data.map((stato) => ({
                  label: stato.descrizione,
                  value: stato.id,
                }));
                setStatoOptions(statoOptions);
              }

              setLoading(false);

    } catch (error) {
        console.error("Errore durante il recupero delle province:", error);
      }
    };

    fetchData();
  }, []);

  const campiObbligatori = ["nome", "cognome", "email", "anniEsperienzaRuolo", "tipologia", "dataUltimoContatto", "tipo", "stato", "livelloScolastico" ];

  const fields = [
    { label: "Tipologia*",                           name: "tipo",                   type: "select",         options: tipologiaOptions },
    { label: "Tipo Ricerca",                        name: "ricerca",                type: "select",         options: [
      { label: "C", value: "C" },
      { label: "R", value: "R"},
      { label: "C-R", value: "C-R"}
    ] },
    { label: "Nome*",                               name: "nome",                       type: "text" },
    { label: "Cognome*",                            name: "cognome",                    type: "text" },
    { label: "Data di Nascita",                     name: "dataNascita",                type: "date" },
    { label: "Email*",                              name: "email",                      type: "text" },
    { label: "Cellulare",                           name: "cellulare",                  type: "text" },
    { label: "Anni di Esperienza",                 name: "anniEsperienza",             type: "decimalNumber" },
    { label: "Residenza",                           name: "citta",                      type: "text" },
    { label: "Modalità di lavoro",                  name: "modalita",                   type: "select",            options: [
    { value: 1, label: "Full Remote" },
    { value: 2, label: "Ibrido" },
    { value: 3, label: "On Site"},
  ] },
    { label: "Anni di Esperienza nel Ruolo*",        name: "anniEsperienzaRuolo",        type: "decimalNumber" },
    { label: "Livello Scolastico*",                  name: "livelloScolastico",          type: "select",              options: livelloScolasticoOptions },
    { label: "Facoltà",                             name: "facolta",                    type: "select",              options: facoltaOptions },
    { label: "Job Title*",                          name: "tipologia",                  type: "select",              options: jobTitleOptions },
    { label: "Data Inserimento*",                   name: "dataUltimoContatto",         type: "date" },
    { label: "Stato*",                               name: "stato",                      type: "select",              options: statoOptions },
    { label: "Owner*",                               name: "owner",                      type: "select",              options: ownerOptions },
    { label: "Seleziona le Skills",                 name: "skills",                     type: "multipleSelectSkill", options: skillsOptions },
    { label: "RAL/Tariffa",                         name: "ral",                        type: "text" },
    { label: "Disponibilità",                       name: "disponibilita",              type: "text" },
    { label: "Note",                                name: "note",                       type: "note" },
    { label: "Curriculim Vitae",                    name: "cv",                         type: "modificaFileCV" },
    { label: "Consultant File",                     name: "cf",                         type: "modificaFileCF" },


  ];

  const initialValues = {
    id:                                 staffing.id                                                                   ,
    tipo:                               staffing.tipo                      && staffing.tipo.id                  || null,
    ricerca:                            staffing.ricerca                                                              || null,
    nome:                               staffing.nome                                                                 || null,
    cognome:                            staffing.cognome                                                              || null,
    dataNascita:                        staffing.dataNascita                                                          || null,
    email:                              staffing.email                                                                || null,
    cellulare:                          staffing.cellulare                                                            || null,
    anniEsperienza:                     staffing.anniEsperienza                                                       || null,
    citta:                              staffing.citta                                                                || null,
    modalita:                           staffing.modalita                                                             || null,
    anniEsperienzaRuolo:                staffing.anniEsperienzaRuolo                                                  || null,
    livelloScolastico:                  staffing.livelloScolastico          && staffing.livelloScolastico.id          || null,
    facolta:                            staffing.facolta                    && staffing.facolta.id                    || null,
    tipologia:                          staffing.tipologia                  && staffing.tipologia.id                  || null,
    dataUltimoContatto:                 staffing.dataUltimoContatto                                                   || null,
    stato:                              staffing.stato                      && staffing.stato.id                      || null,
    owner:                              staffing.owner                      && staffing.owner.id                      || null,
    skills:                             staffing.skills ? staffing.skills.map((skill) => skill.id) :                  [],
    ral:                                staffing.ral                                                                  || null,
    disponibilita:                      staffing.disponibilita                                                        || null,
    // cv:                                 recruitingData.files?.find(file => file.tipologia.descrizione === 'CV')             || null,
    cv:                                 staffing.files ? staffing.files.find(file => file && file.tipologia && file.tipologia.descrizione === 'CV') || null : null,
    cf:                                 staffing.files ? staffing.files.find(file => file && file.tipologia && file.tipologia.descrizione === 'CF') || null : null,
    note:                               recruitingData.note                                                                 || null,

  };




const handleSubmit = async (values, fileCV, fileCF) => {
  const errors = validateFields(values);
  const hasErrors = Object.keys(errors).length > 0;

  if (!hasErrors) {
      try {
      const skills = values.skills ? values.skills.join(',') : '';
      delete values.skills;

      const cv = values.cv;
      const cf = values.cf;

      delete values.cv;
      delete values.cf;

      const datiResponse = await axios.post("/staffing/salva", values, {
      params: { skill: skills },
      headers: headers,
      });

      const candidatoId = datiResponse.data;

      const config = {
      headers: {
      Authorization: `Bearer ${accessToken}`,
      }
  };

try{

  Object.keys(values).forEach(key => {
    if (!campiObbligatori.includes(key) && !values[key]) {
    values[key] = null;
    }
});
  //invio del file "cv" se presente
    if (fileCV) {
    if( fileCV instanceof File) {
      const formDataCV = new FormData();
      formDataCV.append('file', fileCV);
      formDataCV.append('tipo', 1);
      const responseCV = await axios.post(`/staffing/react/staff/salva/file/${candidatoId}`, formDataCV,
      {headers: headers});
  } 
}
} catch(error) {
      console.error("Errore nell'invio del CV", error);
  }

  try{

    // Invio del file "cf", se presente
if(fileCF) {
  if( fileCF instanceof File) {
      const formDataCF = new FormData();
      formDataCF.append('file', fileCF);
      formDataCF.append('tipo', 2);
      const responseCF = await axios.post(`/staffing/react/staff/salva/file/${candidatoId}`, formDataCF, {headers: headers});
  }
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
    {loading ? (
      <CircularProgress /> // Mostra un indicatore di caricamento o un messaggio mentre i dati sono in caricamento
    ) : (
      <Box sx={{ flexGrow: 1, p: 3, marginLeft: '12.2em', marginTop: '0.5em', marginBottom: '0.8em', marginRight: '0.8em', backgroundColor: '#FEFCFD', borderRadius: '10px', display: 'flex', justifyContent: 'center', flexDirection: 'column' }}>
      <Typography variant="h4" component="h1" sx={{ mb: 4, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>{nomeCandidato} {cognomeCandidato}</Typography>

          <FieldBoxFile
          fields={fields}
          initialValues={initialValues}
          campiObbligatori={campiObbligatori}
          onSubmit={handleSubmit}
          title=""
          skillsOptions={skillsOptions} 
          idCandidato={idCandidato}

          />
          </Box>
    )}
    </Box>
  );
};

export default ModificaStaffing;
