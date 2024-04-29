import { Box, Button, InputBase, Stack, useTheme } from "@mui/material";
import Logo from "../logo";
import { customShadows } from "../theme/custom-shadows";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import apiRequest from "../api";
import { useState } from "react";

export default function PageTemplate({ children, searchInitQuery }) {
  const theme = useTheme();
  const navigate = useNavigate();

  const [searchQuery, setSearchQuery] = useState(searchInitQuery);

  return (
    <Box sx={{
      px: "15%"
    }}
    >
      <Stack p={3}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={1} px={2} pb={3}>
          <Logo/>
          <InputBase
            placeholder="Search..."
            value={searchQuery}
            onChange={(event) => setSearchQuery(event.target.value)}
            onKeyDown={(event) => {
              if (event.key !== 'Enter' || event.target.value.length === 0) return;
              navigate("/search?q=" + event.target.value + "&p=0");
            }}
            sx={{
              width: "50%",
              background: theme.palette.background.paper,
              p: 1,
              px: 2,
              borderRadius: 2,
              border: "solid",
              borderColor: theme.palette.grey[100],
              borderWidth: 1,
              boxShadow: customShadows().card
            }}
          />
          <Button
            variant="outlined"
            sx={{
              alignSelf: "center",
              height: 1
            }}
            onClick={() => {
              Cookies.remove("authToken", { path: '' });
              apiRequest("/auth/logout").then(() => {
                navigate("/login");
              });
          }}>
            Log out
          </Button>
        </Stack>
        {children}
      </Stack>
    </Box>
  );
}