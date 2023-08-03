### !Before running the project, you must run the Docker to make it work with the DB.

# How to run docker

```
cd docker
docker-compose up -d

//check running containers
docker ps
```



## Postgresql

```
docker exec -it <<your catainer name>> bash
psql -U root -W -h localhost -d test_db
```
container name : seoyoon-postgres-db

### List tables

```
\dt
```

### Table information

```
\d accelerators
\d accelerator_connections
```

### Read database value

```
SELECT * FROM accelerators;
SELECT * FROM accelerator_connections;
```

### Swagger address

http://localhost:8080/swagger-ui/#

