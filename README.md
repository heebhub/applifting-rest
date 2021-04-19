# Endpoint Monitoring Service

This service can monitor endpoints added to the database at a certain time interval. 

## Getting started
Make sure to install [Docker](https://www.docker.com/) first! Then, run the following command:
```
git clone https://github.com/heebhub/applifting-rest.git
docker compose up --build
```

That's all! :sunglasses:

## Specifications
The service is written in Java 12 with Spring Boot framework and MySQL database. Before the application starts, Flyway (the database migration tool) creates tables to provide the correct order of the schema creation. Javadoc and logs are included.

## REST Endpoint list
The API runs on http://localhost:8080/. To use the service, you need to add a custom HTTP header: `accessToken`. Available UUIDs are:
|      <b>Header name</b>     | <b>Header value</b> |
|:-----------------------:|:------:|
|        `accessToken`       |   07618a19-b17c-4437-8619-505af0f6906d  |
|     `accessToken`     |   0c360165-b418-4caa-acea-92be46dc15a0  |

Available REST endpoints and methods are listed below:

|      <b>Address</b>     | <b>Method</b> |                                     <b>Description</b>                                                   |
|:-----------------------:|:------:|:---------------------------------------------------------------------------------------------------------------:|
|        `/endpoints`       |   GET  |                                     Gets the list of all recorded endpoints.                                    |
|     `/endpoints/{id}`     |   GET  |        Gets an endpoint by selected id. If the user token is wrong, the user won't get the endpoint data.       |
| `/endpoints/{id}/results` |   GET  | Gets 10 recent results for selected endpoint. If the user token is wrong, the user won't get the endpoint data. |
|     `/endpoints/{id}`     |   PUT  |            Updates selected endpoint. If the user token is wrong, the endpoint data won't be updated.           |
|        `/endpoints`       |  POST  |                                               Adds a new endpoint.                                              |
|     `/endpoints/{id}`     | DELETE |       Deletes selected endpoint by its id. If the user token is wrong, the endpoint data won't be deleted.      |
