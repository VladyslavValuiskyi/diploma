INSERT INTO users (id,username, password, enabled) VALUES (1,'admin', '0606ea2095680b98e337557acf74f104', TRUE); --admin
INSERT INTO users (id, username, password, enabled) VALUES (2,'user@user.user', 'd9240cbb574f950e82c90d6dc814f1d7', TRUE); --user
INSERT INTO authorities (user_id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (2, 'ROLE_USER');