// import React, { useState, useEffect, useRef } from "react";


// import {
//     Grid,
//     Button,
//     TextField,
//     Select,
//     MenuItem,
//     InputLabel,
//     FormControl,
//     Box,
//     Typography,
//     Checkbox,
//     ListItemText,
//     FormHelperText,
//     Dialog,
//     DialogActions,
//     DialogContent, 
//     DialogContentText, 
//     DialogTitle,
//     Autocomplete,
//     Chip
// } from "@mui/material";
// import CloudDownloadIcon from "@mui/icons-material/CloudDownload";
// import CloudUploadIcon  from "@mui/icons-material/CloudUpload";
// import DeleteIcon       from "@mui/icons-material/Delete";
// import axios            from "axios";



// const FieldBoxFile = ({
//     fields,
//     initialValues = {},
//     disableFields = {},
//     onSubmit,
//     showBackButton = true,
//     showSaveButton = true,
//     title = "",
//     campiObbligatori,
//     skillsOptions,
//     idCandidato,
    
// }) => {




//     const [values, setValues] = useState(initialValues || {});
//     const [errors, setErrors] = useState({});

//     const [ fileCV,               setFileCV             ] = useState(null);
//     const [ fileCF,               setFileCF             ] = useState(null);
//     const [ fileMultipli,                               ] = useState([]);
//     const [ openDialog,           setOpenDialog         ] = useState(false);
//     const [ selectedFileId,       setSelectedFileId     ] = useState(null);

//     const user = JSON.parse(localStorage.getItem("user"));
//     const accessToken = user?.accessToken;

//     const headers = {
//         Authorization: `Bearer ${accessToken}`
//     };

    
//     //apertura popup eliminazione cv e cf
//     const handleOpenDeleteDialogCVCF = (fileId, fileType) => {
//         setSelectedFileId({ id: fileId, type: fileType });
//         setOpenDialog(true);
//     };
    

//     const getDisabledStyles = (isDisabled) => {
//         return isDisabled ? { color: "black" } : {};
//     };
    
//     const handleChange = (name) => (event) => {
//         const { value } = event.target;
//         let fileValue = value;
//         setValues({ ...values, [name]: fileValue });
//         if (errors[name]) {
//         setErrors({ ...errors, [name]: "" });
//         }
//     };

//       //funzione di change per decimalNumber
//     const handleChangeDecimal = (name) => (event) => {
//     let { value } = event.target;
//     value = value.replace(/,/g, '.');
//     if (!value || value.match(/^\d+(\.\d{0,2})?$/)) {
//     setValues({ ...values, [name]: value });
//     }
//     };


//     const validate = () => {
//         let tempErrors = {};
//         campiObbligatori.forEach((field) => {
//         if (!values[field]) {
//             tempErrors[field] = "Campo obbligatorio";
//         }
//         });
//         setErrors(tempErrors);
//         return !Object.keys(tempErrors).length;
//     };

//     const handleSubmit = (e) => {
//         e.preventDefault();
//         const isValid = validate();

//         if (isValid) {
//             onSubmit(values, fileCV, fileCF, fileMultipli);
//         }
//     };

//     const [isWeekPickerVisible, setIsWeekPickerVisible] = useState(false);
//     const weekPickerRef = useRef(null);
//     useEffect(() => {
//         const handleClickOutside = (event) => {
//         if (
//             weekPickerRef.current &&
//             !weekPickerRef.current.contains(event.target)
//         ) {
//             setIsWeekPickerVisible(false);
//         }
//         };

//         if (isWeekPickerVisible) {
//         document.addEventListener("mousedown", handleClickOutside);
//         }

//         return () => {
//         document.removeEventListener("mousedown", handleClickOutside);
//         };
//     }, [isWeekPickerVisible]); 



//     const handleDownloadCVCF = async (fileId, fileDescrizione) => {
//         const url = `/files/react/download/file/${fileId}`;
//         try {
//             const response = await axios({
//                 method: 'GET',
//                 url: url,
//                 responseType: 'blob', 
//                 headers: headers
//             });

//             const fileURL = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
//             const link = document.createElement('a');
//             link.href = fileURL;
//             link.setAttribute('download', `${fileDescrizione}.pdf`); 
//             document.body.appendChild(link);
        
