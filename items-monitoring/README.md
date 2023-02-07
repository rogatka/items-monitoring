## Monitoring API
### Summary
Application allows to retrieve items from MongoDB and Redis cache.
Application also has simple UI to search for items (using ElasticSearch) and generate PDF or XLSX reports.

### How to run locally
Application requires 
- Keycloak (for authentication) 
- Redis (for cache)
- MongoDB
- ElasticSearch

**'mongo-docker'** directory:
1. Run the 'docker-compose' from the 'mongo-docker' directory (docker-compose up). The 'mongo-init.js' will create a
   user that is used by the application to connect to MongoDB

**'redis-docker'** directory:
2. Run the 'docker-compose' from the 'redis-docker' directory (docker-compose up).

**'keycloak-docker'** directory:
3. Run the 'docker-compose' from the 'keycloak-docker' directory (docker-compose up).
4. Create realm with name "items_monitoring_realm", client with name "items_monitoring_client" and user with role 'USER'

**'elasticsearch-docker'** directory:
5. Run the 'docker-compose' from the 'elasticsearch-docker' directory (docker-compose up).

**current project's directory:**
6. Copy username and password for user created on Step #1 to application-local.yml
7. Copy password for Redis which were used in docker-compose.yml from Step #2 to application-local.yml
8. Copy required info from Step #4 to application.yml
9. Run the application

**zipkin-docker** directory _(optionally - to enable tracing)_:

Run the 'docker-compose' from the 'zipkin-docker' directory (docker-compose up)