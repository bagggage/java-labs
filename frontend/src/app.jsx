import ThemeProvider from './theme';
import Login from './pages/login';
import Signup from './pages/signup';
import { Box } from '@mui/material';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Search from './pages/search';
import Home from './pages/home';
import PrivateRoute from './auth/private-route';

function App() {
  return (
    <ThemeProvider>
      <Box
        sx={{
          height: 1,
        }}
      >
        <BrowserRouter>
          <Routes>
            <Route path='login'   element={<Login/>}/>
            <Route path='signup'  element={<Signup/>}/>
            <Route path=''        element={<PrivateRoute><Home/></PrivateRoute>}/>
            <Route path='search'  element={<PrivateRoute><Search/></PrivateRoute>}/>
            <Route path='profile' element={<PrivateRoute><Home/></PrivateRoute>}/>
            <Route path="*" element={<Navigate to="/" replace />}/>
          </Routes>
        </BrowserRouter>
      </Box>
    </ThemeProvider>
  );
}

export default App;
