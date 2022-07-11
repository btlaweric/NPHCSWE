# Required
## JDK
`18.0.1.1`
## Maven
`3.8.5`
# Build
`./mvnw clean install`
# Run
`./mvnw spring-boot:run`
# Test
`./mvnw test`
# Default localhost path
`localhost:8080`
# H2 Database
## Console
`http://localhost:8080/h2-console`
## Credentials
| User  | Password  | 
| :------------ |:---------------:|
| sa      | &nbsp; |
# Added Features
+ `GET /users` Added **sortBy** as query parameter.

| Parameter  | Type | Value | Default Value
| ------------- | ------------- |------------- |------------- |
| sortBy  | String  | COLUMN_NAME  | id

# Assumptions
+ ` GET /users`
  + If no query parameters is provided, use default parameters. 
  + Will only return an error if query parameters are invalid.
  + if **sortBy** is invalid COLUMN_NAME, will default to **id**.
+ `PUT/PATCH /users`
  + Will only process if it is a valid JSON User object, no partials allowed.
+ CSV file must be in UTF-8 Encoding.
+ No file size limit.

# Design Decisions
+ `POST /users/upload`  Due to following behaviours below, **200** will never be returned , only **201** and **400**.
  + Will not process csv file is empty, will return an error.
  + Does not check if old data is the same as new data, will just update.
+ Had to create a User JSON object in order to parse JSON errors.
+ In the event where **sortBy** is given an invalid value, **id** will be the default sort value.