create table user_sequence
(
    next_val bigint null
)
    engine = MyISAM
    charset = utf8mb4;

INSERT INTO user_sequence (next_val) VALUES (1);

create table endpoint_sequence
(
    next_val bigint null
)
    engine = MyISAM
    charset = utf8mb4;

create table result_sequence
(
    next_val bigint null
)
    engine = MyISAM
    charset = utf8mb4;

INSERT INTO result_sequence (next_val) VALUES (1);

create table user
(
    id       int          not null
        primary key,
    username varchar(100) not null,
    email    varchar(150) not null,
    uuid     varchar(255) not null
)
    charset = utf8mb4;

INSERT INTO monitoring_system.user (id, username, email, uuid) VALUES (1, 'abc', 'abc@yahoo.com', '07618a19-b17c-4437-8619-505af0f6906d');
INSERT INTO monitoring_system.user (id, username, email, uuid) VALUES (2, 'def', 'def@gmail.com', '0c360165-b418-4caa-acea-92be46dc15a0');
INSERT INTO user_sequence (next_val) VALUES (3);

create table endpoint
(
    id            int          not null
        primary key,
    name          varchar(45)  not null,
    url           varchar(255) not null,
    time_interval int          not null,
    user_id       int          not null,
    created_on    timestamp    null,
    last_visited  timestamp    not null,
    constraint endpoint_user_id_fk
        foreign key (user_id) references user (id)
)
    charset = utf8mb4;

create index endpoint_user_fk_idx
    on endpoint (user_id);

INSERT INTO monitoring_system.endpoint (id, name, url, time_interval, user_id, created_on, last_visited) VALUES (1, 'Google', 'https://www.google.com', 19, 1, '2021-03-21 18:47:26', '2021-04-15 01:44:05');
INSERT INTO monitoring_system.endpoint (id, name, url, time_interval, user_id, created_on, last_visited) VALUES (2, 'Steam', 'https://translate.google.com/', 7, 2, '2021-03-29 22:55:02', '2021-04-15 01:44:04');
INSERT INTO endpoint_sequence (next_val) VALUES (3);

create table result
(
    id          int       not null
        primary key,
    payload     longtext  not null,
    endpoint_id int       null,
    status_code int       null,
    updated_on  timestamp null,
    constraint result_endpoint_fk
        foreign key (endpoint_id) references endpoint (id)
            on update cascade on delete cascade
)
    charset = utf8mb4;

