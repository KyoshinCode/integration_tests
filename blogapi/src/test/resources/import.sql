--this script initiates db for integration tests 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'janush@domain.com', 'Janush')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'test@domain.com', 'Test', 'Testowy')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'abc@domain.com', 'abc', 'abcd')
insert into user (id, account_status, email, first_name, last_name) values (null, 'REMOVED', 'cba@domain.com', 'cba', 'dcba') 