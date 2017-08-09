-- create database webforum;

use webforum;

create table users(
	id int auto_increment primary key,
    username varchar(30) not null unique,
	password varchar(30) not null,
    name varchar(30),
	surName varchar(30),
	mail varchar(30) not null unique,
	dateReg datetime not null,
	role varchar(30) not null,
	banned boolean not null,
	picture varchar(50) not null,
	deleted boolean not null

);

create table forums(
	id int auto_increment primary key,
    name varchar(50) not null,
    description varchar(250),
    ownerUsername varchar(30) not null, -- owner
    creationDate datetime not null, -- creation date
    locked boolean not null,
    type varchar(30) not null,
	parentForumId int, -- nadforum,
	deleted boolean not null,
    FOREIGN KEY (ownerUsername) REFERENCES users(username),
    FOREIGN KEY (parentForumId) REFERENCES forums(id)
);


create table topics(
	id int auto_increment primary key,
	name varchar(50) not null,
    description varchar(250),
    content text not null,
    ownerUsername varchar(30) not null, -- owner
	creationDate datetime not null, -- creation date
    pinned boolean not null,
    locked boolean not null,
    parentForumId int not null,
	deleted boolean not null,
    FOREIGN KEY (parentForumId) REFERENCES forums(id),
    FOREIGN KEY (ownerUsername) REFERENCES users(username)
);

create table replies(
	id int auto_increment primary key,
    content text not null,
    ownerUsername varchar(30) not null, -- owner
    creationDate datetime not null, -- creation date
    parentTopicId int not null,
	deleted boolean not null,
	FOREIGN KEY (ownerUsername) REFERENCES users(username),
	FOREIGN KEY (parentTopicId) REFERENCES topics(id)

);
