FROM --platform=linux/amd64 node:22-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install --omit=dev

# Copy proto files
COPY proto/ /app/proto/

# Copy source code
COPY . .

# Expose port
EXPOSE 4001

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:4001/health || exit 1

# Start the application
CMD ["npm", "start"]
