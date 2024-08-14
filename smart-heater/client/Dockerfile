# Use the official Node.js image
FROM node:20

# Update CA certificates and install necessary packages
RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

# Set environment variables
ENV NODE_EXTRA_CA_CERTS=/etc/ssl/certs/ca-certificates.crt
ENV NPM_CONFIG_STRICT_SSL=false

# Create and change to the app directory
WORKDIR /client

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the port the app runs on
EXPOSE 5173

# Run the application
CMD ["npm", "run", "dev"]