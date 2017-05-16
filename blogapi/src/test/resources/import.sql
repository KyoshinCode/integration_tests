--this script initiates db for integration tests 
insert into user (id, account_status, email, first_name, last_name) values (15, 'CONFIRMED', 'john@domain.com', 'John', 'Steward') 
insert into blog_post  (id, entry, user_id)  values (30, 'cokolwiek', 15)
insert into like_post  (id, post_id, user_id)  values (null, 30, 15)