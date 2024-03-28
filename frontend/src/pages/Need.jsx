import React, { useState, useEffect }                   from 'react';
import axios                                            from 'axios';
// import BusinessCenterIcon                               from '@mui/icons-material/BusinessCenter'; //aziende
import NeedCard                                         from '../components/card/NeedCard';
import InfiniteScroll                                   from 'react-infinite-scroll-component';
import RicercheNeed from '../components/ricerche/RicercheNeed';

import { 
    Box,
    Grid,
    CircularProgress,
    } from '@mui/material';

    const Need = () => {



        const [ originalNeed,              setOriginalNeed          ] = useState([]);
        const [ loading,                   setLoading               ] = useState(false);
        const [                            setAlert                 ] = useState(false);

        
        //stati per le ricerche
        const [ tipologiaOptions,               setTipologiaOptions         ] = useState([]);
        const [ ownerOptions,                   setOwnerOptions             ] = useState([]);
        const [ statoOptions,                   setStatoOptions             ] = useState([]);
        const [ aziendaOptions,             setAziendaOptions             ] = useState([]);
        const [ filtri,                         setFiltri                   ] = useState(() => {
            const filtriSalvati = localStorage.getItem('filtriRicercaNeed');
            return filtriSalvati ? JSON.parse(filtriSalvati) : {
            descrizione: '',
            tipologia: '',
            stato: '',
            owner: ''
            };
        });
        const quantita = 10;

        //stati per la paginazione
        const [ pagina,                 setPagina           ] = useState(0);
        const [ hasMore,                setHasMore          ] = useState(true);



        const user = JSON.parse(localStorage.getItem('user'));
        const accessToken = user?.accessToken;

        const headers = {
            Authorization: `Bearer ${accessToken}`
        };


        //caricamento dati al montaggio
        const fetchData = async ()  => {
            // if(originalNeed.length > 0) { return; };

            setLoading(true);

            const filtriDaInviare = {
                descrizione: filtri.descrizione || null,
                tipologia: filtri.tipologia || null,
                owner: filtri.owner || null,
                stato: filtri.stato || null,
                azienda: filtri.azienda || null,
                pagina: 0,
                quantita: 10
            };

            try {
            const responseNeed          = await axios.get("/need/react/modificato",         { headers: headers, params: filtriDaInviare });
            const responseAzienda       = await axios.get("/aziende/react/select",          { headers: headers });
            const responseOwner         = await axios.get("/aziende/react/owner",           { headers: headers });
            const responseTipologia     = await axios.get("/need/react/tipologia",          { headers: headers });
            const responseStato         = await axios.get("/need/react/stato",              { headers: headers });


            if (Array.isArray(responseOwner.data)) {
                setOwnerOptions(responseOwner.data.map((owner) => ({ label: owner.descrizione, value: owner.id})));
            } else {
                console.error("I dati ottenuti non sono nel formato Array; ", responseOwner.data);
            }

            if (Array.isArray(responseAzienda.data)) {
                setAziendaOptions(responseAzienda.data.map((azienda) => ({ label: azienda.denominazione, value: azienda.id })));
            } else {
                console.error("I dati ottenuti non sono nel formato Array:", responseAzienda.data);
            }

            if (Array.isArray(responseTipologia.data)) {
                setTipologiaOptions(responseTipologia.data.map((tipologia) => ({ label: tipologia.descrizione, value: tipologia.id})));
            } else {
                console.error("I dati ottenuti non sono nel formato Array; ", responseTipologia.data);
            }

            if (Array.isArray(responseStato.data)) {
                setStatoOptions(responseStato.data.map((stato) => ({ label: stato.descrizione, value: stato.id})));
            } else {
                console.error("I dati ottenuti non sono nel formato Array; ", responseStato.data);
            }
            if (Array.isArray(responseNeed.data)) {
                const needConId = responseNeed.data.map((need) => ({...need}));
                const sortedNeed = needConId.sort((a, b) => b.id - a.id);

                setOriginalNeed(sortedNeed);
                setHasMore(needConId.length >= quantita);

            } else {
                console.error("I dati ottenuti non sono nel formato Array; ", responseNeed.data);
            }
            setLoading(false);
            } catch(error) {
                console.error("Errore durante il recupero dei dati: ", error);
            }
        };


        useEffect(() => {
            const filtriSalvati = localStorage.getItem('filtriRicercaNeed');
            if(filtriSalvati) {
                setFiltri(JSON.parse(filtriSalvati));
                handleRicerche();
            } else {
            fetchData();
            }
            // eslint-disable-next-line
        }, []);



        //caricamento dati con paginazione
        const fetchMoreData = async () => {

            const paginaSuccessiva = pagina + 1;

            const filtriAttivi = Object.values(filtri).some(value => value !== null && value !== '');

            const url = filtriAttivi ?
            "/need/react/ricerca/modificato" :
            "/need/react/modificato";


            const filtriDaInviare = {
                descrizione: filtri.descrizione || null,
                tipologia: filtri.tipologia || null,
                owner: filtri.owner || null,
                stato: filtri.stato || null,
                azienda: filtri.azienda || null,
                pagina: paginaSuccessiva,
                quantita: 10
            };
            try {
                const responsePaginazione   = await axios.get(url,    { headers: headers , params: filtriDaInviare });
                if (Array.isArray(responsePaginazione.data)) {
                    const needConId = responsePaginazione.data.map((need) => ({...need }));
                    setOriginalNeed((prev) => [...prev, ...needConId]);
                    setHasMore(needConId.length >= quantita);
                } else {
                    console.error("I dati ottenuti non sono nel formato array: ", responsePaginazione.data);
                }
                setLoading(false);
            } catch(error) {
                console.error("Errore durante il recupero dei dati: ", error);
            }
            setPagina((prevPagina) => prevPagina + 1);
        };


        //funzione di ricerca
        const handleRicerche = async () => {
            const filtriDaInviare = {
                descrizione: filtri.descrizione || null,
                tipologia: filtri.tipologia || null,
                owner: filtri.owner || null,
                stato: filtri.stato || null,
                azienda: filtri.azienda || null,
                pagina: 0,
                quantita: 10
            };
            setLoading(true);
            try {
                const response = await axios.get("/need/react/ricerca/modificato", { headers: headers, params: filtriDaInviare });
                const responseAzienda       = await axios.get("/aziende/react/select",          { headers: headers });
                const responseOwner         = await axios.get("/aziende/react/owner",           { headers: headers });
                const responseTipologia     = await axios.get("/need/react/tipologia",          { headers: headers });
                const responseStato         = await axios.get("/need/react/stato",              { headers: headers });


                if (Array.isArray(responseOwner.data)) {
                    setOwnerOptions(responseOwner.data.map((owner) => ({ label: owner.descrizione, value: owner.id})));
                } else {
                    console.error("I dati ottenuti non sono nel formato Array; ", responseOwner.data);
                }

                if (Array.isArray(responseAzienda.data)) {
                    setAziendaOptions(responseAzienda.data.map((azienda) => ({ label: azienda.denominazione, value: azienda.id })));
                } else {
                    console.error("I dati ottenuti non sono nel formato Array:", responseAzienda.data);
                }

                if (Array.isArray(responseTipologia.data)) {
                    setTipologiaOptions(responseTipologia.data.map((tipologia) => ({ label: tipologia.descrizione, value: tipologia.id})));
                } else {
                    console.error("I dati ottenuti non sono nel formato Array; ", responseTipologia.data);
                }

                if (Array.isArray(responseStato.data)) {
                    setStatoOptions(responseStato.data.map((stato) => ({ label: stato.descrizione, value: stato.id})));
                } else {
                    console.error("I dati ottenuti non sono nel formato Array; ", responseStato.data);
                }

                if (Array.isArray(response.data)) {
                    setOriginalNeed(response.data);
                    setHasMore(response.data.length >= quantita);
                } else {
                    console.error("I dati ottenuti non sono nel formato Array: ", response.data);
                }
            } catch(error) {
                console.error("Errore durante il recupero dei dati filtrati: ", error);
            } finally {
                setLoading(false);
            }
        };






        

        //funzione cambiamento stato select
        const handleFilterChange = (name) => (event) => {
            const newValue = event.target.value;
            setFiltri({...filtri, [name]:newValue});
            if(name === 'descrizione' && newValue === '') {
                fetchData();
            } else {
                handleRicerche();
            }
        };

        useEffect(() => {
            const { descrizione, ...otherFilters } = filtri;
            const filtriHasValues = Object.values(otherFilters).some(x => x !== '' && x !== null);
            if (filtriHasValues) {
                handleRicerche();
            }
        }, [filtri.tipologia, filtri.stato, filtri.owner, filtri.azienda]);

        useEffect(() => {
            localStorage.setItem('filtriRicercaNeed', JSON.stringify(filtri));
        }, [filtri]);
        





        //funzione di reset dei campi di ricerca
        const handleReset = async () => {
            setFiltri({
                descrizione: '',
                stato: '',
                tipologia: '',
                owner: '',
                azienda: ''
            });
            setPagina(0);
            setOriginalNeed([]);
            setHasMore(true);

            await fetchData(0);
        };


        //funzione per cancellare il need
        const handleDelete = async (id) => {
            try{
                const responseDelete = await axios.delete(`/need/react/elimina/${id}`, {headers: headers});
                await fetchData(0);
            } catch(error) {
                console.error("Errore durante la cancellazione: ", error);
            }
        };


        //funzione per il refresh
        const handleRefresh = async () => {
            await fetchData(0);
        };








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
                    zIndex: 1000, 
                }}>
                    <RicercheNeed 
                    filtri={filtri}
                    onFilterChange={handleFilterChange}
                    onReset={handleReset}
                    tipologiaOptions={tipologiaOptions}
                    statoOptions={statoOptions}
                    ownerOptions={ownerOptions}
                    aziendaOptions={aziendaOptions}
                    onRicerche={handleRicerche}
                    
                    />
                    </Box>
                    <InfiniteScroll
                    dataLength={originalNeed.length}
                    next={fetchMoreData}
                    hasMore={hasMore}
                    // loader={'Caricamento in corso...'}
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
                            originalNeed.map((need, index) => (
                                <Grid item xs={12} md={6} key={index}>
                                    <NeedCard valori={need}
                                    statoOptions={statoOptions}
                                    onDelete={() => handleDelete(need.id)}
                                    onRefresh={handleRefresh}

                                    />
                                </Grid>
                            ))
                        )
                        }
                        </Grid>
                        </InfiniteScroll>
                    </Box>
                </Box>
        );
    };
    export default Need;