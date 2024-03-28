import React, { useState, useEffect }         from "react";
import Button                                 from "@mui/material/Button";
import { Select, TextField}                   from "@mui/material";
import axios                                  from "axios";
// import { DatePicker }                         from "@mui/x-date-pickers/DatePicker";
// import { LocalizationProvider }               from '@mui/x-date-pickers/LocalizationProvider';
// import { AdapterDateFns }                     from '@mui/x-date-pickers/AdapterDateFns';

// import "../../styles/HR.css";








const IntervisteSearchBox = ({ data, onSearch, onReset, onSearchTextChange, OriginalInterviste }) => {
  const savedSearchTerms = JSON.parse(localStorage.getItem("ricercaInterviste")) || {
    stato: '',
    intervistatore: '',
    data: null,
  };


  const [ searchTerm,   setSearchTerm   ] = useState(savedSearchTerms);
  const [ ownerOptions, setOwnerOptions ] = useState([]);
  const [ statoOptions, setStatoOptions ] = useState([]);
  const [ filteredData, setFilteredData ] = useState([]);
  const [ selectedDate, setSelectedDate ] = useState('');


  const accessToken = localStorage.getItem("accessToken"); 
  const headers = {
    Authorization: `Bearer ${accessToken}`,
  };

  const handleKeyDown = (e) => {
    if (e.keyCode === 13) { // Verifica se è stato premuto il tasto "Invio"
      handleSearch();     
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const responseOwner = await axios.get("/aziende/react/owner"           , { headers: headers});
        const responseStato = await axios.get("/staffing/react/stato/candidato", { headers: headers});

                if (Array.isArray(responseStato.data)) {
                  setStatoOptions(responseStato.data.map((stato) => ({ label: stato.descrizione, value: stato.id })));
                } else {
                  console.error("I dati ottenuti non sono nel formato Array:", responseStato.data);
                } 

        if (Array.isArray(responseOwner.data)) {
          setOwnerOptions(responseOwner.data.map((owner) => ({ label: owner.descrizione, value: owner.id })));
        } else {
          console.error("I dati ottenuti non sono nel formato Array:", responseOwner.data);
        } 
      } catch (error) {
        console.error("Errore durante il recupero dei dati:", error);
      }
    };

    fetchData();
  }, []);


  const handleSearch = () => {
    const filteredData = OriginalInterviste.filter(item =>
      Object.keys(searchTerm).every(key =>
        searchTerm[key] === '' ||
        (key === 'candidato' && item[key]?.owner?.descrizione?.toLowerCase().includes(String(searchTerm[key]).toLowerCase())) ||
        (key === 'stato' && item[key]?.descrizione?.toLowerCase().includes(String(searchTerm[key]).toLowerCase())) ||

        String(item[key]).toLowerCase().includes(String(searchTerm[key]).toLowerCase())
      )
    );
    localStorage.setItem("ricercaInterviste", JSON.stringify(searchTerm));

    onSearch(filteredData);
    setFilteredData(filteredData);
  };
  

  const handleReset = () => {
    setSearchTerm({
      stato: '',
      intervistatore: '',
      data: null,
    });
    onSearchTextChange("");
    onReset();
    setFilteredData([]); 
    localStorage.removeItem("ricercaInterviste");

  };



  const handleDateChange = (event) => {
    const newDate = event.target.value;
    setSelectedDate(newDate);
    setSearchTerm({...searchTerm, data: newDate});
  };
  



  const uniformStyle = {
    borderRadius: '40px',
    fontSize: '0.8rem',
    textAlign: 'start',
    color: '#757575',
    width: '100%', // Assicurati che questo si adatti al layout del tuo form
  };



  return (
    <div className="gridContainer" style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr) auto', gap: '10px', alignItems: 'center', borderBottom: '2px solid #dbd9d9',}}>

    {/* prima colonna */}
            <Select
              style={uniformStyle}
                  className="dropdown-menu"
                  value={searchTerm.stato}
                  onChange={e => setSearchTerm({...searchTerm, stato: e.target.value })}
                  sx={{
                    mt:1,
                    borderRadius: "40px",
                    fontSize: "0.8rem",
                    textAlign: "start",
                    color: "#757575",
                    height: '2.6em'

                  }}
                  native
                  onKeyDown={handleKeyDown}
                >
                  <option value="" disabled>
                    Stato
                  </option>
                    {statoOptions.map((option) => (
                    <option key={option.value} value={option.label}>
                      {option.label}
                    </option>
                  ))}
                </Select>


                
    {/* seconda colonna */}
                <Select
                  style={uniformStyle}
                  className="dropdown-menu"
                  value={searchTerm.intervistatore}
                  onChange={e => setSearchTerm({...searchTerm, owner: e.target.value })}
                  sx={{
                    mt:1,
                    borderRadius: "40px",
                    fontSize: "0.8rem",
                    textAlign: "start",
                    color: "#757575",
                    height: '2.6em'

                  }}
                  native
                  onKeyDown={handleKeyDown}
                >
                  <option value="" disabled>
                    Intervistatore
                  </option>
                  {ownerOptions.map((option) => (  
                    <option key={option.value} value={option.label}>
                      {option.label}
                    </option>
                  ))}
                </Select>



                {/* <TextField
                    style={uniformStyle}
                    type="date"
                    label="date"
                    value={searchTerm.data}
                    InputLabelProps={{ shrink: true}}
                    variant="outlined"
                    onKeyDown={handleKeyDown}
                    onChange={handleDateChange}
                    InputProps={{
                      style: {
                          // marginBottom: "15px",
                        borderRadius: "40px", // Imposta i bordi arrotondati
                      //   fontSize: "0.8rem",
                        textAlign: "start",
                        color: "#757575",
                        height: '2.4em'

                      }
                    }}
                  /> */}




                {/* terza colonna */}
                <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
                <Button
                  className="button-search"
                  variant="contained"
                  onClick={handleSearch}
                  
                  sx={{
                    width: '100px',
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
                    width: '100px', 
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
              </div>
            </div>
  );


};

export default IntervisteSearchBox;
