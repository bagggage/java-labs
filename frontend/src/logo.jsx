import { Typography, Link } from "@mui/material";

export default function Logo() {
  return (
    <Link href="/profile" underline="none" color="ButtonText">
      <Typography variant="h4">
        Git all in one
      </Typography>
    </Link>
  );
}