DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
     user_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL,
     name VARCHAR(255) NOT NULL,
     email VARCHAR(512) NOT NULL,
     CONSTRAINT pk_user PRIMARY KEY (user_id),
     CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE items (
     item_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL,
     owner_id BIGINT NOT NULL,
     name VARCHAR(512) NOT NULL,
     description VARCHAR(512) NOT NULL,
     available BOOLEAN NOT NULL,
     request_id BIGINT,
     CONSTRAINT pk_item PRIMARY KEY (item_id),
     CONSTRAINT items_fk FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE
);

/*CREATE TYPE BOOKING_STATUS AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');*/

CREATE TABLE bookings (
     booking_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL,
     item_id BIGINT NOT NULL,
     booker_id BIGINT NOT NULL,
     start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     status INTEGER NOT NULL,
     CONSTRAINT pk_booking PRIMARY KEY (booking_id),
     CONSTRAINT bookings_items_fk FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE,
     CONSTRAINT bookings_users_fk FOREIGN KEY (booker_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE comments (
      comment_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL,
      comment_text VARCHAR(512) NOT NULL,
      item_id BIGINT NOT NULL,
      author_id BIGINT NOT NULL,
      created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      CONSTRAINT pk_comment PRIMARY KEY (comment_id),
      CONSTRAINT comments_items_fk FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE,
      CONSTRAINT comments_users_fk FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE requests (
      request_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL,
      request_author_id BIGINT NOT NULL,
      description VARCHAR(512) NOT NULL,
      created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      CONSTRAINT pk_request PRIMARY KEY (request_id),
      CONSTRAINT requests_users_fk FOREIGN KEY (request_author_id) REFERENCES users(user_id) ON DELETE CASCADE
);
