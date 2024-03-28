import React, { useState, useEffect }             from "react";
import { useNavigate, useLocation }               from "react-router-dom";
import axios                                      from "axios";
import { Box, Typography } from "@mui/material";
import FieldBoxFile from "../../components/FieldBoxFile";


const ModificaContatto = () => {

const navigate = useNavigate();
const location = useLocation();
const valori   = location.state;


const [ aziendeOptions, setAziendeOptions] = useState([]);
const [ ownerOptions,   setOwnerOptions  ] = useState([]);


const user = JSON.parse(localStorage.getItem("user"));
const accessToken = user?.accessToken;

const headers = {
    Authorization: `Bearer ${accessToken}`
};


useEffect(() => {
    const fetchAziendeOptions = async () => {
        try {
        const responseAziende = await axios.get("/aziende/react/select",{ headers: headers});
        const responseOwner   = await axios.get("/aziende/react/owner", { headers: headers});
        
            if (Array.isArray(responseOwner.data)) {
            const ownerOptions = responseOwner.data.map((owner) => ({
                label: owner.descrizione,
                value: owner.id, 
            }));
            setOwnerOptions(ownerOptions);
            }


            if (Array.isArray(responseAziende.data)) {
            const aziendeOptions = responseAziende.data.map((aziende) => ({
                label: aziende.denominazione,
                value: aziende.id,
            }));
            setAziendeOptions(aziendeOptions);
            }
        } catch (error) {
            console.error("Errore durante il recupero delle province:", error);
        }
        };

    fetchAziendeOptions();
    }, []);


const campiObbligatori = [ "nome", "idAzienda", "email", "idOwner", "status", "ruolo", "dataCreazione" ];

const fields = [
    { label: "Nome Contatto*",            name: "nome",                         type: "text"                                 },
    { label: "Azienda*",                  name: "idAzienda",                    type: "select",      options: aziendeOptions },
    { label: "Email*",                    name: "email",                        type: "text"                                 },
    { label: "Cellulare",                 name: "cellulare",                    type: "text"                                 },
    { label: "Proprietario*",             name: "idOwner",                      type: "select",      options: ownerOptions   },
    { label: "Stato*",                    name: "status",                       type: "selectValue", options: [
        { value: "1", label: "Verde" },
        { value: "2", label: "Giallo" },
        { value: "3", label: "Rosso" },
    ] },
    { label: "Ruolo*",                    name: "ruolo",                        type: "text" },
    { label: "Data di Creazione*",        name: "dataCreazione",                type: "date" },
    { label: "Ultima Attività",           name: "dataUltimaAttivita",           type: "date" },
    { label: "Note",                      name: "note",                         type: "note" },
];

const initialValues = {
    id:                 valori.id                                                  ,
    nome:               valori.nome                                                || null,
    idAzienda:          valori.cliente && valori.cliente.id                        || null,
    idOwner:            valori.owner   && valori.owner.id                          || null,
    email:              valori.email                                               || null,
    cellulare:          valori.cellulare                                           || null,
    ruolo:              valori.ruolo                                               || null,
    dataCreazione:      valori.dataCreazione                                       || null,
    dataUltimaAttivita: valori.dataUltimaAttivita                                  || null,
    status:             valori.status                                              || null,
    note:               valori.note                                                || null,
};



const handleSubmit = async (values) => {
    const errors = validateFields(values);
    const hasErrors = Object.keys(errors).length > 0;
    if (!hasErrors) {
    try {
        const response = await axios.post("/keypeople/react/salva", values, {
        headers: headers
        });
        navigate("/keypeople");
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
        <Typography variant="h4" component="h1" sx={{ mt: 3, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>{valori.nome} {valori.cognome}</Typography>
    
        <FieldBoxFile
        fields={fields}
        initialValues={initialValues}
        campiObbligatori={campiObbligatori}
        onSubmit={handleSubmit}
        title=""
        />
        </Box>
    </Box>

    );
};


export default ModificaContatto;