//             link.click();
//             document.body.removeChild(link);
//             } catch (error) {
//             console.error('Si è verificato un errore durante il download del file:', error);
//             }
//         };


//         const handleDeleteCVCF = async (fileId, idCandidato, fileType) => {
//             const idc = idCandidato;
//             try {
//             const response = await axios.delete(`/files/react/elimina/file/candidato/${fileId}/${idc}`, { headers: headers })
//             if(response.data === "OK") {
//             } else {
//                 console.error("Errore dal server: ", response.data);
//             }
//             if (fileType === 'cv') {
//                 setValues({ ...values, cv: null });
//             } else if (fileType === 'cf') {
//                 setValues({ ...values, cf: null });
//             }
//             setOpenDialog(false); 
//             setSelectedFileId(null);  
//             } catch (error) {
//             console.error("Si è verificato un errore durante l'eliminazione del file:", error);
//             }
//         };


//         const handleChangeCV = (name) => (event) => {
//             const file = event.target.files[0];
//             if (file) {
//                 setFileCV(file);
//                 setValues({ ...values, cv: { descrizione: file.name } });
//             }
//         };
        
//         const handleChangeCF = (name) => (event) => {
//             const file = event.target.files[0];
//             if (file) {
//                 setFileCF(file);
//                 setValues({ ...values, cf: { descrizione: file.name } });
//             }
//         };


//     //*******************parte del return per la creazione grafica dei componenti*******************//

//     const renderField = (field) => {
//         const isDisabled = disableFields[field.name];
//         const errorMessage = errors[field.name];
//         switch (field.type) {
//         case "text":
//             return (
//             <TextField
//                 id="filled-basic" 
//                 variant="filled"  
//                 style={getDisabledStyles(field.disabled)}
//                 label={field.label}
//                 name={field.name}
//                 onChange={handleChange(field.name)}
//                 value={values[field.name] || ""}
//                 fullWidth
//                 error={!!errors[field.name]} 
//                 helperText={errors[field.name]} 
//                 disabled={field.disabled}
//                 sx={{
//                     borderRadius: '20px', 
//                     backgroundColor: '#EDEDED', 
//                     '& .MuiFilledInput-root': {
//                         backgroundColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-underline:after': {
//                         borderBottomColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     },
//                     '&:hover .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     }
//                 }} 
                
//             />
//             );


//             case 'decimalNumber':
//                 return (
//                     <TextField
//                     id="filled-basic" 
//                     variant="filled" 
//                     label={field.label}
//                     name={field.name}
//                     value={values[field.name] || ''}
//                     onChange={handleChangeDecimal(field.name)} 
//                     fullWidth
//                     error={!!errors[field.name]}
//                     helperText={errors[field.name]}
//                     disabled={field.disabled}
//                     sx={{ 
//                         width: "100%",
//                         textAlign: "left",
//                         borderRadius: '20px', 
//                     backgroundColor: '#EDEDED', 
//                     '& .MuiFilledInput-root': {
//                         backgroundColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-underline:after': {
//                         borderBottomColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     },
//                     '&:hover .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     }

//                         }}
                    
                    
//                     />
//                 );

//                 case "select":
//                     return (
//                     <Autocomplete
//                         fullWidth
//                         variant="filled"
//                         options={field.options}
//                         value={values[field.name] || null}
//                         onChange={(event, value) => {
//                             setValues({ ...values, [field.name]: value });
//                             if (errors[field.name]) {
//                                 setErrors({ ...errors, [field.name]: "" });
//                             }
//                         }}
//                         disableCloseOnSelect
//                         renderInput={(params) => (
//                             <TextField
//                                 id="filled-basic"  
//                                 variant="filled"  
//                                 {...params}
//                                 label={field.label}
//                                 error={!!errors[field.name]}
//                                 helperText={errors[field.name]}
//                                 sx={{ 
//                                     borderRadius: '20px', 
//                                     backgroundColor: '#EDEDED', 
//                                     '& .MuiFilledInput-root': {
//                                         backgroundColor: 'transparent',
//                                         '&::before': {
//                                             borderBottom: 'none',  
//                                         },
//                                         '&:hover::before': {
//                                             borderBottom: 'none',  
//                                         },
//                                     },
//                                 }} 
//                             />
//                         )}
//                     />
//                     );

