# Use official Node.js runtime as the base image with specific platform for compatibility
FROM node:22

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies inside container to get correct platform binaries
RUN rm -rf node_modules package-lock.json && npm install

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Install serve to serve the built application
RUN npm install -g serve

# Expose port
EXPOSE 3000

# Start the application
CMD ["serve", "-s", "dist", "-l", "3000"]
