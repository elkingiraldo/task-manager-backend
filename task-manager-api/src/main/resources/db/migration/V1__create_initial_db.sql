CREATE TABLE USER_TBL (
	id SERIAL PRIMARY KEY,
    user_name VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL
);

CREATE TABLE TASK_TBL (
	id SERIAL PRIMARY KEY,
    description VARCHAR(256) NOT NULL,
    edc DATE NOT NULL,
    user_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL
);

INSERT INTO USER_TBL (user_name, password) VALUES ('elkingiraldo91', 'elkinpassword');
INSERT INTO USER_TBL (user_name, password) VALUES ('jairo53', 'jairopwd');
INSERT INTO USER_TBL (user_name, password) VALUES ('martha58', 'marthapwd');

INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing', DATE '2021-05-16', 1, 'PENDING');
INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing', DATE '2019-05-16', 1, 'DONE');

