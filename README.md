# CRUD-приложение с логированием через AOP и интеграцией с Kafka

## Содержание

- [Описание](#описание)
- [Функциональность](#функциональность)
    - [CRUD API для задач (Task)](#crud-api-для-задач-task)
    - [Kafka-интеграция](#kafka-интеграция)
    - [Логирование через аспекты (AOP)](#логирование-через-аспекты-aop)
    - [Настройка службы уведомлений (NotificationService)](#настройка-службы-уведомлений-notificationservice)
- [Сборка и запуск](#сборка-и-запуск)

## Описание

Данный проект представляет собой CRUD-сервис для управления задачами (Task), который использует Spring Boot, Kafka и
логирование через AOP (Aspect-Oriented Programming).

При обновлении задачи отправляется сообщение в Kafka, а затем Consumer передаёт его в сервис уведомлений (
NotificationService), который отправляет e-mail c сообщением об обновленной задачи.

## Функциональность

#### CRUD API для задач (Task):

- **POST /tasks** — создание задачи
- **GET /tasks/{id}** — получение задачи по ID
- **PUT /tasks/{id}** — обновление задачи
- **DELETE /tasks/{id}** — удаление задачи
- **GET /tasks** — получение списка всех задач

```json
{
  "title": "test title",
  "description": "test description",
  "status": "open",
  "user_id": 1
}
 ```

#### Kafka-интеграция:

- Producer при PUT запросе отправляет обновления статуса задачи в Kafka-топик
- Consumer слушает топик и передаёт изменения в NotificationService
- NotificationService отправляет e-mail с изменением статуса задачи

#### Логирование через аспекты (AOP):

- @Before — перед выполнением метода
- @AfterReturning — после успешного выполнения метода
- @AfterThrowing — при выбрасывании исключения
- @Around — замер времени выполнения метода

#### Настройка службы уведомлений (NotificationService):

Для корректной работы службы уведомлений (NotificationService) необходимо настроить параметры SMTP-сервера в файле
.env. Эти параметры обеспечивают возможность отправки электронных писем через указанный почтовый сервер. В файле .env
должны быть заданы следующие переменные:

- MAIL_HOST — адрес SMTP-сервера.
- MAIL_PORT — порт SMTP-сервера.
- MAIL_PROTOCOL — используемый протокол (обычно smtp).
- MAIL_USERNAME — имя пользователя для аутентификации на SMTP-сервере.
- MAIL_PASSWORD — пароль для аутентификации на SMTP-сервере.
- MAIL_TO — адрес электронной почты, на который будут отправляться уведомления (если их несколько, можно указать
  через пробел).

## Сборка и запуск

Запуск с Docker Compose:

```bash
docker-compose up -d --build
```  