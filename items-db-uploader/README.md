## Db Uploader API
### Summary
Application consumes messages from Message Broker (Apache Kafka) and then conditionally uploads them to DB (MongoDB)


### How to run locally
Application requires:
- Kafka server (with SSL to authenticate)
- MongoDB

**'kafka-docker' directory:**
1. Run the 'kafka-generate-ssl' file from 'kafka-docker' directory. It will generate 'truststore' and 'keystore'
   directories with certs (don't forget to provide 'localhost' as the username when script will ask you to)
2. Copy passwords which you used during Step #1 into 'kafka_keystore_credentials', 'kafka_sslkey_credentials',
   'kafka_truststore_credentials' accordingly.
3. Run the 'docker-compose' from the 'kafka-docker' directory (docker-compose up)

**'mongo-docker' directory:**
4. Run the 'docker-compose' from the 'mongo-docker' directory (docker-compose up). The 'mongo-init.js' will create a 
user that is used by the application to connect to MongoDB

**current project's directory:**
5. Copy certs from Step #1 to '/resources/ssl' directory (current project).
6. Copy passwords which you used during Step #1 to application-local.yml
7. Copy username and password for user created on Step #4 to application-local.yml
8. Run the application