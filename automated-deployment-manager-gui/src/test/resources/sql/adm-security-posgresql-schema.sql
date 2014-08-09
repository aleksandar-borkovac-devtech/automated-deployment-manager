
    create table ADM_PRIVILEGES (
        ID int8 not null unique,
        CREATED timestamp not null,
        CREATED_BY varchar(255) not null,
        ALTERED timestamp,
        ALTERED_BY varchar(255),
        DESCRIPTION varchar(255),
        NAME varchar(255) not null,
        VALID bool not null,
        SCP_ID int8 not null,
        primary key (ID)
    );

    create table ADM_ROLES (
        ID int8 not null unique,
        CREATED timestamp not null,
        CREATED_BY varchar(255) not null,
        ALTERED timestamp,
        ALTERED_BY varchar(255),
        DESCRIPTION varchar(255),
        FROZEN bool not null,
        NAME varchar(255) not null,
        VALID bool not null,
        SCP_ID int8 not null,
        primary key (ID)
    );

    create table ADM_ROLE_PRIVILEGES (
        ROL_ID int8 not null,
        PRV_ID int8 not null,
        primary key (ROL_ID, PRV_ID)
    );

    create table ADM_SCOPES (
        ID int8 not null unique,
        CREATED timestamp not null,
        CREATED_BY varchar(255) not null,
        DESCRIPTION varchar(255),
        NAME varchar(255) not null unique,
        primary key (ID)
    );

    create table ADM_SCOPE_MANAGERS (
        USR_ID int8 not null,
        SCP_ID int8 not null,
        primary key (USR_ID, SCP_ID)
    );

    create table ADM_USERS (
    	USERNAME varchar(4000),
    	PASSWORD varchar(100),    
        ACTIVE char(1) not null,
        ACTIVE_FROM timestamp,
        ACTIVE_UNTIL timestamp,
        ALTERED timestamp,
        ALTERED_BY varchar(255),
        BLOCKED char(1) not null,
        COMMENTS varchar(255),
        CREATED timestamp not null,
        CREATED_BY varchar(255) not null,
        LAST_LOGIN_DATE timestamp,
        id int8 not null,
        primary key (id)
    );

    create table ADM_USER_ROLES (
        ID int8 not null unique,
        CREATED timestamp not null,
        CREATED_BY varchar(255) not null,
        ALTERED timestamp,
        ALTERED_BY varchar(255),
        ACTIVE char(1) not null,
        ACTIVE_FROM timestamp not null,
        ACTIVE_UNTIL timestamp,
        ROL_ID int8 not null,
        USR_ID int8 not null,
        primary key (ID)
    );

    alter table ADM_PRIVILEGES 
        add constraint FK_SCOPE_PRIVILEGE 
        foreign key (SCP_ID) 
        references ADM_SCOPES;

    alter table ADM_ROLES 
        add constraint FK_SCOPE_ROLE 
        foreign key (SCP_ID) 
        references ADM_SCOPES;

    alter table ADM_ROLE_PRIVILEGES 
        add constraint FK_ROLE_PRIVILEGE 
        foreign key (ROL_ID) 
        references ADM_ROLES;

    alter table ADM_ROLE_PRIVILEGES 
        add constraint FK_PRIVILEGE_ROLE 
        foreign key (PRV_ID) 
        references ADM_PRIVILEGES;

    alter table ADM_SCOPE_MANAGERS 
        add constraint FK_USER_SCOPE_MANAGER 
        foreign key (USR_ID) 
        references ADM_USERS;

    alter table ADM_SCOPE_MANAGERS 
        add constraint FK_SCOPE_USER 
        foreign key (SCP_ID) 
        references ADM_SCOPES;

    alter table ADM_USER_ROLES 
        add constraint FK_USER_ROLE_ROLE 
        foreign key (ROL_ID) 
        references ADM_ROLES;

    alter table ADM_USER_ROLES 
        add constraint FK_USER_ROLE 
        foreign key (USR_ID) 
        references ADM_USERS;

    create sequence ADM_PRIVILEGES_SEQ;

    create sequence ADM_ROLES_SEQ;

    create sequence ADM_SCOPES_SEQ;

    create sequence ADM_USER_ROLES_SEQ;
