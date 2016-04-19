drop table if exists account;
create table account
(
  id character(10) not null,
  accountHolderName character varying(100),
  accountCurrency character varying(10),
  balance float,
  constraint accountPK primary key (id)
);