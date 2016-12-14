drop table if exists `ddtest`.`queue`;

create table `ddtest`.`queue`(
	`id` bigint(20) not null default 0 comment '排队记录id',
	`patient_id` bigint(20) not null default 0 comment '病人id',
	`patient_name` varchar(40) not null default '' comment '病人名称',
	`doctor_id` bigint(20) not null default 0 comment '名医id',
	`hospital_id` bigint(20) not null default 0 comment '医院id',
	`status` tinyint(4) not null default 0 comment '排队状态，0-排队中，1-已完成，2-失败，3-取消',
	`appointment_time` datetime not null default '2015-12-20' comment '预约日期',
	`add_time` datetime not null default '2015-12-20' comment '排队时间(记录添加时间)',
	primary key (`id`),
	unique index `idx_patientid_addtime` (`patient_id`, `add_time`)
)
engine=InnoDB
default character set=utf8
comment='排队记录表';

select * from queue where patient_id=1;