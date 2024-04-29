import { Button, Box, Card, Stack, Typography, Divider, Link, TextField } from '@mui/material';
import { useRef, useState } from 'react';
import { isPassCorrect, isLoginCorrect, isAuth } from '../auth';
import { Navigate, useNavigate } from 'react-router-dom';
import apiRequest from '../api';
import Cookies from "js-cookie";

export default function Login() {
  const navigate = useNavigate();

  const loginValue = useRef("");
  const passValue = useRef("");

  const [isPassWrong, setPassWrong] = useState(false);
  const [isLoginWrong, setLoginWrong] = useState(false);

  const onLogin = (event) => {
    const isPassError = !isPassCorrect(passValue.current.value);
    const isLoginError = !isLoginCorrect(loginValue.current.value);

    setPassWrong(isPassError);
    setLoginWrong(isLoginError);

    if (isPassError || isLoginError) return;

    try {
      apiRequest("/auth/login", "POST", {
        login: loginValue.current.value,
        password: passValue.current.value
      }, false).then((response) => {
        if (!response.ok) {
          setPassWrong(true);
          setLoginWrong(true);
          return;
        }

        response.json().then((data) => {
          if (data.token === null || data.token === undefined) return;

          Cookies.set("authToken", "Bearer " + data.token, { secure: false });
          navigate("/");
        });
      });
    } catch(error) {
      setPassWrong(true);
      setLoginWrong(true);
    }
  };

  if (isAuth() === true) return (<Navigate to="/" replace/>);

  return (
    <Stack alignItems="center" justifyContent="center" sx={{height: 1}}>
      <Card sx={{p: 2}}>
        <Typography variant="h2">
          Git All In One
        </Typography>
      </Card>
      <Box sx={{height: 20}}/>
      <Card 
        sx={{
          p: 4,
          width: 1,
          maxWidth: 350,
        }}
      >
        <Stack spacing={5}>
          <Stack spacing={2}>
            <TextField
              label="Login"
              inputRef={loginValue}
              error={isLoginWrong}
            />
            <TextField
              label="Password"
              type="password"
              autoComplete="current-password"
              inputRef={passValue}
              error={isPassWrong}
            />
            <Link align="right" variant="subtitle2" underline="hover">
              Forgot password?
            </Link>
          </Stack>
          <Stack spacing={2}>
            <Button variant="contained" onClick={onLogin}>Log in</Button>
            <Divider/>
            <Button variant="outlined" onClick={() => { navigate("/signup") }}>Sign up</Button>
          </Stack>
        </Stack>
      </Card>
    </Stack>
  );
}