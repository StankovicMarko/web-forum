use webforum;

SELECT * FROM users;

SELECT * FROM forums;
select * from topics;
select * from replies;

update forums set deleted = 0 where id>0;
update topics set deleted = 0 where id>0;
update replies set deleted = 0 where id>0;

SELECT * FROM forums WHERE deleted = 0 AND type = 'Public' AND 
	(name like '%public%' or ownerUsername like '%public%');


select * from topics where deleted = 0 and 
	(name like '%a%' or ownerUsername like '%a%' or description like '%a%' or content like '%a%') 
		and parentForumId in (select id from forums where deleted = 0 and 
(type = 'Public' or type = 'Open'));


SELECT A.*, B.picture 
		FROM replies A, users B 
			WHERE A.deleted = 0 AND (A.content like '%a%' or A.ownerUsername like '%a%') and 
			A.ownerUsername = B.username order by id desc;


SELECT A.*, B.picture 
		FROM replies A, users B 
			WHERE A.deleted = 0 AND (A.content like '%a%' or A.ownerUsername like '%a%')
		and A.ownerUsername = B.username 
			and parentTopicId in (select id from topics where parentForumId in (select id from forums where
				deleted = 0 and type='Public'));


SELECT * FROM topics WHERE deleted = 0 AND ownerUsername = 'admin' order by id desc;

	SELECT A.id, A.content, A.ownerUsername, A.creationDate,
		A.parentTopicId, A.deleted, B.picture 
			FROM replies A, users B 
				WHERE A.deleted = 0 AND A.ownerUsername ='admin' and 
				A.ownerUsername = B.username order by id desc;

SELECT name, surname, mail, dateReg, role, banned, picture FROM users WHERE username = 'admin' AND deleted = 0;

UPDATE replies SET deleted = 1 WHERE id = 1;

select role from users where username in (select ownerUsername from topics where id = 1);
select username, role from users where username in (select ownerUsername from topics where id = 1);
select ownerUsername from topics where id = 1;


SELECT A.id, A.content, A.ownerUsername, A.creationDate, A.parentTopicId, A.deleted, B.picture 
	FROM replies A, users B 
		WHERE A.deleted = 0 AND A.parentTopicId = 1 and 
			A.ownerUsername = B.username order by creationDate desc, id desc limit 0, 5;

SELECT picture FROM users WHERE username = 'admin';
UPDATE topics SET deleted = 1 WHERE id = 1;
UPDATE replies SET deleted = 1 WHERE parentTopicId = 1;

insert into replies (content, ownerUsername, creationDate, parentTopicId, deleted)
	values ('aa', 'admin', now(), 1, 0);