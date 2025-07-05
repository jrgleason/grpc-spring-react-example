import React, { useState, useEffect } from 'react';
import './App.css';

interface User {
  id: number;
  name: string;
  email: string;
  role: string;
  createdAt: number;
}

interface CreateUserRequest {
  name: string;
  email: string;
  role: string;
}

function App() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [activeTab, setActiveTab] = useState<'rest' | 'grpc'>('rest');
  
  // Form state
  const [formData, setFormData] = useState<CreateUserRequest>({
    name: '',
    email: '',
    role: 'USER'
  });

  // Fetch users using REST API
  const fetchUsersRest = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch('/api/users');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const usersData = await response.json();
      setUsers(usersData);
      setSuccess('Users loaded successfully via REST API');
    } catch (err) {
      setError(`Failed to fetch users: ${err instanceof Error ? err.message : 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  // Note: gRPC-Web implementation would go here
  // For now, we'll use the REST API as fallback
  const fetchUsersGrpc = async () => {
    setError('gRPC-Web client not yet implemented. Using REST API instead.');
    await fetchUsersRest();
  };

  const fetchUsers = () => {
    if (activeTab === 'rest') {
      fetchUsersRest();
    } else {
      fetchUsersGrpc();
    }
  };

  useEffect(() => {
    fetchUsers();
  }, [activeTab]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    
    try {
      const url = editingUser ? `/api/users/${editingUser.id}` : '/api/users';
      const method = editingUser ? 'PUT' : 'POST';
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const userData = await response.json();
      
      if (editingUser) {
        setUsers(users.map(user => user.id === editingUser.id ? userData : user));
        setSuccess('User updated successfully');
      } else {
        setUsers([...users, userData]);
        setSuccess('User created successfully');
      }
      
      handleCloseModal();
    } catch (err) {
      setError(`Failed to save user: ${err instanceof Error ? err.message : 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this user?')) {
      return;
    }

    setLoading(true);
    setError(null);
    
    try {
      const response = await fetch(`/api/users/${id}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      setUsers(users.filter(user => user.id !== id));
      setSuccess('User deleted successfully');
    } catch (err) {
      setError(`Failed to delete user: ${err instanceof Error ? err.message : 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (user: User) => {
    setEditingUser(user);
    setFormData({
      name: user.name,
      email: user.email,
      role: user.role
    });
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingUser(null);
    setFormData({ name: '', email: '', role: 'USER' });
  };

  const clearMessages = () => {
    setError(null);
    setSuccess(null);
  };

  return (
    <div className="container">
      <div className="header">
        <h1>React gRPC Demo</h1>
        <p>Demonstrating communication between React frontend and Spring Boot gRPC backend</p>
      </div>

      {error && (
        <div className="error">
          {error}
          <button onClick={clearMessages} style={{ float: 'right', background: 'none', border: 'none', color: 'inherit' }}>×</button>
        </div>
      )}

      {success && (
        <div className="success">
          {success}
          <button onClick={clearMessages} style={{ float: 'right', background: 'none', border: 'none', color: 'inherit' }}>×</button>
        </div>
      )}

      <div className="card">
        <div className="tabs">
          <button 
            className={`tab ${activeTab === 'rest' ? 'active' : ''}`}
            onClick={() => setActiveTab('rest')}
          >
            REST API
          </button>
          <button 
            className={`tab ${activeTab === 'grpc' ? 'active' : ''}`}
            onClick={() => setActiveTab('grpc')}
          >
            gRPC (Coming Soon)
          </button>
        </div>

        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <h2>Users ({activeTab.toUpperCase()})</h2>
          <div>
            <button className="button" onClick={fetchUsers} disabled={loading}>
              {loading ? 'Loading...' : 'Refresh'}
            </button>
            <button className="button success" onClick={() => setShowModal(true)} disabled={loading}>
              Add User
            </button>
          </div>
        </div>

        {loading && <div className="loading">Loading users...</div>}

        <div className="user-list">
          {users.map(user => (
            <div key={user.id} className="user-card">
              <h3>{user.name}</h3>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Role:</strong> {user.role}</p>
              <p><strong>Created:</strong> {new Date(user.createdAt * 1000).toLocaleDateString()}</p>
              <div style={{ marginTop: '15px' }}>
                <button className="button" onClick={() => handleEdit(user)}>
                  Edit
                </button>
                <button className="button danger" onClick={() => handleDelete(user.id)}>
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>

        {users.length === 0 && !loading && (
          <div className="loading">No users found. Add some users to get started!</div>
        )}
      </div>

      {/* Modal for Add/Edit User */}
      {showModal && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>{editingUser ? 'Edit User' : 'Add New User'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="name">Name:</label>
                <input
                  type="text"
                  id="name"
                  value={formData.name}
                  onChange={e => setFormData({ ...formData, name: e.target.value })}
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="email">Email:</label>
                <input
                  type="email"
                  id="email"
                  value={formData.email}
                  onChange={e => setFormData({ ...formData, email: e.target.value })}
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="role">Role:</label>
                <select
                  id="role"
                  value={formData.role}
                  onChange={e => setFormData({ ...formData, role: e.target.value })}
                >
                  <option value="USER">User</option>
                  <option value="ADMIN">Admin</option>
                  <option value="MODERATOR">Moderator</option>
                </select>
              </div>
              
              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '20px' }}>
                <button type="button" className="button" onClick={handleCloseModal}>
                  Cancel
                </button>
                <button type="submit" className="button success" disabled={loading}>
                  {loading ? 'Saving...' : (editingUser ? 'Update' : 'Create')}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
