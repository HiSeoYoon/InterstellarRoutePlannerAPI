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