Mars Photos Downloader
===

Application that downloads photos of Mars and finds the largest and the smallest photo from NASA Open API: https://api.nasa.gov/

## How to Run

* Clone the repository to your computer
* Open project in your IDE and configure JDK 17 for the project
* Make sure PostgreSQL is installed on your computer
* Run class `MarsPicturesDownloaderApplication`
* Open your browser and go to `http://localhost:8080`

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot)
* [PostgreSQL](https://www.postgresql.org/)
* [Flyway](https://flywaydb.org/) - Database-migration tool
* [Gradle](https://gradle.org/) - Dependency management
* [Testcontainers](https://www.testcontainers.org/) & [REST Assured](https://rest-assured.io/) - For integration tests