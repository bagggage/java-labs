# Spring Boot REST API

Git links service REST API.

Includes:
- Spring JPA
- Spring Boot Web
- PostgreSQL Driver

## Api
    "host/api/..."

### Users
- /users

#### GET
- /{username} - get user by username
- ?id=... - get user by id

#### DELETE
- {username}/remove

### Repos
- /repos