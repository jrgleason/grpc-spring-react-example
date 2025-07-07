import React, { useState } from 'react';
import { useQuery, useMutation, gql } from '@apollo/client';
import './App.css';

// GraphQL Queries and Mutations
const GET_USERS = gql`
  query GetUsers {
    users {
      id
      name
      email
      role
      createdAt
    }
  }
`;

const CREATE_USER = gql`
  mutation CreateUser($input: CreateUserInput!) {
    createUser(input: $input) {
      id
      name
      email
      role
      createdAt
    }
  }
`;

const UPDATE_USER = gql`
  mutation UpdateUser($id: ID!, $input: UpdateUserInput!) {
    updateUser(id: $id, input: $input) {
      id
      name
      email
      role
      createdAt
    }
  }
`;

const DELETE_USER = gql`
  mutation DeleteUser($id: ID!) {
    deleteUser(id: $id)
  }
`;

const App = () => {
  const [showForm, setShowForm] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [formData, setFormData] = useState({ name: '', email: '', role: 'USER' });

  // GraphQL Hooks
  const { data, loading, error, refetch } = useQuery(GET_USERS);
  const [createUser] = useMutation(CREATE_USER, {
    refetchQueries: [{ query: GET_USERS }],
  });
  const [updateUser] = useMutation(UPDATE_USER, {
    refetchQueries: [{ query: GET_USERS }],
  });
  const [deleteUser] = useMutation(DELETE_USER, {
    refetchQueries: [{ query: GET_USERS }],
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      if (editingUser) {
        await updateUser({
          variables: {
            id: editingUser.id,
            input: formData,
          },
        });
      } else {
        await createUser({
          variables: {
            input: formData,
          },
        });
      }
      
      // Reset form
      setFormData({ name: '', email: '', role: 'USER' });
      setShowForm(false);
      setEditingUser(null);
    } catch (error) {
      console.error('Error saving user:', error);
    }
  };

  const handleEdit = (user) => {
    setEditingUser(user);
    setFormData({
      name: user.name,
      email: user.email,
      role: user.role,
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await deleteUser({
          variables: { id },
        });
      } catch (error) {
        console.error('Error deleting user:', error);
      }
    }
  };

  const handleCancel = () => {
    setFormData({ name: '', email: '', role: 'USER' });
    setShowForm(false);
    setEditingUser(null);
  };

  if (loading) return <div className="loading">Loading users...</div>;
  if (error) return <div className="error">Error: {error.message}</div>;

  const users = data?.users || [];

  return (
    <div className="App">
      <header className="App-header">
        <h1>📊 User Management (GraphQL)</h1>
        <p>Full-stack gRPC + GraphQL Federation Demo</p>
        <div className="architecture-info">
          <span>React</span> → <span>Apollo Gateway</span> → <span>GraphQL Service</span> → <span>gRPC Service</span>
        </div>
      </header>

      <main className="main-content">
        <div className="controls">
          <button 
            onClick={() => setShowForm(!showForm)}
            className="btn btn-primary"
          >
            {showForm ? 'Cancel' : 'Add User'}
          </button>
          <button 
            onClick={() => refetch()}
            className="btn btn-secondary"
          >
            Refresh
          </button>
        </div>

        {showForm && (
          <div className="form-container">
            <h3>{editingUser ? 'Edit User' : 'Create New User'}</h3>
            <form onSubmit={handleSubmit} className="user-form">
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
                  <option value="MODERATOR">Moderator</option>
                </select>
              </div>
              
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingUser ? 'Update' : 'Create'}
                </button>
                <button type="button" onClick={handleCancel} className="btn btn-secondary">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        <div className="users-container">
          <h2>Users ({users.length})</h2>
          {users.length === 0 ? (
            <div className="empty-state">
              <p>No users found. Create your first user!</p>
            </div>
          ) : (
            <div className="users-grid">
              {users.map((user) => (
                <div key={user.id} className="user-card">
                  <div className="user-info">
                    <h3>{user.name}</h3>
                    <p className="email">{user.email}</p>
                    <span className={`role role-${user.role.toLowerCase()}`}>
                      {user.role}
                    </span>
                    <p className="created-at">
                      Created: {new Date(user.createdAt).toLocaleDateString()}
                    </p>
                  </div>
                  <div className="user-actions">
                    <button
                      onClick={() => handleEdit(user)}
                      className="btn btn-sm btn-edit"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(user.id)}
                      className="btn btn-sm btn-delete"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="api-info">
          <h3>🔄 API Architecture</h3>
          <div className="api-details">
            <div className="api-item">
              <strong>GraphQL Endpoint:</strong>
              <code>http://localhost:4000/graphql</code>
            </div>
            <div className="api-item">
              <strong>gRPC Service:</strong>
              <code>localhost:9090</code>
            </div>
            <div className="api-item">
              <strong>Protocol:</strong>
              <span>GraphQL → gRPC Federation</span>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default App;
