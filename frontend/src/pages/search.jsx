import { useSearchParams} from "react-router-dom";
import { Button, Card, Divider, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import apiRequest from "../api";
import PageTemplate from "../components/page-template";
import RepositoryList from "../components/repository-list";

export default function Search() {
  let [searchParams, setSearchParams] = useSearchParams();

  const query = searchParams.get("q") || "";

  const [repos, setRepos] = useState([]);
  const [page, setPage] = useState(null);
  const [pageSwitch, setPageSwitch] = useState([]);

  useEffect(() => {
    apiRequest("/repos/search?name=" + searchParams.get("q") + "&p=" + (searchParams.get("p") || 0))
    .then((response) => {
      if (!response.ok) {
        setRepos(
          <Typography variant="h3" align="center" color="text.disabled">
            {"Nothing found :("}
          </Typography>
        );
        setPage(null);
        return;
      }
  
      response.json().then((data) => {
        setRepos(
          <RepositoryList repositories={data.content}/>
        );
        setPage(data);
      });
    });
  }, [searchParams]);

  useEffect(() => {
    if (page === null || page.totalPages < 2) {
      setPageSwitch([]);
      return;
    }

    setPageSwitch(
      <Card sx={{
        width: "fit-content",
        p: 1,
        mt: 2,
        alignSelf: "center"
      }}
      >
        <Stack direction="row" spacing={1} alignItems="center">
          <Button disabled={page.number === 0}
            onClick={() => setSearchParams({q: query, p: page.number - 1})}
          >
            {"<"}
          </Button>
          <Typography
            align="center"
            variant="subtitle1"
          >
            {page.number + 1 + " / " + page.totalPages}
          </Typography>
          <Button disabled={page.number === page.totalPages - 1}
            onClick={() => setSearchParams({q: query, p: page.number + 1})}
          >
            {">"}
          </Button>
        </Stack>
      </Card>
    );
  }, [query, setSearchParams, page]);

  return (
    <PageTemplate searchInitQuery={query}>
      <Card
        sx={{
          p: 4,
          width: 1
        }}
      >
        <Typography variant="h4">Results:</Typography>
        <Divider sx={{mb: 3}}/>
        <Stack spacing={2}>
          {repos}
        </Stack>
      </Card>
      {pageSwitch}
    </PageTemplate>
  );
}