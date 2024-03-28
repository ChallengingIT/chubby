import React, { useState, useEffect }                   from 'react';
import { useNavigate  }                                 from 'react-router-dom';
import axios                                            from 'axios';
import SearchIcon                                       from '@mui/icons-material/Search';
// import BusinessCenterIcon                               from '@mui/icons-material/BusinessCenter'; //aziende
import CloseIcon                                        from '@mui/icons-material/Close';
import KeypeopleCard                                    from '../components/card/KeypeopleCard';
import InfiniteScroll                                   from 'react-infinite-scroll-component';
import RicercheKeypeople                                from '../components/ricerche/RicercheKeypeople';

import { 
    Box,
    Grid,
    CircularProgress,
    } from '@mui/material';


const Keypeople = () => {

    const navigate = useNavigate();

    const [ originalKeypeople,   setOriginalKeypeople ] = useState([]);
    const [ loading,             setLoading           ] = useState(false);


    //stati ricerche
    const [ clienteOptions,             setClienteOptions             ] = useState([]);
    const [ ownerOptions,               setOwnerOptions               ] = useState([]);
    const [ filtri,                     setFiltri                     ] = useState(() => {
        const filtriSalvati = localStorage.getItem('filtriRicercaKeypeople');
        return filtriSalvati ? JSON.parse(filtriSalvati) : {
        nome:  '',
        azienda: '',
        stato: '',
        owner:''
        };
    });

    //stati per la paginazione
    const [ pagina,             setPagina       ] = useState(0);
    const [ hasMore,            setHasMore      ] = useState(false);
    const quantita = 10;


    const user = JSON.parse(localStorage.getItem('user'));
    const accessToken = user?.accessToken;

    const headers = {
        Authorization: `Bearer ${accessToken}`
    };
    




    const fetchData = async () => {

        const filtriDaInviare = {
            nome: filtri.nome || null,
            azienda: filtri.azienda || null,
            owner: filtri.owner || null,
            stato: filtri.stato || null,
            pagina: 0,
            quantita: 10
        };


        setLoading(true);
        try {
    
        const response        = await axios.get("/keypeople/react/mod",             { headers: headers, params: filtriDaInviare});
        const responseCliente = await axios.get("/aziende/react/select",            { headers: headers });
        const responseOwner   = await axios.get("/aziende/react/owner",             { headers: headers });

        if (Array.isArray(responseOwner.data)) {
            setOwnerOptions(responseOwner.data.map((owner, index) => ({ label: owner.descrizione, value: owner.id })));
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseOwner.data);
        } 


        if (Array.isArray(responseCliente.data)) {
            setClienteOptions(responseCliente.data.map((cliente) => ({ label: cliente.denominazione, value: cliente.id })));
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseCliente.data);
        }
    
        if (Array.isArray(response.data)) {
            const keypeopleConId = response.data.map((keypeople) => ({ ...keypeople}));
            setOriginalKeypeople(keypeopleConId);
            setHasMore(keypeopleConId.length >= quantita);
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", response.data);
        }
        setLoading(false);
        } catch (error) {
            console.error("Errore durante il recupero dei dati:", error);
        }
    };
    
    useEffect(() => {
        const filtriSalvati = localStorage.getItem('filtriRicercaKeypeople');
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
        "/keypeople/react/ricerca/mod" :
        "/keypeople/react/mod";


        const filtriDaInviare = {
            nome: filtri.nome || null,
            azienda: filtri.azienda || null,
            owner: filtri.owner || null,
            stato: filtri.stato || null,
            pagina: paginaSuccessiva,
            quantita: 10
        };
        
        try {
            const response        = await axios.get(url,             { headers: headers, params: filtriDaInviare});
            if (Array.isArray(response.data)) {
                const keypeopleConId = response.data.map((keypeople) => ({...keypeople}));
                setOriginalKeypeople((prev) => [...prev, ...keypeopleConId]);
                setHasMore(response.data.length >= quantita);
            } else {
                console.error("I dati ottenuti non sonon nel formato Array: ", response.data);
            }
        } catch(error) {
            console.error("Errore durante il recupero dei dati: ", error);
        }
        setPagina((prevPagina) => prevPagina + 1);
    };

    //funzione per la ricerca

    const handleRicerche = async () => {

        const filtriDaInviare = {
            nome: filtri.nome || null,
            azienda: filtri.azienda || null,
            owner: filtri.owner || null,
            stato: filtri.stato || null,
            pagina: 0,
            quantita: 10,
        };
        setLoading(true);
        try {
            const response = await axios.get("/keypeople/react/ricerca/mod", { headers: headers, params: filtriDaInviare });
            const responseCliente = await axios.get("/aziende/react/select",            { headers: headers });
            const responseOwner   = await axios.get("/aziende/react/owner",             { headers: headers });

            if (Array.isArray(responseOwner.data)) {
                setOwnerOptions(responseOwner.data.map((owner, index) => ({ label: owner.descrizione, value: owner.id })));
            } else {
                console.error("I dati ottenuti non sono nel formato Array:", responseOwner.data);
            } 


            if (Array.isArray(responseCliente.data)) {
                setClienteOptions(responseCliente.data.map((cliente) => ({ label: cliente.denominazione, value: cliente.id })));
            } else {
                console.error("I dati ottenuti non sono nel formato Array:", responseCliente.data);
            }

            if (Array.isArray(response.data)) {
                setOriginalKeypeople(response.data);
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
        if( name === 'nome' && newValue === '') {
            fetchData();
        } else {
            handleRicerche();
        }
    };

    useEffect(() => {
        const { nome, ...otherFilters } = filtri;
        const filtriHasValues = Object.values(otherFilters).some(x => x !== '' && x != null);
    
        if (filtriHasValues) {
            handleRicerche();
        }
    }, [filtri.azienda, filtri.stato, filtri.owner]);

    useEffect(() => {
        localStorage.setItem('filtriRicercaKeypeople', JSON.stringify(filtri));
    }, [filtri]);
    

    //funzione di reset dei campi di ricerca

    const handleReset = async () => {
        setFiltri({
            nome: '',
            azienda: '',
            stato: '',
            owner: ''
        });
        setPagina(0);
        setOriginalKeypeople([]);
        setHasMore(true);
        await fetchData(0);
    };


    const statoOptions = [
        { label: 'Caldo', value: '1' },
        { label: 'Tiepido', value: '2' },
        { label: 'Freddo', value: '3' }
    ];


     //funzione per cancellare l'azienda
    const handleDelete = async (id) => {
        try{
            const responseDelete = await axios.delete(`/keypeople/react/elimina/${id}`, {headers: headers});
            await fetchData(0);
        } catch(error) {
            console.error("Errore durante la cancellazione: ", error);
        }
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
                    <RicercheKeypeople
                        filtri={filtri}
                        onFilterChange={handleFilterChange}
                        onReset={handleReset}
                        aziendaOptions={clienteOptions}
                        statoOptions={statoOptions}
                        ownerOptions={ownerOptions}
                        onRicerche={handleRicerche}
                        />
                </Box>
                <InfiniteScroll
                dataLength={originalKeypeople.length}
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
                            <CircularProgress /> 
                        </Box>
                    ) : (
                        originalKeypeople.map((keypeople, index) => (
                            <Grid item xs={12} md={6} key={index}>
                                <KeypeopleCard 
                                valori={keypeople}
                                onDelete={() => handleDelete(keypeople.id)}/>
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
export default Keypeople;