import './App.css';
import { BrowserRouter, Routes, Route }                               from 'react-router-dom';
import React, { useEffect }                                           from 'react';
import PrivateRoute                                                   from './components/PrivateRoute.jsx';


import Homepage                                                        from './pages/Homepage';
import Aziende                                                         from './pages/Aziende.jsx';
import Need                                                            from './pages/Need.jsx';
import Recruiting                                                      from './pages/Recruiting.jsx';
import AggiungiAziende                                                 from './pages/AggiungiPagine/AggiungiAziende.jsx';
import AggiungiNeed                                                    from './pages/AggiungiPagine/AggiungiNeed';
import ModificaKeypeople                                               from './pages/ModificaPagine/ModificaKeypeople.jsx';
import ModificaNeed                                                    from './pages/ModificaPagine/ModificaNeed.jsx';
import DettaglioNeed                                                   from './pages/DettaglioPagine/DettaglioNeed.jsx';
import Layout                                                          from './components/Layout.jsx';
// import Dashboard                                                       from './pages/Dashboard.jsx';
import AggiungiKeypeople                                               from './pages/AggiungiPagine/AggiungiKeypeople.jsx';
import AggiungiRecruiting                                              from './pages/AggiungiPagine/AggiungiRecruiting.jsx';
import ModificaRecruiting                                              from './pages/ModificaPagine/ModificaRecruiting.jsx';
import LoginPage                                                       from './pages/LoginPage.jsx';
import IntervisteList                                                  from './pages/IntervisteList.jsx';
import AggiungiIntervista                                              from './pages/AggiungiPagine/AggiungiIntervista.jsx';
import ModificaIntervista                                              from './pages/ModificaPagine/ModificaInterviste.jsx';
import AggiungiNeedID                                                  from './pages/AggiungiPagine/AggiungiNeedID.jsx';
import ModificaAziende                                                 from './pages/ModificaPagine/ModificaAziende.jsx';
import Keypeople                                                       from './pages/KeyPeople.jsx';
import Hiring                                                          from './pages/Hiring.jsx';
import NeedLife                                                        from './components/NeedLife.jsx';
import NeedMatch2                                                      from './pages/NeedMatch2.jsx';
import AziendeListaNeedCard                                            from './pages/AziendeListaNeedCard.jsx';
import DettaglioIntervista                                             from './pages/VisualizzaPagine/DettaglioIntervista.jsx';
import NuovoNeed from './pages/AggiungiPagine/NuovoNeed.jsx';
import Dashboard from './pages/Dashboard.jsx';
import NeedForm from './pages/NeedForm.jsx';




const App = () => {


useEffect(() => {
  const handleBeforeUnload = () => {
    localStorage.clear();
  };

  window.addEventListener('beforeunload', handleBeforeUnload);

  return () => {
    window.removeEventListener('beforeunload', handleBeforeUnload);
  };
}, []);




  return (
    <BrowserRouter>
      <Routes>
      <Route path='/' element={<LoginPage /> } />
      <Route path="/login" element={<LoginPage />} />
      <Route element={<Layout />}>

                {/* <Route path="/homepage"  element={<PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <Homepage />
                    </PrivateRoute>
                  } /> */}
                <Route path="/dashboard" element={
                    <PrivateRoute roles={['ROLE_ADMIN',  'ROLE_RECRUITER', 'ROLE_BM']}>
                      <Dashboard />
                    </PrivateRoute>
                  } />
                <Route path="/aziende" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <Aziende />
                    </PrivateRoute>
                  } />
                <Route path="/aziende/aggiungi"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <AggiungiAziende />
                    </PrivateRoute>
                  } />
                <Route path="/aziende/modifica/:id"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <ModificaAziende />
                    </PrivateRoute>
                  } />
                <Route path="/need/:id"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <AziendeListaNeedCard />
                    </PrivateRoute>
                  } />
                <Route path="/need/aggiungi/:id"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <AggiungiNeedID />
                    </PrivateRoute>
                  } />
                <Route path="/keypeople"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <Keypeople />
                    </PrivateRoute>
                  } />
                <Route path="/keypeople/aggiungi"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <AggiungiKeypeople />
                    </PrivateRoute>
                  } />
                <Route path="/keypeople/modifica/:id" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <ModificaKeypeople />
                    </PrivateRoute>
                  } />
                <Route path="/need" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <Need />
                    </PrivateRoute>
                  } />
                <Route path="/need/aggiungi"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <AggiungiNeed />
                    </PrivateRoute>
                  } />
                <Route path="/need/modifica/:id"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <ModificaNeed />
                    </PrivateRoute>
                  } />
                <Route path="/need/dettaglio/:id" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <DettaglioNeed />
                    </PrivateRoute>
                  } />
                <Route path="/need/match/:id"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <NeedMatch2 />
                    </PrivateRoute>
                  } />
                <Route path="/recruiting"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <Recruiting />
                    </PrivateRoute>
                  } />
                <Route path="/recruiting/aggiungi" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <AggiungiRecruiting />
                    </PrivateRoute>
                  } />
                <Route path="/recruiting/modifica/:id"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <ModificaRecruiting />
                    </PrivateRoute>
                  } />
                <Route path="/recruiting/intervista/:id"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <IntervisteList />
                    </PrivateRoute>
                  } />
                <Route path="/intervista/aggiungi"element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <AggiungiIntervista />
                    </PrivateRoute>
                  } />
                <Route path="/intervista/modifica/:id" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <ModificaIntervista />
                    </PrivateRoute>
                  } />
                    <Route path="/intervista/visualizza/:id" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <DettaglioIntervista />
                    </PrivateRoute>
                  } />
                <Route path="/hiring" element={
                    <PrivateRoute roles={['ROLE_ADMIN', 'ROLE_BM', 'ROLE_RECRUITER']}>
                      <Hiring />
                    </PrivateRoute>
                  } />
        </Route>
      </Routes>
    </BrowserRouter>
  );

          };
  
export default App;
