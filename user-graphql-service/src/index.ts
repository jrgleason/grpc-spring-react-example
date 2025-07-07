import { ApolloServer } from 'apollo-server-express';
import { buildFederatedSchema } from '@apollo/federation';
import express from 'express';
import { typeDefs } from './schema';
import { resolvers } from './resolvers';

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
  app.get('/health', (req: any, res: any) => {
    res.status(200).json({ 
      status: 'healthy', 
      service: 'user-graphql-service',
      timestamp: new Date().toISOString() 
    });
  });

  // Start the server
  await server.start();
  
  // Apply Apollo GraphQL middleware
  server.applyMiddleware({ app: app as any, path: '/graphql' });

  const port = process.env.PORT || 4001;
  
  return new Promise<void>((resolve) => {
    app.listen(port, () => {
      console.log(`🚀 User GraphQL Service ready at http://localhost:${port}${server.graphqlPath}`);
      console.log(`🏥 Health check available at http://localhost:${port}/health`);
      resolve();
    });
  });
}

// Start the server
startServer().catch((error) => {
  console.error('Failed to start User GraphQL Service:', error);
  process.exit(1);
});
