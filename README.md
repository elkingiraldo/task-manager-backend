# task-manager-backend
Backend implementation for Task Manager app

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

### Users in DB

| username  | password |
| ------------- | ------------- |
| elkingiraldo91  | elkinpassword  |
| jairo53  | jairopwd  |
| martha58  | marthapwd  |
