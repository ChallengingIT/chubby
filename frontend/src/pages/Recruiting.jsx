import React, { useEffect, useState }         from "react";
import { useNavigate, useLocation }           from "react-router-dom";
import axios                                  from "axios";
import NoteButton                             from "../components/button/NoteButton.jsx";
import EuroButton                             from "../components/button/EuroButton.jsx";
import PersonInfoButton                       from "../components/button/PersonInfoButton.jsx";
import DeleteButton                           from "../components/button/DeleteButton.jsx";
import ClipButton                             from "../components/button/ClipButton.jsx";
import { Link }                               from "react-router-dom";
import SmileGreenIcon                         from '../components/icone/SmileGreenIcon.jsx';
import SmileOrangeIcon                        from '../components/icone/SmileOrangeIcon.jsx';
import SmileRedIcon                           from '../components/icone/SmileRedIcon.jsx';
import Tabella                                from "../components/Tabella.jsx";




import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Box,
  Button,
  Typography
} from '@mui/material';
import RicercheRecruiting from "../components/ricerche/RicercheRecruiting.jsx";


const Recruiting = () => {

  const navigate = useNavigate();
  const location = useLocation();

  const [ originalRecruiting,         setOriginalRecruiting   ] = useState([]);
  const [ filteredRecruiting,         setFilteredRecruiting   ] = useState([]);
  const [ searchText,                 setSearchText           ] = useState([]);
  const [ notePopup,                  setNotePopup            ] = useState(false);
  const [ ralPopup,                   setRalPopup             ] = useState(false);
  const [ openDialog,                 setOpenDialog           ] = useState(false);
  const [ selectedNote,               setSelectedNote         ] = useState('');
  const [ selectedRal,                setSelectedRal          ] = useState('');
  const [ deleteId,                   setDeleteId             ] = useState(null);
  const [ tipologiaOptions,           setTipologiaOptions     ] = useState([]);
  const [ tipoOptions,                setTipoOptions          ] = useState([]);
  const [ statoOptions,               setStatoOptions         ] = useState([]);
  const [ openFiltri,                 setOpenFiltri           ] = useState(false);
  const [ loading,                    setLoading              ] = useState(false);
  const [ righeTot,                   setRigheTot             ] = useState(0);
  const [ filtri,                     setFiltri               ] = useState(() => {
    const filtriSalvati = localStorage.getItem('filtriRicercaRecruiting');
    return filtriSalvati ? JSON.parse(filtriSalvati) : {
    nome: '',
    cognome: '',
    tipologia: '',
    stato: '',
    tipo:''
    };
});


//stati per la paginazione
const [ pagina,                 setPagina       ] = useState(0);
const quantita = 10;


  
  const user = JSON.parse(localStorage.getItem('user'));
  const accessToken = user?.accessToken;

  const headers = {
    Authorization: `Bearer ${accessToken}`
  };


  const fetchData = async () => {

    setLoading(true);

    const filtriDaInviare = {
      nome: null,
      cognome: null,
      email: null,
      tipologia: null,
      tipo: null,
      stato: null,
      pagina: 0,
      quantita: 10
  };



    try {
        const response          = await axios.get("/staffing/react/mod",          { headers: headers, params: filtriDaInviare });
        const responseTipologia = await axios.get("/aziende/react/tipologia",        { headers });
        const responseTipo      = await axios.get("/staffing/react/tipo",            { headers });
        const responseStato     = await axios.get("/staffing/react/stato/candidato", { headers });


        if (Array.isArray(responseStato.data)) {
            setStatoOptions(responseStato.data.map((stato, index) => ({ label: stato.descrizione, value: stato.id })));
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseStato.data);
        } 


        if (Array.isArray(responseTipologia.data)) {
            setTipologiaOptions(responseTipologia.data.map((tipologia, index) => ({ label: tipologia.descrizione, value: tipologia.id })));
    
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseTipologia.data);
        } 

        if (Array.isArray(responseTipo.data)) {
            setTipoOptions(responseTipo.data.map((tipo, index) => ({ label: tipo.descrizione, value: tipo.id })));
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseTipo.data);
        } 
        const { record, candidati } = response.data;

    if (candidati && Array.isArray(candidati)) {
        setOriginalRecruiting(candidati); 

        if (typeof record === 'number') {
            setRigheTot(record);
        } else {
            console.error("Il numero di record ottenuto non è un numero: ", record);
        }
    } else {
        console.error("I dati ottenuti non contengono 'candidati' come array: ", response.data);
    }
        setLoading(false);
        } catch(error) {
        console.error("Errore durante il recupero dei dati: ", error);
        }
    };
    
    
    useEffect(() => {
        fetchData();
        // eslint-disable-next-line
    }, []);



    //funzione per la paginazione
    const fetchMoreData = async (pagina) => {

      const filtriAttivi = Object.values(filtri).some(value => value !== null && value !== '');
      const url = filtriAttivi ?
      "/staffing/react/mod/ricerca" :
      "/staffing/react/mod";


      const filtriDaInviare = {
        nome: filtri.nome || null,
        cognome: filtri.cognome || null,
        email: null,
        tipologia: filtri.tipologia || null,
        tipo: filtri.tipo || null,
        stato: filtri.stato || null,
        pagina: pagina,
        quantita: 10
    };
  
      try{
        const response          = await axios.get(url,          { headers: headers, params: filtriDaInviare });
        const { record, candidati } = response.data;

        if (candidati && Array.isArray(candidati)) {
            setOriginalRecruiting(candidati); 
    
            if (typeof record === 'number') {
                setRigheTot(record);
            } else {
                console.error("Il numero di record ottenuto non è un numero: ", record);
            }
        } else {
            console.error("I dati ottenuti non contengono 'candidati' come array: ", response.data);
        }
      } catch(error) {
      console.error("Errore durante il recupero dei dati: ", error);
      }
    };


    //funzione per il cambio pagina
    const handlePageChange = (newPage) => {
      setPagina(newPage);
      fetchMoreData(newPage);
  };



  const openDeleteDialog = (id) => {
    setDeleteId(id);
    setOpenDialog(true);
  };

  const navigateToAggiungi = () => {
    navigate('/recruiting/aggiungi');
  };

  const navigateToModificaCandidato = (nome) => {
    navigate(`/recruiting/modifica/${nome}`);
  };

  const handleDelete = async () => {
    try {
      const responseDelete = await axios.delete(`/staffing/elimina/${deleteId}`, { headers: headers });
      setOpenDialog(false);
      fetchData();
    } catch(error) {
      console.error("Errore durante la cancellazione: ", error);
    }
  };

  const handleCloseNotesModal = () => {
    setNotePopup(false);
  };


  const getSmileIcon = (params) => {
    const rating = params.row.rating;
  
    if (rating <= 1) {
      return <SmileRedIcon />;
    } else if (rating >= 2 && rating < 3) {
      return <SmileOrangeIcon />;
    } else if (rating >= 3) {
      return <SmileGreenIcon />;
    } else {
      return rating; 
    }
  };


  // const handleSearch = (filteredData) => {
  //   setFilteredRecruiting(filteredData)
  // };

  // const handleReset = () => {
  //   setSearchText('');
  //   fetchData();
  // };






