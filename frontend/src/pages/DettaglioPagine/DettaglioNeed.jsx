import React, { useState, useEffect }             from "react";
import { useLocation }                            from "react-router-dom";
import axios                                      from "axios";
import { Box, Typography }                        from "@mui/material";
import FieldBoxFile                               from "../../components/FieldBoxFile";

const DettaglioNeed = () => {

  const location = useLocation();

  const { needData = {} } = location.state || {};


  const [ skillsOptions,          setSkillsOptions    ] = useState([]);

  const user = JSON.parse(localStorage.getItem("user"));
  const accessToken = user?.accessToken;

  const headers = {
    Authorization: `Bearer ${accessToken}`
  };





    useEffect(() => {
    const fetchAziendeOptions = async () => {
      try {
        const responseSkill        = await axios.get("/staffing/react/skill", { headers: headers });

      if (Array.isArray(responseSkill.data)) {
        const skillsOptions = responseSkill.data.map((skill) => ({
          value: skill.id,
          label: skill.descrizione
        }));
        setSkillsOptions(skillsOptions);
        }
                
          } catch (error) {
            console.error("Errore durante il recupero delle province:", error);
          }
        };
        fetchAziendeOptions();
      }, []);



  const fields = [
    { label: "Descrizione",         name: "descrizione",            type: "text",                 disabled: true },
    { label: "Priorità",            name: "priorita",               type: "text",                 disabled: true },
    { label: "Week",                name: "week",                   type: "text",                 disabled: true },
    { label: "Tipologia",           name: "tipologia",              type: "text",                 disabled: true },
    { label: "Tipologia Azienda",   name: "tipo",                   type: "text",                 disabled: true },
    { label: "Owner",               name: "owner",                  type: "text",                 disabled: true },
    { label: "Stato",               name: "stato",                  type: "text",                 disabled: true },
    { label: "Headcount",           name: "numeroRisorse",          type: "text",                 disabled: true },
    { label: "Location",            name: "location",               type: "text",                 disabled: true },
    { label: "Skills 1",            name: "skills",                 type: "multipleSelectSkill",  disabled: true },
    { label: "Seniority",           name: "anniEsperienza",         type: "text",                 disabled: true },
    { label: "Note",                name: "note",                   type: "note",                 disabled: true },
  ];




  const initialValues = {
    id:                         needData.id                                                 || "",
    descrizione:                needData.descrizione                                        || "",
    priorita:                   needData.priorita                                           || "",
    week:                       needData.week                                               || "",
    tipologia:                  needData.tipologia && needData.tipologia.descrizione        || "",
    tipo:                       needData.tipo                                               || "",
    owner:                      needData.owner     && needData.owner.descrizione            || "",
    stato:                      needData.stato     && needData.stato.descrizione            || "",
    numeroRisorse:              needData.numeroRisorse                                      || "",
    location:                   needData.location                                           || "",
    skills:                    (needData.skills?.map(skill => skill?.id))                   || [],
    anniEsperienza:             needData.anniEsperienza                                     || "",
    note:                       needData.note                                               || "",          
  };


  return (
    <Box sx={{ display: 'flex', backgroundColor: '#00853C', height: '100%', width: '100%', overflow: 'hidden'}}>
          <Box sx={{height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'auto'}}>
          <Typography variant="h4" component="h1" sx={{ margin: '30px', fontWeight: 'bold', fontSize: '1.8rem'}}>Dettaglio Need di {needData.cliente.denominazione}</Typography>
          <FieldBoxFile
          fields={fields}
          initialValues={initialValues}
          title=""
          skillsOptions={skillsOptions} 
          showSaveButton={false} 
          />
          </Box>
    </Box>
  );
};

export default DettaglioNeed;
