import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import Avatar from "@mui/material/Avatar";
import Tooltip from "@mui/material/Tooltip";
import MenuItem from "@mui/material/MenuItem";
import LinkIcon from "@mui/icons-material/Link";
import GitHubIcon from "@mui/icons-material/GitHub";
import ChangeThemeButton from "./changeTheme";
import { Container, Divider } from "@mui/material";
import HomeIcon from "@mui/icons-material/Home";
import DashboardIcon from "@mui/icons-material/Dashboard";
import SettingsIcon from "@mui/icons-material/Settings";
import BugReportIcon from "@mui/icons-material/BugReport";
import LogoutIcon from "@mui/icons-material/Logout";
import Link from "next/link";

function ResponsiveAppBar() {
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(
    null
  );

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  return (
    <AppBar position="fixed">
      <Container maxWidth="xl">
        <Toolbar>
          <Typography
            variant="h6"
            component="a"
            href="/"
            sx={{
              display: "flex",
              alignItems: "center",
              fontFamily: "monospace",
              fontWeight: 700,
              color: "inherit",
              textDecoration: "none",
              gap: 2,
            }}
          >
            <LinkIcon />
            Short URL
          </Typography>

          <Box sx={{ flexGrow: 1, display: "flex", justifyContent: "end" }}>
            <IconButton
              href="https://github.com/Angel-Concha-Layme/short-url"
              target="_blank"
              color="inherit"
            >
              <GitHubIcon />
            </IconButton>
            <ChangeThemeButton />
          </Box>

          <Box>
            <Tooltip title="Open settings">
              <IconButton onClick={handleOpenUserMenu}>
                <Avatar
                  sx={{
                    color: "white",
                    bgcolor: "transparent",
                    width: 30,
                    height: 30,
                  }}
                />
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ width: 320, maxWidth: "100%" }}
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: "bottom",
                horizontal: "right",
              }}
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              <MenuItem
                sx={{ display: "flex", alignItems: "center", gap: 1 }}
                onClick={handleCloseUserMenu}
                component={Link}
                href="/"
              >
                <HomeIcon />
                Home
              </MenuItem>

              <MenuItem
                sx={{ display: "flex", alignItems: "center", gap: 1 }}
                onClick={handleCloseUserMenu}
                component={Link}
                href="/dashboard"
              >
                <DashboardIcon />
                Dashboard
              </MenuItem>

              <MenuItem
                sx={{ display: "flex", alignItems: "center", gap: 1 }}
                onClick={handleCloseUserMenu}
                component={Link}
                href="/settings"
              >
                <SettingsIcon />
                Settings
              </MenuItem>
              <Divider />
              <MenuItem
                sx={{ display: "flex", alignItems: "center", gap: 1 }}
                onClick={handleCloseUserMenu}
                component="a"
                href="https://github.com/Angel-Concha-Layme/short-url/issues"
                target="_blank"
              >
                <BugReportIcon />
                Report a Bug
              </MenuItem>
              <Divider />
              <MenuItem
                sx={{ display: "flex", alignItems: "center", gap: 1 }}
                onClick={handleCloseUserMenu}
                component={Link}
                href="/logout"
              >
                <LogoutIcon />
                Log Out
              </MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default ResponsiveAppBar;
