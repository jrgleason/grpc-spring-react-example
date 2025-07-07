import * as grpc from '@grpc/grpc-js';
import * as protoLoader from '@grpc/proto-loader';
import path from 'path';

// Load the protobuf definition
const PROTO_PATH = path.join(__dirname, '../proto/user_service.proto');

const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true,
});

const userProto = grpc.loadPackageDefinition(packageDefinition) as any;

export class UserGrpcClient {
  private client: any;

  constructor() {
    const grpcUrl = process.env.GRPC_SERVICE_URL || 'user-grpc-service:9090';
    this.client = new userProto.org.jrg.grpc.UserService(
      grpcUrl,
      grpc.credentials.createInsecure()
    );
  }

  async getUser(id: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.client.getUser({ id: parseInt(id) }, (error: any, response: any) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async getAllUsers(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.client.getAllUsers({}, (error: any, response: any) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async createUser(input: { name: string; email: string; role: string }): Promise<any> {
    return new Promise((resolve, reject) => {
      this.client.createUser(input, (error: any, response: any) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async updateUser(id: string, input: { name?: string; email?: string; role?: string }): Promise<any> {
    return new Promise((resolve, reject) => {
      const request = { id: parseInt(id), ...input };
      this.client.updateUser(request, (error: any, response: any) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      });
    });
  }

  async deleteUser(id: string): Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.client.deleteUser({ id: parseInt(id) }, (error: any, response: any) => {
        if (error) {
          reject(error);
        } else {
          resolve(response.success || true);
        }
      });
    });
  }
}
