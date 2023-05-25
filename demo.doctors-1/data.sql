-- Clear information on tables -- 
DELETE from bpeople_demo_doctors.appointment;
INSERT INTO bpeople_demo_doctors.appointment_seq ( next_val ) VALUES (
	1
);

DELETE from bpeople_demo_doctors.companyserv;
INSERT INTO bpeople_demo_doctors.companyserv_seq ( next_val ) VALUES (
	1
);

DELETE from bpeople_demo_doctors.profile_img;
INSERT INTO bpeople_demo_doctors.pics_seq ( next_val ) VALUES (
	1
);

DELETE from bpeople_demo_doctors.confirmation_token;
INSERT INTO bpeople_demo_doctors.token_seq ( next_val ) VALUES (
	1
);

-- delete from user_role before user_accounts;
DELETE from bpeople_demo_doctors.user_role;

-- delete the userroles defined earlier 
DELETE from bpeople_demo_doctors.userrole;
-- update userrole_seq to next_val = 1;
INSERT INTO bpeople_demo_doctors.userrole_seq ( next_val ) VALUES(
     1
);


-- update bpeople_demo_doctors.person_seq set next_val = 1;
DELETE from bpeople_demo_doctors.user_accounts;
DELETE from bpeople_demo_doctors.user_seq;
INSERT INTO bpeople_demo_doctors.user_seq ( next_val ) VALUES(
     1
);

DELETE from bpeople_demo_doctors.persons;
DELETE from bpeople_demo_doctors.person_seq;
INSERT INTO bpeople_demo_doctors.person_seq ( next_val ) VALUES(
     1
);






-- INSERTING A DOCTOR USER --


-- insert into persons 1st --
INSERT INTO bpeople_demo_doctors.persons (`person_id`, `birth_date`, `active_job`, `app_status`, `current_job`, `email`, `first_name`, `is_affiliated_to_agency`, `last_img_id`, `phone`, `private_currentjob`, `total_hours`, `unread_notifs`, `work_experience`) VALUES ('1', '2000-06-11 21:00:00', 0, 1, '', 'doctor@apxsoftware.ro', 'Doctor One', 0, 0, '0722695137', 0, 0, 0, 0);
-- update the seq for the persons
update bpeople_demo_doctors.person_seq set next_val = 2 where next_val = 1;

-- insert into user_accounts --
INSERT INTO bpeople_demo_doctors.user_accounts (`user_id`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enable`, `job_approved`, `password`, `username`, `person_id`) VALUES (1, 1, 1, 1, 'doctor@apxsoftware.ro', 1, 0, '$2a$12$ZqL/.26VNsU2xTkuk6oL3.IVQIXMX.E6BQdBr0nYCdMVMqMKSjo0y', 'doctor@apxsoftware.ro', 1);

-- insert into user_seq --
update bpeople_demo_doctors.user_seq set next_val = 2;


-- insert userrole (USER / PACIENT / DOCTOR)
INSERT INTO bpeople_demo_doctors.userrole (role_id, permission  ) VALUES(
     1, 'USER'
);
INSERT INTO bpeople_demo_doctors.userrole (role_id, permission  ) VALUES(
     2, 'PACIENT'
);
INSERT INTO bpeople_demo_doctors.userrole (role_id, permission  ) VALUES(
     3, 'DOCTOR'
);
-- update insert userrole_seq next value
update bpeople_demo_doctors.userrole_seq set next_val = 4;

-- insert user_role to give id1 DOCTOR permissions -- 
INSERT INTO bpeople_demo_doctors.user_role (role_id, user_id  ) VALUES(
      1, 1
);

INSERT INTO bpeople_demo_doctors.user_role (role_id, user_id  ) VALUES(
      3, 1
);


-- INSERTING A PACIENT USER --

-- insert into persons 1st --
INSERT INTO bpeople_demo_doctors.persons (`person_id`, `birth_date`, `active_job`, `app_status`, `current_job`, `email`, `first_name`, `is_affiliated_to_agency`, `last_img_id`, `phone`, `private_currentjob`, `total_hours`, `unread_notifs`, `work_experience`) VALUES 
        ('2', '2000-06-11 21:00:00', 0, 1, '', 'client@apxsoftware.ro', 'Client One', 0, 0, '0722695137', 0, 0, 0, 0);
-- update the seq for the persons
update bpeople_demo_doctors.person_seq set next_val = 3 where next_val = 2;

-- insert into user_accounts --
INSERT INTO bpeople_demo_doctors.user_accounts (`user_id`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enable`, `job_approved`, `password`, `username`, `person_id`) VALUES 
        (2, 1, 1, 1, 'client@apxsoftware.ro', 1, 0, '$2a$12$slIFmlAhvMOeigky8SxMv.csSeUKkg/47vfkH.lfFkqwk1HFb1lC.', 'client@apxsoftware.ro', 2);

-- insert into user_seq --
update bpeople_demo_doctors.user_seq set next_val = 3 where next_val = 2;

-- insert user_role to give id1 DOCTOR permissions -- 
INSERT INTO bpeople_demo_doctors.user_role (role_id, user_id  ) VALUES(
      1, 2
);

INSERT INTO bpeople_demo_doctors.user_role (role_id, user_id  ) VALUES(
      2, 2
);