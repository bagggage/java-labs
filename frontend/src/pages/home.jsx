import { Modal, Stack, Button, Card, Typography, Divider, TextField, MenuItem } from "@mui/material";
import apiRequest from "../api";
import { useEffect, useState } from "react";
import RepositoryList from "../components/repository-list";
import PageTemplate from "../components/page-template";

export const HEADER = {
  H_MOBILE: 64,
  H_DESKTOP: 80,
  H_DESKTOP_OFFSET: 80 - 16,
};

export const NAV = {
  WIDTH: 280,
};

function LinkList(links) {
  return links.map((link) => (
    <Card sx={{
      p: 0,
      textAlign: "center",
      borderRadius: 1
    }}>
      <Button
        href={link.url}
        color="secondary"
        sx={{
          width: "100%"
        }}
      >
        {link.service.toLowerCase()}
      </Button>
    </Card>
  ));
}

export default function Home() {
  const [user, setUser] = useState({});
  const [reposList, setRepos] = useState([]);
  const [linkList, setLinks] = useState([]);
  const [isLinkModalOpen, setIsLinkModalOpen] = useState(false);
  const [linkUrl, setLinkUrl] = useState("");
  const [linkButtonEnabled, setLinkButtonEnabled] = useState(false);

  const onLinkRepos = (event) => {
    if (linkUrl.length === 0) return;
    const username = /[^/]*$/.exec(linkUrl)[0];

    if (username === null || username === undefined) {
      setLinkButtonEnabled(false);
      return;
    }

    console.log(username);

    apiRequest("/users/" + user.username +"/link?username=" + username + "&service=GITHUB", "POST")
      .then((response) => {
        if (!response.ok) return;

        response.json().then((data) => {
          setLinks(LinkList([data]));
        });

        apiRequest("/repos/" + user.username).then((reposResponse) => {
          if (!reposResponse.ok) return;

          reposResponse.json().then((data) => {
            setRepos(<RepositoryList repositories={data} ownerUsername={user.username}/>);
          });
        });

        setLinkButtonEnabled(false);
        setIsLinkModalOpen(false);
    });

    setLinkButtonEnabled(false);
  };

  useEffect(() => {
    if (user.id !== undefined) return;

    apiRequest("/users/current").then((response) => {
      if (!response.ok) return {};

      return response.json();
    }).then((data) => {
      setUser(data);

      console.log(data);

      setLinks(LinkList(data.links));
      setRepos(<RepositoryList repositories={data.repositories} ownerUsername={data.username}/>);
    });
  });

  return (
    <PageTemplate>
      <Stack direction="row" spacing={4} p={2} pt={0}>
        <Card
          sx={{
            p: 4,
            width: 1,
            maxWidth: "30%",
          }}
        >
          <Typography variant="h4">Profile:</Typography>
          <Divider/>
          <Stack spacing={3} py={3}>
            <Typography variant="subtitle1" sx={{color: 'text.disabled'}}>{user.username}</Typography>
            <Typography variant="subtitle1" sx={{color: 'text.disabled'}}>{user.name}</Typography>
            <Typography variant="subtitle1" sx={{color: 'text.disabled'}}>{user.email}</Typography>
            <Divider/>
          </Stack>
          <Typography variant="h4">Links:</Typography>
          <Divider/>
          <Stack spacing={2} py={2}>
            {linkList}
            <Button color="error" variant="outlined" onClick={() => setIsLinkModalOpen(true)}>Link +</Button>
            <Modal open={isLinkModalOpen} onClose={() => setIsLinkModalOpen(false)}>
              <Card sx={{
                p: 2,
                width: "40%",
                maxWidth: 400,
                minWidth: 150,
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%)',
              }}>
                <Typography align="center" id="modal-modal-title" variant="h4" component="h2">
                  Add link to your repositories
                </Typography>
                <Stack spacing={2}>
                  <Divider/>
                  <TextField
                    label="Service"
                    select
                    size="small"
                  >
                    <MenuItem key="GITHUB" value="GITHUB">Github</MenuItem>
                  </TextField>
                  <TextField type="url" label="URL" onChange={(event) => {
                    setLinkUrl(event.target.value);
                    setLinkButtonEnabled(event.target.value.length > 0);
                  }}/>
                  <Button disabled={!linkButtonEnabled} variant="contained" onClick={onLinkRepos}>Link!</Button>
                </Stack>
              </Card>
            </Modal>
          </Stack>
        </Card>
        <Card
          sx={{
            p: 4,
            width: 1,
            maxWidth: "70%",
          }}
        >
          <Typography variant="h4">Repositories:</Typography>
          <Divider sx={{mb: 3}}/>
          <Stack spacing={2}>
            {reposList}
          </Stack>
        </Card>
      </Stack>
    </PageTemplate>
  );
}