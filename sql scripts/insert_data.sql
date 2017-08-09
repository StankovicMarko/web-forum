use webforum;

insert into users(id,username,password,name,surName,mail,dateReg,role,banned,picture,deleted) 
	values(1,'admin','admin','admin','admin','admin@forum.com','2017-03-05','admin',0,'images/default-avatar.png',0);

insert into users(id,username,password,name,surName,mail,dateReg,role,banned,picture,deleted) 
	values(2,'moder','moder','moder','moder','moder@forum.com','2017-03-05','moderator',0,'images/default-avatar.png',0);

insert into users(id,username,password,name,surName,mail,dateReg,role,banned,picture,deleted) 
	values(3,'regi','regi','regi','regi','regi@forum.com','2017-03-05','registered',0,'images/default-avatar.png',0);


insert into forums(id,name,description,ownerUsername,creationDate,locked,type,parentForumId,deleted) 
	values(1,'Public','public description','admin','2017-03-05',0,'Public',null,0);

insert into forums(id,name,description,ownerUsername,creationDate,locked,type,parentForumId,deleted) 
	values(2,'Open','open description','admin','2017-03-05',0,'Open',null,0);

insert into forums(id,name,description,ownerUsername,creationDate,locked,type,parentForumId,deleted) 
	values(3,'Closed','closed description','admin','2017-03-05',0,'Closed',null,0);

insert into forums(id,name,description,ownerUsername,creationDate,locked,type,parentForumId,deleted) 
	values(4,'Public sub','public description','admin','2017-03-05',0,'Public',1,0);

insert into forums(id,name,description,ownerUsername,creationDate,locked,type,parentForumId,deleted) 
	values(5,'Open sub','open description','admin','2017-03-05',0,'Open',2,0);

insert into forums(id,name,description,ownerUsername,creationDate,locked,type,parentForumId,deleted) 
	values(6,'Closed sub','closed description','admin','2017-03-05',0,'Closed',3,0);


insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(1,'Admin Topic','admin made this','admin made this topic and its awesome','admin','2017-03-05',0,0,1,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(2,'Moder Topic','moder made this','moder made this topic and its awesome','moder','2017-03-05',0,0,1,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(3,'Regi Topic','regi made this','regi made this topic and its awesome','regi','2017-03-05',0,0,1,0);

insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(4,'Admin Topic 2','admin made this','admin made this topic and its awesome','admin','2017-03-05',0,0,2,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(5,'Moder Topic 2','moder made this','moder made this topic and its awesome','moder','2017-03-05',0,0,2,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(6,'Regi Topic 2','regi made this','regi made this topic and its awesome','regi','2017-03-05',0,0,2,0);

insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(7,'Admin Topic 3','admin made this','admin made this topic and its awesome','admin','2017-03-05',0,0,3,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(8,'Moder Topic 3','moder made this','moder made this topic and its awesome','moder','2017-03-05',0,0,3,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(9,'Regi Topic 3','regi made this','regi made this topic and its awesome','regi','2017-03-05',0,0,3,0);

insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(10,'Admin Topic 4','admin made this','admin made this topic and its awesome','admin','2017-03-05',0,0,4,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(11,'Moder Topic 4','moder made this','moder made this topic and its awesome','moder','2017-03-05',0,0,4,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(12,'Regi Topic 4','regi made this','regi made this topic and its awesome','regi','2017-03-05',0,0,4,0);

insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(13,'Admin Topic 5','admin made this','admin made this topic and its awesome','admin','2017-03-05',0,0,5,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(14,'Moder Topic 5','moder made this','moder made this topic and its awesome','moder','2017-03-05',0,0,5,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(15,'Regi Topic 5','regi made this','regi made this topic and its awesome','regi','2017-03-05',0,0,5,0);


insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(16,'Admin Topic 6','admin made this','admin made this topic and its awesome','admin','2017-03-05',0,0,6,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(17,'Moder Topic 6','moder made this','moder made this topic and its awesome','moder','2017-03-05',0,0,6,0);
insert into topics(id,name,description,content,ownerUsername,creationDate,pinned,locked,parentForumId,deleted) 
	values(18,'Regi Topic 6','regi made this','regi made this topic and its awesome','regi','2017-03-05',0,0,6,0);
