import { Box, Button, Card, Divider, Stack, Typography, useTheme } from "@mui/material";

export default function RepositoryList(props) {
  const theme = useTheme();

  const repositories = props.repositories;
  const ownerUsername = props.ownerUsername;

  return repositories.map((repo) => (
    <>
      <Stack direction="row" justifyContent="space-between">
        <Stack width="50%">
          <Typography variant="subtitle1">{repo.name}</Typography>
          <Typography variant="subtitle2" color="text.disabled">
            {
              (repo.owner === null || repo.owner === undefined) ?
              ownerUsername : repo.owner.username
            }
          </Typography>
        </Stack>
        { repo.language === null ? [] : (
          <Box alignContent="center">
            <Card sx={{
              px: 2, py: 0.5,
              backgroundColor: theme.palette.success.light,
              borderRadius: 1
            }}
            >
              <Typography variant="subtitle1" sx={{color: "white"}}>{repo.language}</Typography>
            </Card>
          </Box>
          )
        }
        <Button href={
          repo.gitUrl
        }>
          Git URL
        </Button>
      </Stack>
      <Divider/>
    </>
  ));
}