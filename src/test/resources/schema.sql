CREATE TABLE IF NOT EXISTS tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NOT NULL,
    user_id     BIGSERIAL
);

-- Вставка тестовых данных в таблицу tasks
INSERT INTO tasks (title, description, status, user_id)
VALUES ('Task 1', 'Description 1', 'Status 1', 1),
       ('Task 2', 'Description 2', 'Status 2', 2),
       ('Task 3', 'Description 3', 'Status 3', 3),
       ('Task 4', 'Description 4', 'Status 4', 4),
       ('Task 5', 'Description 5', 'Status 5', 5);