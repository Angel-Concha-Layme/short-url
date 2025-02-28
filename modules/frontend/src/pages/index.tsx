import { Container } from "@mui/material";
import Head from "next/head";
import Header from "@/components/header";

export default function Home() {
  return (
    <>
      <Head>
        <title>Short Url</title>
        <meta name="description" content="Short Url" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <Header />
      <Container component="main" maxWidth="xl"></Container>
    </>
  );
}
