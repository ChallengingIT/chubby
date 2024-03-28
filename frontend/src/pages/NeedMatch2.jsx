import React, { useEffect, useState}                  from 'react'
import { useParams }                                  from 'react-router-dom';
import { useNavigate }                                from "react-router-dom";
import axios                                          from 'axios';
import { Modal, Box, Button, Typography, selectClasses }             from '@mui/material';
import { useLocation }                                from 'react-router-dom';
import Tabella from '../components/Tabella.jsx';
import DeleteButton from '../components/button/DeleteButton.jsx';
import ModalBox from '../components/ModalBox.jsx';
import RicercheNeedMatch from '../components/ricerche/RicercheNeedMatch.jsx';

const NeedMatch2 = () => {
    const navigate                  = useNavigate();
    const params                    = useParams();
    const { id }                    = params;
    const location                  = useLocation();
    const valori                    = location.state;
    const descrizione               = valori.descrizione;
    const nomeAzienda               = valori.cliente?.denominazione;


    const [ originalCandidati,                  setOriginalCandidati            ] = useState([]);
    const [ originalStorico,                    setOriginalStorico              ] = useState([]);
    const [ originalAssociati,                  setOriginalAssociati            ] = useState([]);
    const [ ownerOptions,                       setOwnerOptions                 ] = useState([]);
    const [ statoOptions,                       setStatoOptions                 ] = useState([]);
    const [ isModalOpen,                        setIsModalOpen                  ] = useState(false);
    const [ initialValuesAggiorna,              setInitialValuesAggiorna        ] = useState([]);


    //stati per le ricerche
    const [ tipoOptions,                setTipoOptions         ] = useState([]);
    const [ tipologiaOptions,           setTipologiaOptions          ] = useState([]);
    const [ openFiltri,                 setOpenFiltri               ] = useState(false);
    const [ filtri,                     setFiltri                   ] = useState(() => {
        const filtriSalvati = localStorage.getItem('filtriRicercaNeedMatch');
        return filtriSalvati ? JSON.parse(filtriSalvati) : {
        nome: '',
        cognome: '',
        tipologia: '',
        tipologia: '',
        seniority: ''
        };
    }); 
    const seniority = [
        { label: 'Neo', value: '0', min: 0, max: 1 },
        { label: 'Junior', value: '1', min: 1, max: 2 },
        { label: 'Middle', value: '2', min: 2, max: 3 },
        { label: 'Senior', value: '3', min: 3 }
      ];


    //stati per la paginazione
    const [ paginaCandidati,            setPaginaCandidati          ] = useState(0);
    const [ righeTotCandidati,          setRigheTotCandidati        ] = useState(0);

    const [ paginaStorico,              setPaginaStorico            ] = useState(0);
    const [ righeTotStorico,            setRigheTotStorico          ] = useState(0);

    const [ paginaAssociati,            setPaginaAssociati          ] = useState(0);
    const [ righeTotAssociati,          setRigheTotAssociati        ] = useState(0);

    const quantita = 10;


    const user = JSON.parse(localStorage.getItem("user"));
    const accessToken = user?.accessToken;

    const headers = {
        Authorization: `Bearer ${accessToken}`
    };

    const navigateToCercaCandidato = (params) => {
        navigate('/recruiting', { state: { params } });
    };


    const fetchData = async () => {

        const filtriCandidati = {
            nome: filtri.nome || null,
            cognome: filtri.cognome || null,
            tipologia: filtri.tipologia || null,
            tipo: filtri.tipologia || null,
            seniority: filtri.seniority || null,
            pagina: 0,
            quantita: 10
            };

            const paginazione = {
                pagina: 0,
                quantita: 10
            };
            try {
                const candidatiResponse   = await axios.get(`/need/react/match/associabili/mod/${id}`,              { headers: headers, params: filtriCandidati});
                // const candidatiResponse   = await axios.get(`/staffing/react/mod`,                                  { headers: headers, params: filtriCandidati});
                const storicoResponse     = await axios.get(`/need/react/storico/${id}`,                            { headers: headers, params: paginazione});
                const associatiResponse   = await axios.get(`/need/react/match/associati/mod/${id}`,                { headers: headers, params: paginazione});
                const responseTipologia   = await axios.get("/aziende/react/tipologia",                             { headers: headers});
                const responseTipo        = await axios.get("/staffing/react/tipo"    ,                             { headers: headers});
                const ownerResponse       = await axios.get("/aziende/react/owner", { headers: headers});
                const statoResponse       = await axios.get("/associazioni/react/stati", { headers: headers});

                if (Array.isArray(ownerResponse.data)) {
                    const ownerOptions = ownerResponse.data.map((owner) => ({
                    label: owner.descrizione,
                    value: owner.id,
                    }));
                    setOwnerOptions(ownerOptions);
                }

                    if (Array.isArray(statoResponse.data)) {
                    const statoOptions = statoResponse.data.map((stato) => ({
                        label: stato.descrizione,
                        value: stato.id,
                    }));
                    setStatoOptions(statoOptions);
                }

                if (Array.isArray(responseTipologia.data)) {
                    const tipologiaOptions = responseTipologia.data.map((tipologia) => ({
                        label: tipologia.descrizione,
                        value: tipologia.id,
                    }));
                    setTipologiaOptions(tipologiaOptions);
                    }
            
                    if (Array.isArray(responseTipo.data)) {
                        const tipoOptions = responseTipo.data.map((tipo) => ({
                        label: tipo.descrizione,
                        value: tipo.id,
                        }));
                    setTipoOptions(tipoOptions);
                    }


                    const { data: candidatiData } = candidatiResponse;
                    const { data: storicoData   } = storicoResponse;
                    const { data: associatiData } = associatiResponse;

                    const recordCandidati = candidatiData.record;
                    const candidati = candidatiData.candidati;

                    const recordStorico = storicoData.record;
                    const associazioni = storicoData.associazioni;

                    const recordAssociati = associatiData.record;
                    const associati = associatiData.candidati;

                    setRigheTotCandidati(recordCandidati);
                    setOriginalCandidati(candidati);

                    setRigheTotStorico(recordStorico);
                    setOriginalStorico(associazioni);

                    setRigheTotAssociati(recordAssociati);
                    setOriginalAssociati(associati);

                    // const { recordCandidati, candidati } = candidatiResponse.data;
                    // if (candidati && Array.isArray(candidati)) {
                    //     setOriginalCandidati(candidati);
                    //     if (typeof recordCandidati === 'number') {
                    //         setRigheTotCandidati(recordCandidati);
                    //     } else {
                    //         console.error ("Il numero di record degli associabili ottenuto non è un numero: ", recordCandidati);
                    //     }
                    // } else {
                    //     console.error("I dati ottenuti non contengono associabili come array: ", candidatiResponse.data);
                    // } 


                    // const { record, associazioni } = storicoResponse.data;
                    // if( associazioni && Array.isArray(associazioni)) {
                    //     setOriginalStorico(associazioni);
                    //     if (typeof record === 'number') {
                    //         setRigheTotStorico(record);
                    //     } else {
                    //         console.error ("Il numero di record di storico ottenuto non è un numero: ", record);
                    //     }
                    // } else {
                    //     console.error("I dati di storico non sono nel formato Array: ", storicoResponse.data);
                    // }

                    // const { recordAssociati, associati } = associatiResponse.data;
                    // if ( associati && Array.isArray(associati)) {
                    //     setOriginalAssociati(associati);
                    //     if (typeof recordAssociati === 'number') {
                    //         setRigheTotAssociati(recordAssociati);
                    //     } else {
                    //         console.error("il numero di record di associati ottenuto non è un numero: ", recordAssociati);
                    //     }
                    // } else {
                    //     console.error("I dati di associati non sono nel formato Array: ", associatiResponse.data);
                    // }
                } catch(error) {
                    console.error("Errore durante il recupero dei dati: ", error);
                }
            };





                useEffect(() => {
                    const filtriSalvati = localStorage.getItem('filtriRicercaNeedMatch');
                    if(filtriSalvati) {
                        setFiltri(JSON.parse(filtriSalvati));
                        handleRicerche();
                    } else {
                    fetchData();
                    }
                // eslint-disable-next-line
                }, []);


    //funzione per la paginazione

    const fetchMoreDataCandidati = async (paginaCandidati) => {
        const filtriAttivi = Object.values(filtri).some(value => value !== null && value !== '');
        const url = filtriAttivi ?
        `/need/react/match/associabili/ricerca/mod/${id}` :
        `/need/react/match/associabili/mod/${id}`;


            const filtriCandidati = {
                nome: filtri.nome || null,
                cognome: filtri.cognome || null,
                tipologia: filtri.tipologia || null,
                tipo: filtri.tipo || null,
                seniority: filtri.seniority || null,
                pagina: paginaCandidati,
                quantita: 10
            };
            
            try {
                const candidatiResponse = await axios.get(url, { headers: headers, params: filtriCandidati});
                const { data: candidatiData } = candidatiResponse;
                const recordCandidati = candidatiData.record;
                const candidati = candidatiData.candidati; 
        
                setRigheTotCandidati(recordCandidati);
                setOriginalCandidati(candidati);
                // const { recordCandidati, candidati } = candidatiResponse.data;
            
                // if (candidati && Array.isArray(candidati)) {
                // setOriginalCandidati(candidati);
                // if (typeof recordCandidati === 'number' ) {
                //     setRigheTotAssociati(recordCandidati);
                // } else {
                //     console.error("Il numero di record di associabili ottenuto non è un numero: ", recordCandidati);
                // }
                // } else {
                // console.error("I dati ottenuti da associabili non sono nel formato Array:", candidatiResponse.data);
                //}
            } catch(error) {
                console.error("Errore durante il recupero dei dati: ", error);
                }
            };


                const fetchMoreDataStorico = async (paginaStorico) => {

                    const paginazione = {
                    pagina: paginaStorico,
                    quantita: 10
                    };
                
                    try {
                    const storicoResponse     = await axios.get(`/need/react/storico/${id}`, { headers: headers, params: paginazione});
                    const { recordStorico, storico } = storicoResponse.data;
                
                            if (storico && Array.isArray(storico)) {
                            setOriginalStorico(storico);
                            if (typeof storicoResponse === 'number' ) {
                                setRigheTotAssociati(recordStorico);
                            } else {
                                console.error("Il numero di record di storico ottenuto non è un numero: ", recordStorico);
                            }
                            } else {
                            console.error("I dati ottenuti di storico non sono nel formato Array:", storicoResponse.data);
                            }
                    } catch(error) {
                    console.error("Errore durante il recupero dei dati: ", error);
                    }
                };
                
                
                const fetchMoreDataAssociati = async (paginaAssociati) => { 
                    const paginazione = {
                    pagina: paginaAssociati,
                    quantita: 10
                    };
                    try {
                    const associatiResponse   = await axios.get(`/need/react/match/associati/mod/${id}`, { headers: headers, params: paginazione});
                    const { recordAssociati, associati } = associatiResponse.data;
                
                    if (associati && Array.isArray(associati)) {
                        setOriginalAssociati(associati);
                        if (typeof recordAssociati === 'number' ) {
                        setRigheTotAssociati(recordAssociati);
                        } else {
                        console.error("Il numero di record ottenuto non è un numero: ", recordAssociati);
                        }
                    } else {
                        console.error("I dati ottenuti non sono nel formato Array:", associatiResponse.data);
                    }
                    } catch(error) {
                    console.error("Errore durante il recupero dei dati: ", error);
                    }
                    };


            //funzione per il cambio pagina

            const handlePageChangeCandidati = (newPage) => {
                setPaginaCandidati(newPage);
                fetchMoreDataCandidati(newPage);
            };

            const handlePageChangeStorico = (newPage) => {
                setPaginaStorico(newPage);
                fetchMoreDataStorico(newPage);
            };

            const handlePageChangeAssociati = (newPage) => {
                setPaginaAssociati(newPage);
                fetchMoreDataAssociati(newPage);
            };


             //funzione per il cambio stato dei select
                const handleFilterChange = (name) => (event) => {
                    const newValue = event.target.value;
                
                    if (name === 'seniority') {
                    const selectedOption = seniority.find(option => option.value === newValue);
                
                    const min = selectedOption.min ?? parseInt(newValue);
                    const max = selectedOption.max ?? Infinity;


                
                    setFiltri({ ...filtri, [name]: newValue, minimo: min, massimo: max });
                    } else {
                    setFiltri({ ...filtri, [name]: newValue });
                    }
                
                    // Esegui la ricerca
                    handleRicerche();
                };


            useEffect(() => {
                localStorage.setItem('filtriRicercaNeedMatch', JSON.stringify(filtri));
              }, [filtri]);
            


            //funzione per le ricerche
            const handleRicerche = async (minimo, massimo) => {
                const paginazione = {
                    pagina: 0,
                    quantita: 10
                };
                const filtriCandidati = {
                    nome: filtri.nome || null,
                    cognome: filtri.cognome || null,
                    tipologia: filtri.tipologia || null,
                    tipo: filtri.tipo || null,
                    minimo: filtri.minimo || null,
                    massimo: filtri.massimo || null,
                    pagina: 0,
                    quantita: 10
                };

                try{
                    const candidatiResponse = await axios.get(`/need/react/match/associabili/ricerca/mod/${id}`, { headers: headers, params: filtriCandidati});
                    const storicoResponse     = await axios.get(`/need/react/storico/${id}`,                            { headers: headers, params: paginazione});
                    const associatiResponse   = await axios.get(`/need/react/match/associati/mod/${id}`,                { headers: headers, params: paginazione});
                    const responseTipologia    = await axios.get("/aziende/react/tipologia",                             { headers: headers});
                    const responseTipo        = await axios.get("/staffing/react/tipo"    ,                             { headers: headers});
    
                    if (Array.isArray(responseTipologia.data)) {
                        const tipologiaOptions = responseTipologia.data.map((tipologia) => ({
                            label: tipologia.descrizione,
                            value: tipologia.id,
                        }));
                        setTipologiaOptions(tipologiaOptions);
                        }
                
                        if (Array.isArray(responseTipo.data)) {
                            const tipoOptions = responseTipo.data.map((tipo) => ({
                            label: tipo.descrizione,
                            value: tipo.id,
                            }));
                        setTipoOptions(tipoOptions);
                        }


                        const { data: candidatiData } = candidatiResponse;
                        const { data: storicoData   } = storicoResponse;
                        const { data: associatiData } = associatiResponse;
    
                        const recordCandidati = candidatiData.record;
                        const candidati = candidatiData.candidati;
    
                        const recordStorico = storicoData.record;
                        const associazioni = storicoData.associazioni;
    
                        const recordAssociati = associatiData.record;
                        const associati = associatiData.candidati;
    
                        setRigheTotCandidati(recordCandidati);
                        setOriginalCandidati(candidati);
    
                        setRigheTotStorico(recordStorico);
                        setOriginalStorico(associazioni);
    
                        setRigheTotAssociati(recordAssociati);
                        setOriginalAssociati(associati);
                } catch(error) {
                    console.error("Errore durante il recupero dei dati filtrati: ", error);
                }
            };


            
            useEffect(() => {
                const { nome, cognome, ...otherFilters } = filtri;
                const filtriHasValues = Object.values(otherFilters).some(x => x !== '' && x != null);

                if (filtriHasValues) {
                    handleRicerche();
                }
            }, [filtri.tipo, filtri.tipologia, filtri.seniority]);


            const handleReset = () => {
                setFiltri({
                    nome: '', 
                    cognome: '',
                    tipo: '',
                    tipologia: '',
                    seniority: '',
                    minimo: '',
                    massimo: ''
                });
                setPaginaCandidati(0);
                fetchData();
            };
                        


            const handleGoBack = () => {
                window.history.back();
            };


            const handleDeleteAssociati = async (row) => {
                try {
                    const idNeed = parseInt(id);
                    const idCandidato = row;
                    const url = `/associazioni/react/rimuovi/candidato/associa?idNeed=${idNeed}&idCandidato=${idCandidato}`;
                    const responseDeleteAssociati = await axios.delete(url, {headers: headers});
                    fetchData();
                } catch(error) {
                    console.error("Errore durante l'eliminazione: ", error)
                }
            };


            const handleDeleteStorico = async (row) => {
                try {
                    const idAssociazione = row;
                    const url = `/associazioni/react/rimuovi/associa/${idAssociazione}`;
                    const responseDeleteStorico = await axios.delete(url, { headers: headers });
                    fetchData();
                } catch(error) {
                    console.error("Errore durante l'eliminazioneì: ", error);
                }
            };

            const handleAssocia = async (row) => {
                try{
                    const idNeed = parseInt(id);
                    const idCandidato = row.id;
                    const url = `/associazioni/react/associa?idNeed=${idNeed}&idCandidato=${idCandidato}`;
                    const responseAssocia = await axios.post(url, { headers: headers });
                    fetchData();
                } catch(error) {
                    console.error("Errore durante il recuper dei dati: ", error);
                }
            };


            const handleOpenModal = (selectedRow) => {
                const initialValuesWithDefaults = {
                    cliente: nomeAzienda || '',
                    idNeed: descrizione || '',
                    candidato: `${selectedRow.nome || ''} ${selectedRow.cognome || ''}`.trim(),
                    idCandidato: selectedRow.id || ''
                };
                fieldsAggiorna.forEach(field => {
                    if (field.type === 'select' && !initialValuesWithDefaults[field.name]) {
                        initialValuesWithDefaults[field.name] = field.options?.[0]?.value || '';
                    }
                });

                setInitialValuesAggiorna(initialValuesWithDefaults);
                setIsModalOpen(true);
                };

                const handleCloseModal = () => {
                    setIsModalOpen(false);
                };

                const handleSaveModal = async (selectedRow) => {
                    try {
                        const idNeedNum = parseInt(id);
                        const idCandidatoNum = parseInt(selectedRow.idCandidato);

                        const updateValues = {
                            ...selectedRow,
                            idNeed: idNeedNum,
                            idCandidato: idCandidatoNum
                        };

                        delete updateValues.candidato;
                        delete updateValues.cliente;

                        const response = await axios.post(`/associazioni/salva`, updateValues, { headers: headers });;
                        fetchData();
                    } catch(error) {
                        console.error("Errore durante il recupero dei dati: ", error);
                    }
                    handleCloseModal();
                };


                const fieldsAggiorna = [
                    { label: 'Cliente',                         name:'cliente',         type:'text'                                 },
                    { label: 'Need',                            name:'idNeed',          type:'text'                                 },
                    { label: 'Candidato',                       name:'candidato',       type:'text'                                 },
                    { label: 'Stato',                           name:'stato',           type:'select', options: statoOptions || []  },
                    { label: 'Data Aggiornamento',              name:'dataModifica',    type:'date'                                 },
                    { label: 'Owner',                           name:'idOwner',         type:'select', options: ownerOptions || []  },
                ];


                const disableFields = {
                    cliente:    true,
                    idNeed:     true,
                    candidato:  true
                };

                const tabellaCandidati = [
                        { field: "nome",                    headerName: "Nome",          flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "left"  }}>
                                <div onClick={() => navigateToCercaCandidato(params.row)}>
                                
                        {params.row.nome} {params.row.cognome}
                    </div>
                    
                            
                        </div>
                        
                        
                            ),},
                        { field: "email",                   headerName: "E-Mail",         flex: 1.4},
                        { field: "tipologia",               headerName: "Job Title",      flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "start" }}>
                            {params.row.tipologia && params.row.tipologia.descrizione
                                ? params.row.tipologia.descrizione
                                : "N/A"}
                            </div>
                        ),},
                        { field: "rating",                  headerName: "Rating",         flex: 1},
                        { field: "stato",                   headerName: "Stato",         flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "start" }}>
                            {params.row.stato && params.row.stato.descrizione
                                ? params.row.stato.descrizione
                                : "N/A"}
                            </div>
                        ),},
                        { field: "dataUltimoContatto",      headerName: "Contatto",        flex: 1},
                        { field: "azioni",                  headerName: "Azioni",          flex: 1, renderCell: (params) => (
                            <div>
                            <Button
                            onClick={() => handleAssocia(params.row)}
                            sx={{ backgroundColor: '#00853C',
                            fontWeight: 'bold',
                            color: 'white',
                            "&:hover": {
                                backgroundColor: "#00853C",
                                transform: "scale(1.05)",
                                color: 'white',
                            },
                            }}>Associa</Button>
                            </div>
                        )},
                ];


                const tabellaStorico = [
                        { field: "dataModifica",            headerName: "Data",         flex: 1 },
                        { field: "tipo",                    headerName: "Tipologia",    flex: 1, renderCell: (params) => (
                        <div style={{ textAlign: "start" }}>
                            {params.row.candidato && params.row.candidato.tipo ? params.row.candidato.tipo.descrizione : "N/A"}
                        </div>
                        )},
                        { field: "candidato",                    headerName: "Nome",         flex: 1, renderCell: (params) => (
                        <div style={{ textAlign: "left"  }}>
                            <div onClick={() => navigateToCercaCandidato(params.row)}>
                            {params.row.candidato ? `${params.row.candidato.nome} ${params.row.candidato.cognome}` : ""}
                                </div>
                        </div>
                    
                    
                        ),},
                        { field: "tipologia",             headerName: "Job Title",    flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "start" }}>
                            {params.row.candidato && params.row.candidato.tipologia.descrizione}
                            </div>
                        ),},
                        { field: "stato",                 headerName: "Stato",       flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "start" }}>
                            {params.row.stato && params.row.stato.descrizione}
                            </div>
                        ),},
                        { field: "owner",                   headerName: "Owner",       flex: 1, renderCell: (params) => (
                        <div style={{ textAlign: "start" }}>
                            {params.row.owner && params.row.owner.descrizione}
                        </div>
                        )},
                        { field: "azioni",                  headerName: "Azioni",       flex: 1, renderCell: (params) => (
                        <div>
                            <DeleteButton onClick={handleDeleteStorico} id={params.row.id}/>
                        </div>
                        ), }
                ];


                const tabellaAssociati = [
                        { field: "nome",                    headerName: "Nome",         flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "left"  }}>
                            <div onClick={() => navigateToCercaCandidato(params.row)}>
                                    
                                    {params.row.nome} {params.row.cognome}
                                </div>
                        
                        </div>
                            ),},
                        { field: "email",                   headerName: "E-Mail",        flex: 1.4},
                        { field: "tipologia",               headerName: "Job Title",     flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "start" }}>
                            {params.row.tipologia && params.row.tipologia.descrizione
                                ? params.row.tipologia.descrizione
                                : "N/A"}
                            </div>
                        ),},
                        { field: "rating",                  headerName: "Rating",        flex: 1},
                        { field: "stato",                   headerName: "Stato",         flex: 1, renderCell: (params) => (
                            <div style={{ textAlign: "start" }}>
                            {params.row.stato && params.row.stato.descrizione
                                ? params.row.stato.descrizione
                                : "N/A"}
                            </div>
                        ),},
                        { field: "dataUltimoContatto",       headerName: "Contatto",       flex: 1},
                        { field: "elimina",                  headerName: "Elimina",        flex: 1, renderCell: (params) => (
                            <div>
                            <DeleteButton onClick={handleDeleteAssociati} id={params.row.id}/>
                            </div>
                        ), },
                        { field: "status",                   headerName: "Status",         flex: 1, renderCell: (params) => (
                            <div>
                            <Button
                            onClick={() => handleOpenModal(params.row)}
                            sx={{ backgroundColor: '#00853C',
                            fontWeight: 'bold',
                            color: 'white',
                            "&:hover": {
                                backgroundColor: "#00853C",
                                transform: "scale(1.05)",
                                color: 'white',
                                
                            },
                            }}>Aggiorna</Button>
                            </div>
                        )}
                ];

                return (
                    <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: 'auto', flexGrow: 1}}>
                    <Box sx={{ 
                            p: 2,
                            ml: 25,
                            mt: 1.5,
                            mb: 0.5,
                            mr: 0.8,
                            backgroundColor: '#FEFCFD',
                            borderRadius: '10px',
                            height: 'auto',
                            width: '100%',
                            flexDirection: 'column',
                            overflow: 'hidden'
                            
                            }}>
                                    <Box sx={{ 
                                            position: 'sticky', 
                                            top: 0, 
                                            zIndex: 1000, 
                                        }}>  
                                        <RicercheNeedMatch
                                        filtri={filtri}
                                        onFilterChange={handleFilterChange}
                                        onReset={handleReset}
                                        tipoOptions={tipoOptions}
                                        tipologiaOptions={tipologiaOptions}
                                        seniorityOptions={seniority}
                                        onRicerche={handleRicerche}
                                        onGoBack={handleGoBack}
                                        />
                                        </Box>

                            <Typography variant="h4" component="h1" sx={{ ml: 5, fontWeight: 'bold', fontSize: '1.8rem', color: '#00853C'}}>{descrizione} {nomeAzienda}</Typography>
                            <Modal
                            open={isModalOpen}
                            onClose={handleCloseModal}
                            aria-labelledby="modal-title"
                            aria-describedby="modal-description"
                            sx={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            ml: 10
                            }}
                        >
                            <ModalBox
                            fields=        {fieldsAggiorna}
                            initialValues= {initialValuesAggiorna}
                            disableFields= {disableFields}
                            onSubmit=      {handleSaveModal}
                            title="Modifica stato Associazioni"
                            showBackButton={true}
                            onClose={handleCloseModal}
                            
                            />
                            </Modal>
                            <Box sx={{ height: 'auto', mt: 2, width: '100%', mb: 3}}>
                            <Tabella 
                                data={originalCandidati} 
                                columns={tabellaCandidati} 
                                title="Candidati"             
                                getRowId={(row) => row.id}
                                pagina={paginaCandidati}
                                quantita={quantita}
                                righeTot={righeTotCandidati}
                                onPageChange={handlePageChangeCandidati}
                                />
                            </Box>

                            <Box sx={{ height: 'auto', mt: 2, width: '100%', mb: 3,}}>
                            <Tabella 
                                data={originalStorico}     
                                columns={tabellaStorico}  
                                title="Storico"              
                                getRowId={(row) => row.id}
                                pagina={paginaStorico}
                                quantita={quantita}
                                righeTot={righeTotStorico}
                                onPageChange={handlePageChangeStorico}
                                />
                            </Box>

                            <Box sx={{ height: 'auto', mt: 2, width: '100%', mb: 3}}>
                            <Tabella 
                                data={originalAssociati}    
                                columns={tabellaAssociati} 
                                title="Candidati Associati"   
                                getRowId={(row) => row.id}
                                pagina={paginaAssociati}
                                quantita={quantita}
                                righeTot={righeTotAssociati}
                                onPageChange={handlePageChangeAssociati}
                                />
                            </Box>
                            {/* <Box sx={{ display: 'flex', justifyContent: 'center'}}>
                            <Button
                                color="primary"
                                onClick={handleGoBack}
                                sx={{
                                    backgroundColor: "black",
                                    borderRadius: '40px',
                                    color: "white",
                                    width: '250px',
                                    height: '30px', 
                                    margin: 'auto',
                                    marginBottom: '20px',
                                    marginTop: 'auto',
                                    "&:hover": {
                                    backgroundColor: "black",
                                    transform: "scale(1.05)",
                                    },
                                }}
                                >
                                Indietro
                            </Button>
                            </Box> */}
                            </Box>
                    </Box>
                );
};

export default NeedMatch2;