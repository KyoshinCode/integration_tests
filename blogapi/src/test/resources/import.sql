--this script initiates db for integration tests 
--insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward') 
insert into user (id, account_status, email, first_name) values (1, 'NEW', 'newuser@domain.com', 'Emily')
insert into user (id, account_status, email, first_name) values (2, 'CONFIRMED', 'confuser@domain.com', 'Johny')