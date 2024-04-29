import React from 'react';
import { Navigate } from 'react-router-dom';
import { isAuth } from '.';

export default function PrivateRoute({ children}) {
  if (isAuth()) {
    return children;
  }

  return <Navigate to="login" />
}