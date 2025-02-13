import { useColorScheme, Switch } from "@mui/material";
import Head from "next/head";

export default function Home() {
    const { mode, setMode } = useColorScheme();

    if (!mode) {
        return null;
    }

    return (
        <>
            <Head>
                <title>Short Url</title>
                <meta name="description" content="Short Url" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/favicon.ico" />
            </Head>

            <main>
                <Switch
                    checked={mode === "dark"}
                    onChange={() => setMode(mode === "dark" ? "light" : "dark")}
                />
            </main>
        </>
    );
}
