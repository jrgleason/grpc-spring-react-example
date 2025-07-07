import { UserGrpcClient } from './grpc-client';

const grpcClient = new UserGrpcClient();

export const resolvers = {
  Query: {
    user: async (_: any, { id }: { id: string }) => {
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
        return response.users?.map((user: any) => ({
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
    createUser: async (_: any, { input }: { input: { name: string; email: string; role: string } }) => {
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

    updateUser: async (_: any, { id, input }: { id: string; input: { name?: string; email?: string; role?: string } }) => {
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

    deleteUser: async (_: any, { id }: { id: string }) => {
      try {
        return await grpcClient.deleteUser(id);
      } catch (error) {
        console.error('Error deleting user:', error);
        throw new Error(`Failed to delete user: ${error}`);
      }
    },
  },

  User: {
    __resolveReference: async (user: { id: string }) => {
      try {
        return await grpcClient.getUser(user.id);
      } catch (error) {
        console.error('Error resolving user reference:', error);
        return null;
      }
    },
  },
};
