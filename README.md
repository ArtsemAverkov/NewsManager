Система управления новостями
Данный документ предоставляет описание проекта "Система управления новостями" и его интерфейсов.

Стэк
Используемые технологии и инструменты в проекте:

Spring Boot 3.x
Java 17
Gradle
PostgreSQL
Spring Security
Spring Cloud Config
WireMock
Docker
spring-cloud-feign-client
testcontainers
Структура проекта
Проект имеет следующую структуру:

src: Исходный код проекта
docs: Документация
docker: Файлы Docker для контейнеризации приложения и базы данных
Запуск проекта
Для запуска проекта выполните следующие шаги:

Склонируйте репозиторий с проектом:
bash
Copy code
$ git clone https://github.com/ArtsemAverkov/NewsManager.git
Перейдите в директорию проекта:
bash
Copy code
$ cd project
Соберите проект с помощью Gradle:
bash
Copy code
$ gradle build
Запустите проект с помощью Docker Compose:
bash
Copy code
$ docker-compose up
Проверьте работу проекта, открыв веб-браузер и перейдя по адресу http://localhost:8088/news.


Использование
Вы можете запустить прект выбрав один из профилей Prod или Test 
для этого в 
services:
newsManager:
build:
context: services/newsManager
dockerfile: Dockerfile
image: 'news-manager:news'
environment:
- SPRING_APPLICATION_NAME=news-api
- SPRING_PROFILES_ACTIVE=prod <- здесь укажите Prod или Test

Главное их отличие это кэш Prod использут для кэширования Redis
а профиль test использует LRU/LFU кэш.

если вы выбрали @Prod то environment будет выглядеть примерно так
environment:
- SPRING_APPLICATION_NAME=news-api
- SPRING_PROFILES_ACTIVE=prod
- SPRING_CONFIG_IMPORT=optional:configserver:http://settingApiManager:8086
- SPRING_DATA_REDIS_HOST=redis
- SPRING_DATA_REDIS_PORT=6379
- SPRING_DATASOURCE_URL=jdbc:postgresql://postgres/root
- USER_MANAGER_URL=http://userManager:8084/users

если вы выбрали @Test то environment будет выглядеть примерно так
environment:
- SPRING_APPLICATION_NAME=news-api
- SPRING_PROFILES_ACTIVE=test
- SPRING_CONFIG_IMPORT=optional:configserver:http://settingApiManager:8086
- SPRING_CACHE_ALGORITHM=LRU
- SPRING_CACHE_MAX-SIZE=100
- SPRING_DATASOURCE_URL=jdbc:postgresql://postgres/root
- USER_MANAGER_URL=http://userManager:8084/users

После установки вы можете использовать проект для следующих целей:

Просмотр всех новостей по адресу http://localhost:8088/news
Если вы хотите посмотреть новость с ее комментариями, введите адрес http://localhost:8088/news/{id}, где {id} - это номер новости. Однако, вас попросят войти. Если вы зарегистрированы, введите свое имя и пароль. Если нет, то зарегистрируйтесь. Для регистрации отправьте POST запрос по адресу http://localhost:8088/users/create с данными в формате JSON, например:
json
Copy code
{
"username": "test",
"password": "test",
"role": "SUBSCRIBER"
}
Где role - это роль, с которой вы регистрируетесь. Для вас доступно две роли: SUBSCRIBER, с которой вы можете просматривать новости, комментарии, а также оставлять и удалять только свои комментарии, и JOURNALIST, которая дает возможность делать то же самое, но еще и добавлять и удалять только свои новости.

Добавление новости осуществляется POST запросом по адресу http://localhost:8088/news. Отправьте запрос в виде JSON, например:
json
Copy code
{
"title": "Same Title",
"text": "Same Text"
}
Удаление новости производится DELETE запросом по адресу http://localhost:8088/news/{id}, где {id} - это id новости, которую вы оставили. Обратите внимание, что только автор новости может удалить ее.

Обновление новости производится PATCH запросом по адресу http://localhost:8088/news/{id}, где {id} - это id новости, которую вы хотите обновить. Отправьте запрос с данными в формате JSON, например:

json
Copy code
{
"title": "Same Title",
"text": "Same Text"
}
Поиск новостей по содержанию или дате осуществляется GET запросом по адресу http://localhost:8088/news/search?data=2023-06-30 или http://localhost:8088/news/search?query=sameText. Результат будет представлен в виде списка новостей.

Добавление комментария осуществляется POST запросом по адресу http://localhost:8088/comment. Отправьте запрос в виде JSON, например:

json
Copy code
{
"newsId": 1,
"text": "Same Text"
}
Где newsId - это id новости, в которой вы хотите оставить комментарий.

Удаление комментария производится DELETE запросом по адресу http://localhost:8088/comment/{id}, где {id} - это id комментария, который вы оставили.

Обновление комментария производится PATCH запросом по адресу http://localhost:8088/comment/{id}, где {id} - это id комментария, который вы хотите обновить. Отправьте запрос с данными в формате JSON, например:

json
Copy code
{
"text": "Same Text"
}
Поиск комментариев по содержанию или дате осуществляется GET запросом по адресу http://localhost:8088/comment/search?data=2023-06-30 или http://localhost:8088/comment/search?query=sameText. Результат будет представлен в виде комментариев и новостей, к которым они относятся.
Заключение
По всем дополнительным вопросам или замечаниям вы можете связаться по адресу https://www.linkedin.com/in/артем-аверков-aa7663239/.

Спасибо за внимание!