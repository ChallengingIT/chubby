import React, { useState, useEffect } from "react";
import axios                          from "axios";
import Need                           from "../../pages/Need.jsx";
import { 
  TextField, 
  Button, 
  MenuItem, 
  Select, 
  InputLabel, 
  FormControl,
  Box
} from '@mui/material';


const NeedMatchSearchBox = ({ data, onSearch, onReset, onSearchTextChange, OriginalAssociabili}) => {

  const savedSearchTerms = JSON.parse(localStorage.getItem("ricercaNeedMatch")) || {
    nome: '',
    cognome: '',
    jobtitle: '',
    tipologia: '',
    seniority: '',

  };


  

const accessToken = localStorage.getItem("accessToken"); 

const headers = {
  Authorization: `Bearer ${accessToken}`,
};


  const [ searchTerm,                     setSearchTerm                 ] = useState(savedSearchTerms);
  const [ tipoOptions,                    setTipoOptions                ] = useState([]);
  const [ jobTitleOptions,                setJobTitleOptions            ] = useState([]);
  const [ tipologiaOptions,               setTipologiaOptions           ] = useState([]);
  const [ filteredData,                   setFilteredData               ] = useState([]);
  const seniorities = [
    { label: 'Neo', value: '0' },
    { label: 'Junior', value: '1' },
    { label: 'Middle', value: '2' },
    { label: 'Senior', value: '3' }
  ];

  useEffect(() => {
    const fetchData = async () => {
      try {
        const responseJobTitle = await axios.get("/aziende/react/tipologia", { headers: headers});
        const responseTipo     = await axios.get("/staffing/react/tipo"    , { headers: headers});

        if (Array.isArray(responseJobTitle.data)) {
          setJobTitleOptions(responseJobTitle.data.map((jobTitle) => ({ label: jobTitle.descrizione, value: jobTitle.id })));
        } else {
          console.error("I dati ottenuti non sono nel formato Array:", responseJobTitle.data);
        } 


        if (Array.isArray(responseTipo.data)) {
          setTipoOptions(responseTipo.data.map((tipo) => ({ label: tipo.descrizione, value: tipo.id })));
        } else {
          console.error("I dati ottenuti non sono nel formato Array:", responseTipo.data);
        } 
      } catch (error) {
        console.error("Errore durante il recupero dei dati:", error);
      }
    };

    fetchData();
  }, []);

  
  const handleSearch = () => {

    if (Array.isArray(OriginalAssociabili)) {
    const filteredData = OriginalAssociabili.filter((item) =>
      Object.keys(searchTerm).every((key) =>
        searchTerm[key] === '' ||
        (key === 'owner' && item[key]?.descrizione?.toLowerCase().includes(String(searchTerm[key]).toLowerCase())) ||
        (key === 'stato' && item[key]?.descrizione?.toLowerCase().includes(String(searchTerm[key]).toLowerCase())) ||
        (key === 'tipologia' && item[key]?.descrizione?.toLowerCase().includes(String(searchTerm[key]).toLowerCase())) ||
        String(item[key]).toLowerCase().includes(String(searchTerm[key]).toLowerCase())
      )
    );
    localStorage.setItem("ricercaNeedMatch", JSON.stringify(searchTerm));

    onSearch(filteredData);
    setFilteredData(filteredData);
  } else {
    console.error("OriginalAssociabili non è definito o non è un array");
  }
  };
  
  
  const handleReset = () => {
    setSearchTerm({
      nome: '',
      cognome: '',
      jobtitle: '',
      tipologia: '',
      seniority: '',
    });
    onSearchTextChange("");
    onReset();
    setFilteredData([]);
    localStorage.removeItem("ricercaNeedMatch");

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
        display: 'flex', justifyContent: 'center', border: 'solid 1px #c4c4c4', width: '100%', marginTop: '-2%', height: '1.4em'
      }}
      type="text"
                  placeholder="Nome"
                  className="text-form"
                  id="nome"
                  max="4"
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
                display: 'flex', justifyContent: 'center', border: 'solid 1px #c4c4c4', width: '100%', marginTop: '-2%', height: '1.4em'
              }}
                  type="text"
                  placeholder="Cognome"
                  className="text-form"
                  id="cognome"
                  max="4"
                  value={searchTerm.cognome}
                  onKeyDown={handleKeyDown}
                  onChange={(e) => setSearchTerm({ ...searchTerm, cognome: e.target.value })}
                />

        {/* Seconda colonna */}
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
                height: '2.4em'

              }}
              native
              onKeyDown={handleKeyDown}
            >
              <option value="" disabled>
                Job Title
              </option>
              {jobTitleOptions.map((option) => (
                <option key={option.value} value={option.label}>
                  {option.label}
                </option>
              ))}
            </Select>
          <Select
              className="dropdown-menu"
              value={searchTerm.tipo}
              onChange={e => setSearchTerm({...searchTerm, tipo: e.target.value })}
              sx={{
                marginTop: '1%',
                borderRadius: "40px",
                fontSize: "0.8rem",
                textAlign: "start",
                color: "#757575",
                height: '2.4em'

              }}
              native
              onKeyDown={handleKeyDown}
            >
              <option value="" disabled>
                Tipologia
              </option>
              {tipoOptions.map((option) => (
                <option key={option.value} value={option.label}>
                  {option.label}
                </option>
              ))}
            </Select>
        {/* Terza colonna */}
          <Select
              className="dropdown-menu"
              value={searchTerm.seniority}
              onChange={e => setSearchTerm({...searchTerm, seniority: e.target.value })}
              sx={{
                marginTop: '1%',
                borderRadius: "40px",
                fontSize: "0.8rem",
                textAlign: "start",
                color: "#757575",
                height: '2.4em'

              }}
              native
              onKeyDown={handleKeyDown}
            >
              <option value="" disabled>
                Seniority
              </option>
              {seniorities.map((option) => (
                <option key={option.value} value={option.label}>
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
            height: "2em",
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
            height: "2em",
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

export default NeedMatchSearchBox;