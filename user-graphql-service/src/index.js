import { ApolloServer } from 'apollo-server-express';
import { buildFederatedSchema } from '@apollo/federation';
import express from 'express';
import { typeDefs } from './schema.js';
import { resolvers } from './resolvers.js';

async function startServer() {
  // Create federated schema
  const schema = buildFederatedSchema([
    {
      typeDefs,
      resolvers,
    },
  ]);

  // Create Apollo Server
  const server = new ApolloServer({
    schema,
    introspection: process.env.NODE_ENV !== 'production',
    debug: process.env.NODE_ENV !== 'production',
  });

  // Create Express app
  const app = express();

  // Add health check endpoint
  app.get('/health', (req, res) => {
    res.status(200).json({ 
      status: 'healthy', 
      service: 'user-graphql-service',
      timestamp: new Date().toISOString()
    });
  });

  // Apply the Apollo GraphQL middleware
  await server.start();
  server.applyMiddleware({ 
    app, 
    path: '/graphql',
    cors: {
      origin: true,
      credentials: true
    }
  });

  const PORT = process.env.PORT || 4001;
  
  app.listen(PORT, () => {
    console.log(`🚀 User GraphQL Service ready at http://localhost:${PORT}${server.graphqlPath}`);
    console.log(`📊 Health check available at http://localhost:${PORT}/health`);
  });
}

// Error handling
process.on('unhandledRejection', (err) => {
  console.error('Unhandled Promise Rejection:', err);
  process.exit(1);
});

// Start the server
startServer().catch((error) => {
  console.error('Failed to start server:', error);
  process.exit(1);
});
