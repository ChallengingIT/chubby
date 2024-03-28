// import React from 'react';
// import { Box, Grid, Card, CardContent  } from '@mui/material';
// import { Gauge, gaugeClasses } from '@mui/x-charts/Gauge';


// function ProvaDashboard() {

//     return (
//         <Box sx={{ display: 'flex', backgroundColor: '#EEEDEE', height: '100vh', width: '100vw', overflow: 'hidden' }}>
//                 <Box sx={{
//                 p: 2,
//                 ml: 26,
//                 mt: 1.5,
//                 mb: 0.5,
//                 mr: 0.8,
//                 backgroundColor: '#FEFCFD',
//                 borderRadius: '20px',
//                 height: '97vh',
//                 width: '100%',
//                 flexDirection: 'column',
//                 }}>
//     <Grid container spacing={2}>
//         <Grid item xs={12}>
//             <Card
//             fullWidth
//                 sx={{
//                 borderRadius: '20px', 
//                 maxWidth: '80%', 
//                 justifyContent: 'center', 
//                 margin: 'auto', 
//                 cursor: 'pointer', 
//                 height: 'auto', 
//                 border: '2px solid #00853C', 
//                 transition: 'transform 0.3s ease, border-width 0.3s ease', 
//                 '&:hover': {
//                 transform: 'scale(1.05)', 
//                 border: '4px solid #00853C' 
//             }}
//         }
//             >
//             <CardContent>
//                 {/* Contenuto della prima card */}
//                 Card 1 - Full Width
//             </CardContent>
//             </Card>
//         </Grid>
//         <Grid item xs={6}>
//             <Card
//             sx={{
//                 borderRadius: '20px', 
//                 maxWidth: '80%', 
//                 justifyContent: 'center', 
//                 margin: 'auto', 
//                 cursor: 'pointer', 
//                 height: 'auto', 
//                 border: '2px solid #00853C', 
//                 transition: 'transform 0.3s ease, border-width 0.3s ease', 
//                 '&:hover': {
//                 transform: 'scale(1.05)', 
//                 border: '4px solid #00853C' 
//             }}
//         }>
//             <CardContent>
//                 <Grid container spacing={2} flexDirection="row">
//                 <Grid item xs={4}>
//                     {/* Primo Gauge */}
//                     <Gauge
//                         value={75}
//                         startAngle={-110}
//                         endAngle={110}
//                         sx={{
//                             [`& .${gaugeClasses.valueText}`]: {
//                             fontSize: 40,
//                             transform: 'translate(0px, 0px)',
//                             },
//                         }}
//                         text={
//                             ({ value, valueMax }) => `${value} / ${valueMax}`
//                         }
//                         /> 
//                     </Grid>

//                     <Grid item xs={4}>
//                     {/* Secondo Gauge */}
//                     <Gauge
//                         value={75}
//                         startAngle={-110}
//                         endAngle={110}
//                         sx={{
//                             [`& .${gaugeClasses.valueText}`]: {
//                             fontSize: 40,
//                             transform: 'translate(0px, 0px)',
//                             },
//                         }}
//                         text={
//                             ({ value, valueMax }) => `${value} / ${valueMax}`
//                         }
//                         /> 
//                     </Grid>

//                     <Grid item xs={4}>
//                     {/* Terzo Gauge */}
//                     <Gauge
//                         value={75}
//                         startAngle={-110}
//                         endAngle={110}
//                         sx={{
//                             [`& .${gaugeClasses.valueText}`]: {
//                             fontSize: 40,
//                             transform: 'translate(0px, 0px)',
//                             },
//                         }}
//                         text={
//                             ({ value, valueMax }) => `${value} / ${valueMax}`
//                         }
//                         /> 
//             </Grid>
//             </Grid>

//                 {/* Contenuto della seconda card */}
//                 Card 2 - 40% Width
//             </CardContent>
//             </Card>
//         </Grid>
//         <Grid item xs={6}>
//             <Card
//             sx={{
//                 borderRadius: '20px', 
//                 maxWidth: '80%', 
//                 justifyContent: 'center', 
//                 margin: 'auto', 
//                 cursor: 'pointer', 
//                 height: 'auto', 
//                 border: '2px solid #00853C', 
//                 transition: 'transform 0.3s ease, border-width 0.3s ease', 
//                 '&:hover': {
//                 transform: 'scale(1.05)', 
//                 border: '4px solid #00853C' 
//             }}
//         }>
//             <CardContent>
//                 {/* Contenuto della terza card */}
//                 Card 3 - 60% Width
//             </CardContent>
//             </Card>
//         </Grid>
//         </Grid>
//                 </Box>
//             </Box>
//     );
// };

// export default ProvaDashboard;