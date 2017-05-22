--this script initiates db for integration tests 
insert into user (id, account_status, email, first_name, last_name) values (10, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
INSERT INTO blog_post (id, entry, user_id) VALUES (20, 'test', 10)
INSERT INTO like_post (id, post_id, user_id) VALUES (null, 20, 10)