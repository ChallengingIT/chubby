
//funzionante al 21 dicembre 2023 9:00

import React, { useEffect, useState}          from 'react'
import { Link, useParams }                    from 'react-router-dom';
import { useNavigate, useLocation }           from "react-router-dom";
import axios                                  from 'axios';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
  Box,
  Typography
} from '@mui/material';
import AddIcon from "@mui/icons-material/Add";
import Tabella from '../components/Tabella.jsx';
import DeleteButton from '../components/button/DeleteButton.jsx';
import EditButton from '../components/button/EditButton.jsx';
import VisibilityButton from '../components/button/VisibilityButton.jsx';

function IntervisteList() {
  const navigate                = useNavigate();
  const params                  = useParams();
  const { id }                  = params;
  const location                = useLocation();
  const { recruitingData = {} } = location.state || {};
  const candidatoID             = id;







  const [ originalInterviste,       setOriginalInterviste   ] = useState([]);
  const [ candidatoData,            setCandidatoData        ] = useState([]);
  const [ openDialog,               setOpenDialog           ] = useState(false);
  const [ deleteId,                 setDeleteId             ] = useState(null);


  //stati per la paginazione
  const [ righeTot,                 setRigheTot             ] = useState(0);
  const [ pagina,                   setPagina               ] = useState(0);
  const [ loading,                  setLoading              ] = useState(false);
  const quantita = 10;
  


    const user = JSON.parse(localStorage.getItem("user"));
    const accessToken = user?.accessToken;

    const headers = {
      Authorization: `Bearer ${accessToken}`
    };

  const fetchData = async () => {
    setLoading(true);
    const paginazione = {
      pagina: 0,
      quantita: 10
    };

    const filtriDaInviare = {
      nome: recruitingData.nome,
      cognome: recruitingData.cognome,
      email: recruitingData.email,
      pagina: 0,
      quantita: 1
    }

    try {
      const response                           = await axios.get(`/intervista/react/mod/${id}`       , { headers: headers, params: paginazione});  //queste sono le interviste effettive
      const responseCandidatoFiltrato          = await axios.get("/staffing/react/mod/ricerca",        { headers: headers, params: filtriDaInviare }); //questo è il candidato

      if (typeof responseCandidatoFiltrato.data === 'object') {
        setCandidatoData([responseCandidatoFiltrato.data]); 
      }

      const { record, interviste } = response.data;
      if (interviste && Array.isArray(interviste)) {
        setOriginalInterviste(interviste);
        if (typeof record === 'number') {
          setRigheTot(record);
        } else {
          console.error("Il numero di recordo ottenuto non è un numero: ", record);
        }
      } else {
        console.error("I dati non contengono interviste in formato array: ", response.data);
      }
    } catch (error) {
      console.error("Errore durante il recupero dei dati:", error);
    }
  };

  

  useEffect(() => {
    fetchData();
  }, []);



   //funzione per la paginazione
   const fetchMoreData = async (pagina) => {
    const paginazione = {
      pagina: pagina,
      quantita: 10
  };
  const filtriDaInviare = {
    nome: recruitingData.nome,
    cognome: recruitingData.cognome,
    email: recruitingData.email,
    pagina: 0,
    quantita: 1
  }

    try{
      const responseIntervista                = await axios.get(`/intervista/react/mod/${id}`       , { headers: headers, params: paginazione});
      const responseCandidatoFiltrato          = await axios.get("/staffing/react/mod/ricerca", { headers: headers, params: filtriDaInviare });

      const { record, interviste } = responseIntervista.data;

      if (interviste && Array.isArray(interviste)) {
          setOriginalInterviste(interviste); 
  
          if (typeof record === 'number') {
              setRigheTot(record);
          } else {
              console.error("Il numero di record ottenuto non è un numero: ", record);
          }
      } else {
          console.error("I dati ottenuti non contengono 'interviste' come array: ", responseIntervista.data);
      }
    } catch(error) {
    console.error("Errore durante il recupero dei dati: ", error);
    }
  };

  useEffect(() => {
  }, [candidatoData]); 

  const handleGoBack = () => {
navigate("/recruiting");
  };

  const openDeleteDialog = (id) => {
    setDeleteId(id);
    setOpenDialog(true);
  };


      //funzione per il cambio pagina
      const handlePageChange = (newPage) => {
        setPagina(newPage);
        fetchMoreData(newPage);
    };
  



  const navigateToAggiungiIntervista = () => {
    navigate("/intervista/aggiungi", { state: {  candidatoID  }});
  };



  const handleDelete = async (id) => {
    try {

        const response = await axios.delete(`/intervista/react/elimina/${deleteId}`, { headers: headers});
        setOpenDialog(false);

  
  
        fetchData();
      
    } catch (error) {
      console.error("Errore durante l'eliminazione dell'intervista:", error);
    }
  };


const candidatoNome    = candidatoData.length > 0 ? candidatoData[0].nome : '';
const candidatoCognome = candidatoData.length > 0 ? candidatoData[0].cognome : '';




  const prepareDataForChild = (rowData) => {
    return {
      rowData,
      candidatoData,
    };
  };
  
const table1 = [
  // { field: "id", headerName: "Id", width: 70},
  { field: "stato",               headerName: "Stato",          flex: 1,
    renderCell: (params) => (
      <div style={{ textAlign: "start" }}>
        {params.row.stato && params.row.stato.descrizione
          ? params.row.stato.descrizione
          : "N/A"}
      </div>
    ),
  },
  { field: "dataColloquio",       headerName: "Data Colloquio", flex: 1},
  { field: "candidato",           headerName: "Intervistatore", flex: 1,
renderCell: (params) => (
  <div style={{ textAlign: "start" }}>
        {params.row.owner?.descrizione || "N/A"}
      </div>
    ),
},
  { field: "dataAggiornamento",   headerName: (
      <div style={{ lineHeight: '1' }}>
        Follow Up <br /> Data Aggiornamento
      </div>
    ),
    width: 350,
    renderCell: (params) => (
      <div style={{ textAlign: "start" }}>
        {params.row.tipo?.descrizione || "N/A"}
      </div>
    ),
  },
  
   //dataAggiornamento e follo up si chiama intervista.tipo.descrizione
  { field: "azioni",         headerName: "Azioni",          flex: 1, renderCell: (params) => (
    <div>
      <Link
      to={`/intervista/visualizza/${params.row.id}`}
      state={params.row}
  
>
<VisibilityButton />
</Link>
<Link
to={`/intervista/modifica/${params.row.id}`}
state={params.row}
>
<EditButton />
</Link>
      
<DeleteButton onClick={() => openDeleteDialog(params.row.id)} />
    </div>
  ), },
];


  return (
    <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: '100vh', flexGrow: 1, overflow: 'hidden'}}>
      <Box sx={{
        p: 2,
        ml: 25,
        mt: 1.5,
        mb: 0.5,
        mr: 0.8,
        backgroundColor: '#FEFCFD',
        borderRadius: '10px',
        height: '99%',
        width: '100%',
        flexDirection: 'column',
        overflow: 'hidden'
      }}>
              <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', width: '100%', mt: 5}}>
              <Button 
                onClick={handleGoBack}
                sx={{
                    backgroundColor: '#00853C',
                    color: 'white', 
                    border: 'none', 
                    fontSize: '0.8rem', 
                    cursor: 'pointer', 
                    display: 'flex', 
                    alignItems: 'center', 
                    padding: '0.5rem 1rem', 
                    outline: 'none', 
                    borderRadius: '10px',
                    '&:hover': {
                      backgroundColor: '#00853C',
                      color: 'white',
                      transform: 'scale(1.05)'
                    }
                }}
                >
                <span style={{ marginRight: '0.5rem' }}>{"<"}</span> 
                Indietro
                </Button>
                <Typography variant="h4" component="h1" sx={{  fontWeight: 'bold', fontSize: '1.4em'}}>Lista ITW di {recruitingData.nome} {recruitingData.cognome}</Typography>
                    <Button onClick={navigateToAggiungiIntervista}
                    startIcon={<AddIcon />}
                    sx={{
                        width: "100%",
                        maxWidth: "16em",
                        height: '3em',
                        backgroundColor: "#00853C",
                        color: "white",
                        borderRadius: "10px",
                        fontSize: "0.7rem",
                        fontWeight: "bolder",
                        
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        textAlign:'center',
                
                        "&:hover": {
                        backgroundColor: "#00853C",
                        transform: "scale(1.05)",
                        color: 'white',
                        },
                    }}>Aggiungi Intervista</Button>
                    </Box>
                    <Box sx={{ mr: 0.2, mt: 10}}>

                    <Tabella
                        data={originalInterviste} 
                        columns={table1} 
                        title="Interviste" 
                        getRowId={(row) => row.id}
                        pagina={pagina}
                        quantita={quantita}
                        righeTot={righeTot}
                        onPageChange={handlePageChange}
                        />
                        
                    </Box>


                        <Dialog
                        open={openDialog}
                        onClose={() => setOpenDialog(false)}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                        
                    >
                        <DialogTitle id="alert-dialog-title">{"Conferma Eliminazione"}</DialogTitle>
                        <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            Sei sicuro di voler eliminare questa intervista?
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

export default IntervisteList;