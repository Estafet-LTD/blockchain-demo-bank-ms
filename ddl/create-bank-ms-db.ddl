create sequence ACCOUNT_ID_SEQ start 1 increment 1;
create sequence TRANSACTION_ID_SEQ start 1 increment 1;
create table ACCOUNT (ACCOUNT_ID int4 not null, ACCOUNT_NAME varchar(255) not null, CURRENCY varchar(255) not null, PUBLIC_KEY varchar(255) not null, WALLET_ADDRESS varchar(255) not null, primary key (ACCOUNT_ID));
create table MESSAGE_EVENT (TOPIC_ID varchar(255) not null, MESSAGE_REFERENCE varchar(255) not null, VERSION int4, primary key (TOPIC_ID));
create table TRANSACTION (TRANSACTION_ID int4 not null, AMOUNT float8 not null, STATUS varchar(255) not null, WALLET_TRANSACTION_ID varchar(255), ACCOUNT_ID int4 not null, primary key (TRANSACTION_ID));
alter table TRANSACTION add constraint TRANSACTION_TO_ACCOUNT_FK foreign key (ACCOUNT_ID) references ACCOUNT;
