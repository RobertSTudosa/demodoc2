create table appointment (appointment_id bigint not null, appointment_time time, appointment_token varchar(255), date date, date_created datetime, pacient_email varchar(255), pacient_name varchar(255), pacient_phone varchar(255), doctor_person_id bigint, pacient_person_id bigint, primary key (appointment_id)) engine=InnoDB
create table appointment_fixed_times (appointment_appointment_id bigint not null, fixed_times time) engine=InnoDB
create table appointment_seq (next_val bigint) engine=InnoDB
insert into appointment_seq values ( 1 )
create table companyserv (companyserv_id bigint not null, name varchar(255), appointment_appointment_id bigint, primary key (companyserv_id)) engine=InnoDB
create table companyserv_seq (next_val bigint) engine=InnoDB
insert into companyserv_seq values ( 1 )
create table confirmation_token (token_id bigint not null, confirmation_token varchar(255), created_date datetime, user_id bigint not null, primary key (token_id)) engine=InnoDB
create table person_seq (next_val bigint) engine=InnoDB
insert into person_seq values ( 1 )
create table persons (person_id bigint not null, birth_date datetime, active_job bit not null, app_status integer not null, current_job varchar(255), email varchar(255), employment_status varchar(255), end_job datetime, first_name varchar(255), is_affiliated_to_agency bit not null, job_wish_desc varchar(255), last_img_id bigint not null, last_name varchar(255), location varchar(255), phone varchar(255), private_currentjob bit not null, start_job datetime, status_start_date datetime, total_hours double precision not null, unread_notifs bit not null, work_experience integer not null, primary key (person_id)) engine=InnoDB
create table pics_seq (next_val bigint) engine=InnoDB
insert into pics_seq values ( 1 )
create table profile_img (pic_id bigint not null, data longblob, pic_name varchar(255), pic_type varchar(255), person_person_id bigint, primary key (pic_id)) engine=InnoDB
create table token_seq (next_val bigint) engine=InnoDB
insert into token_seq values ( 1 )
create table user_accounts (user_id bigint not null, account_non_expired bit, account_non_locked bit, credentials_non_expired bit, email varchar(255), enable bit not null, job_approved bit not null, password varchar(255), username varchar(255), person_id bigint not null, primary key (user_id)) engine=InnoDB
create table user_role (role_id bigint not null, user_id bigint not null) engine=InnoDB
create table user_seq (next_val bigint) engine=InnoDB
insert into user_seq values ( 1 )
create table userrole (role_id bigint not null, permission varchar(255), primary key (role_id)) engine=InnoDB
create table userrole_seq (next_val bigint) engine=InnoDB
insert into userrole_seq values ( 1 )
alter table user_accounts add constraint UK_4nq5c82rn9wf9m8yklxmm6mkd unique (person_id)
alter table user_accounts add constraint UK_f9sl209luxhu4rylls0h1m625 unique (email)
alter table appointment add constraint FKauddc25j9ht6naydy7jlubgrw foreign key (doctor_person_id) references persons (person_id)
alter table appointment add constraint FKn92k52hhpykhyc22j7uo5t05a foreign key (pacient_person_id) references persons (person_id)
alter table appointment_fixed_times add constraint FK6jqh2r4wm687afie94oq18srq foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table companyserv add constraint FKinrxlgwh2xiaj62942sj2tjo8 foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table confirmation_token add constraint FKj3w7tl2jyc79oqgcn8eqsja4n foreign key (user_id) references user_accounts (user_id)
alter table profile_img add constraint FKdo330tfew6xx3efb4qk9ed2j1 foreign key (person_person_id) references persons (person_id)
alter table user_accounts add constraint FKgk3yo8u7qaj123dbtrjfthxrl foreign key (person_id) references persons (person_id)
alter table user_role add constraint FKrmrso5qtt0pxqlgbtwi5iyjxv foreign key (user_id) references user_accounts (user_id)
alter table user_role add constraint FK5bspsi5hrs4wqpdgea6bfa3f2 foreign key (role_id) references userrole (role_id)
create table appointment (appointment_id bigint not null, appointment_time time, appointment_token varchar(255), date date, date_created datetime, pacient_email varchar(255), pacient_name varchar(255), pacient_phone varchar(255), doctor_person_id bigint, pacient_person_id bigint, primary key (appointment_id)) engine=InnoDB
create table appointment_fixed_times (appointment_appointment_id bigint not null, fixed_times time) engine=InnoDB
create table appointment_seq (next_val bigint) engine=InnoDB
insert into appointment_seq values ( 1 )
create table companyserv (companyserv_id bigint not null, name varchar(255), appointment_appointment_id bigint, primary key (companyserv_id)) engine=InnoDB
create table companyserv_seq (next_val bigint) engine=InnoDB
insert into companyserv_seq values ( 1 )
create table confirmation_token (token_id bigint not null, confirmation_token varchar(255), created_date datetime, user_id bigint not null, primary key (token_id)) engine=InnoDB
create table person_seq (next_val bigint) engine=InnoDB
insert into person_seq values ( 1 )
create table persons (person_id bigint not null, birth_date datetime, active_job bit not null, app_status integer not null, current_job varchar(255), email varchar(255), employment_status varchar(255), end_job datetime, first_name varchar(255), is_affiliated_to_agency bit not null, job_wish_desc varchar(255), last_img_id bigint not null, last_name varchar(255), location varchar(255), phone varchar(255), private_currentjob bit not null, start_job datetime, status_start_date datetime, total_hours double precision not null, unread_notifs bit not null, work_experience integer not null, primary key (person_id)) engine=InnoDB
create table pics_seq (next_val bigint) engine=InnoDB
insert into pics_seq values ( 1 )
create table profile_img (pic_id bigint not null, data longblob, pic_name varchar(255), pic_type varchar(255), person_person_id bigint, primary key (pic_id)) engine=InnoDB
create table token_seq (next_val bigint) engine=InnoDB
insert into token_seq values ( 1 )
create table user_accounts (user_id bigint not null, account_non_expired bit, account_non_locked bit, credentials_non_expired bit, email varchar(255), enable bit not null, job_approved bit not null, password varchar(255), username varchar(255), person_id bigint not null, primary key (user_id)) engine=InnoDB
create table user_role (role_id bigint not null, user_id bigint not null) engine=InnoDB
create table user_seq (next_val bigint) engine=InnoDB
insert into user_seq values ( 1 )
create table userrole (role_id bigint not null, permission varchar(255), primary key (role_id)) engine=InnoDB
create table userrole_seq (next_val bigint) engine=InnoDB
insert into userrole_seq values ( 1 )
alter table user_accounts add constraint UK_4nq5c82rn9wf9m8yklxmm6mkd unique (person_id)
alter table user_accounts add constraint UK_f9sl209luxhu4rylls0h1m625 unique (email)
alter table appointment add constraint FKauddc25j9ht6naydy7jlubgrw foreign key (doctor_person_id) references persons (person_id)
alter table appointment add constraint FKn92k52hhpykhyc22j7uo5t05a foreign key (pacient_person_id) references persons (person_id)
alter table appointment_fixed_times add constraint FK6jqh2r4wm687afie94oq18srq foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table companyserv add constraint FKinrxlgwh2xiaj62942sj2tjo8 foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table confirmation_token add constraint FKj3w7tl2jyc79oqgcn8eqsja4n foreign key (user_id) references user_accounts (user_id)
alter table profile_img add constraint FKdo330tfew6xx3efb4qk9ed2j1 foreign key (person_person_id) references persons (person_id)
alter table user_accounts add constraint FKgk3yo8u7qaj123dbtrjfthxrl foreign key (person_id) references persons (person_id)
alter table user_role add constraint FKrmrso5qtt0pxqlgbtwi5iyjxv foreign key (user_id) references user_accounts (user_id)
alter table user_role add constraint FK5bspsi5hrs4wqpdgea6bfa3f2 foreign key (role_id) references userrole (role_id)
create table appointment (appointment_id bigint not null, appointment_time time, appointment_token varchar(255), date date, date_created datetime, pacient_email varchar(255), pacient_name varchar(255), pacient_phone varchar(255), doctor_person_id bigint, pacient_person_id bigint, primary key (appointment_id)) engine=InnoDB
create table appointment_fixed_times (appointment_appointment_id bigint not null, fixed_times time) engine=InnoDB
create table appointment_seq (next_val bigint) engine=InnoDB
insert into appointment_seq values ( 1 )
create table companyserv (companyserv_id bigint not null, name varchar(255), appointment_appointment_id bigint, primary key (companyserv_id)) engine=InnoDB
create table companyserv_seq (next_val bigint) engine=InnoDB
insert into companyserv_seq values ( 1 )
create table confirmation_token (token_id bigint not null, confirmation_token varchar(255), created_date datetime, user_id bigint not null, primary key (token_id)) engine=InnoDB
create table person_seq (next_val bigint) engine=InnoDB
insert into person_seq values ( 1 )
create table persons (person_id bigint not null, birth_date datetime, active_job bit not null, app_status integer not null, current_job varchar(255), email varchar(255), employment_status varchar(255), end_job datetime, first_name varchar(255), is_affiliated_to_agency bit not null, job_wish_desc varchar(255), last_img_id bigint not null, last_name varchar(255), location varchar(255), phone varchar(255), private_currentjob bit not null, start_job datetime, status_start_date datetime, total_hours double precision not null, unread_notifs bit not null, work_experience integer not null, primary key (person_id)) engine=InnoDB
create table pics_seq (next_val bigint) engine=InnoDB
insert into pics_seq values ( 1 )
create table profile_img (pic_id bigint not null, data longblob, pic_name varchar(255), pic_type varchar(255), person_person_id bigint, primary key (pic_id)) engine=InnoDB
create table token_seq (next_val bigint) engine=InnoDB
insert into token_seq values ( 1 )
create table user_accounts (user_id bigint not null, account_non_expired bit, account_non_locked bit, credentials_non_expired bit, email varchar(255), enable bit not null, job_approved bit not null, password varchar(255), username varchar(255), person_id bigint not null, primary key (user_id)) engine=InnoDB
create table user_role (role_id bigint not null, user_id bigint not null) engine=InnoDB
create table user_seq (next_val bigint) engine=InnoDB
insert into user_seq values ( 1 )
create table userrole (role_id bigint not null, permission varchar(255), primary key (role_id)) engine=InnoDB
create table userrole_seq (next_val bigint) engine=InnoDB
insert into userrole_seq values ( 1 )
alter table user_accounts add constraint UK_4nq5c82rn9wf9m8yklxmm6mkd unique (person_id)
alter table user_accounts add constraint UK_f9sl209luxhu4rylls0h1m625 unique (email)
alter table appointment add constraint FKauddc25j9ht6naydy7jlubgrw foreign key (doctor_person_id) references persons (person_id)
alter table appointment add constraint FKn92k52hhpykhyc22j7uo5t05a foreign key (pacient_person_id) references persons (person_id)
alter table appointment_fixed_times add constraint FK6jqh2r4wm687afie94oq18srq foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table companyserv add constraint FKinrxlgwh2xiaj62942sj2tjo8 foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table confirmation_token add constraint FKj3w7tl2jyc79oqgcn8eqsja4n foreign key (user_id) references user_accounts (user_id)
alter table profile_img add constraint FKdo330tfew6xx3efb4qk9ed2j1 foreign key (person_person_id) references persons (person_id)
alter table user_accounts add constraint FKgk3yo8u7qaj123dbtrjfthxrl foreign key (person_id) references persons (person_id)
alter table user_role add constraint FKrmrso5qtt0pxqlgbtwi5iyjxv foreign key (user_id) references user_accounts (user_id)
alter table user_role add constraint FK5bspsi5hrs4wqpdgea6bfa3f2 foreign key (role_id) references userrole (role_id)
create table appointment (appointment_id bigint not null, appointment_time time, appointment_token varchar(255), date date, date_created datetime, pacient_email varchar(255), pacient_name varchar(255), pacient_phone varchar(255), doctor_person_id bigint, pacient_person_id bigint, primary key (appointment_id)) engine=InnoDB
create table appointment_fixed_times (appointment_appointment_id bigint not null, fixed_times time) engine=InnoDB
create table appointment_seq (next_val bigint) engine=InnoDB
insert into appointment_seq values ( 1 )
create table companyserv (companyserv_id bigint not null, name varchar(255), appointment_appointment_id bigint, primary key (companyserv_id)) engine=InnoDB
create table companyserv_seq (next_val bigint) engine=InnoDB
insert into companyserv_seq values ( 1 )
create table confirmation_token (token_id bigint not null, confirmation_token varchar(255), created_date datetime, user_id bigint not null, primary key (token_id)) engine=InnoDB
create table person_seq (next_val bigint) engine=InnoDB
insert into person_seq values ( 1 )
create table persons (person_id bigint not null, birth_date datetime, active_job bit not null, app_status integer not null, current_job varchar(255), email varchar(255), employment_status varchar(255), end_job datetime, first_name varchar(255), is_affiliated_to_agency bit not null, job_wish_desc varchar(255), last_img_id bigint not null, last_name varchar(255), location varchar(255), phone varchar(255), private_currentjob bit not null, start_job datetime, status_start_date datetime, total_hours double precision not null, unread_notifs bit not null, work_experience integer not null, primary key (person_id)) engine=InnoDB
create table pics_seq (next_val bigint) engine=InnoDB
insert into pics_seq values ( 1 )
create table profile_img (pic_id bigint not null, data longblob, pic_name varchar(255), pic_type varchar(255), person_person_id bigint, primary key (pic_id)) engine=InnoDB
create table token_seq (next_val bigint) engine=InnoDB
insert into token_seq values ( 1 )
create table user_accounts (user_id bigint not null, account_non_expired bit, account_non_locked bit, credentials_non_expired bit, email varchar(255), enable bit not null, job_approved bit not null, password varchar(255), username varchar(255), person_id bigint not null, primary key (user_id)) engine=InnoDB
create table user_role (role_id bigint not null, user_id bigint not null) engine=InnoDB
create table user_seq (next_val bigint) engine=InnoDB
insert into user_seq values ( 1 )
create table userrole (role_id bigint not null, permission varchar(255), primary key (role_id)) engine=InnoDB
create table userrole_seq (next_val bigint) engine=InnoDB
insert into userrole_seq values ( 1 )
alter table user_accounts add constraint UK_4nq5c82rn9wf9m8yklxmm6mkd unique (person_id)
alter table user_accounts add constraint UK_f9sl209luxhu4rylls0h1m625 unique (email)
alter table appointment add constraint FKauddc25j9ht6naydy7jlubgrw foreign key (doctor_person_id) references persons (person_id)
alter table appointment add constraint FKn92k52hhpykhyc22j7uo5t05a foreign key (pacient_person_id) references persons (person_id)
alter table appointment_fixed_times add constraint FK6jqh2r4wm687afie94oq18srq foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table companyserv add constraint FKinrxlgwh2xiaj62942sj2tjo8 foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table confirmation_token add constraint FKj3w7tl2jyc79oqgcn8eqsja4n foreign key (user_id) references user_accounts (user_id)
alter table profile_img add constraint FKdo330tfew6xx3efb4qk9ed2j1 foreign key (person_person_id) references persons (person_id)
alter table user_accounts add constraint FKgk3yo8u7qaj123dbtrjfthxrl foreign key (person_id) references persons (person_id)
alter table user_role add constraint FKrmrso5qtt0pxqlgbtwi5iyjxv foreign key (user_id) references user_accounts (user_id)
alter table user_role add constraint FK5bspsi5hrs4wqpdgea6bfa3f2 foreign key (role_id) references userrole (role_id)
create table appointment (appointment_id bigint not null, appointment_time time, appointment_token varchar(255), date date, date_created datetime, pacient_email varchar(255), pacient_name varchar(255), pacient_phone varchar(255), doctor_person_id bigint, pacient_person_id bigint, primary key (appointment_id)) engine=InnoDB
create table appointment_fixed_times (appointment_appointment_id bigint not null, fixed_times time) engine=InnoDB
create table appointment_seq (next_val bigint) engine=InnoDB
insert into appointment_seq values ( 1 )
create table companyserv (companyserv_id bigint not null, name varchar(255), appointment_appointment_id bigint, primary key (companyserv_id)) engine=InnoDB
create table companyserv_seq (next_val bigint) engine=InnoDB
insert into companyserv_seq values ( 1 )
create table confirmation_token (token_id bigint not null, confirmation_token varchar(255), created_date datetime, user_id bigint not null, primary key (token_id)) engine=InnoDB
create table person_seq (next_val bigint) engine=InnoDB
insert into person_seq values ( 1 )
create table persons (person_id bigint not null, birth_date datetime, active_job bit not null, app_status integer not null, current_job varchar(255), email varchar(255), employment_status varchar(255), end_job datetime, first_name varchar(255), is_affiliated_to_agency bit not null, job_wish_desc varchar(255), last_img_id bigint not null, last_name varchar(255), location varchar(255), phone varchar(255), private_currentjob bit not null, start_job datetime, status_start_date datetime, total_hours double precision not null, unread_notifs bit not null, work_experience integer not null, primary key (person_id)) engine=InnoDB
create table pics_seq (next_val bigint) engine=InnoDB
insert into pics_seq values ( 1 )
create table profile_img (pic_id bigint not null, data longblob, pic_name varchar(255), pic_type varchar(255), person_person_id bigint, primary key (pic_id)) engine=InnoDB
create table token_seq (next_val bigint) engine=InnoDB
insert into token_seq values ( 1 )
create table user_accounts (user_id bigint not null, account_non_expired bit, account_non_locked bit, credentials_non_expired bit, email varchar(255), enable bit not null, job_approved bit not null, password varchar(255), username varchar(255), person_id bigint not null, primary key (user_id)) engine=InnoDB
create table user_role (role_id bigint not null, user_id bigint not null) engine=InnoDB
create table user_seq (next_val bigint) engine=InnoDB
insert into user_seq values ( 1 )
create table userrole (role_id bigint not null, permission varchar(255), primary key (role_id)) engine=InnoDB
create table userrole_seq (next_val bigint) engine=InnoDB
insert into userrole_seq values ( 1 )
alter table user_accounts add constraint UK_4nq5c82rn9wf9m8yklxmm6mkd unique (person_id)
alter table user_accounts add constraint UK_f9sl209luxhu4rylls0h1m625 unique (email)
alter table appointment add constraint FKauddc25j9ht6naydy7jlubgrw foreign key (doctor_person_id) references persons (person_id)
alter table appointment add constraint FKn92k52hhpykhyc22j7uo5t05a foreign key (pacient_person_id) references persons (person_id)
alter table appointment_fixed_times add constraint FK6jqh2r4wm687afie94oq18srq foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table companyserv add constraint FKinrxlgwh2xiaj62942sj2tjo8 foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table confirmation_token add constraint FKj3w7tl2jyc79oqgcn8eqsja4n foreign key (user_id) references user_accounts (user_id)
alter table profile_img add constraint FKdo330tfew6xx3efb4qk9ed2j1 foreign key (person_person_id) references persons (person_id)
alter table user_accounts add constraint FKgk3yo8u7qaj123dbtrjfthxrl foreign key (person_id) references persons (person_id)
alter table user_role add constraint FKrmrso5qtt0pxqlgbtwi5iyjxv foreign key (user_id) references user_accounts (user_id)
alter table user_role add constraint FK5bspsi5hrs4wqpdgea6bfa3f2 foreign key (role_id) references userrole (role_id)
create table appointment (appointment_id bigint not null, appointment_time time, appointment_token varchar(255), date date, date_created datetime, pacient_email varchar(255), pacient_name varchar(255), pacient_phone varchar(255), doctor_person_id bigint, pacient_person_id bigint, primary key (appointment_id)) engine=InnoDB
create table appointment_fixed_times (appointment_appointment_id bigint not null, fixed_times time) engine=InnoDB
create table appointment_seq (next_val bigint) engine=InnoDB
insert into appointment_seq values ( 1 )
create table companyserv (companyserv_id bigint not null, name varchar(255), appointment_appointment_id bigint, primary key (companyserv_id)) engine=InnoDB
create table companyserv_seq (next_val bigint) engine=InnoDB
insert into companyserv_seq values ( 1 )
create table confirmation_token (token_id bigint not null, confirmation_token varchar(255), created_date datetime, user_id bigint not null, primary key (token_id)) engine=InnoDB
create table person_seq (next_val bigint) engine=InnoDB
insert into person_seq values ( 1 )
create table persons (person_id bigint not null, birth_date datetime, active_job bit not null, app_status integer not null, current_job varchar(255), email varchar(255), employment_status varchar(255), end_job datetime, first_name varchar(255), is_affiliated_to_agency bit not null, job_wish_desc varchar(255), last_img_id bigint not null, last_name varchar(255), location varchar(255), phone varchar(255), private_currentjob bit not null, start_job datetime, status_start_date datetime, total_hours double precision not null, unread_notifs bit not null, work_experience integer not null, primary key (person_id)) engine=InnoDB
create table pics_seq (next_val bigint) engine=InnoDB
insert into pics_seq values ( 1 )
create table profile_img (pic_id bigint not null, data longblob, pic_name varchar(255), pic_type varchar(255), person_person_id bigint, primary key (pic_id)) engine=InnoDB
create table token_seq (next_val bigint) engine=InnoDB
insert into token_seq values ( 1 )
create table user_accounts (user_id bigint not null, account_non_expired bit, account_non_locked bit, credentials_non_expired bit, email varchar(255), enable bit not null, job_approved bit not null, password varchar(255), username varchar(255), person_id bigint not null, primary key (user_id)) engine=InnoDB
create table user_role (role_id bigint not null, user_id bigint not null) engine=InnoDB
create table user_seq (next_val bigint) engine=InnoDB
insert into user_seq values ( 1 )
create table userrole (role_id bigint not null, permission varchar(255), primary key (role_id)) engine=InnoDB
create table userrole_seq (next_val bigint) engine=InnoDB
insert into userrole_seq values ( 1 )
alter table user_accounts add constraint UK_4nq5c82rn9wf9m8yklxmm6mkd unique (person_id)
alter table user_accounts add constraint UK_f9sl209luxhu4rylls0h1m625 unique (email)
alter table appointment add constraint FKauddc25j9ht6naydy7jlubgrw foreign key (doctor_person_id) references persons (person_id)
alter table appointment add constraint FKn92k52hhpykhyc22j7uo5t05a foreign key (pacient_person_id) references persons (person_id)
alter table appointment_fixed_times add constraint FK6jqh2r4wm687afie94oq18srq foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table companyserv add constraint FKinrxlgwh2xiaj62942sj2tjo8 foreign key (appointment_appointment_id) references appointment (appointment_id)
alter table confirmation_token add constraint FKj3w7tl2jyc79oqgcn8eqsja4n foreign key (user_id) references user_accounts (user_id)
alter table profile_img add constraint FKdo330tfew6xx3efb4qk9ed2j1 foreign key (person_person_id) references persons (person_id)
alter table user_accounts add constraint FKgk3yo8u7qaj123dbtrjfthxrl foreign key (person_id) references persons (person_id)
alter table user_role add constraint FKrmrso5qtt0pxqlgbtwi5iyjxv foreign key (user_id) references user_accounts (user_id)
alter table user_role add constraint FK5bspsi5hrs4wqpdgea6bfa3f2 foreign key (role_id) references userrole (role_id)
