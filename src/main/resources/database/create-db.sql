create table accounts (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	username character varying(55) NOT NULL,
	password character varying(155) NOT NULL,
	disabled boolean default false,
	date_disabled bigint default 0,
	uuid character varying(155),
	date_created bigint default 0
);

create table roles (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(55) NOT NULL UNIQUE
);

create table account_permissions(
	user_id bigint REFERENCES accounts(id),
	permission character varying(55)
);

create table account_roles(
	role_id bigint NOT NULL REFERENCES roles(id),
	user_id bigint NOT NULL REFERENCES accounts(id)
);

create table states(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL
);

create table locations(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL,
	state_id bigint NOT NULL REFERENCES states(id)
);

create table projects (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL,
	user_id bigint NOT NULL REFERENCES accounts(id),
	location_id bigint NOT NULL REFERENCES locations(id)
);

create table app_products(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	stripe_id text
);

create table app_plans(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	stripe_id text,
    amount bigint default 0,
    nickname character varying (255),
    description text,
    frequency character varying(141),
    currency character varying (100),
    project_limit bigint,
	app_product_id bigint NOT NULL REFERENCES app_products(id)
);
