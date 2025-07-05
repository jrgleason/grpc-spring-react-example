import React, { useState, useEffect } from 'react';
import './App.css';
import { UserServiceClient } from './generated/User_serviceServiceClientPb';
import { 
  User, 
  GetAllUsersRequest, 
  CreateUserRequest, 
  UpdateUserRequest, 
  DeleteUserRequest,
  GetUserRequest 
} from './generated/user_service_pb';

interface UserData {
  id: number;
  name: string;
  email: string;
  role: string;
  createdAt: string;
}

const App: React.FC = () => {
  const [users, setUsers] = useState<UserData[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [editingUser, setEditingUser] = useState<UserData | null>(null);
  const [formData, setFormData] = useState({ name: '', email: '', role: 'USER' });

  // Initialize gRPC client
  const grpcClient = new UserServiceClient('http://localhost:8080', null, null);

  const loadUsers = async () => {
    setLoading(true);
    setError(null);
    try {
      const request = new GetAllUsersRequest();
      
      const response = await new Promise<any>((resolve, reject) => {
        grpcClient.getAllUsers(request, {}, (err: any, response: any) => {
          if (err) {
            reject(err);
          } else {
            resolve(response);
          }
        });
      });

      const usersList = response.getUsersList();
      const usersData: UserData[] = usersList.map((user: User) => ({
        id: user.getId(),
        name: user.getName(),
        email: user.getEmail(),
        role: user.getRole(),
        createdAt: new Date(user.getCreatedAt() * 1000).toISOString()
      }));
      
      setUsers(usersData);
    } catch (err: any) {
      setError(`Failed to load users: ${err.message}`);
      console.error('gRPC Error:', err);
    } finally {
      setLoading(false);
    }
  };

  const createUser = async () => {
    if (!formData.name || !formData.email) return;
    
    setLoading(true);
    setError(null);
    try {
      const request = new CreateUserRequest();
      request.setName(formData.name);
      request.setEmail(formData.email);
      request.setRole(formData.role);

      await new Promise<any>((resolve, reject) => {
        grpcClient.createUser(request, {}, (err: any, response: any) => {
          if (err) {
            reject(err);
          } else {
            resolve(response);
          }
        });
      });

      setFormData({ name: '', email: '', role: 'USER' });
      setShowForm(false);
      await loadUsers();
    } catch (err: any) {
      setError(`Failed to create user: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const updateUser = async () => {
    if (!editingUser || !formData.name || !formData.email) return;
    
    setLoading(true);
    setError(null);
    try {
      const request = new UpdateUserRequest();
      request.setId(editingUser.id);
      request.setName(formData.name);
      request.setEmail(formData.email);
      request.setRole(formData.role);

      await new Promise<any>((resolve, reject) => {
        grpcClient.updateUser(request, {}, (err: any, response: any) => {
          if (err) {
            reject(err);
          } else {
            resolve(response);
          }
        });
      });

      setFormData({ name: '', email: '', role: 'USER' });
      setEditingUser(null);
      setShowForm(false);
      await loadUsers();
    } catch (err: any) {
      setError(`Failed to update user: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const deleteUser = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      const request = new DeleteUserRequest();
      request.setId(id);

      await new Promise<any>((resolve, reject) => {
        grpcClient.deleteUser(request, {}, (err: any, response: any) => {
          if (err) {
            reject(err);
          } else {
            resolve(response);
          }
        });
      });

      await loadUsers();
    } catch (err: any) {
      setError(`Failed to delete user: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (user: UserData) => {
    setEditingUser(user);
    setFormData({ name: user.name, email: user.email, role: user.role });
    setShowForm(true);
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingUser(null);
    setFormData({ name: '', email: '', role: 'USER' });
  };

  useEffect(() => {
    loadUsers();
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <h1>🚀 React + Spring Boot gRPC Demo</h1>
        <p>User Management with gRPC-Web</p>
      </header>

      <main className="main-content">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <div className="controls">
          <button 
            onClick={() => setShowForm(!showForm)}
            disabled={loading}
            className="primary-button"
          >
            {showForm ? 'Cancel' : '+ Add User'}
          </button>
          <button 
            onClick={loadUsers}
            disabled={loading}
            className="secondary-button"
          >
            🔄 Refresh
          </button>
        </div>

        {showForm && (
          <div className="user-form">
            <h3>{editingUser ? 'Edit User' : 'Add New User'}</h3>
            <form onSubmit={(e) => {
              e.preventDefault();
              editingUser ? updateUser() : createUser();
            }}>
              <div className="form-group">
                <label htmlFor="name">Name:</label>
                <input
                  type="text"
                  id="name"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="email">Email:</label>
                <input
                  type="email"
                  id="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="role">Role:</label>
                <select
                  id="role"
                  value={formData.role}
                  onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                >
                  <option value="USER">User</option>
                  <option value="ADMIN">Admin</option>
                  <option value="MANAGER">Manager</option>
                </select>
              </div>
              <div className="form-actions">
                <button type="submit" disabled={loading} className="primary-button">
                  {editingUser ? 'Update' : 'Create'} User
                </button>
                <button type="button" onClick={handleCancel} className="secondary-button">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {loading && <div className="loading">Loading...</div>}

        <div className="users-grid">
          {users.map((user) => (
            <div key={user.id} className="user-card">
              <div className="user-info">
                <h3>{user.name}</h3>
                <p className="user-email">{user.email}</p>
                <p className="user-role">{user.role}</p>
                <p className="user-date">
                  Created: {new Date(user.createdAt).toLocaleDateString()}
                </p>
              </div>
              <div className="user-actions">
                <button 
                  onClick={() => handleEdit(user)}
                  disabled={loading}
                  className="edit-button"
                >
                  ✏️ Edit
                </button>
                <button 
                  onClick={() => deleteUser(user.id)}
                  disabled={loading}
                  className="delete-button"
                >
                  🗑️ Delete
                </button>
              </div>
            </div>
          ))}
        </div>

        {users.length === 0 && !loading && (
          <div className="empty-state">
            <h3>No users found</h3>
            <p>Click "Add User" to create your first user</p>
          </div>
        )}
      </main>
    </div>
  );
};

export default App;
