create database shopping;
use shopping;
CREATE TABLE IF NOT EXISTS `customer` (
	id int NOT NULL unique primary key,
	`email` varchar(200) NOT NULL unique,
	`password` varchar(200) not NULL
);

CREATE TABLE IF NOT EXISTS `order` (
	id int NOT NULL unique primary key,
	cust_id int, 
    `date` datetime,
    foreign key (cust_id) references `customer`(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS `item` (
	id int NOT NULL unique primary key,
	`name` varchar(200) unique not NULL,
	price  decimal(10,2) not null
);

CREATE TABLE IF NOT EXISTS `order_item` (
	oid int,
    iid int,
    qty int,
    UNIQUE KEY `my_uniq_id` (`oid`,`iid`),
    foreign key (oid) references `order`(id) on delete cascade,
    foreign key (iid) references `item`(id) on delete cascade
);

