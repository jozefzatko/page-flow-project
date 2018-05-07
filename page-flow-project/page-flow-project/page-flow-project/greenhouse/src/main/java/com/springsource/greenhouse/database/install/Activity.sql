create table MemberAction (id SERIAL,
					actionType varchar not null,
					performTime timestamp not null,
					latitude real,
					longitude real,
					member bigint,
					primary key (id),					
					foreign key (member) references Member(id));

create table Badge (name varchar,
					description varchar not null,
					level smallint not null,
					primary key (name));
					
create table AwardedBadge(id SERIAL,
					badge varchar not null,
					awardTime timestamp not null,
					member bigint not null,
					memberAction bigint,
					primary key (id),
					foreign key (badge) references Badge(name),					
					foreign key (member) references Member(id),
					foreign key (memberAction) references MemberAction(id));
					
insert into Badge (name, description, level) values ('Newbie', 'You joined!', 1);