//         case "multipleSelectSkill":
//             return (
//             <Autocomplete
//                 multiple
//                 options={skillsOptions}
//                 value={values.skills || []}
//                 onChange={(event, value) => {
//                     setValues({ ...values, skills: value });
//                     if (errors[field.name]) {
//                         setErrors({ ...errors, [field.name]: "" });
//                     }
//                 }}
//                 disableCloseOnSelect
//                 renderTags={(value, getTagProps) =>
//                     value.map((option, index) => (
//                         <Chip label={option.label} {...getTagProps({ index })} />
//                     ))
//                 }
//                 renderInput={(params) => (
//                     <TextField
//                         {...params}
//                         fullWidth
//                         label={field.label}
//                         error={!!errors[field.name]}
//                         helperText={errors[field.name]}
//                         id="filled-basic"  
//                         variant="filled" 
//                         sx={{
//                             borderRadius: '20px', 
//                             backgroundColor: '#EDEDED', 
//                             '& .MuiFilledInput-root': {
//                                 backgroundColor: 'transparent',
//                                 '&::before': {
//                                     borderBottom: 'none',  
//                                 },
//                                 '&:hover::before': {
//                                     borderBottom: 'none',  
//                                 },
//                             },
//                         }} 
//                     />
//                 )}
//             />
//             );
        

//         case "selectValue":
//             return (
//             <FormControl fullWidth disabled={isDisabled}>
//                 <InputLabel>{field.label}</InputLabel>
//                 <Select
//                 name={field.name}
//                 value={values[field.name] || ""}
//                 onChange={handleChange(field.name)}
//                 disabled={field.disabled}
//                 style={{ width: "100%", textAlign: "left" }}
//                 id="filled-basic"  
//                 variant="filled" 
//                 sx={{ 
//                     borderRadius: '20px', 
//                     backgroundColor: '#EDEDED', 
//                     '& .MuiFilledInput-root': {
//                         backgroundColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-underline:after': {
//                         borderBottomColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     },
//                     '&:hover .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     }
//                 }}  
//                 >
//                 {field.options.map((option) => (
//                     <MenuItem key={option.value} value={option.value}>
//                     {option.label}
//                     </MenuItem>
//                 ))}
//                 </Select>
//             </FormControl>
//             );

//         case "weekPicker":
//             return (
//             <TextField
//                 type="week"
//                 label={field.label}
//                 name={field.name}
//                 value={values[field.name] || ""}
//                 onChange={handleChange(field.name)}
//                 InputLabelProps={{ shrink: true }}
//                 error={!!errors[field.name]}
//                 fullWidth
//                 id="filled-basic"  
//                 variant="filled" 
//                 sx={{ 
//                     borderRadius: '20px', 
//                     backgroundColor: '#EDEDED', 
//                     '& .MuiFilledInput-root': {
//                         backgroundColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-underline:after': {
//                         borderBottomColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     },
//                     '&:hover .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     }
//                 }}  
//             />
//             );


//         case "number":
//             return (
//             <TextField
//                 type="number"
//                 label={field.label}
//                 name={field.name}
//                 onChange={(e) => {
//                 const value = e.target.value;
//                 if (
//                     value === "" ||
//                     (/^-?\d+$/.test(value) && parseInt(value) >= 0)
//                 ) {
//                     handleChange(field.name)(e);
//                 }
//                 }}
//                 value={values[field.name] || ""}
//                 fullWidth
//                 disabled={isDisabled}
//                 InputProps={{
//                 inputProps: {
//                     min: 0, 
//                     step: 1, 
//                 },
//                 }}
//                 id="filled-basic"  
//                 variant="filled" 
//                 sx={{
//                     borderRadius: '20px', 
//                     backgroundColor: '#EDEDED', 
//                     '& .MuiFilledInput-root': {
//                         backgroundColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-underline:after': {
//                         borderBottomColor: 'transparent',
//                     },
//                     '& .MuiFilledInput-root::before': {
//                         borderBottom: 'none', 
//                     },
//                     '&:hover .MuiFilledInput-root::before, &:hover .MuiFilledInput-underline::before': {
//                         borderBottom: 'none', 
//                     }
//                 }} 
                
//             />
//             );

