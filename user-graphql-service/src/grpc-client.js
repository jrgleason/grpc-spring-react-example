import grpc from '@grpc/grpc-js';
import protoLoader from '@grpc/proto-loader';
import path from 'path';
import { fileURLToPath } from 'url';

// Get __dirname equivalent for ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Load the protobuf definition
const PROTO_PATH = path.join(__dirname, '../proto/user_service.proto');

const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true,
});

const userProto = grpc.loadPackageDefinition(packageDefinition);

class UserGrpcClient {
  constructor() {
    const grpcUrl = process.env.GRPC_SERVICE_URL || 'user-grpc-service:9090';
    this.client = new userProto.org.jrg.grpc.UserService(
      grpcUrl,
      grpc.credentials.createInsecure()
    );
  }

  async getUser(id) {
    return new Promise((resolve, reject) => {
      this.client.getUser({ id: parseInt(id) }, (error, response) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async getAllUsers() {
    return new Promise((resolve, reject) => {
      this.client.getAllUsers({}, (error, response) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async createUser(input) {
    return new Promise((resolve, reject) => {
      this.client.createUser(input, (error, response) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async updateUser(id, input) {
    return new Promise((resolve, reject) => {
      const request = { id: parseInt(id), ...input };
      this.client.updateUser(request, (error, response) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async deleteUser(id) {
    return new Promise((resolve, reject) => {
      this.client.deleteUser({ id: parseInt(id) }, (error, response) => {
        if (error) {
          reject(error);
        } else {
          resolve(response.success || true);
        }
      });
    });
  }
}

export { UserGrpcClient };
