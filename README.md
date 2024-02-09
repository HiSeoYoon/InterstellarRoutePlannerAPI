#### 요구사항

Interstellar Route Planner는 우주 여행에 대한 비용을 계산하는 서버를 구축하여 API로 제공한다. 여행은 개인 운송과 HTC 운송으로 나뉘며, 각각의 비용을 계산하여 사용자에게 최저 가격을 제공해야 한다.(여행 거리(AUs), 승객 수 및 주차 기간에 따른 최저 가격을 제안하는 프로그램을 작성해야 한다.) 이를 위해 가격 책정 모델을 구현해야하며, HTC의 가속기 및 연결된 경로에 대한 정보를 저장하여 사용한다.



#### 계산 조건

1. Journey to the accelerator:
   - **Personal Transport**: £0.30/[AU](https://en.wikipedia.org/wiki/Astronomical_unit) (standard fuel cost) plus £5 per day for ship storage at the accelerator - (fits up to 4 people)
   - **HTC Transport**: £0.45/[AU](https://en.wikipedia.org/wiki/Astronomical_unit) - (fits up to 5 people)
2. An outbound and an inbound hyperspace journey:
   - **Spaceflight**: £0.10/passenger/hyperplane-unit



#### 구현 항목

- `GET`: `/transport/{distance}?passengers={number}&parking={days}` - 여행 거리(AUs), 승객 수 및 주차 기간에 따른 최저 가격을 반환
- `GET`: `/accelerators` - accelerators의 정보 리스트를 반환
- `GET`: `/accelerators/{acceleratorID}` - accelerator 정보 반환
- `GET`: `/accelerators/{acceleratorID}/to/{targetAcceleratorID}` - `acceleratorID` 에서 `targetAcceleratorID` 까지 도달하는 최저 가격을 반환



#### 사용된 기술

- API documentation - Swagger
- JUnit tests
- CI/CD configuration - github actions
- Dockerhub image
- Dijkstra Algorithm





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

