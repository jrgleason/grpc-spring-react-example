import React from 'react';
import ReactDOM from 'react-dom/client';
import { ApolloClient, InMemoryCache, ApolloProvider } from '@apollo/client';
import './index.css';
import App from './App';

// Create Apollo Client for GraphQL
const graphqlClient = new ApolloClient({
  uri: import.meta.env.VITE_GRAPHQL_URL || 'http://localhost:4000/graphql',
  cache: new InMemoryCache(),
  // Enable dev tools in development
  connectToDevTools: import.meta.env.DEV,
});

const root = ReactDOM.createRoot(
  document.getElementById('root')
);

root.render(
  <React.StrictMode>
    <ApolloProvider client={graphqlClient}>
      <App />
    </ApolloProvider>
  </React.StrictMode>
);
