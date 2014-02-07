# Terra Traveler schema
 
# --- !Ups

insert into role (role) values ('ORG');
insert into role (role) values ('BIZ');
insert into role (role) values ('NORM');
insert into role (role) values ('INVITEE');

insert into status(id, status) values (1, 'DEFAULT');
insert into status(id, status) values (2, 'NO_CONTACT');
insert into status(id, status) values (3, 'INVITED');
insert into status(id, status) values (4, 'VISITOR');
insert into status(id, status) values (6, 'CONVERSION');
insert into status(id, status) values (7, 'PLANNING');
insert into status(id, status) values (8, 'ONGOING');
insert into status(id, status) values (9, 'ENDED');

insert into third_party (id, name) values (1, 'Google');
insert into third_party (id, name) values (2, 'FourSquare');

insert into activity_category(id, category) values(1, 'Fitness');
insert into activity_category(id, category) values(2, 'Outdoors');
insert into activity_category(id, category) values(3, 'Nightlife');
insert into activity_category(id, category) values(4, 'Eating');
insert into activity_category(id, category) values(5, 'Pets');
insert into activity_category(id, category) values(6, 'Kids');
insert into activity_category(id, category) values(7, 'Sports');
insert into activity_category(id, category) values(8, 'Entertainment');
insert into activity_category(id, category) values(9, 'Tech');
insert into activity_category(id, category) values(10, 'Transportation');
insert into activity_category(id, category) values(11, 'Housing share');
insert into activity_category(id, category) values(12, 'Maker');
insert into activity_category(id, category) values(13, 'Performance');
insert into activity_category(id, category) values(14, 'Hangout');
insert into activity_category(id, category) values(15, 'Cultural');
insert into activity_category(id, category) values(16, 'Traveler');
insert into activity_category(id, category) values(17, 'Activism');
insert into activity_category(id, category) values(18, 'Shop');
insert into activity_category(id, category) values(19, 'Professional');
insert into activity_category(id, category) values(20, 'Create');
insert into activity_category(id, category) values(21, 'After work');

insert into activity_type (id, type) values (1, 'Running');
insert into activity_type (id, type) values (2, 'Gym');
insert into activity_type (id, type) values (3, 'Walk');
insert into activity_type (id, type) values (4, 'Clubbing');
insert into activity_type (id, type) values (5, 'Cafe');
insert into activity_type (id, type) values (6, 'Dog walk');
insert into activity_type (id, type) values (7, 'Dog park');
insert into activity_type (id, type) values (8, 'Child park');
insert into activity_type (id, type) values (9, 'Site-see');
insert into activity_type (id, type) values (10, 'Drinks');
insert into activity_type (id, type) values (11, 'Lunch');
insert into activity_type (id, type) values (12, 'Dinner');
insert into activity_type (id, type) values (13, 'Breakfast');
insert into activity_type (id, type) values (14, 'Theater');
insert into activity_type (id, type) values (15, 'Attend Game');
insert into activity_type (id, type) values (16, 'Play game sport court or field');
insert into activity_type (id, type) values (17, 'Museum or Gallery');
insert into activity_type (id, type) values (18, 'Shopping');
insert into activity_type (id, type) values (19, 'Bicycling');
insert into activity_type (id, type) values (20, 'Dancing');
insert into activity_type (id, type) values (21, 'Movie');
insert into activity_type (id, type) values (22, 'Concert');
insert into activity_type (id, type) values (23, 'Event');
insert into activity_type (id, type) values (24, 'Ride-share');
insert into activity_type (id, type) values (26, 'Hike');
insert into activity_type (id, type) values (27, 'Picnic');
insert into activity_type (id, type) values (28, 'Park');
insert into activity_type (id, type) values (29, 'Night Sky');
insert into activity_type (id, type) values (30, 'Games board or computer');
insert into activity_type (id, type) values (31, 'Hacking');
insert into activity_type (id, type) values (32, 'Comedy');
insert into activity_type (id, type) values (33, 'Apartment');
insert into activity_type (id, type) values (34, 'Art Photography');
insert into activity_type (id, type) values (35, 'Mall');
insert into activity_type (id, type) values (37, 'Beach');
insert into activity_type (id, type) values (39, 'Live music');
insert into activity_type (id, type) values (40, 'Demonstration');
insert into activity_type (id, type) values (41, 'Rally');

# --- !Downs

delete from "role";
delete from activity_type;
delete from activity_category;
delete from status;

