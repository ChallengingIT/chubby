import React, { useState, useEffect }                   from 'react';
import { useNavigate  }                                 from 'react-router-dom';
import axios                                            from 'axios';
import AziendeCard                                      from '../components/card/AziendeCard';
import InfiniteScroll                                   from "react-infinite-scroll-component";
import RicercheAziende                                  from '../components/ricerche/RicercheAziende';    

import { 
    Box,
    Grid,
    CircularProgress,
    } from '@mui/material';


const Aziende = () => {

    const navigate = useNavigate();

    const [ originalAziende,   setOriginalAziende ] = useState([]);
    const [ loading,           setLoading         ] = useState(false);
    const [                    setAlert           ] = useState(false);




    //stati ricerche
    // const [ clienteOptions,             setClienteOptions             ] = useState([]);
    const [ ownerOptions,               setOwnerOptions               ] = useState([]);
    const [ provinceOptions,            setProvinceOptions            ] = useState([]);
    // const [ filtri,                     setFiltri                     ] = useState(() => {
    //     const filtriSalvati = localStorage.getItem('filtriRicercaAziende');
    //     return filtriSalvati ? JSON.parse(filtriSalvati) : {
    //         denominazione: '',
    //         tipologia: '',
    //         stato: '',
    //         owner: ''
    //     };
    // });


    const getValueLabel = (value) => {
        const option = ownerOptions.find((option) => option.value === value);
        return option ? option.label : null;
      };
      




    const [filtri, setFiltri] = useState(() => {
        const filtriSalvati = localStorage.getItem('filtriRicercaAziende');
        if (filtriSalvati) {
          const filtriParsed = JSON.parse(filtriSalvati);
          // Assicurati che ownerOptions sia definito o caricato prima di fare questa operazione
          if (filtriParsed.owner) {
            filtriParsed.ownerLabel = getValueLabel(filtriParsed.owner);
          }
          return filtriParsed;
        }
        return {
          denominazione: '',
          tipologia: '',
          stato: '',
          owner: '',
          ownerLabel: '', // Aggiungi anche la label corrispondente da mostrare nell'UI
        };
      });
      
    

    //stato paginazione
    const [ pagina,                 setPagina       ] = useState(0);
    const [ hasMore,                setHasMore      ] = useState(true);

    const quantita = 10;


    const user = JSON.parse(localStorage.getItem('user'));
    const accessToken = user?.accessToken;

    const headers = {
        Authorization: `Bearer ${accessToken}`
    };



    const fetchData = async() => {
        setLoading(true);
        const filtriDaInviare = {
            ragione: filtri.denominazione || null,
            tipologia: filtri.tipologia || null,
            owner: filtri.owner || null,
            stato: filtri.stato || null,
            pagina: 0,
            quantita: 10
        };
            try {
            const responseAziende   = await axios.get("/aziende/react/mod",     { headers: headers , params: filtriDaInviare });
            // const responseCliente   = await axios.get("/aziende/react/select",  { headers });
            const responseOwner     = await axios.get("/aziende/react/owner",   { headers });
            const provinceResponse = await axios.get("/aziende/react/province", { headers: headers });

            if (Array.isArray(responseOwner.data)) {
            setOwnerOptions(responseOwner.data.map((owner, index) => ({ label: owner.descrizione, value: owner.id })));
            } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseOwner.data);
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
        
            if (Array.isArray(responseAziende.data)) {
                const aziendeConId = responseAziende.data
                .map((aziende) => ({ ...aziende }));
                setOriginalAziende(aziendeConId);
                setHasMore(aziendeConId.length >= quantita); 
                // setPagina(pagina + 1);
            } else {
                console.error("I dati ottenuti non sono nel formato Array:", responseAziende.data);
            }
            setLoading(false);
            } catch (error) {
            console.error("Errore durante il recupero dei dati:", error);
            }
        };


        useEffect(() => {
            const filtriSalvati = localStorage.getItem('filtriRicercaAziende');
            if (filtriSalvati) {
                setFiltri(JSON.parse(filtriSalvati));
                handleRicerche();
            } else {
            fetchData();
            }
            // eslint-disable-next-line
        }, []); 

        //funzione per la paginazione
        const fetchMoreData = async () => {
            const paginaSuccessiva = pagina + 1;

            const filtriAttivi = Object.values(filtri).some(value => value !== null && value !== '');

            const url = filtriAttivi ?
            "/aziende/react/ricerca/mod" :
            "/aziende/react/mod";

            const filtriDaInviare = {
                ragione: filtri.denominazione || null,
                tipologia: filtri.tipologia || null,
                owner: filtri.owner || null,
                stato: filtri.stato || null,
                pagina: paginaSuccessiva,
                quantita: quantita
            };
            try {
                const responsePaginazione   = await axios.get(url,     { headers: headers , params: filtriDaInviare });
                if (Array.isArray(responsePaginazione.data)) {
                    const aziendeConId = responsePaginazione.data
                    .map((aziende) => ({ ...aziende }));
                    setOriginalAziende((prev) => [...prev, ...aziendeConId]);
                    setHasMore(responsePaginazione.data.length >= quantita);  
                    } else {
                    console.error("I dati ottenuti non sono nel formato Array:", responsePaginazione.data);
                }
                setLoading(false);
                } catch (error) {
                console.error("Errore durante il recupero dei dati:", error);
                }
                setPagina((prevPagina) => prevPagina + 1);
            };

            //funzione di ricerca
            const handleRicerche = async () => {
        
                const filtriDaInviare = {
                    ragione: filtri.denominazione || null,
                    tipologia: filtri.tipologia || null,
                    owner: filtri.owner || null,
                    stato: filtri.stato || null,
                    pagina: 0,
                    quantita: quantita
                };
                setLoading(true);     
                try {
                    const response          = await axios.get("/aziende/react/ricerca/mod", { headers: headers, params: filtriDaInviare });
                    const responseOwner     = await axios.get("/aziende/react/owner",   { headers });

                    if (Array.isArray(responseOwner.data)) {
                    setOwnerOptions(responseOwner.data.map((owner, index) => ({ label: owner.descrizione, value: owner.id })));
                    } else {
                    console.error("I dati ottenuti non sono nel formato Array:", responseOwner.data);
                    } 
        
                    if (Array.isArray(response.data)) {
                        setOriginalAziende(response.data);
                        setHasMore(response.data.length >= quantita); 
                        } else {
                        console.error("I dati ottenuti non sono nel formato Array:", response.data);
                    }
                } catch (error) {
                    console.error("Errore durante il recupero dei dati filtrati:", error);
                } finally {
                    setLoading(false);
                }
            };



            //funzione cambio stato select
            const handleFilterChange = (name) => (event) => {
                const newValue = event.target.value;
                setFiltri({ ...filtri, [name]: newValue });
                
                if (name === 'denominazione' && newValue === '') {
                    fetchData();
                } else {
                    handleRicerche();
                }
            };

            useEffect(() => {
                const { denominazione, ...otherFilters } = filtri;
                const filtriHasValues = Object.values(otherFilters).some(x => x !== '' && x != null);
            
                if (filtriHasValues) {
                    handleRicerche();
                }
            }, [filtri.tipologia, filtri.stato, filtri.owner]);


            useEffect(() => {
                localStorage.setItem('filtriRicercaAziende', JSON.stringify(filtri));
            }, [filtri]);


            

            //funzione di reset dei campi di ricerca
            const handleReset = async() => {
                setFiltri({
                    denominazione: '',
                    stato: '',
                    owner:'',
                    tipologia:''
                });
                setPagina(0);
                setOriginalAziende([]);
                setHasMore(true);
        
                await fetchData(0);
                };



        //funzione per cancellare l'azienda
        const handleDelete = async (id) => {
            try{
                const responseDelete = await axios.delete(`/aziende/react/elimina/${id}`, {headers: headers});
                await fetchData(0);
            } catch(error) {
                console.error("Errore durante la cancellazione: ", error);
            }
        };


        //funzione per il refresh
        const handleRefresh = async () => {
        await fetchData(0);
        };


                //funzione per il refresh
                // const handleRefresh = async () => {
                //     await fetchData(0);
                // };


        const tipologiaOptions = [
            { label: "Cliente", value: "CLIENTE" },
            { label: "Prospect", value: "PROSPECT" },
            { label: "Ex cliente", value: "EXCLIENTE" }
        ];

        const statoOptions = [
            { label: 'Caldo', value: '1' },
            { label: 'Tiepido', value:'2'},
            { label: 'Freddo', value: '3' }
        ];


    // const campiObbligatori = [ "denominazione", "ragioneSociale", "email", "idOwner", "status", "citta", "provincia" ];

    // const fields = [
    //     { type: 'titleGroups',                  label: 'Informazioni Generali',         xs: 12, sm: 12                      },
    //     { label: "Nome Azienda*",                   name: "denominazione",            type: "text", xs: 12, sm: 12                             },
    //     { label: "Ragione Sociale*",                name: "ragioneSociale",           type: "text", xs: 12, sm: 12                             },
    //     { label: "Email*",                          name: "email",                    type: "text", xs: 12, sm: 12                             },
    //     { label: "Sito Web",                        name: "sito",                     type: "text", xs: 12, sm: 12                             },

    //     { type: 'titleGroups',                  label: 'Posizione',         xs: 12, sm: 12                      },
    //     { label: "Città*",                          name: "citta",                    type: "text", xs: 12, sm: 12                             },
    //     { label: "CAP",                             name: "cap",                      type: "text", xs: 12, sm: 12                             },
    //     { label: "Paese",                           name: "paese",                    type: "text", xs: 12, sm: 12                             },
    //     { label: "Provincia*",                      name: "provincia",                type: "select", options: provinceOptions, xs: 12, sm: 12 },
    //     { label: "Sede Operativa",                  name: "sedeOperativa",            type: "text", xs: 12, sm: 12                             },
    //     { label: "Sede Legale",                     name: "sedeLegale",               type: "text", xs: 12, sm: 12                             },
    //     { type: 'titleGroups',                  label: 'altro', xs: 12, sm: 12 },
    //     { label: "Pec",                             name: "pec",                      type: "text", xs: 12, sm: 12                             },
    //     { label: "Partita IVA",                     name: "pi",                       type: "text", xs: 12, sm: 12                             },
    //     { label: "Codice Fiscale",                  name: "cf",                       type: "text", xs: 12, sm: 12                             },
    //     { label: "Codice Destinatario",             name: "codiceDestinatario",       type: "text", xs: 12, sm: 12                             },
    //     { label: "Settore di mercato",              name: "settoreMercato",           type: "text", xs: 12, sm: 12                             },
    //     { label: "Owner*",                          name: "idOwner",                  type: "select", options: ownerOptions, xs: 12, sm: 12    },
    //     { label: "Tipologia",                       name: "tipologia",                type: "select", options: [
    //         { value: "Cliente", label: "Cliente" },
    //         { value: "Prospect", label: "Prospect" },
    //         { value: "Ex cliente", label: "Ex cliente" }
    //     ], xs: 12, sm: 12  },
    //     { label: "Stato*",                          name: "status",                    type: "select", options: [
    //         { value: 1, label: "Verde" },
    //         { value: 2, label: "Giallo" },
    //         { value: 3, label: "Rosso" },
    //     ], xs: 12, sm: 12  },
        
    //     { label: "Note", name: "note", type: "note", xs: 12, sm: 12 },
    // ];



    // const handleSubmit = async (values) => {
    //     const errors    = validateFields(values);
    //     const hasErrors = Object.keys(errors).length > 0;
    //     if (!hasErrors) {
    //         try {
        
    //             Object.keys(values).forEach(key => {
    //             if (!campiObbligatori.includes(key) && !values[key]) {
    //                 values[key] = null;
    //             }
    //             });

    //             const response = await axios.post("/aziende/react/salva", values, {
    //             headers: headers
    //             });
    //             if (response.data === "DUPLICATO") {
    //             setAlert({ open: true, message: "Email già utilizzata!" });
    //             console.error("L'email fornita è già in uso.");
    //             return; 
    //             }
    //             handleCloseModal(true);
    //             fetchData();
    //         } catch (error) {
    //             console.error("Errore durante il salvataggio:", error);
    //         }
    //         }
    //     };
        
    //     const validateFields = (values) => {
    //         let errors = {};
    //         campiObbligatori.forEach(field => {
    //         if (!values[field]) {
    //             errors[field] = 'Questo campo è obbligatorio';
    //         }
    //         });
    //         return errors;
    //     };


    return(
        <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: 'auto', width: '100vw' }}>
            <Box sx={{ 
                flexGrow: 1, 
                p: 3, 
                marginLeft: '13.2em', 
                marginTop: '0.5em', 
                marginBottom: '0.8em', 
                marginRight: '0.8em', 
                backgroundColor: '#FEFCFD', 
                borderRadius: '20px', 
                minHeight: '98vh',
                mt: 1.5 
            }}>
                <Box sx={{ 
                    position: 'sticky', 
                    top: 0, 
                    zIndex: 1000
                }}>
                    <RicercheAziende
                    filtri={filtri}
                    onFilterChange={handleFilterChange}
                    onReset={handleReset}
                    tipologiaOptions={tipologiaOptions}
                    statoOptions={statoOptions}
                    ownerOptions={ownerOptions}
                    onRicerche={handleRicerche}
                    />
                    </Box>
                        <InfiniteScroll
                        dataLength={originalAziende.length}
                        next={fetchMoreData}
                        hasMore={hasMore}
                        loader={'Caricamento in corso...'}
                        >

                            {/* Main Content Area */}
                <Grid container spacing={2} sx={{ mt: 1, mb: 4}}>
                    { loading ? (
                        <Box
                        sx={{
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            height: '100%'
                        }}>
                            <CircularProgress sx={{ color: '#00853C'}}/> 
                        </Box>
                    ) : (
                        originalAziende.map((aziende, index) => (
                            <Grid item xs={12} md={6} key={index}>
                                <AziendeCard
                                valori={aziende}
                                onDelete={() => handleDelete(aziende.id)}
                                onRefresh={handleRefresh}


                                />
                            </Grid>
                        ))
                    )
                    }
                    </Grid>
                    </InfiniteScroll>
                    </Box>
{/* 
                <Modal  
                        open={openModal}
                        onClose={(event, reason) => {
                            if (reason !== 'backdropClick') {
                                handleCloseModal();
                            }   
                        }}
                        aria-labelledby="modal-title"
                        aria-describedby="modal-description"
                        sx={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                        }}>
                        <Box sx={{ 
                            display: 'flex', 
                            flexDirection: 'column', 
                            backgroundColor: 'white', 
                            width: '40vw', 
                            height: '70vh', 
                            borderRadius: '20px', 
                            overflow: 'hidden',
                            // border: '2px solid #00853C'

                        }}>
                            <Box sx={{ 
                                display: 'flex', 
                                justifyContent: 'space-between', 
                                alignItems: 'center', 
                                m: 2 
                            }}>
                                <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#00853C'}}>
                                    Aggiungi Azienda
                                </Typography>
                                <IconButton onClick={handleCloseModal}>
                                    <CloseIcon />
                                </IconButton>
                            </Box>
                            <Box sx={{ overflowY: 'auto' }}>
                                <Grid container direction="column" spacing={1} sx={{ pl: 2, pr: 2 }}>
                                    <Grid item>
                                        <AggiungiBox
                                        fields={fields}
                                        campiObbligatori={campiObbligatori}
                                        title=''
                                        onSave={handleSubmit}
                                        />


                                    </Grid>
                                </Grid>
                            </Box>
                            </Box>
                        </Modal> */}

                    </Box>
    );
};
export default Aziende;