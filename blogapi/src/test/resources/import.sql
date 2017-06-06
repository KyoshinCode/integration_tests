--this script initiates db for integration tests 
-- For UserRepositoryTest
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
-- For LikePostRepository
--insert into user (id, account_status, email, first_name, last_name) values (8, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
--insert into blog_post (id, entry, user_id) values (3, 'blog post entry', 8)
--insert into like_post (id, post_id, user_id) values (null, 3, 8)