useEffect(() => {
  localStorage.setItem('filtriRicercaRecruiting', JSON.stringify(filtri));
}, [filtri]);



const handleRicerche = async () => {

  // if (!Object.values(filtri).every(value => value === '')) {
    const filtriDaInviare = {
        nome: filtri.nome || null,
        cognome: filtri.cognome || null,
        email: null,
        tipologia: filtri.tipologia || null,
        tipo: filtri.tipo || null,
        stato: filtri.stato || null,
        pagina: 0,
        quantita: 10
    };



    setLoading(true);
 
    try {
        const response          = await axios.get("/staffing/react/mod/ricerca", { headers: headers, params: filtriDaInviare });
        const responseTipologia = await axios.get("/aziende/react/tipologia",        { headers });
        const responseTipo      = await axios.get("/staffing/react/tipo",            { headers });
        const responseStato     = await axios.get("/staffing/react/stato/candidato", { headers });


        if (Array.isArray(responseStato.data)) {
            setStatoOptions(responseStato.data.map((stato, index) => ({ label: stato.descrizione, value: stato.id })));
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseStato.data);
        } 


        if (Array.isArray(responseTipologia.data)) {
            setTipologiaOptions(responseTipologia.data.map((tipologia, index) => ({ label: tipologia.descrizione, value: tipologia.id })));
    
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseTipologia.data);
        } 

        if (Array.isArray(responseTipo.data)) {
            setTipoOptions(responseTipo.data.map((tipo, index) => ({ label: tipo.descrizione, value: tipo.id })));
        } else {
            console.error("I dati ottenuti non sono nel formato Array:", responseTipo.data);
        } 


        const { record, candidati } = response.data;
        if (candidati && Array.isArray(candidati)) {
          setOriginalRecruiting(candidati);
          if (typeof record === 'number') {
            setRigheTot(record);
          } else {
            console.error("Il numero di record dei candidati in ricercha non è un numero: ", record);
          }
        } else {
            console.error("I dati ottenuti per la ricerca non sono nel formato Array:", response.data);
        }
    } catch (error) {
        console.error("Errore durante il recupero dei dati filtrati:", error);
    } finally {
        setLoading(false);
    }
