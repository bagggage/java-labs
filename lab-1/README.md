# Spring Boot REST API

Git links service REST API.

## Description
This application helps to store your git repositories.

Includes:
- Spring JPA
- Spring Boot Web
- PostgreSQL Driver

## Api
```host/api/...```

## Users
- ```/users```

#### GET
- ```/{username}``` - get user by username
- ```?id=...``` - get user by id

#### POST
- ```/{username}/add``` - add new user.
(JSON Object)
```
{
    "name":     "...",
    "username": "...",
    "email":    "..."
}
```
- ```/{username}/link?service=...&username=...``` - connect third-party git service. 

#### PATCH
- ```/{username}``` - update user's username, name or email. Uses request body as argument.
(JSON Object)
```
{
    "name":     "...",
    "username": "...",
    "email":    "..."
}
```

#### DELETE
- ```/{username}``` - delete user

## Repos
- ```/repos```

#### GET
- ```?name...``` = get repository by name

#### PATCH
- ```/{name}/contribute?username=...``` - add contributor to repository