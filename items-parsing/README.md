## Parsing API
### Summary
Application parses items and sends them directly to Message Broker (Apache Kafka).
Parsing is running on the schedule and on application's startup.

### How to run locally
Application requires:
- Kafka server (with SSL to authenticate)
#### Required environment variables (for local run):
| Variable name | Description                                 |
|---------------|---------------------------------------------|
| RAWG_API_KEY  | API Key from [rawg.io](https://www.rawg.io) |

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

### Changelog:
- 2023-01-31 Added parser to track games' rating for the last month.  