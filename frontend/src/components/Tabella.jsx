import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import CircularProgress from '@mui/material/CircularProgress';
import { DataGrid } from '@mui/x-data-grid/DataGrid';


const Tabella = ({ data, columns, title, getRowId, pagina, quantita, onPageChange, righeTot }) => {

    // const [ rowPerPage, setRowsPerPage ] = useState( parseInt(localStorage.getItem("rowsPerPage") || "25", 10));
    // const [ page, setPage ] = useState( parseInt(localStorage.getItem("currentPage" || "0", 10)));
    const [ loading, setLoading] = useState(false);
    const [ showNoDataMessage, setShowNoDataMessage ] = useState(false);



    useEffect(() => {
        if(data.length === 0) {
            setLoading(true);
            const timer = setTimeout(() => {
                setLoading(false);
                setShowNoDataMessage(true);
            }, 500);
            return () => clearTimeout(timer);
        } else {
            setLoading(false);
            setShowNoDataMessage(false);
        }
    }, [data]);

return (
    <Box sx={{
        backgroundColor: 'white',
        boxShadow: "10px 10px 10px rgba(0, 0, 0, 0.6)",
        borderRadius: '40px',
        height: 'auto',
        display: 'flex',
        mt: 2,
        border: '1.5px solid #00853C',
        flexDirection: 'column',
        fontSize: '1.4em',
    }}
    >
        { title && (
                    <Box
                    sx={{
                        display: 'flex',
                        alignItems: 'flex-start',
                        color: 'black',
                        mt: 2,
                        mb: 1,
                        ml: 3,
                        fontWeight: 'bold'
                    }}
                    >
                        {title}
                    </Box>
                )}

            <DataGrid
            rows={data}
            columns={columns}
            paginationMode='server'
            pageSizeOptions={[10]}
            rowCount={righeTot}
            autoHeight
            paginationModel={ {page: pagina, pageSize: quantita}}
            onPaginationModelChange={({ page }) => {
                onPageChange(page);
            }}

            loading={data.length === 0 && loading}
            noRowsOverlay={
                showNoDataMessage ? ( 
                    <h1>Nessun dato</h1>
                ) : (
                <CircularProgress style={{ color: '#00853C' }} />
                )
            }  
            getRowClassName={(params) => 
                params.indexRelativeToCurrentPage % 2 === 0 ? 'even-row' : 'odd-row'
                            }
            sx={{
                borderStyle: 'none',
                '& .MuiDataGrid-columnHeader': {
                    borderBottom: '2px solid #c4c4c4',
                },
                '& .even-row': {
                    backgroundColor: '#ffffff',
                    },
                '& .odd-row': {
                    backgroundColor: '#ECECEC',
                    },
                '&. MuiDataGrid-row:nth-of-type(even)': {
                    backgroundColor: '#ececec',
                },
                '& .MuiDataGrid-row': {
                    border: 'hidden',
                    width: '100%'
                },
                '& .MuiDataGrid-footerContainer': {
                    borderTop: '2px solid #dbd9d9',
                    width: '100%'
                },
                '& .MuiDataGrid-cell': {
                    fontSize: '1em',
                    fontFamily: 'Roboto'
                },
                '& .MuiDataGrid-columnHeaderTitle': {
                    color: '#808080',
                    fontWeight: 'bolder',
                    fontSize: '1em'
                },
                

            }}          
        />
    </Box>

);

    // return (
    //     <Box
    //     sx={{
    //         width: '97%',
    //         height: 'auto',
    //         minHeight: '40vh',
    //         backgroundColor: 'white',
    //         borderRadius: '40px',
    //         display: 'flex',
    //         flexDirection: 'column',
    //         ml: 4,
    //         mr: 1,
    //         borderWidth: '0',
    //         boxShadow: "10px 10px 10px rgba(0, 0, 0, 0.6)",
    //         fontSize: '1.4rem',
    //         fontWeight: 'bolder',
    //         border: '1.5px solid #00853C'
    //     }}
    //     >
    //         <Box
    //         sx={{
    //             minHeight: '1vh',
    //             display: 'flex',
    //             flexDirection: 'row',
    //             justifyContent: 'flex-start',
    //             paddingLeft: '1.6%',
    //             paddingTop: '1%',
    //             backgroundColor: 'white',
    //             color: 'black',
    //             borderRadius: '40px 40px 0 0',
    //             borderBottom: '2px solid #c4c4c4',
    //         }}
    //         >
    //             { title && (
    //                 <Box
    //                 sx={{
    //                     display: 'flex',
    //                     alignItems: 'center',
    //                     color: 'black',
    //                     fontSize: '0.8em',
    //                     mb: 1
    //                 }}
    //                 >
    //                     {title}
    //                 </Box>
    //             )}
    //         </Box>


    //         <DataGrid
    //        rows={data}
    //        columns={columns}
    //        paginationMode='server'
    //        rowCount={righeTot}
    //        autoHeight
    //        paginationModel={ {page: pagina, pageSize: quantita}}
    //        onPaginationModelChange={({ page }) => {
    //            onPageChange(page);
    //        }}

    //         loading={data.length === 0 && loading}
    //         noRowsOverlay={
    //             showNoDataMessage ? ( 
    //                 <h1>Nessun dato</h1>
    //             ) : (
    //             <CircularProgress style={{ color: '#00853C' }} />
    //             )
    //         }  
    //         getRowClassName={(params) => 
    //             params.indexRelativeToCurrentPage % 2 === 0 ? 'even-row' : 'odd-row'
    //                         }
    //         sx={{
    //             borderStyle: 'none',
    //             '& .MuiDataGrid-columnHeader': {
    //                 borderBottom: '2px solid #c4c4c4',
    //             },
    //             '& .even-row': {
    //                 backgroundColor: '#ffffff',
    //                 },
    //             '& .odd-row': {
    //                 backgroundColor: '#ECECEC',
    //                 },
    //             '&. MuiDataGrid-row:nth-of-type(even)': {
    //                 backgroundColor: '#ececec',
    //             },
    //             '& .MuiDataGrid-row': {
    //                 border: 'hidden',
    //                 width: '100%'
    //             },
    //             '& .MuiDataGrid-footerContainer': {
    //                 borderTop: '2px solid #dbd9d9',
    //                 width: '100%'
    //             },
    //             '& .MuiDataGrid-cell': {
    //                 fontSize: '1em',
    //                 fontFamily: 'Roboto'
    //             },
    //             '& .MuiDataGrid-columnHeaderTitle': {
    //                 color: '#808080',
    //                 fontWeight: 'bolder',
    //                 fontSize: '1em'
    //             },
                

    //         }}          

    //         />

    //     </Box>
    // );
};

export default Tabella;