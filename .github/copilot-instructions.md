principle :
- begin the answer with "I have read the instructions file"



context information :
- I use clean architecture with folder's structure:
  
        note(it's just a part of my files in a service, I only put 1 file in a folder, but in reality, there are many files in a folder)
      ├── application
      │   ├── client (adapter to impl)
      │   │   ├── KeycloakClient.java
      │   ├── command (application dto)
      │   │   ├── KeycloakTokenCommand.java
      │   ├── mapper (for adapter impl)
      │   │   └── KeycloakMapper.java
      │   ├── repository (for adapter impl)
      │   │   └── AuthTokenRepository.java
      │   ├── service (impl usecase)
      │   │   ├── LoginService.java
      │   └── usecase(interface for service)
      │       ├── LoginUseCase.java
      ├── domain
      │   ├── exception
      │   │   ├── ConflictException.java
      │   └── model
      │       └── AuthToken.java
      ├── infrastructure
      │   ├── config
      │   ├── exception
      │   │   └── GlobalExceptionHandler.java
      │   ├── mapper
      │   │   ├── adapter
      │   │   │   ├── AuthMapperAdapter.java
      │   │   │   └── KeycloakMapperAdapter.java
      │   │   └── mapstruct
      │   │       ├── AuthMapperMapstruct.java (mapstruct)
      │   │       └── KeycloakMapperMapstruct.java
      │   └── web
      │       ├── AuthController.java
      │       ├── data
      │       │   ├── adapter
      │       │   │   ├── AuthMapperAdapter.java (impl mapper)
      │       │   │   └── AuthTokenRepositoryAdapter.java (impl repo)
      │       │   └── springdata
      │       │       └── SpringDataAuthTokenRepository.java (extend jpa repo)
      │       ├── dto
      │       │   ├── LoginRequest.java
      │       │   ├── MetaDto.java
      │       │   ├── PaginationDto.java
      │       │   ├── ResponseDto.java
      │       │   └── TokenResponse.java
      │       └── entity
      │           └── AuthTokenEntity.java
      ├── resources
      │   ├── application-prod.yaml
      │   ├── application.yaml
      │   ├── db
      │   │   └── migration
      │   │       └── V1__init_auth_schema.sql
      │   ├── static
      │   └── templates
      ├── test
      │   └── java
      │       └── org
      │           └── example
      │               └── authservice
      │                   ├── AuthServiceApplicationTests.java
      │                   ├── application
      │                   │   └── LoginServiceUnitTest.java
      │                   └── 



