## Db Uploader API
### Summary
Application consumes messages from Message Broker (Apache Kafka). 
Then, conditionally uploads the data to MongoDB and ElasticSearch.


### How to run locally
Application requires:
- Kafka server (with SSL to authenticate)
- MongoDB
- ElasticSearch

**'kafka-docker' directory:**
1. Run the 'kafka-generate-ssl' file from 'kafka-docker' directory. It will generate 'truststore' and 'keystore'
   directories with certs (don't forget to provide 'localhost' as the username when script will ask you to)
2. Copy passwords which you used during Step #1 into 'kafka_keystore_credentials', 'kafka_sslkey_credentials',
   'kafka_truststore_credentials' accordingly.
3. Run the 'docker-compose' from the 'kafka-docker' directory (docker-compose up)

**'mongo-docker' directory:**
4. Run the 'docker-compose' from the 'mongo-docker' directory (docker-compose up). The 'mongo-init.js' will create a 
user that is used by the application to connect to MongoDB

**'elasticsearch-docker'** directory:
5. Run the 'docker-compose' from the 'elasticsearch-docker' directory (docker-compose up).

**current project's directory:**
6. Copy certs from Step #1 to '/resources/ssl' directory (current project).
7. Copy passwords which you used during Step #1 to application-local.yml
8. Copy username and password for user created on Step #4 to application-local.yml
9. Run the application

**zipkin-docker** directory _(optionally - to enable tracing)_:

Run the 'docker-compose' from the 'zipkin-docker' directory (docker-compose up)