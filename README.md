[![CircleCI](https://circleci.com/gh/skunkwerksuk/timekeeper-absences.svg?style=svg)](https://circleci.com/gh/skunkwerksuk/timekeeper-absences)

# Timekeeper Absences API
A CRUD API to interact with timekeeper absences

## Running the service locally
### Quickstart (no DB)
Open the `resources/application.properties` file.

Delete all lines in the file and paste the following:
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
server.port=8081
```
Next open the `build.gradle` file and add the following below the `dependencies` block:
```
implementation 'com.h2database:h2'
```
You're all set!

__NOTE: Data will not be persisted beyond runtime.__

### Full PostgreSQL setup

#### Install
To run the full persistent API service you will need to navigate to the 
[PostgreSQL download site](https://www.postgresql.org/download/) and download PostgreSQL with pgAdmin.

When the download and install has completed open the pgAdmin webapp and complete the default setup.

#### User role setup
Next we need to create a new `Object -> Create -> Login/Group Role` with the following credentials:

```
General -> Name: admin
Definition -> Password: admin
Privilages -> Can login?: Yes
           -> Superuser?: Yes
```
__NOTE: This should default all the other privalages to Yes except stream replication.)__

#### Database setup
Next we need to create an empty database for Spring JPA to interact with.

In the object tree on the left of the window, on `Databases` right click, `Create -> Database`
with the following properties:

```
General -> Database: timekeeper-absences
        -> Owner: admin
```

Now you are all set up to build and run the service.

### Running the service

The service is configured using Gradle so you will need to download and install it from the 
[download site](https://gradle.org/install/).

Once you have installed gradle (make sure you have added gradle to PATH) open a terminal and `cd` into
the project directory and execute:
```
gradle build
```

if that doesn't work or Windows isn't executing the command, try:

```
./gradlew build
```

This will build the project and create the build files needed to run. To run the project, execute:
```
gradle bootRun
```
If that has run successfully then you should see the following in the console output:
```
2019-09-04 18:36:53.476  INFO 1547 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8081 (http)
2019-09-04 18:36:53.482  INFO 1547 --- [           main] timekeeper.absences.AbsencesApplication  : Started AbsencesApplication in 4.573 seconds
```
To test the application is working as expected - [explore the API docs.](localhost:8081/swagger-ui.html)
