import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import authService from '../services/auth.service'; 

const PrivateRoute = ({ children, roles }) => {
    const location = useLocation();
    const currentUser = authService.getCurrentUser();
  
    const hasRole = currentUser && roles.some(role => currentUser.roles.includes(role));
    if (!currentUser || !hasRole) {
      // Se l'utente non è autenticato o non ha i ruoli richiesti, reindirizzalo alla pagina di login
      return <Navigate to="/login" state={{ from: location }} replace />;
    }
  
    // Se l'utente è autenticato e ha i ruoli richiesti, renderizza i componenti figli
    return children;
  };
  

export default PrivateRoute;
