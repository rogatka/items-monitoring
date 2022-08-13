## Parsing API
### Summary
Application allows to parse items from shop's page and sends them to Message Broker (Apache Kafka).
Parsing is performed on the schedule (**PARSING_CRONE** env variable should contain cron-expression)

P.S. Currently application works only with 1 shop and 1 category (smartphones)

### How to run locally
Application requires:
- Kafka server (with SSL to authenticate)

**'kafka-docker' directory:**
1. Run the 'kafka-generate-ssl' file from 'kafka-docker' directory. It will generate 'truststore' and 'keystore'
   directories with certs (don't forget to provide 'localhost' as the username when script will ask you to)
2. Copy passwords which you used during Step #1 into 'kafka_keystore_credentials', 'kafka_sslkey_credentials',
   'kafka_truststore_credentials' accordingly.
3. Run the 'docker-compose' from the 'kafka-docker' directory (docker-compose up)

**current project's directory:**
5. Copy certs from Step #1 to '/resources/ssl' directory (current project).
6. Copy passwords which you used during Step #1 to application-local.yml
7. Run the application