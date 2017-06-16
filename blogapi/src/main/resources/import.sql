--this script initiates db for h2 db (used in test profile)
-- Test users
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'brian@domain.com', 'Brian')
insert into user (id, account_status, email, first_name) values (null, 'REMOVED', 'edward@domain.com', 'Edward')
insert into user (id, account_status, email, first_name) values (null, 'CONFIRMED', 'joanna@domain.com', 'Joanna')
insert into user (id, account_status, email, first_name) values (null, 'CONFIRMED', 'henry@domain.com', 'Henry')

-- Test posts
insert into blog_post (id, entry, user_id) values (null, 'Test post', 1);