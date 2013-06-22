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

create table parent (
  id                        bigint not null,
  name                      varchar(255) not null,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_parent primary key (id))
;

create table question (
  id                        bigint not null,
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
  constraint pk_question primary key (id))
;

create sequence child_seq;

create sequence parent_seq;

create sequence question_seq;

alter table child add constraint fk_child_parent_1 foreign key (parent_id) references parent (id);
create index ix_child_parent_1 on child (parent_id);



# --- !Downs

drop table if exists child cascade;

drop table if exists parent cascade;

drop table if exists question cascade;

drop sequence if exists child_seq;

drop sequence if exists parent_seq;

drop sequence if exists question_seq;

