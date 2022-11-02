## Register new user
```shell script
curl -X POST -H 'Content-Type: application/json' -d @register-data.json http://localhost:8080/user/register
```
or
```shell script
curl  --request POST 'http://localhost:8080/user/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName":"Alice",
    "lastName":"Doe",
    "username":"alice",
    "email":"alice@example.org"
}'
```

## Login using username and password
```shell script
curl -v -X POST -H 'Content-Type: application/json' -d @login-data.json http://localhost:8080/user/login
```
or
```shell script
curl -v --request POST 'http://localhost:8080/user/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"alice",
    "password":"<password-value>"
}'
```

## Add new user
```shell script
curl --request POST 'http://localhost:8080/user/add' \
--header 'Authorization: Bearer JWT-value-of-current-user-goes-here' \
--form 'firstName="Billy"' \
--form 'lastName="Bones"' \
--form 'username="bill"' \
--form 'email="bill@example.org"' \
--form 'role="ROLE_SUPER_ADMIN"' \
--form 'notLocked="true"' \
--form 'active="true"'
```

## Update user
```shell script
curl --request PUT 'http://localhost:8080/user/update' \
--header 'Authorization: Bearer JWT-value-of-current-user-goes-here' \
--form 'firstName="Billy"' \
--form 'lastName="Bones2"' \
--form 'username="bill"' \
--form 'email="bill@example.org"' \
--form 'role="ROLE_SUPER_ADMIN"' \
--form 'notLocked="true"' \
--form 'active="true"' \
--form 'currentUsername="bill"'
```

## Delete user
```shell script
curl --request DELETE 'http://localhost:8080/user/delete/123' \
--header 'Authorization: Bearer JWT-value-of-current-user-goes-here'
```
