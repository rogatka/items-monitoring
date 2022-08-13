## Monitoring API
### Summary
Application allows to retrieve items from DB (MongoDB) to track item's price history.

### How to run locally
Application requires 
- Keycloak (for authentication) 
- Redis (for cache)
- MongoDB

**'mongo-docker' directory:**
1. Run the 'docker-compose' from the 'mongo-docker' directory (docker-compose up). The 'mongo-init.js' will create a
   user that is used by the application to connect to MongoDB

**'redis-docker' directory:**
2. Run the 'docker-compose' from the 'redis-docker' directory (docker-compose up).

**'keycloak-docker' directory:**
3. Run the 'docker-compose' from the 'keycloak-docker' directory (docker-compose up).
4. Configure realm, client and user with role 'USER'

**current project's directory:**
5. Copy username and password for user created on Step #1 to application-local.yml
6. Copy password for Redis which were used in docker-compose.yml from Step #2 to application-local.yml
7. Copy required info from Step #4 to application.yml
8. Run the application