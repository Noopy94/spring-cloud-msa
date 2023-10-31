# spring-cloud-msa

spring cloud msa 개인 study입니다.

inflearn springcloud로 개발하는 msa 강의를 참고 하고 있습니다.

1. Service Discovery
2. API Gateway Service
3. Microservice(User, Catalogs, Orders)
4. Configuration
5. Kafka
6. mariaDB
7. 카프카 DB 연동

### 이 강의의 카프카 study log

#### 1.강의와 함께 참조 자료

https://cjw-awdsd.tistory.com/53

#### 2. 테스트 코드와 프로덕션 차이점

프로덕션 레벨에서는 카프카 브로커 3개 이상을 활용해서 클러스터를 구성을 권장한다.
테스트 레벨에서는 단일 브로커를 사용해서 구성

#### 3. 카프카 실행 순서

- 주의점: window의 경우 .sh 대신 /windows/.bat 을 실행한다.

1. 주키퍼 실행

- $ ./bin/zookeeper-server-start.sh ./config/zookeeper.properties

2. 카프카 실행

- $ ./bin/kafka-server-start.sh ./config/server.properties

3. 카프카 커넥트 실행

- $ ./bin/connect-distributed ./etc/kafka/connect-distributed.properties

4. 카프카 커넥트 토픽 리스트 확인

- $ ./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

5. 소스 커넥터(현 테스트에서는 자바 스프링 -> 싱크 커넥터 -> DB 형식이라 사용하지 않는다.)

{
"name": "my-source-connect",
"config": {
"connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
"connection.url": "jdbc:mysql://localhost:3306/test",
"connection.user":"root",
"connection.password":"비밀번호",
"mode":"incrementing",
"incrementing.column.name" : "id",
"table.whitelist" : "users",
"topic.prefix" : "example*topic*",
"tasks.max" : "1",
}
}
cUrl -X POST -d @- http://localhost:8083/connectors --header "content-Type:application/json

등록한다.

6. 싱크 커넥터

{
"name": "my-pksink-connect",
"config": {
"connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
"connection.url": "jdbc:mysql://localhost:3306/test",
"connection.user":"root",
"connection.password":"비밀번호",
"auto.create":"true",
"auto.evolve":"true",
"delete.enabled":"false",
"tasks.max":"1",
"topics":"example_topic_users"
}
}

cUrl -X POST -d @- http://localhost:8083/connectors --header "content-Type:application/json"

등록한다.

단, upsert나 update의 경우 pk 명시 필수
name을 사용하지 않는다면 자동으로 고유한 이름 생성(고유 식별자에 기반하여 생성)

{
"connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
"tasks.max": 1,
"topics": "your_topic",
"connection.url": "jdbc:your_database_url",
"connection.user": "your_username",
"connection.password": "your_password",
"auto.create": true,
"insert.mode": "upsert", // or "update"
"pk.mode": "record_key",
"pk.fields": "your_primary_key_field"
}

을 사용한다.

사용예시

ex)

CREATE TABLE employees (
employee_id INT PRIMARY KEY,
employee_name VARCHAR(100),
employee_dept VARCHAR(100)
);

{
"connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
"tasks.max": 1,
"topics": "your_topic",
"connection.url": "jdbc:your_database_url",
"connection.user": "your_username",
"connection.password": "your_password",
"auto.create": true,
"insert.mode": "upsert",
"pk.mode": "record_key",
"pk.fields": "employee_id"
}
를 사용한다.
