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

INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing in future', DATE '2021-05-16', 1, 'PENDING');
INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing 2016', DATE '2016-05-16', 1, 'PENDING');
INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing in past', DATE '2019-08-16', 1, 'DONE');
INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing change state', DATE '2018-05-16', 1, 'PENDING');

INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing in future', DATE '2012-05-16', 2, 'DONE');
INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing 2016', DATE '2016-05-16', 2, 'DONE');

INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing in future', DATE '2025-05-16', 3, 'PENDING');
INSERT INTO TASK_TBL (description, edc, user_id, status) VALUES ('I need to do somenthing 2016', DATE '2021-05-16', 3, 'PENDING');
