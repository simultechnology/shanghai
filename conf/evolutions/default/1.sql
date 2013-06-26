# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table child (
  id                        bigint not null,
  parent_id                 bigint,
  name                      varchar(255) not null,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_child primary key (id))
;

create table entry (
  entry_id                  bigint not null,
  school_id                 bigint,
  play_date                 timestamp,
  school_year               integer,
  group_name                varchar(255),
  level                     integer,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_entry primary key (entry_id))
;

create table parent (
  id                        bigint not null,
  name                      varchar(255) not null,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_parent primary key (id))
;

create table question (
  question_id               bigint not null,
  school_year               integer,
  level                     integer,
  subject                   varchar(255),
  content                   TEXT,
  choice1                   TEXT,
  choice2                   TEXT,
  choice3                   TEXT,
  choice4                   TEXT,
  answer                    integer,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_question primary key (question_id))
;

create table result (
  result_id                 bigint not null,
  school_year               integer,
  level                     integer,
  subject                   varchar(255),
  answer                    integer,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_result primary key (result_id))
;

create table room (
  room_number               integer not null,
  status                    boolean,
  constraint pk_room primary key (room_number))
;

create table school (
  school_id                 bigint not null,
  name                      varchar(255),
  constraint pk_school primary key (school_id))
;

create sequence child_seq;

create sequence entry_seq;

create sequence parent_seq;

create sequence question_seq;

create sequence result_seq;

create sequence room_seq;

create sequence school_seq;

alter table child add constraint fk_child_parent_1 foreign key (parent_id) references parent (id);
create index ix_child_parent_1 on child (parent_id);
alter table entry add constraint fk_entry_school_2 foreign key (school_id) references school (school_id);
create index ix_entry_school_2 on entry (school_id);



# --- !Downs

drop table if exists child cascade;

drop table if exists entry cascade;

drop table if exists parent cascade;

drop table if exists question cascade;

drop table if exists result cascade;

drop table if exists room cascade;

drop table if exists school cascade;

drop sequence if exists child_seq;

drop sequence if exists entry_seq;

drop sequence if exists parent_seq;

drop sequence if exists question_seq;

drop sequence if exists result_seq;

drop sequence if exists room_seq;

drop sequence if exists school_seq;