// }
};


useEffect(() => {
  const { nome, cognome, ...otherFilters } = filtri;
  const filtriHasValues = Object.values(otherFilters).some(x => x !== '' && x != null);

  if (filtriHasValues) {
      handleRicerche();
  }
}, [filtri.tipologia, filtri.stato, filtri.tipo, filtri.nome, filtri.cognome]);



const handleFilterChange = (name) => (event) => {
  const newValue = event.target.value;
  setFiltri({ ...filtri, [name]: newValue });
  // if (name === 'denominazione' && newValue === '') {
  //     fetchData();
  // } else {
  //     handleRicerche();
  // }
};




const handleOpenFiltri = () => setOpenFiltri(true);
const handleCloseFiltri = () => setOpenFiltri(false);



const handleReset = () => {
    setFiltri({
        nome: '',
        cognome: '',
        tipo:'',
        tipologia:'',
        stato: ''
    });
    localStorage.removeItem("RicercheRecruiting");
    setPagina(0);

    fetchData();
};

  const handleDownloadCV = async (idFile, fileDescrizione) => {
    const url = `/files/react/download/file/${idFile}`;
    try {
      const responseDownloadCV = await axios({
        method: 'GET',
        url: url,
        responseType: 'blob',
        headers: headers
      });
      const fileURL = window.URL.createObjectURL(new Blob([responseDownloadCV.data], { type: 'application/pdf' }));
      const link = document.createElement('a');
      link.href = fileURL;
      link.setAttribute('download', `${fileDescrizione}`);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch(error) {
      console.error("Si è verificato un errore durante il download del file: ", error);
    }
  };

  const columns = [
    // { field: "id",            headerName: "ID",             width: 70  },
    { field: "nome",         headerName: "Nome",           flex: 1.3, renderCell: (params) => (
      <div style={{ textAlign: "left"  }}>
      <Link 
      to={`/recruiting/modifica/${params.row.id}`}
      state={{ recruitingData: params.row }}
      style={{ color: 'black' }}
    >
      {params.row.nome} {params.row.cognome}
    </Link>
    </div>
      ),
    },
    { field: "email",          headerName: "Email",          flex: 2},
    { field: "tipologia",      headerName: "Job Title",      flex: 1.4, renderCell: (params) => (
      <div style={{ textAlign: "start" }}>
        {params.row.tipologia && params.row.tipologia.descrizione
          ? params.row.tipologia.descrizione
          : "N/A"}
      </div>
    ),
  }, 
    { field: "rating",        headerName: "Rating",         flex: 0.6, renderCell: (params) => getSmileIcon(params), }, //fino a 1.9 è rosso, da 2 a 3 giallo, sopra 3 è verde
    // { field: "nrating",       headerName: "N. Rating",      width: 90  },
    { field: "owner",         headerName: "Owner",         flex: 0.6, renderCell: (params) => (
        <div style={{ textAlign: "start" }}>
          {params.row.owner && params.row.owner.descrizione
            ? params.row.owner.descrizione
            : "N/A"}
        </div>
      ),
    },
    {
      field: "stato",
      headerName: "Stato",
      flex: 0.6,
      renderCell: (params) => (
        <div style={{ textAlign: "start" }}>
          {params.row.stato && params.row.stato.descrizione
            ? params.row.stato.descrizione
            : "N/A"}
        </div>
      ),
    },
    { field: "dataUltimoContatto",      headerName: "Contatto",       flex: 1 },
    { field: "azioni",        headerName: "Azioni",          flex: 1.6, renderCell: (params) => (
      <Box>
        <NoteButton onClick={() => {
          setNotePopup(true);
          setSelectedNote(params.row.note);
        }} />

        <EuroButton onClick={() => {
          setRalPopup(true);
          setSelectedRal(params.row.ral)
        }}
        />
      <Link
      to={`/recruiting/intervista/${params.row.id}`}
      state = {{ recruitingData: params.row.id}}
      >
    <PersonInfoButton /> 
    </Link>
    <ClipButton 
    idFile={params.row.file ? params.row.file.id : null} 
    fileDescrizione={params.row.file ? params.row.file.descrizione : null}
    onClick={() => handleDownloadCV(
        params.row.file ? params.row.file.id : null,
        params.row.file ? params.row.file.descrizione : null
    )}
/>
        <DeleteButton onClick={() => openDeleteDialog(params.row.id)} />
      </Box>
    ), },
  ];






return (
  <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: 'auto', minHeight: '100vh', flexGrow: 1}}>
    <Box sx={{
      p: 2,
      ml: 26,
      mt: 1.5,
      mb: 0.5,
      mr: 0.8,
      backgroundColor: '#FEFCFD',
      borderRadius: '20px',
      height: '97vh',
      width: '100%',
      flexDirection: 'column',
    }}>
      <RicercheRecruiting 
      filtri={filtri}
      onFilterChange={handleFilterChange}
      onReset={handleReset}
      tipologiaOptions={tipologiaOptions}
      statoOptions={statoOptions}
      tipoOptions={tipoOptions}
      onRicerche={handleRicerche}
      />
      <Box sx={{ mr: 0.2}}>
        { loading ? (
          <Typography variant="body1" sx={{ margin: '30px' }}>Caricamento...</Typography>
        ) : ( 
          <Tabella
          data={originalRecruiting} 
          columns={columns} 
          title="Candidati" 
          getRowId={(row) => row.id}
          pagina={pagina}
          quantita={quantita}
          righeTot={righeTot}
          onPageChange={handlePageChange} 
        />
        )}
      </Box>
            {notePopup && (
                <Dialog open={notePopup} onClose={handleCloseNotesModal} sx={{ '& .MuiDialog-paper': { width: '400px', height: 'auto' } }}>
                  <DialogTitle>Note</DialogTitle>
                  <DialogContent>
                    <DialogContentText>
                      {selectedNote}
                    </DialogContentText>
                  </DialogContent>
                  <DialogActions>
                    <Button onClick={handleCloseNotesModal}>Chiudi</Button>
                  </DialogActions>
                </Dialog>
              )}

              {ralPopup && (
                <Dialog open={ralPopup} onClose={() => setRalPopup(false)} sx={{ '& .MuiDialog-paper': { width: '400px', height: 'auto' } }}>
                  <DialogTitle>RAL</DialogTitle>
                  <DialogContent>
                    <DialogContentText>
                      {selectedRal}
                    </DialogContentText>
                  </DialogContent>
                  <DialogActions>
                    <Button onClick={() => setRalPopup(false)}>Chiudi</Button>
                  </DialogActions>
                </Dialog>
              )}


              <Dialog
                    open={openDialog}
                    onClose={() => setOpenDialog(false)}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                    
                  >
                    <DialogTitle id="alert-dialog-title">{"Conferma Eliminazione"}</DialogTitle>
                    <DialogContent>
                      <DialogContentText id="alert-dialog-description">
                        Sei sicuro di voler eliminare questo candidato?
                      </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                      <Button onClick={() => setOpenDialog(false)} color="primary" style={{
                                backgroundColor: "black",
                                color: "white",
                                "&:hover": {
                                  backgroundColor: "black",
                                  transform: "scale(1.05)",
                                },
                              }}>
                        Annulla
                      </Button>
                      <Button onClick={handleDelete} color="primary" variant="contained" type="submit"
                                style={{
                                  backgroundColor: "#00853C",
                                  color: "white",
                                  "&:hover": {
                                    backgroundColor: "#00853C",
                                    color: "white",
                                    transform: "scale(1.05)",
                                  },
                                }}>
                        Conferma
                      </Button>
                    </DialogActions>
                  </Dialog>
    </Box>
  </Box>

);
};

export default Recruiting;