//             case "date":
//                 return (
//                 <TextField
//                     type="date"
//                     label={field.label}
//                     name={field.name}
//                     value={values[field.name] || ""}
//                     onChange={handleChange(field.name)}
//                     InputLabelProps={{ shrink: true }}
//                     error={!!errors[field.name]}
//                     disabled={field.disabled}
//                     fullWidth
//                     id="filled-basic"  
//                     variant="filled" 
//                     sx={{
//                         borderRadius: '20px', 
//                         backgroundColor: '#EDEDED', 
//                         '& .MuiFilledInput-root': {
//                             backgroundColor: 'transparent',
//                             '&::before': {
//                                 borderBottom: 'none',  
//                             },
//                             '&:hover::before': {
//                                 borderBottom: 'none',  
//                             },
//                         },
//                     }} 
//                 />
//                 );
            

//             case "note":
//                 return (
//                 <TextField
//                     label={field.label}
//                     name={field.name}
//                     multiline
//                     rows={4}
//                     onChange={handleChange(field.name)}
//                     value={values[field.name] || ""}
//                     disabled={field.disabled}
//                     fullWidth
//                     id="filled-basic"  
//                     variant="filled" 
//                     sx={{
//                         borderRadius: '20px', 
//                         backgroundColor: '#EDEDED', 
//                         '& .MuiFilledInput-root': {
//                             backgroundColor: 'transparent',
//                             '&::before': {
//                                 borderBottom: 'none',  
//                             },
//                             '&:hover::before': {
//                                 borderBottom: 'none',  
//                             },
//                         },
//                     }} 
//                 />
//                 );
            

//             case "modificaFileCV":
//                 return(
//                     <Box sx={{ display: 'flex', flexDirection: 'column', width: '90%', overflow: 'hidden'}}>
//                             <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', mb: 4}}>
//                             <Typography variant="subtitle1" gutterBottom sx={{ display: 'flex', textAlign: 'center', justifyContent: 'center'}}>{field.label}</Typography>
//                             <Button
//                                     variant="contained"
//                                     color="primary"
//                                     sx={{ 
//                                     justifyContent:"flex-end", 
//                                     backgroundColor: 'black', 
//                                     color: 'white',
//                                     mr: 3,
//                                     ':hover': {
//                                         backgroundColor: 'black',
//                                     }
//                                 }}
//                                     startIcon={<CloudUploadIcon />}
//                                     component="label"
//                                 >
//                                     <input
//                                         type="file"
//                                         hidden
//                                         onChange={handleChangeCV(field.name)}
//                                     />
//                                 </Button>
//                             </Box> 
//                         <Box sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-around'}}>
//                         <Typography variant="body2">
//                         {values.cv?.descrizione || 'Nessun file selezionato'}
//                             </Typography>
//                             <Box sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-around', gap: 3}}>
//                             <Button
//                                     variant="contained"
//                                     color="primary"
//                                     sx={{ 
//                                     justifyContent:"flex-end",
//                                     backgroundColor: 'black',
//                                     color: 'white',
//                                     '&:hover': {
//                                         backgroundColor: 'black'
//                                     }  
//                                 }}
//                                 startIcon={<CloudDownloadIcon />}
//                                 disabled={!values[field.name]}
//                                 component="label"
//                                 onClick={() => handleDownloadCVCF(values.cv.id, values.cv.descrizione)}
//                                 >

//                                 </Button>
//                                 <Button
//                                     variant="contained"
//                                     color="primary"
//                                     sx={{ 
//                                         justifyContent:"flex-end", 
//                                         backgroundColor: '#00853C', 
//                                         color: 'white',
//                                         ':hover': {
//                                             backgroundColor: 'red',
//                                         }
//                                     }}
//                                     startIcon={<DeleteIcon />}
//                                     component="label"
//                                     disabled={!values[field.name]}
//                                     onClick={() => handleOpenDeleteDialogCVCF(values.cv.id, 'cv')}
//                                 >
//                                 </Button>
//                                 </Box>
//                         </Box>
//                     </Box>
//                 );
            



