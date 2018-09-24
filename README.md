# Dropwlab

How to start the Dropwlab application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/dropwlab-delaval-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`
4. localhost:8080/hello-farmer
5. http://localhost:8080/hello-farmer?name=Bästa+MyFarm3.0+User

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
