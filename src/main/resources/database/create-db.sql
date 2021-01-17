create table accounts (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	username character varying(55) NOT NULL,
	password character varying(155) NOT NULL,
	disabled boolean default false,
	date_disabled bigint default 0,
	uuid character varying(155)
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

create table projects (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(255) NOT NULL,
	uri text NOT NULL,
	avg_resp double default 0.0,
	user_id bigint NOT NULL REFERENCES accounts(id)
);

create table project_status (
	project_id bigint NOT NULL REFERENCES projects(id),
    status_code bigint NOT NULL,
    avg_response double default 0.0,
    response_sum double default 0.0,
    latest_response double default 0.0,
    notified boolean default false,
    notified_count bigint default 0,
    total_http_validations bigint default 0,
    operational_http_validations bigint default 0,
    validation_date bigint default 0,
    initial_saving boolean default true
);

create table project_emails (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	email character varying(255) NOT NULL,
	project_id bigint NOT NULL REFERENCES projects(id)
);

create table project_phones (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	phone character varying(255) NOT NULL,
	project_id bigint NOT NULL REFERENCES projects(id)
);

create table okay_product(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	stripe_id text,
	stripe_product_type character varying (255) default 'service'
);

create table okay_plans(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	stripe_id text,
    amount bigint default 0,
    nickname character varying (255),
    description text,
    frequency character varying(141),
    currency character varying (100),
    project_limit bigint,
	okay_product_id bigint NOT NULL REFERENCES okay_product(id)
);
