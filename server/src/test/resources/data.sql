INSERT INTO users (id, name, email)
VALUES (10, 'user1', 'user1@somemail.ru'),
       (20, 'user2', 'user2@somemail.ru'),
       (30, 'user3', 'user3@somemail.ru'),
       (40, 'user4', 'user4@somemail.ru'),
       (50, 'user5', 'user5@somemail.ru'),
       (60, 'user6', 'user6@somemail.ru');

INSERT INTO requests (id, description, requestor_id, created)
VALUES (10, 'description1', 10, null),
       (20, 'description2', 20, null),
       (30, 'description3', 30, null),
       (40, 'description4', 40, null),
       (50, 'description5', 50, null);

INSERT INTO items (id, name, description, is_available, owner_id, request_id)
VALUES (10, 'item1', 'description1', 'true', 10, 10),
       (20, 'item2', 'description2', 'true', 10, 20),
       (30, 'item3', 'description3', 'true', 30, 30),
       (40, 'item4', 'description4', 'true', 40, 40),
       (50, 'item5', 'description5', 'false', 50, 50);

INSERT INTO bookings (id, start_date, end_date, item_id, booker_id, status)
VALUES (10, TIMESTAMP '2024-12-11 10:10:10', TIMESTAMP '2024-12-11 11:11:11', 10, 20, 'APPROVED'),
       (20, CAST(CURRENT_DATE AS TIMESTAMP) + INTERVAL '1' DAY, CAST(CURRENT_DATE AS TIMESTAMP) + INTERVAL '2' DAY, 40, 40, 'APPROVED'),
       (30, null, null, 30, 20, 'WAITING'),
       (40, null, null, 40, 20, 'WAITING'),
       (50, TIMESTAMP '2024-12-11 10:10:10', TIMESTAMP '2024-12-11 11:11:11', 50, 20, 'APPROVED'),
       (60, TIMESTAMP '2024-12-11 10:10:10', TIMESTAMP '2024-12-11 11:11:11', 10, 20, 'REJECTED'),
       (70, TIMESTAMP '2024-12-11 10:10:10', TIMESTAMP '2024-12-11 11:11:11', 10, 20, 'WAITING');

INSERT INTO comments (id, text, item_id, author_id, created)
VALUES (10, 'comment1', 10, 20, null),
       (20, 'comment1', 10, 10, null),
       (30, 'comment3', 10, 30, null),
       (40, 'comment4', 40, 50, null),
       (50, 'comment5', 50, 40, null);