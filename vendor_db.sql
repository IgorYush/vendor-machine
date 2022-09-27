CREATE SCHEMA vendor;

create table vendor.wallet (
    id UUID primary key not null,
    amount integer not null
);

create table vendor.users (
    id UUID primary key not null,
    username varchar(20) not null,
    password text not null,
    created_date timestamp,
    active boolean default false,
    wallet_id UUID not null,
    constraint fk_users_wallet foreign key (wallet_id) REFERENCES vendor.wallet (id)
);

create table vendor.roles (
    id UUID primary key not null,
    name varchar(20) not null
);

create table vendor.users_roles (
    primary key (user_id, role_id),
    user_id UUID not null,
    role_id UUID not null,
    constraint fk_user foreign key (user_id) references vendor.users (id),
    constraint fk_role foreign key (role_id) references vendor.roles (id)
);

create table vendor.product (
    id UUID primary key not null,
    cost integer not null,
    name varchar(20),
    creator_id UUID not null,
    constraint fk_user_creator foreign key (creator_id) references vendor.users (id)
);

ALTER TABLE vendor.users ALTER COLUMN active SET DEFAULT FALSE;


INSERT into vendor.roles VALUES ('8dd8c73a-06bf-4e1b-9959-8b08006d54ec', 'ROLE_USER');

INSERT into vendor.roles VALUES ('ebb29ccb-7238-4833-833e-c3963d2d0e5f', 'ROLE_ADMIN');

INSERT into vendor.roles VALUES ('8af7afc4-df95-4a28-91ec-63940ae90077', 'ROLE_SELLER');

INSERT into vendor.roles VALUES ('8bb71819-40ff-41b5-8052-0517ae497965', 'ROLE_BUYER');


INSERT into vendor.wallet  VALUES ('29c3e8f5-87aa-4622-9dc0-b35178b7ce1a', 0);


INSERT into vendor.users VALUES ('1f1c5edf-6b03-4376-bf04-beadbb9960b8', 'admin', '$2a$10$CnP5l7XUT4QwBNd2shJ0refV.hN69kmdAUDrSo.6sf.ZjgWiUdaH6', '2022-09-27 00:18:56.808', false, '29c3e8f5-87aa-4622-9dc0-b35178b7ce1a');