import { UserGrpcClient } from './grpc-client.js';

const grpcClient = new UserGrpcClient();

const resolvers = {
  Query: {
    user: async (_, { id }) => {
      try {
        const user = await grpcClient.getUser(id);
        return {
          id: user.id.toString(),
          name: user.name,
          email: user.email,
          role: user.role,
          createdAt: user.createdAt || new Date().toISOString(),
        };
      } catch (error) {
        console.error('Error fetching user:', error);
        throw new Error(`Failed to fetch user: ${error}`);
      }
    },

    users: async () => {
      try {
        const response = await grpcClient.getAllUsers();
        return response.users?.map((user) => ({
          id: user.id.toString(),
          name: user.name,
          email: user.email,
          role: user.role,
          createdAt: user.createdAt || new Date().toISOString(),
        })) || [];
      } catch (error) {
        console.error('Error fetching users:', error);
        throw new Error(`Failed to fetch users: ${error}`);
      }
    },
  },

  Mutation: {
    createUser: async (_, { input }) => {
      try {
        const user = await grpcClient.createUser(input);
        return {
          id: user.id.toString(),
          name: user.name,
          email: user.email,
          role: user.role,
          createdAt: user.createdAt || new Date().toISOString(),
        };
      } catch (error) {
        console.error('Error creating user:', error);
        throw new Error(`Failed to create user: ${error}`);
      }
    },

    updateUser: async (_, { id, input }) => {
      try {
        const user = await grpcClient.updateUser(id, input);
        return {
          id: user.id.toString(),
          name: user.name,
          email: user.email,
          role: user.role,
          createdAt: user.createdAt || new Date().toISOString(),
        };
      } catch (error) {
        console.error('Error updating user:', error);
        throw new Error(`Failed to update user: ${error}`);
      }
    },

    deleteUser: async (_, { id }) => {
      try {
        return await grpcClient.deleteUser(id);
      } catch (error) {
        console.error('Error deleting user:', error);
        throw new Error(`Failed to delete user: ${error}`);
      }
    },
  },

  User: {
    __resolveReference: async (user) => {
      try {
        return await grpcClient.getUser(user.id);
      } catch (error) {
        console.error('Error resolving user reference:', error);
        return null;
      }
    },
  },
};

export { resolvers };
