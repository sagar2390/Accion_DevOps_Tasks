FROM nginx:1.19-alpine

LABEL summary="Container Image for nginx 1.19"
      name="Nginx"
      version="1.19"
      maintainer="Sagar Khuntia <sagar.devsecops2025@gmail.com>"
RUN groupadd -r nginx_group && \
    useradd -r -g nginx_group -m -d /home/nginx_user nginx_group

# Remove default server definition which comes nginx bydeafult 
RUN rm /etc/nginx/conf.d/default.conf

# Add a custom server configuration 
COPY nginx.conf /etc/nginx/nginx.conf

# Give necessesery permissions nginx_user
RUN chown -R nginx_user:nginx_group /var/cache/nginx /var/run /etc/nginx

USER nginx_user

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
