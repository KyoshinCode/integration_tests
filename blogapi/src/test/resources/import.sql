--this script initiates db for integration tests 
--insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward') 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'adam@domain.com', 'Adam', 'Steward')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'paul@domain.com', 'Paul', 'Steward')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Stevens')
insert into blog_post (id, entry, user_id) values (12, 'First post', 1)
--insert into like_post (id, post_id, user_id) values (13, 12, 1);