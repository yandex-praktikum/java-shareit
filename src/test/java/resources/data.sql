DELETE FROM PUBLIC.USERS;

INSERT INTO PUBLIC.USERS(NAME, EMAIL)
VALUES ('user1', 'user1@mail.ru');
INSERT INTO PUBLIC.USERS(NAME, EMAIL)
VALUES ('user2', 'user2@mail.ru');
INSERT INTO PUBLIC.USERS(NAME, EMAIL)
VALUES ('user3', 'user3@mail.ru');

INSERT INTO PUBLIC.ITEMS(name, description, is_available, owner_id, request_id)
VALUES('item1', 'description1', true, 1, null);
INSERT INTO PUBLIC.ITEMS(name, description, is_available, owner_id, request_id)
VALUES('item2', 'description2', true, 1, null);
INSERT INTO PUBLIC.ITEMS(name, description, is_available, owner_id, request_id)
VALUES('item3', 'description3', false, 1, null);
INSERT INTO PUBLIC.ITEMS(name, description, is_available, owner_id, request_id)
VALUES('item4', 'description4', true, 2, null);

INSERT INTO PUBLIC.BOOKINGS(start_date, end_date, item_id, booker_id, status)
VALUES ('2022-08-01T12:15','2022-08-10T12:15', 1, 2, 'APPROVED' );
INSERT INTO PUBLIC.BOOKINGS(start_date, end_date, item_id, booker_id, status)
VALUES ('2023-08-01T12:15','2023-08-10T12:15', 1, 2, 'WAITING' );
INSERT INTO PUBLIC.BOOKINGS(start_date, end_date, item_id, booker_id, status)
VALUES ('2022-08-01T12:15','2023-08-10T12:15', 1, 3, 'APPROVED' );

INSERT INTO PUBLIC.REQUESTS(description, requestor_id, created)
VALUES ('itemRequest1', 3, '2022-08-01T12:15');