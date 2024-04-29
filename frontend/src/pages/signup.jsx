import { Button, Card, Stack, Typography, Divider, TextField } from '@mui/material';
import { useRef, useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { isAuth, isPassCorrect } from '../auth';
import apiRequest from '../api';
import Cookies from "js-cookie";

export default function Signup() {
  const navigate = useNavigate();

  const usernameRef = useRef("");
  const nameRef = useRef("");
  const emailRef = useRef("");
  const passRef = useRef("");
  const passRepeatRef = useRef("");

  const errors = {
    "username":   useState(false),
    "email":      useState(false),
    "pass":       useState(false),
    "repeatPass": useState(false)
  };

  const onSingup = (event) => {
    errors["username"][1](() => {
      const val = usernameRef.current.value;
      return val.length === 0 || /(^\d)|[\W\s]+/.test(val);
    });
    errors["email"][1](() => {
      const val = emailRef.current.value;
      return val.length === 0 || (/\w+@\w+[.]\w+/.test(val) === false);
    });

    const isPassWrong = !isPassCorrect(passRef.current.value);

    errors["pass"][1](isPassWrong);
    errors["repeatPass"][1](isPassWrong || passRef.current.value !== passRepeatRef.current.value);

    for (const error in errors) {
      if (error[0] === true) return;
    }

    apiRequest("/auth/signup", "POST", {
      username: usernameRef.current.value,
      name: nameRef.current.value,
      email: emailRef.current.value,
      password: passRef.current.value
    }, false).then((responce) => {
      if (!responce.ok) return;

      responce.json().then((data) => {
        Cookies.set("authToken", "Bearer " + data.token, { secure: false });
        navigate("/");
      })
    });
  };

  if (isAuth() === true) return (<Navigate to="/" replace/>);

  return (
    <Stack alignItems="center" justifyContent="center" sx={{height: 1}}>
      <Card
        sx={{
          p: 4,
          width: 1,
          maxWidth: 350
        }}
      >
        <Stack spacing={2}>
          <Typography variant="h3" align="center">Sing Up</Typography>
          <TextField label="Username" inputRef={usernameRef} error={errors["username"][0]}/>
          <TextField label="Name" inputRef={nameRef}/>
          <TextField label="Email" inputRef={emailRef} error={errors["email"][0]}/>
          <TextField label="Password" inputRef={passRef} type="password" error={errors["pass"][0]}/>
          <TextField label="Repeat password" inputRef={passRepeatRef} type="password" error={errors["repeatPass"][0]} sx={{pb: 2}}/>
          <Button variant="contained" onClick={onSingup}>Sing up</Button>
          <Divider/>
          <Button variant="outlined" onClick={() => { navigate("/login") }}>Login</Button>
        </Stack>
      </Card>
    </Stack>
  );
}