# Dockerfile
FROM nginx:latest

# Copy SSL certificate and key into container
COPY ssl/nginx-selfsigned.crt /etc/ssl/certs/nginx-selfsigned.crt
COPY ssl/nginx-selfsigned.key /etc/ssl/private/nginx-selfsigned.key

# Copy nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Expose ports
EXPOSE 80
EXPOSE 443

# Start nginx
CMD ["nginx", "-g", "daemon off;"]