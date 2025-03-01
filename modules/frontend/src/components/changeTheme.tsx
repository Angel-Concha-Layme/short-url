import * as React from "react";
import { IconButton, useColorScheme } from "@mui/material";
import DarkModeIcon from "@mui/icons-material/DarkMode";
import LightModeIcon from "@mui/icons-material/LightMode";

export default function ChangeThemeSwitch() {
  const { mode, setMode } = useColorScheme();

  return (
    <IconButton
      color="inherit"
      onClick={() => setMode(mode === "dark" ? "light" : "dark")}
    >
      {mode === "dark" ? <DarkModeIcon /> : <LightModeIcon />}
    </IconButton>
  );
}
