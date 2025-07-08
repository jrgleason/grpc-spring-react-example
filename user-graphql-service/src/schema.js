import { gql } from 'apollo-server-express';

const typeDefs = gql`
  extend type Query {
    user(id: ID!): User
    users: [User!]!
  }

  extend type Mutation {
    createUser(input: CreateUserInput!): User!
    updateUser(id: ID!, input: UpdateUserInput!): User!
    deleteUser(id: ID!): Boolean!
  }

  type User @key(fields: "id") {
    id: ID!
    name: String!
    email: String!
    role: UserRole!
    createdAt: String!
  }

  input CreateUserInput {
    name: String!
    email: String!
    role: UserRole!
  }

  input UpdateUserInput {
    name: String
    email: String
    role: UserRole
  }

  enum UserRole {
    USER
    ADMIN
    MODERATOR
  }
`;

export { typeDefs };
