# task-manager-backend
Backend implementation for Task Manager app. The frontend part is into [task-manager-frontend](https://github.com/elkingiraldo/task-manager-frontend) project.

## Compiling
This is a Maven project and for compiling it you should do it with the Maven wrapper provided going into project root path (task-manager-api) and running the following command:

```
$ ./mvnw clean install
```

## Deploying
The project is dockerized and it has a external Postgres Database dockerized too. For deploying both of them you only need to run following command into the roor path of the project:

```
$ docker-compose up
```
## Generalities
The project run in port 8080 with spring security, then you only can access to the following paths in order to see Swagger Documentation:

```
http://localhost:8080/swagger-ui.html
http://localhost:8080/v2/api-docs
```
### FOR DEBBUGING/TESTING
If you want to test in local you need to start the container(task-manager-postgresql) of the DB created.
```
$ docker start task-manager-postgresql
```
Change two properties (spring.datasource.url and spring.flyway.url) into the application.propeties file.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanagerdb
spring.flyway.url=jdbc:postgresql://localhost:5432/taskmanagerdb
```
You can modify data too accessing into the DB with the following commands
```
$ docker exec -it task-manager-postgresql bash
$ psql -U postgres;
$ \c taskmanagerdb;
```

### API responses


| header  | value | description |
| ------------- | ------------- | ------------- |
| Accept  | application/json  | Default value  |
| Accept  | application/xml  | If you want a xml response  |
| locale  | en  | Default value  |
| locale  | es  | If you want a response in spanish  |

### Users in DB

| username  | password |
| ------------- | ------------- |
| elkingiraldo91  | elkinpassword  |
| jairo53  | jairopwd  |
| martha58  | marthapwd  |
