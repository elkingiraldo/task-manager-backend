CREATE TABLE USER_TBL (
	id SERIAL PRIMARY KEY,
    user_name VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL
);

INSERT INTO USER_TBL (user_name, password)
VALUES ('elkingiraldo91', 'elkinpassword');



