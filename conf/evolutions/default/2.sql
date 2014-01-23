# Terra Traveler schema
 
# --- !Ups

insert into role (role) values ('BIZ');
insert into role (role) values ('NORM');

insert into activity_type (id, activity) values (1, 'Running');
insert into activity_type (id, activity) values (2, 'Gym');
insert into activity_type (id, activity) values (3, 'Walk');
insert into activity_type (id, activity) values (4, 'Clubbing');
insert into activity_type (id, activity) values (5, 'Cafe');
insert into activity_type (id, activity) values (6, 'Dog walk');
insert into activity_type (id, activity) values (7, 'Dog park');
insert into activity_type (id, activity) values (8, 'Child park');
insert into activity_type (id, activity) values (9, 'Site-see');
insert into activity_type (id, activity) values (10, 'Drinks');
insert into activity_type (id, activity) values (11, 'Lunch');
insert into activity_type (id, activity) values (12, 'Dinner');
insert into activity_type (id, activity) values (13, 'Theater');
insert into activity_type (id, activity) values (14, 'Breakfast');
insert into activity_type (id, activity) values (15, 'Attend Game');
insert into activity_type (id, activity) values (16, 'Play game, sport, court or field');
insert into activity_type (id, activity) values (17, 'Museum');
insert into activity_type (id, activity) values (18, 'Shopping');
insert into activity_type (id, activity) values (19, 'Bicycling');
insert into activity_type (id, activity) values (20, 'Dancing');
insert into activity_type (id, activity) values (21, 'Movie');
insert into activity_type (id, activity) values (22, 'Concert');
insert into activity_type (id, activity) values (23, 'Event');
insert into activity_type (id, activity) values (24, 'Ride-share');
insert into activity_type (id, activity) values (26, 'Hike');
insert into activity_type (id, activity) values (27, 'Picnic');
insert into activity_type (id, activity) values (28, 'Park');
insert into activity_type (id, activity) values (29, 'Night Sky');
insert into activity_type (id, activity) values (30, 'Play game, board or computer');

# --- !Downs

delete from "role";
delete from activity_type;

