import ChangeThemeSwitch from '@/components/changeTheme';
import Head from 'next/head';

export default function Home() {
  return (
    <>
      <Head>
        <title>Short Url</title>
        <meta name="description" content="Short Url" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main>
        <ChangeThemeSwitch />
      </main>
    </>
  );
}
