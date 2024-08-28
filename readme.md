Это приложение для реализации аутентификации и авторизации с использованием Spring Security и JWT.

Используемые технологии: Spring Boot, Spring Data Jpa, Spring Security, liquebase, PostgreSQL, JWT.

Для запуска приложения необходимо создать базу данных с именем SpringSecurity_jwt в PostgreSQL.

Для старта приложения необходимо запустить TaskSpringSecurityJwtApplication.

В приложении имеется краткая документация с использованием Swagger.
Информация доступа по адресу: http://localhost:8080/swagger-ui/index.html. 

Для создания в БД пользователя User с правами USER необходимо выполнить POST-запрос на адрес http://localhost:8080/api/auth/registration с примером тела запроса:
{
"name": "User", 
"password": "1", 
"email": "user@user.com", 
"role": "USER"
}

Для создания в БД пользователя Admin с правами ADMIN необходимо выполнить POST-запрос на адрес http://localhost:8080/api/auth/registration с примером тела запроса:
{
"name": "Admin",
"password": "1",
"email": "admin@admin.com",
"role": "ADMIN"
}