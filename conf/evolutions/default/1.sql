# Terra Traveler schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
	id INTEGER NOT NULL DEFAULT nextval('task_id_seq'),
	label varchar(255)
);

CREATE SEQUENCE user_id_seq;
CREATE TABLE "user" (
	id INTEGER NOT NULL DEFAULT nextval('user_id_seq'),
	--logged_in BOOLEAN NOT NULL DEFAULT FALSE,
	created TIMESTAMP NOT NULL DEFAULT current_timestamp,
	last_active TIMESTAMP,
	last_login TIMESTAMP
);

CREATE SEQUENCE user_prof_id_seq;
CREATE TABLE user_profile (
	id INTEGER NOT NULL DEFAULT nextval('user_prof_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	user_name VARCHAR(45),
	gender CHAR(8),
	birthdate TIMESTAMP,
	nationality VARCHAR(45),
	primary_loc INTEGER,
	portrait_url VARCHAR(80),
	bio VARCHAR(256),
	story VARCHAR(256),
	PRIMARY KEY(user_id)
);

CREATE TABLE user_contact (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	contact_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	index INTEGER
);

CREATE TABLE group_id_seq;
CREATE TABLE group (
	group_id INTEGER NOT NULL DEFAULT nextval('group_id_seq'),
	leader INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	group_name VARCHAR(60),
	desc VARCHAR(256),
	min_size INTEGER,
	max_size INTEGER
);

CREATE TABLE user_group (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES group(group_id) ON DELETE CASCADE
);

CREATE TABLE group_event (

);

CREATE TABLE preferences (
	id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	pref1 INTEGER,
	pref2 INTEGER,
	pref3 INTEGER
);

CREATE SEQUENCE photo_id_seq;
CREATE TABLE photo (
	id INTEGER NOT NULL DEFAULT nextval('photo_id_seq'),
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	url VARCHAR(80),
	index INTEGER
);

CREATE TABLE disp_id_seq;
CREATE TABLE dispatch (
	id INTEGER NOT NULL DEFAULT nextval('disp_id_seq'),
	disp VARCHAR(200),
	index INTEGER
)

CREATE TABLE user_loc (
	user_id
	loc_id
	index
);

CREATE TABLE loc_id_seq;
CREATE TABLE location (
	loc_id INTEGER NOT NULL DEFAULT nextval('loc_id_seq'),
	city VARCHAR(45),
	country VARCHAR(45),
	address_1 VARCHAR(45),
	address_2 VARCHAR(45),
	address_3 VARCHAR(45),
	postal_code VARCHAR(20),
	geo_coord1 VARCHAR(20),
	geo_coord2 VARCHAR(20),
	desc VARCHAR(256),
	url VARCHAR(60)
);

CREATE TABLE user_interest (
	user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
	interest VARCHAR(45);
);

CREATE TABLE interests (
	interest_id INTEGER NOT NULL DEFAULT nextval('user_id_seq'),
	interest VARCHAR(45)
);

CREATE TABLE event_id_seq;
CREATE TABLE event (
	event_id INTEGER NOT NULL DEFAULT nextval('event_id_seq'),
	date TIMESTAMP,
	location INTEGER NOT NULL REFERENCES location(id) ON DELETE CASCADE, 
	description 
	min_size INTEGER,
	max_size INTEGER
);

CREATE TABLE language (

);

CREATE TABLE place (
	location
);

CREATE TABLE promo (
	id
	date
	begin
	end
	code
	description
	min_persons
	max_persons
	url
	place
};

CREATE TABLE work (
	company
	profession
	position
	project
	resume
	affiliations
	url VARCHAR(80);
);

# --- !Downs

DROP TABLE task;

DROP TABLE "user";
DROP TABLE user_req;

DROP SEQUENCE task_id_seq;

DROP SEQUENCE user_id_seq;
DROP SEQUENCE user_req_seq;