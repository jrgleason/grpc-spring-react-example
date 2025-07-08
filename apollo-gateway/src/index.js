import { ApolloGateway, IntrospectAndCompose } from '@apollo/gateway';
import { ApolloServer } from 'apollo-server-express';
import express from 'express';

async function startGateway() {
  // Create Apollo Gateway with subgraph configuration
  const gateway = new ApolloGateway({
    supergraphSdl: new IntrospectAndCompose({
      subgraphs: [
        { 
          name: 'users', 
          url: process.env.USER_GRAPHQL_SERVICE_URL || 'http://user-graphql-service:4001/graphql' 
        },
        // Add more subgraphs here as you create them
        // { name: 'orders', url: 'http://order-graphql-service:4002/graphql' },
        // { name: 'products', url: 'http://product-graphql-service:4003/graphql' },
      ],
    }),
    // Development settings
    debug: process.env.NODE_ENV !== 'production',
  });

  // Create Apollo Server with the gateway
  const server = new ApolloServer({
    gateway,
    // Enable introspection in development
    introspection: process.env.NODE_ENV !== 'production',
    // Enhanced error reporting in development
    debug: process.env.NODE_ENV !== 'production',
  });

  // Create Express app
  const app = express();

  // Add health check endpoint
  app.get('/health', (req, res) => {
    res.status(200).json({ 
      status: 'healthy', 
      service: 'apollo-gateway',
      timestamp: new Date().toISOString() 
    });
  });

  // Start the server
  await server.start();
  
  // Apply Apollo GraphQL middleware
  server.applyMiddleware({ app, path: '/graphql' });

  const port = process.env.PORT || 4000;
  
  return new Promise((resolve) => {
    app.listen(port, () => {
      console.log(`🚀 Apollo Gateway ready at http://localhost:${port}${server.graphqlPath}`);
      console.log(`🏥 Health check available at http://localhost:${port}/health`);
      resolve();
    });
  });
}

// Start the gateway
startGateway().catch((error) => {
  console.error('Failed to start Apollo Gateway:', error);
  process.exit(1);
});
