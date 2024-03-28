import React, { useState, useEffect }     from "react";
import { Box, Select, Button, TextField } from "@mui/material";
import axios                              from "axios";


const RecruitingSearchBox = ({ data, onSearch, onReset, onSearchTextChange, OriginalRecruiting, initialEmail}) => {
  const savedSearchTerms = JSON.parse(localStorage.getItem("ricercaRecruiting")) || {
    nome: '',
    cognome: '',
    email: '',
    tipo:'',
    tipologia:'',
    stato:'',
  };

  const [ searchTerm,                 setSearchTerm                   ] = useState(savedSearchTerms);
  const [ tipoOptions,                setTipoOptions                  ] = useState([]);
  const [ statoOptions,               setStatoOptions                 ] = useState([]);
  const [ tipologiaOptions,           setTipologiaOptions             ] = useState([]);
  const [ filteredData,               setFilteredData                 ] = useState([]);


  useEffect(() => {
    if (initialEmail) {
      setSearchTerm((prev) => ({ ...prev, email: initialEmail }));
      handleSearchWithInitialEmail(initialEmail);
    }
  }, [initialEmail]);

  const handleSearchWithInitialEmail = (email) => {
    const newSearchTerm = { ...savedSearchTerms, email };
    setSearchTerm(newSearchTerm);
    handleSearch(); 
  };

  const user = JSON.parse(localStorage.getItem("user"));
  const accessToken = user?.accessToken;


     const headers = {
     Authorization: `Bearer ${accessToken}`
 };


  useEffect(() => {
    const fetchData = async () => {
      try {



        const responseTipologia = await axios.get("/aziende/react/tipologia", { headers });
        const responseTipo      = await axios.get("/staffing/react/tipo", { headers });
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

        
      } catch (error) {
        console.error("Errore durante il recupero dei dati:", error);
      }
    };

    fetchData();
  }, []);

  const handleSearch = async () => {
    // Prepara il payload per la richiesta
    const filtriDaInviare = {
      nome: searchTerm.nome || null,
      cognome: searchTerm.cognome || null,
      email: searchTerm.email || null,
      stato: searchTerm.stato || null,
      tipo: searchTerm.tipo || null,
      tipologia: searchTerm.tipologia || null,
    };
  
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      const accessToken = user?.accessToken;
  
      const response = await axios.post("/staffing/react/mod/ricerca",{ headers: headers, params: filtriDaInviare });
  
      if (response.data && Array.isArray(response.data)) {
        onSearch(response.data);
        setFilteredData(response.data);
      } else {
        console.error("I dati ottenuti non sono nel formato Array:", response.data);
      }
    } catch (error) {
      console.error("Errore durante il recupero dei dati:", error);
    }
  };
  
  

  const handleReset = () => {
    setSearchTerm({
      nome: '',
      cognome: '',
      email: '',
      tipo:'',
      tipologia:'',
      stato:'',
    });
    onSearchTextChange("");
    onReset();
    setFilteredData([]);
    localStorage.removeItem("ricercaRecruiting");

  };


  const handleKeyDown = (e) => {
    if (e.keyCode === 13) { // Verifica se è stato premuto il tasto "Invio"
      handleSearch();     
    }
  };

  
  return (
    <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(6, 1fr) auto', gap: '0.5%', alignItems: 'center', margin: '0.4%', borderBottom: '2px solid #dbd9d9'}}>

    {/* Prima colonna */}
    <TextField 
      sx={{
          '& .MuiOutlinedInput-root': {
              '& fieldset': {
                  border: 'none', 
              },
              '&:hover fieldset': {
                  border: 'none',
              },
              '&.Mui-focused fieldset': {
                  border: 'none',
              },
          },
        display: 'flex', justifyContent: 'center', border: 'solid 1px #c4c4c4', width: '100%', marginTop: '-2%'}}
      type="text"
          placeholder="Nome Candidato"
          id="ricercaNome"
          className="text-form"
          maxLength="45"
          value={searchTerm.nome}
          onKeyDown={handleKeyDown}
          onChange={(e) => setSearchTerm({ ...searchTerm, nome: e.target.value })}
          />
        <TextField 
      sx={{
          '& .MuiOutlinedInput-root': {
              '& fieldset': {
                  border: 'none', 
              },
              '&:hover fieldset': {
                  border: 'none',
              },
              '&.Mui-focused fieldset': {
                  border: 'none',
              },
          },
        display: 'flex', justifyContent: 'center', border: 'solid 1px #c4c4c4', width: '100%', marginTop: '-2%'}}
      type="text"
          placeholder="Cognome Candidato"
          id="ricercaCognome"
          className="text-form"
          maxLength="45"
          value={searchTerm.cognome}
          onKeyDown={handleKeyDown}
          onChange={(e) => setSearchTerm({ ...searchTerm, cognome: e.target.value })}
          />
    {/* Seconda colonna */}
    <TextField 
      sx={{
          '& .MuiOutlinedInput-root': {
              '& fieldset': {
                  border: 'none', 
              },
              '&:hover fieldset': {
                  border: 'none',
              },
              '&.Mui-focused fieldset': {
                  border: 'none',
              },
          },
        display: 'flex', justifyContent: 'center', border: 'solid 1px #c4c4c4', width: '100%', marginTop: '-2%'}}
      type="text"
          placeholder="Email Candidato"
          id="ricercaCognome"
          className="text-form"
          maxLength="45"
          value={searchTerm.email}
          onKeyDown={handleKeyDown}
          onChange={(e) => setSearchTerm({ ...searchTerm, email: e.target.value })}
        />
      <Select
                  className="dropdown-menu"
                  value={searchTerm.tipologia}
                  onChange={e => setSearchTerm({...searchTerm, tipologia: e.target.value })}
                  sx={{
                    marginTop: '1%',
                    borderRadius: "40px",
                    fontSize: "0.8rem",
                    textAlign: "start",
                    color: "#757575",
                  }}
                  native
                  onKeyDown={handleKeyDown}
                >
                  <option value="" disabled>
                    Tipologia
                  </option>
                  {tipologiaOptions.map((option) => (
                    <option key={option.value} value={option.label}>
                      {option.label}
                    </option>
                  ))}
                </Select>
    {/* Terza colonna */}
      <Select
  className="dropdown-menu"
  value={searchTerm.tipo}
  onChange={(e) => setSearchTerm({ ...searchTerm, tipo: e.target.value })}
  sx={{
    marginTop: '1%',
    borderRadius: "40px",
    fontSize: "0.8rem",
    textAlign: "start",
    color: "#757575",
  }}
  native
  onKeyDown={handleKeyDown}
>
  <option value="" disabled>
    Tipo
  </option>
  {tipoOptions.map((option) => (
    <option key={option.label} value={option.label}>
      {option.label}
    </option>
  ))}
</Select>

      <Select
  className="dropdown-menu"
  value={searchTerm.stato}
  onChange={(e) => setSearchTerm({ ...searchTerm, stato: e.target.value })}
  sx={{
    marginTop: '1%',
    borderRadius: "40px",
    fontSize: "0.8rem",
    textAlign: "start",
    color: "#757575",
  }}
  native
  onKeyDown={handleKeyDown}
>
  <option value="" disabled>
    Stato
  </option>
  {statoOptions.map((option) => (
    <option key={option.label} value={option.label}>
      {option.label}
    </option>
  ))}
</Select>

    {/* Quarta colonna */}
    <Box style={{ display: 'flex', justifyContent: 'flex-end', gap: '5%', marginLeft: '10px', marginTop: '-5%' }}>
        <Button
          className="button-search"
          variant="contained"
          onClick={handleSearch}
          sx={{
            width: '2rem',
            height: "40px",
            backgroundColor: "#00853C",
            color: "white",
            borderRadius: "10px",
            fontSize: "0.8rem",
            fontWeight: "bolder",
            "&:hover": {
              backgroundColor: "#00853C",
              color: "white",
              transform: "scale(1.05)",
            },
          }}
        >
          Cerca
        </Button>
        <Button
          className="ripristina-link"
          onClick={handleReset}
          sx={{
            width: '2rem',
            color: 'white', 
            backgroundColor: 'black',
            height: "40px",
            borderRadius: "10px",
            fontSize: "0.8rem",
            fontWeight: "bolder",
            "&:hover": {
              backgroundColor: "black",
              color: "white",
              transform: "scale(1.05)",
            },
          }}>
          Reset
        </Button>
      </Box>
</Box>
  );
};

export default RecruitingSearchBox;