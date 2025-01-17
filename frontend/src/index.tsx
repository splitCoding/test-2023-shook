import React from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';
import GlobalStyles from '@/shared/styles/GlobalStyles';
import router from './router';
import ToastProvider from './shared/components/Toast/ToastProvider';
import theme from './shared/styles/theme';

async function main() {
  if (process.env.NODE_ENV === 'development') {
    const { worker } = await import('./mocks/browser');

    await worker.start({
      serviceWorker: {
        url: '/mockServiceWorker.js',
      },
    });
  }

  const root = createRoot(document.getElementById('root') as HTMLElement);

  root.render(
    <React.StrictMode>
      <GlobalStyles />
      <ThemeProvider theme={theme}>
        <ToastProvider>
          <RouterProvider router={router} />
        </ToastProvider>
      </ThemeProvider>
    </React.StrictMode>
  );
}

main();
