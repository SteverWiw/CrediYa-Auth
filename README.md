Proyecto/
├── domain/
│   ├── model/           ← Entidades y objetos de valor
│   └── usecase/         ← Lógica de negocio pura
├── application/         ← Servicios, mappers y orquestación
├── infrastructure/      ← Adaptadores y repositorios
│   ├── entrypoints/     ← Routers y handlers
│   └── driven-adapters/ ← Adaptadores de BD y loggers
├── helpers/             ← Utilidades y excepciones compartidas

Tecnologías

Java 17+
Spring Boot 3
Spring WebFlux (reactivo)
Spring Data R2DBC
PostgreSQL
Lombok
SLF4J + Logback
Springdoc OpenAPI / Swagger
JUnit / Reactor Test

Configuración

Configura la base de datos PostgreSQL y las credenciales en application.yml:

spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/crediya
    username: your_user
    password: your_password


Variables opcionales:

logging:
  level:
    root: INFO

Endpoints
Método	Ruta	                    Descripción	            Request	            Response
POST	/crediYa/api/v1/usuarios	Crear un nuevo usuario	UsuarioRequestDTO	UsuarioResponseDTO o ErrorResponse


Swagger UI disponible en: http://localhost:8080/swagger-ui/index.html
OpenAPI JSON en: http://localhost:8080/v3/api-docs




Ejecución
./gradlew bootRun


Para construir:

./gradlew clean build