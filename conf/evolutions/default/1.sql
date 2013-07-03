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
  selected                  BOOLEAN default false,
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
  school_id                 bigint,
  school_year               integer,
  level                     integer,
  group_name                varchar(255),
  answer                    integer,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_result primary key (result_id))
;

create table room (
  room_number               bigint not null,
  status                    boolean,
  entry_id                  bigint,
  constraint uq_room_entry_id unique (entry_id),
  constraint pk_room primary key (room_number))
;

create table room_queue (
  entry_id                  bigint not null,
  school_id                 bigint,
  school_year               integer,
  group_name                varchar(255),
  level                     integer,
  room_number               integer,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_room_queue primary key (entry_id))
;

create table school (
  school_id                 bigint not null,
  school_name               varchar(255),
  constraint pk_school primary key (school_id))
;

create table score (
  result_id                 bigint,
  subject_code              varchar(255),
  correct_number            integer,
  mistake_number            integer,
  time_over_number          integer,
  total_number              integer,
  time                      bigint,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_score primary key (result_id, subject_code))
;

create table subject (
  subject_code              varchar(255) not null,
  subject_name              varchar(255),
  constraint pk_subject primary key (subject_code))
;

create sequence child_seq;

create sequence entry_seq;

create sequence parent_seq;

create sequence question_seq;

create sequence result_seq;

create sequence room_seq;

create sequence room_queue_seq;

create sequence school_seq;

create sequence score_seq;

create sequence subject_seq;

alter table child add constraint fk_child_parent_1 foreign key (parent_id) references parent (id);
create index ix_child_parent_1 on child (parent_id);
alter table entry add constraint fk_entry_school_2 foreign key (school_id) references school (school_id);
create index ix_entry_school_2 on entry (school_id);
alter table result add constraint fk_result_school_3 foreign key (school_id) references school (school_id);
create index ix_result_school_3 on result (school_id);
alter table room_queue add constraint fk_room_queue_school_4 foreign key (school_id) references school (school_id);
create index ix_room_queue_school_4 on room_queue (school_id);



# --- !Downs

drop table if exists child cascade;

drop table if exists entry cascade;

drop table if exists parent cascade;

drop table if exists question cascade;

drop table if exists result cascade;

drop table if exists room cascade;

drop table if exists room_queue cascade;

drop table if exists school cascade;

drop table if exists score cascade;

drop table if exists subject cascade;

drop sequence if exists child_seq;

drop sequence if exists entry_seq;

drop sequence if exists parent_seq;

drop sequence if exists question_seq;

drop sequence if exists result_seq;

drop sequence if exists room_seq;

drop sequence if exists room_queue_seq;

drop sequence if exists school_seq;

drop sequence if exists score_seq;

drop sequence if exists subject_seq;

