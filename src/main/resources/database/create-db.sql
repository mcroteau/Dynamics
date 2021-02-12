create table products(
	id bigint PRIMARY KEY AUTO_INCREMENT,
    nickname character varying (255),
	stripe_id text
);

create table prices(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	stripe_id text,
    amount bigint default 0,
    nickname character varying (255),
	product_id bigint NOT NULL REFERENCES products(id)
);

create table users (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	username character varying(55) NOT NULL,
	password character varying(155) NOT NULL,
	uuid character varying(155),
	date_created bigint default 0,
	price_id bigint,
	stripe_user_id text,
	stripe_charge_id text,
	stripe_subscription_id text
);

create table roles (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(155) NOT NULL UNIQUE
);

create table user_permissions(
	user_id bigint REFERENCES users(id),
	permission character varying(55)
);

create table user_roles(
	role_id bigint NOT NULL REFERENCES roles(id),
	user_id bigint NOT NULL REFERENCES users(id)
);

create table states(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL
);

create table towns(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL,
    town_uri character varying(255),
	state_id bigint NOT NULL REFERENCES states(id)
);

create table locations (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL,
	description text,
	needs text,
	count bigint default 1,
	location_uri character varying (255),
	town_id bigint NOT NULL REFERENCES towns(id),
	constraint unique_location_uri unique(location_uri)
);

create table daily_counts (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	user_id bigint NOT NULL REFERENCES users(id),
	location_id bigint NOT NULL REFERENCES locations(id),
	count bigint NOT NULL,
	date_entered bigint NOT NULL,
	constraint unique_location_count unique(location_id, date_entered)
);
