alter table TRANSACTION drop constraint TRANSACTION_TO_ACCOUNT_FK;
drop table if exists ACCOUNT cascade;
drop table if exists TRANSACTION cascade;
drop sequence ACCOUNT_ID_SEQ;
