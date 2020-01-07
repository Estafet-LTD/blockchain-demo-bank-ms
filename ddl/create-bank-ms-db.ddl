create sequence ACCOUNT_ID_SEQ start 1 increment 1;
create table ACCOUNT (ACCOUNT_ID int4 not null, ACCOUNT_NAME varchar(255) not null, PUBLIC_KEY varchar(255) not null, WALLET_ID varchar(255) not null, primary key (ACCOUNT_ID));
create table TRANSACTION (TRANSACTION_ID int4 not null, AMOUNT float8 not null, CLEARED boolean not null, STATUS varchar(255) not null, ACCOUNT_ID int4 not null, primary key (TRANSACTION_ID));
alter table TRANSACTION add constraint TRANSACTION_TO_ACCOUNT_FK foreign key (ACCOUNT_ID) references ACCOUNT;
