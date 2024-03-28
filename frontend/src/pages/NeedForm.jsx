import React, { useState, useEffect }               from "react";
import { useNavigate }                              from "react-router-dom";
import CircleOutlinedIcon                           from '@mui/icons-material/CircleOutlined'; //cerchio vuoto
import axios                                        from "axios";
import FieldBoxFileNuovo                            from '../components/FieldBoxFileNuovo.jsx';                  
import { 
    Box, 
    Typography, 
    TextField, 
    Select, 
    MenuItem, 
    Button, 
    List, 
    ListItem, 
    ListItemIcon, 
    ListItemText, 
    Alert, 
    Grid, 
    FormControl, 
    Autocomplete,
    InputLabel,
    FormHelperText
 } from '@mui/material';
import { current } from "@reduxjs/toolkit";

function NeedForm() {

    const navigate = useNavigate();
    
    const [ activeSection,      setActiveSection        ] = useState('Profilo');
    const [ currentPageIndex,   setCurrentPageIndex     ] = useState (0);
    const [ errors,             setErrors               ] = useState({});
    const [ values,             setValues               ] = useState([]);
    const [ totalPage,          setTotalPage            ] = useState(0);


    const [ provinceOptions,    setProvinceOptions      ] = useState([]);
    const [ ownerOptions,       setOwnerOptions         ] = useState([]);
    const [ alert,              setAlert                ] = useState({ open: false, message: ''});

    const user = JSON.parse(localStorage.getItem("user"));
    const accessToken = user?.accessToken;

    const headers = {
        Authorization: `Bearer ${accessToken}`
    };


    useEffect(() => {
        const fetchProvinceOptions = async () => {
        try {
    
            const provinceResponse = await axios.get("/aziende/react/province", { headers: headers });
            const ownerResponse    = await axios.get("/aziende/react/owner",    { headers: headers }   );
    
            if (Array.isArray(ownerResponse.data)) {
            const ownerOptions = ownerResponse.data.map((owner) => ({
                label: owner.descrizione,
                value: owner.id,
            }));
            setOwnerOptions(ownerOptions);
            }
            
            if (Array.isArray(provinceResponse.data)) {
                const provinceOptions = provinceResponse.data.map((province) => ({
                label: province.nomeProvince,
                value: province.nomeProvince,
                }));
                setProvinceOptions(provinceOptions);
            } else {
            console.error("I dati ottenuti non sono nel formato Array:", provinceResponse.data);
            }
        } catch (error) {
            console.error("Errore durante il recupero delle province:", error);
        }
        };
    
        fetchProvinceOptions();
    }, []);


    const menu = [
        { 
            title: 'Profilo',
            icon: <CircleOutlinedIcon />
        },
        {
            title: 'Location',
            icon: <CircleOutlinedIcon />
        },
        {
            title: 'Contatti',
            icon: <CircleOutlinedIcon />
        }
    ];

    const getCurrentPageFields = () => {
        switch (activeSection.toLowerCase()) {
            case 'profilo':
                return campiProfilo;
            case 'location':
                return campiLocation;
            case 'contatti':
                return campiContatti;
            default:
                return [];
        }
    };




    


    const handleBackButtonClick = () => {
        const currentIndex = menu.findIndex(item => item.title.toLowerCase() === activeSection.toLowerCase());
        if (currentIndex > 0) {
            setActiveSection(menu[currentIndex - 1].title);
            setCurrentPageIndex(currentIndex - 1);
        }
    };

    const handleNextButtonClick = () => {
        const currentIndex = menu.findIndex(item => item.title.toLowerCase() === activeSection.toLowerCase());
        if (currentIndex < menu.length - 1) {
            setActiveSection(menu[currentIndex + 1].title);
            setCurrentPageIndex(currentIndex + 1);
        }
    };



    const handleGoBack = () => {
        navigate(-1);
    };



    const handleSubmit = async (values) => {
        const errors    = validateFields(values);
        const hasErrors = Object.keys(errors).length > 0;
    
        if (!hasErrors) {
        try {
    
            Object.keys(values).forEach(key => {
            if (!campiObbligatori.includes(key) && !values[key]) {
                values[key] = null;
            }
            });

            const userString = localStorage.getItem("user");
            if (!userString) {
            console.error("Nessun utente o token trovato in localStorage");
            return;
            }
            const user = JSON.parse(userString);
            const accessToken = user?.accessToken;
            
            if (!accessToken) {
            console.error("Nessun token di accesso disponibile");
            return;
            }
    
            const headers = {
            Authorization: `Bearer ${accessToken}`
            };
    
            const response = await axios.post("/aziende/react/salva", values, {
            headers: headers
            });
            if (response.data === "DUPLICATO") {
            setAlert({ open: true, message: "azienda già esistente!" });
            console.error("L'azienda è già stata salvata.");
            return; 
            }
            navigate("/aziende");
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


    const handleChange = (event) => {
        const { name, value } = event.target;
        if (errors[name]) {
            setErrors(prevErrors => ({
                ...prevErrors,
                [name]: '',
            }));
        }
        setValues(prevValues => ({
            ...prevValues,
            [name]: value
        }));
    };



    const campiObbligatoriProfilo = ["denominazione", "idOwner", 'status'];
    const campiProfilo = [
        { label: 'Nome Azienda',                   name: 'denominazione',            type:'text'                              },
        { label: "Email",                           name: "email",                    type: "text"                             },
        { label: "Partita IVA",                     name: "pi",                       type: "text"                             },
        { label: "Codice Fiscale",                  name: "cf",                       type: "text"                             },
        { label: "Owner",                          name: "idOwner",                  type: "select", options: ownerOptions    },
        { label: "Tipologia",                       name: "tipologia",                type: "select", options: [
            { value: "Cliente", label: "Cliente" },
            { value: "Prospect", label: "Prospect" },
            { value: "EXCLIENTE", label: "Ex Cliente" }
        ]  },
        { label: "Stato",                          name: "status",                    type: "select", options: [
            { value: 1, label: "Verde" },
            { value: 2, label: "Giallo" },
            { value: 3, label: "Rosso" },
        ]  },
    ];

    const campiObbligatoriLocation = [ "citta", "provincia", "sedeOperativa", "cap"];
    const campiLocation = [
        { label: "Città",                          name: "citta",                    type: "text"                             },
        { label: "Paese",                           name: "paese",                    type: "text"                             },
        { label: "Provincia",                      name: "provincia",                type: "select", options: provinceOptions },
        { label: "Sede Operativa",                 name: "sedeOperativa",            type: "text"                            },
        { label: "Sede Legale",                     name: "sedeLegale",               type: "text"                             },
        { label: "CAP",                             name: "cap",                      type: "text"                             },
    ];


    const campiContatti = [
        { label: "Pec",                             name: "pec",                      type: "text"                             },
        { label: "Codice Destinatario",             name: "codiceDestinatario",       type: "text"                             },
        { label: "Sito Web",                        name: "sito",                     type: "text"                             },
        { label: "Note",                            name: "note",                     type: "note"                             },
    ];

    const campiObbligatori = [...campiObbligatoriProfilo, ...campiObbligatoriLocation];




    const renderField = (field) => {
        const { label, name, type, options } = field;
    
        switch (type) {
            case 'text':
                return (
                    <TextField
                    id="filled-basic"  
                    variant="filled" 
                        label={label}
                        name={name}
                        fullWidth
                        onChange={handleChange}
                        required={campiObbligatori.includes(name)}
                        error={!!errors[name]}
                        helperText={errors[name]}
                        sx={{ 
                            borderRadius: '20px', 
                            backgroundColor: '#EDEDED', 
                            '& .MuiFilledInput-root': {
                                backgroundColor: 'transparent',
                                '&::before': {
                                    borderBottom: 'none',  
                                },
                                '&:hover::before': {
                                    borderBottom: 'none',  
                                },
                            },
                        }} 
                    />
                );
                case 'select':
                    return (
                        <Autocomplete
                        
                            fullWidth
                            options={options}
                            getOptionLabel={(option) => option.label}
                            value={values[name] || null}
                            onChange={(event, value) => handleChange({ target: { name, value } })}
                            renderInput={(params) => (
                                <TextField
                                id="filled-basic"  
                                variant="filled" 
                                    {...params}
                                    label={label}
                                    name={name}
                                    required={campiObbligatori.includes(name)}
                                    error={!!errors[name]}
                                    helperText={errors[name]}
                                    sx={{ 
                                        borderRadius: '20px', 
                                        backgroundColor: '#EDEDED', 
                                        '& .MuiFilledInput-root': {
                                            backgroundColor: 'transparent',
                                            '&::before': {
                                                borderBottom: 'none',  
                                            },
                                            '&:hover::before': {
                                                borderBottom: 'none',  
                                            },
                                        },
                                    }} 
                                />
                            )}
                        />
                    );

                    case 'note':
                return (
                    <TextField
                    id="filled-basic"  
                    variant="filled" 
                        label={label}
                        name={name}
                        fullWidth
                        onChange={handleChange}
                        multiline
                        rows={4}
                        required={campiObbligatori.includes(name)}
                        error={!!errors[name]}
                        helperText={errors[name]}
                        sx={{ 
                            borderRadius: '20px', 
                            backgroundColor: '#EDEDED', 
                            '& .MuiFilledInput-root': {
                                backgroundColor: 'transparent',
                                '&::before': {
                                    borderBottom: 'none',  
                                },
                                '&:hover::before': {
                                    borderBottom: 'none',  
                                },
                            },
                        }} 
                    />
                );


            default:
                return null;
        }
    };
    



    const renderNavigationButtons = () => {
        return (
            <Box sx={{ mt: 2, display: 'flex', justifyContent: 'center', gap: 4}}>
                {currentPageIndex > 0 && (
                    <Button onClick={() => handleBackButtonClick()} variant="contained" sx={{ width: '6em', backgroundColor: 'black', borderRadius: '5px', fontWeight: '600', '&:hover': { backgroundColor: 'black', color: '#EDEDED', transform: 'scale(1.04)'}}}>Indietro</Button>
                )}
                {currentPageIndex < menu.length - 1 ? (
                    <Button onClick={() => handleNextButtonClick()} variant="contained" sx={{ width: '6em', backgroundColor: 'black', borderRadius: '5px', fontWeight: '600', '&:hover': { backgroundColor: 'black', color: '#EDEDED', transform: 'scale(1.04)'}}}>Avanti</Button>
                ) : (
                    <Button onClick={() => handleSubmit()} variant="contained" sx={{ width: '6em', backgroundColor: '#5F8671', color: '#EDEDED', fontWeight: '600', borderRadius: '5px', '&:hover': { backgroundColor: '#5F8671', color: '#EDEDED', transform: 'scale(1.04)'}}}>Salva</Button>
                )}
            </Box>
        );
    };


    const renderFormFields = () => {
        const currentPageFields = getCurrentPageFields();
    
        return (
            <Box sx={{ display: 'flex', flexGrow: 1, flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}>
            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2, p: 2, width: '100%' }}>
                {currentPageFields.map(field => (
                    <Box key={field.name} sx={{ mt: 2 }}>
                        {renderField(field)}
                    </Box>
                ))}
            </Box>
              <Box sx={{ mt: 2, display: 'flex', justifyContent: 'center' }}>
              {renderNavigationButtons()}
          </Box>
          </Box>
        );
    };
    

    return (
        <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: '100vh', width: '100vw', flexDirection: 'row' }}>
            <Box sx={{ display: 'flex', height: '98%', width: '100vw', flexDirection: 'row', ml: '12.5em', mt: '0.5em', mb: '0.5em', mr: '0.8em', borderRadius: '20px', overflow: 'hidden' }}>
                <Box sx={{ width: '22%', height: '100%', background: '#5F8671', p:2, overflow: 'hidden' }}>
                    {/* Contenuto della prima Box */}
                    <Box sx={{
                        display: 'flex',
                        justifyContent: 'flex-start',
                        width: '100%'
                    }}>
                        <Button 
                        onClick={handleGoBack}
                        sx={{
                            color: 'black',
                            border:'none',
                            fontSize: '0.8em',
                            cursor: 'pointer',
                            outline: 'none',
                            borderRadius: '10px',
                            mt: 4,
                            ml: 2,
                            fontSize: '0.9em',
                            '&:hover': {
                                color: '#EDEDED'
                            }
                        }}
                        >
                            <span style={{ marginRight: '0.5em'}}>{"<"}</span>
                            Indietro
                        </Button>
                    </Box>
                    <Typography variant="h6" sx={{display: 'flex', justifyContent: 'flex-start', fontWeight: 'bold', mt: 4, ml: 3, fontSize: '1.8em', color: '#EDEDED'}}>  Aggiungi <br /> Azienda </Typography>


                <Box sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'flex-start',
                    width: '100%',
                    ml: 2,
                    mt: 5
                }}>
                <List sx={{ display: 'flex', flexDirection: 'column', width: '90%'}}>
                    {menu.map((item) => (
                        <ListItem
                            key={item.title}
                            selected={activeSection === item.title}
                            sx={{
                                gap: 0,
                                mb: 4,
                                '&:hover, &.Mui-selected': {
                                    backgroundColor: activeSection === item.title ? 'black' : 'transparent',
                                    '& .MuiListItemIcon-root, & .MuiListItemText-primary': {
                                        color: activeSection === item.title ? '#EDEDED' : '#EDEDED',
                                    },
                                    borderRadius: '10px',
                                },
                                borderRadius: '10px',
                            }}
                        >
                            <ListItemIcon>
                                {item.icon}
                            </ListItemIcon>
                            <ListItemText primary={item.title} />
                        </ListItem>
                    ))}
                </List>
                </Box>
                </Box>
                <Box sx={{ flexGrow: 1, height: '100%', background: '#FEFCFD', p: 2,  display: 'flex', flexDirection: 'column' }}>
                    {/* Contenuto della seconda Box */}
                    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', mt: 2, mb: 6}}>
                    <Typography variant="h4" component="h1" sx={{ mt:1, mb: 3, fontWeight: 'bold', fontSize: '1.8'}}>{activeSection}</Typography>
                    </Box>
                        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                            {renderFormFields()}
                        </Box>
                    </Box>
                </Box>
        </Box>
    );
};

export default NeedForm;