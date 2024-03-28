import React, { useState, useEffect }         from "react";
import { useNavigate, useLocation }           from "react-router-dom";
import axios                                  from "axios";
import { Box, Typography } from "@mui/material";
import FieldBoxFile from "../../components/FieldBoxFile";



const ModificaAziende = () => {



const navigate             = useNavigate();
const location             = useLocation();
const valori               = location.state;
const id = valori.id;




const [ provinceOptions, setProvinceOptions] = useState([]);
const [ ownerOptions,    setOwnerOptions   ] = useState([]);
const [ aziendeOptions,  setAziendeOptions ] = useState([]);
const [ datiModifica,    setDatiModifica   ] = useState({});
const [ loading,         setLoading        ] = useState(true);



const user = JSON.parse(localStorage.getItem("user"));
const accessToken = user?.accessToken;

const headers = {
    Authorization: `Bearer ${accessToken}`
};



    const navigateBack = () => {
        navigate(-1); 
    };




    useEffect(() => {
        const fetchProvinceOptions = async () => {
        try {
            const responseProvince = await axios.get("/aziende/react/province", { headers: headers });
            const responseOwner    = await axios.get("/aziende/react/owner", { headers: headers });
            const responseAziende  = await axios.get("/aziende/react/select", { headers: headers });
            const responseModifica = await axios.get(`/aziende/react/${id}`, {headers: headers});


            if (Array.isArray(responseAziende.data)) {
            const aziendeOptions = responseAziende.data.map((aziende) => ({
                label: aziende.denominazione,
                value: aziende.id,
            }));
            setAziendeOptions(aziendeOptions);
        }

        
            if (Array.isArray(responseOwner.data)) {
            const ownerOptions = responseOwner.data.map((owner) => ({
                label: owner.descrizione,
                value: owner.id,
            }));
            setOwnerOptions(ownerOptions);
        }


            if (Array.isArray(responseProvince.data)) {
                const provinceOptions = responseProvince.data.map((province) => ({
                label: province.nomeProvince,
                value: province.nomeProvince,
                }));
                setProvinceOptions(provinceOptions);
            }

            const modificaData = responseModifica.data;
            setDatiModifica(modificaData);
            setLoading(false);

        } catch (error) {
            console.error("Errore durante il recupero delle province:", error);
        }
        };

        fetchProvinceOptions();
    }, []);




const campiObbligatori = [ "denominazione", "idOwner", "status", "citta", "provincia", "sedeOperativa", "settoreMercato" ];

const fields = [
    { label: "Nome Azienda*",           name: "denominazione",        type: "text",       disabled: true                                  },
    { label: "Ragione Sociale*",        name: "ragioneSociale",       type: "text",                                                       },
    { label: "Email",                  name: "email",                type: "text"                                                        },
    { label: "Partita IVA",             name: "pi",                   type: "text"                                                        },
    { label: "Codice Fiscale",          name: "cf",                   type: "text"                                                        },
    { label: "Città*",                  name: "citta",                type: "text"                                                        },
    { label: "CAP",                     name: "cap",                  type: "text"                                                        },
    { label: "Paese",                   name: "paese",                type: "text"                                                        },
    { label: "Provincia*",              name: "provincia",            type: "select",     options: provinceOptions                        },
    { label: "Pec",                     name: "pec",                  type: "text"                                                        },
    { label: "Sede Operativa*",          name: "sedeOperativa",        type: "text"                                                        },
    { label: "Sede Legale",             name: "sedeLegale",           type: "text"                                                        },
    { label: "Codice Destinatario",     name: "codiceDestinatario",   type: "text"                                                        },
    { label: "Sito Web",                name: "sito",                 type: "text"                                                        },
    { label: "Settore di mercato*",      name: "settoreMercato",       type: "text"                                                        },
    { label: "Owner*",                  name: "idOwner",              type: "select",      options: ownerOptions                          },
    { label: "Tipologia",               name: "tipologia",            type: "select",      options: [ 
        { label: "Cliente",    value: "Cliente" },
        { label: "Prospect",   value: "Prospect" },
        { label: "Ex cliente", value: "EXCLIENTE" },
    ] },
    { label: "Stato*",                  name: "status",               type: "selectValue", options: [
        { value: 1, label: "Verde" },
        { value: 2, label: "Giallo" },
        { value: 3, label: "Rosso" },
    ] },
    { label: "Note",                    name: "note",                 type: "note" },
];

const initialValues = {
    id:                           datiModifica.id                              ,
    denominazione:                datiModifica.denominazione                   || null,
    email:                        datiModifica.email                           || null,
    pi:                           datiModifica.pi                              || null,
    cf:                           datiModifica.cf                              || null,
    citta:                        datiModifica.citta                           || null,
    cap:                          datiModifica.cap                             || null,
    paese:                        datiModifica.paese                           || null,
    provincia:                    datiModifica.provincia                       || null,
    pec:                          datiModifica.pec                             || null,
    sedeOperativa:                datiModifica.sedeOperativa                   || null,
    sedeLegale:                   datiModifica.sedeLegale                      || null,
    codicePa:                     datiModifica.codicePa                        || null,
    codiceDestinatario:           datiModifica.codiceDestinatario              || null,
    sito:                         datiModifica.sito                            || null,
    settoreMercato:               datiModifica.settoreMercato                  || null,
    tipologia:                    datiModifica.tipologia                       || null,
    status:                       datiModifica.status                          || null,
    idOwner:                     (datiModifica.owner && datiModifica.owner.id)       || null,
    note:                         datiModifica.note                            || null,
};


    const handleSubmit = async (values) => {
        const errors = validateFields(values);
        const hasErrors = Object.keys(errors).length > 0;
    
        if (!hasErrors) {
        try {

        Object.keys(values).forEach(key => {
            if (!campiObbligatori.includes(key) && !values[key]) {
            values[key] = null;
            }
        });

        const response = await axios.post("/aziende/react/salva", values, {
            headers: headers
        });
        navigateBack();
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
            <Typography variant="h4" component="h1" sx={{ mt:3, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>{valori.denominazione}</Typography>
            { loading ? (
            <Typography variant="body1" sx={{ margin: '30px' }}>Caricamento...</Typography>
                ) : (
                    <FieldBoxFile
                fields={fields}
                initialValues={initialValues}
                onSubmit={handleSubmit}
                title=""
                campiObbligatori={campiObbligatori}
                />
                )}
            </Box>
        </Box>
    
    );
};

export default ModificaAziende;