//                 case "modificaFileCF":
//                     return(
//                         <Box sx={{ display: 'flex', flexDirection: 'column', width: '90%', overflow: 'hidden'}}>
//                             <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', mb: 4}}>
//                             <Typography variant="subtitle1" gutterBottom sx={{ display: 'flex', textAlign: 'center', justifyContent: 'center'}}>{field.label}</Typography>
//                             <Button
//                                     variant="contained"
//                                     color="primary"
//                                     sx={{ 
//                                         justifyContent:"flex-end", 
//                                         backgroundColor: 'black', 
//                                         color: 'white',
//                                         mr: 2,
//                                         ':hover': {
//                                             backgroundColor: 'black',
//                                         }
//                                     }}
//                                     startIcon={<CloudUploadIcon />}
//                                     component="label"
//                                 >
//                                     <input
//                                         type="file"
//                                         hidden
//                                         onChange={handleChangeCF(field.name)}
//                                     />
//                                 </Button>
//                             </Box>
//                             <Box sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-around'}}>
//                             <Typography variant="body2" style={{ marginRight: '10px' }}>
//                             {values.cf?.descrizione || 'Nessun file selezionato'}
//                                 </Typography>
//                                 <Box sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-around', gap: 3}}>

//                                 <Button
//                                     variant="contained"
//                                     color="primary"
//                                     sx={{ 
//                                     justifyContent:"flex-end",
//                                     backgroundColor: 'black',
//                                     color: 'white',
//                                     '&:hover': {
//                                         backgroundColor: 'black'
//                                     } 
//                                 }}
//                                     startIcon={<CloudDownloadIcon />}
//                                     disabled={!values[field.name]}
//                                     component="label"
//                                     onClick={() => handleDownloadCVCF(values.cf.id, values.cf.descrizione)}
//                                     >

//                                 </Button>
                                
//                                 <Button
//                                     variant="contained"
//                                     color="primary"
//                                     sx={{ 
//                                         justifyContent:"flex-end", 
//                                         backgroundColor: '#00853C', 
//                                         color: 'white',
//                                         ':hover': {
//                                             backgroundColor: 'red',
//                                         }
//                                     }}
//                                     startIcon={<DeleteIcon />}
//                                     component="label"
//                                     disabled={!values[field.name]}
//                                     onClick={() => handleOpenDeleteDialogCVCF(values.cf.id, 'cf')}

//                                 >
//                                 </Button>
//                             </Box>
//                             </Box>

//                         </Box>
//                     );

//         default:
//             return null;
//         }
//     };

//     return (
//         <Box
//         sx={{
//             display: 'flex',
//             justifyContent: 'center',
//             alignItems: 'center',
//             margin: 'auto',
//             flexDirection: 'column',
//         }}
//         >
//             <Dialog
//     open={openDialog}
//     onClose={() => setOpenDialog(false)}
//     aria-labelledby="alert-dialog-title"
//     aria-describedby="alert-dialog-description"
// >
//     <DialogTitle id="alert-dialog-title">
//         {"Conferma Eliminazione"}
//     </DialogTitle>
//     <DialogContent>
//         <DialogContentText id="alert-dialog-description">
//             Sei sicuro di voler eliminare questo file?
//         </DialogContentText>
//     </DialogContent>
//     <DialogActions>
//         <Button onClick={() => setOpenDialog(false)}
//         sx={{
//             backgroundColor: 'black',
//             color: 'white',
//             "&:hover": {
//                 backgroundColor: 'black'
//             }
            
//             }}>
//             Annulla
//             </Button>
//         <Button onClick={() => {
//             if (selectedFileId.type === 'cv') {
//                 handleDeleteCVCF(selectedFileId.id, idCandidato, 'cv');
//             } else if (selectedFileId.type === 'cf') {
//                 handleDeleteCVCF(selectedFileId.id, idCandidato, 'cf');
//             }
//             setOpenDialog(false);
//         }} 
//         autoFocus 
//         sx={{
//             backgroundColor: '#00853C',
//             color: 'black',
//             "&:hover": {
//                 backgroundColor: '#00853C',
//             }
//         }}>
//             Elimina
//         </Button>
//     </DialogActions>
// </Dialog>
//         <Typography variant="h5" style={{ marginBottom: "20px" }}>
//             {title}
//         </Typography>
//         <form onSubmit={handleSubmit}>
//             <Grid container spacing={5}>
//             {fields.map((field) => (
//                 <Grid item xs={field.type === "note" ? 12 : 6} key={field.name}>
//                 {renderField(field)}
//                 </Grid>
//             ))}
//             </Grid>
//             <Typography variant="h6" sx={{ mt: 2, color: '#666565', fontSize: '1em'}}>* Campo Obbligatorio</Typography>
//         </form>
//         </Box>
//     );
//     };

//     export default FieldBoxFile;
