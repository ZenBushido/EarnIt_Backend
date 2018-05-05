--Account
TRUNCATE TABLE "public"."accounts" CASCADE;

insert into "public"."accounts" ( "id", "create_date", "account_code") values ( 1, '2017-07-04 10:41:44', '4PDF5GS');

--Parents
--TRUNCATE TABLE "public"."parents";

insert into "public"."parents" ( "id", "first_name", "password", "avatar", "phone", "last_name", "create_date", "email", "update_date", "account_id") values ( 1, 'John', 'test123', 'http://images4.fanpop.com/image/photos/18200000/Ted-Mosby-ted-mosby-18275746-325-400.jpg', '98981212', 'Doe ', '2017-07-04 11:49:27', 'johndoe@gmail.com ', '2017-07-04 11:49:29', '1');
insert into "public"."parents" ( "id", "first_name", "password", "avatar", "phone", "last_name", "create_date", "email", "update_date", "account_id") values ( 2, 'Mary', 'test123', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQj0baWDQjyngmXhPxkkrPFdogL2ifSYxQqudySxr2NBRmF4xc', '98981213', 'Doe', '2017-07-04 11:51:00', 'marydoe@gmail.com ', '2017-07-04 11:51:03', '1');

--Child
--TRUNCATE TABLE "public"."childrens" ;

insert into "public"."childrens" ( "id", "first_name", "password", "avatar", "phone", "last_name", "create_date", "email", "update_date", "message", "account_id") values ( 1, 'Billy', 'test123', 'https://vignette3.wikia.nocookie.net/himym/images/d/d3/DavidHenrie.jpg/revision/latest?cb=20110603135946', '98981214', 'Doe', '2017-07-04 12:17:34', 'billydoe@gmail.com', '2017-07-04 12:17:38', null, '1');
insert into "public"."childrens" ( "id", "first_name", "password", "avatar", "phone", "last_name", "create_date", "email", "update_date", "message", "account_id") values ( 2, 'Ginnie', 'test123', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDmIC0gfEHkTQR0Y3v6AQxSmITzq9DFWQe-4qPVvtx0V-2nDnW', '98981215', 'Doe', '2017-07-04 12:19:50', 'ginniedoe@gmail.com', '2017-07-04 12:19:52', null, '1');

--Goal
--TRUNCATE TABLE  "public"."goals" ;

insert into "public"."goals" ( "id", "amount", "create_date", "update_date", "child_id", "name") values ( 1, '5000', '2017-07-04 12:21:22', '2017-07-04 12:21:24', '1', 'Trip to Disney Land');

-- repitation schedules
TRUNCATE TABLE  "public"."repitition_schedule" CASCADE;

insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 1, '2017-07-04 13:02:07', 'Weekly');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 2, '2017-10-11 13:15:20', 'Bi-Weekly');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 3, '2017-11-14 13:16:01', 'Monthly');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 4, '2017-11-09 13:16:16', 'Weekly');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 5, '2017-07-04 13:02:07', 'Bi-weekly');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 6, '2017-07-04 13:02:07', 'Monthly');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 7, '2017-07-04 13:02:07', 'Once');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 8, '2017-07-04 13:02:07', 'Once');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 9, '2017-07-04 13:02:07', 'Weekly');
insert into "public"."repitition_schedule" ( "id", "expiry_date", "repeat") values ( 10, '2017-07-04 13:02:07', 'Monthly');

-- Tasks
--TRUNCATE TABLE "public"."tasks";

insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 1, 'due', '50', '2017-07-17 13:00:15', '1', '2017-07-04 13:01:28', '10', '2017-07-04 13:01:30', '2', 'Buy pet supplies', '0');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 2, 'due', '50', '2017-07-17 13:00:15', '1', '2017-07-04 13:01:28', '9', '2017-07-04 13:01:30', '2', 'Weekly house cleaning', '1');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 3, 'due', '40', '2017-07-12 13:00:15', '1', '2017-07-04 13:01:28', '8', '2017-07-04 13:01:30', '2', 'Laundry', '0');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 4, 'due', '100', '2017-07-19 13:00:15', '1', '2017-07-04 13:01:28', '7', '2017-07-04 13:01:30', '2', 'Visit Grandma', '1');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 5, 'due', '5', '2017-07-20 13:00:15', '1', '2017-07-04 13:01:28', '6', '2017-07-04 13:01:30', '2', 'Bathe the dog', '0');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 6, 'due', '100', '2017-07-10 13:00:15', '1', '2017-07-04 13:01:28', '5', '2017-07-04 13:01:30', '1', 'Paint the fence', '0');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 7, 'due', '50', '2017-07-29 13:00:15', '1', '2017-07-04 13:01:28', '4', '2017-07-04 13:01:30', '1', 'Clear the lawn', '0');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 8, 'due', '10', '2017-07-19 13:00:15', '1', '2017-07-04 13:01:28', '3', '2017-07-04 13:01:30', '1', 'Clean the Car', '1');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 9, 'due', '30', '2017-07-09 13:00:15', '1', '2017-07-04 13:01:28', '2', '2017-07-04 13:01:30', '1', 'Clean the garage.', '0');
insert into "public"."tasks" ( "id", "status", "allowance", "due_date", "goal_id", "create_date", "repetition_schedule_id", "update_date", "child_id", "name", "picture_required") values ( 10, 'due', '20', '2017-07-09 13:00:15', '1', '2017-07-04 13:01:28', '1', '2017-07-04 13:01:30', '1', 'Take out trash', '0');
