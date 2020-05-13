# task-manager-backend
Backend implementation for Task Manager app

# Compiling
This is a Maven project and for compiling it you need to go into root path project (task-manager-api) and run the following command:

```
$ mvn clean install
```

# Deploying
The project is dockerized and it has a external Postgres Database dockerized too. For deploying both of them you only need to run following command into the roor path of the project:

```
$ docker-compose up
```
