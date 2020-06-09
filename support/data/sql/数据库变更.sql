/* 手机设置 */
alter table base_user add COLUMN APP_CONFIG VARCHAR(500) NULL DEFAULT NULL COMMENT 'APP配置' AFTER `CONTACT_ADDRESS`;


/* 报警历史增加索引 */
create index IDX_ALARM_HISTORY_VIN on tls_alarm_history (VIN);
create index IDX_ALARM_HISTORY_BGT on tls_alarm_history (BEGIN_TIME);
create index IDX_ALARM_HISTORY_ORG on tls_alarm_history (ORGANIZATION_ID);


ALTER TABLE `tls_alarm_history` ADD COLUMN `DEAL_ACCOUNT` VARCHAR(50) NULL DEFAULT NULL COMMENT '处理人账号' AFTER `DEAL_TIME`;


create index IDX_VEHICLE_TID on tls_vehicle_base_info (TERMINAL_ID);

create index IDX_DIC_ITEM_DCODE on base_dic_item (DIC_CODE);
create index IDX_DIC_ITEM_CODE on base_dic_item (ITEM_CODE);

create index IDX_AREA_CODE on base_area (AREA_CODE);

create index IDX_AUTHORIZE_OBJ_ID on base_authorize (AUTHORIZE_OBJECT_ID);

create index IDX_FAULT_DICT_ITEM_PID on base_fault_dict_item (DICT_ID);
create index IDX_FAULT_DICT_ITEM_CODE on base_fault_dict_item (CODE);

create index IDX_LOG_ACCOUNT on base_log (OPERATE_ACCOUNT);
create index IDX_LOG_ORG_ID on base_log (ORGANIZATION_ID);

CREATE TABLE `tls_user_alarm_info` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`user_Id` BIGINT NULL DEFAULT NULL,
	`alarm_id` BIGINT NULL DEFAULT NULL,
	`read_Flag` TINYINT NULL DEFAULT 0,
	PRIMARY KEY (`ID`)
)
;


create index IDX_USER_ALARM_UID on tls_user_alarm_info (USER_ID);
create index IDX_USER_ALARM_AID on tls_user_alarm_info (ALARM_ID);

create index IDX_AREA_USER_AREA on tls_area_user (AREA_CODE);
create index IDX_AREA_USER_UID on tls_area_user (USER_ID);

create index IDX_CHIP_MODEL_TID on tls_chip_model (TENANT_ID);
create index IDX_CHIP_MODEL_CODE on tls_chip_model (CODE);

create index IDX_ENGINE_MODEL_TID on tls_engine_model (TENANT_ID);
create index IDX_ENGINE_MODEL_CODE on tls_engine_model (CODE);

create index IDX_MAINT_PLAN_ORGID on tls_maintenance_plan (ORG_ID);
create index IDX_MAINT_PLAN_VMID on tls_maintenance_plan (VEHICLE_MODEL_ID);

create index IDX_MAINT_PLAN_ITEM_PID on tls_maintenance_plan_item (maintenance_plan_id);

create index IDX_MON_SETTING_ORGID on tls_monitor_setting (ORGANIZATION_ID, CODE);
create index IDX_NOTICE_TRATE_ORG_CODE on tls_notice_strategy (ORGANIZATION_ID, CODE);

create index IDX_RPT_RANGE_DAILY_VAL on tls_report_range_daily (VIN, DATE_VAL, TYPE);

create index IDX_TERMINAL_CODE on tls_terminal (CODE);
create index IDX_TERMINAL_MID on tls_terminal (MODEL_ID);

create index IDX_TERMINAL_CLOG_CODE on tls_terminal_change_log (CODE);

create index IDX_TERMINAL_MODEL_TID on tls_terminal_model (TENANT_ID);
create index IDX_TERMINAL_MODEL_CODE on tls_terminal_model (CODE);


create index IDX_VA_STATUS_ORGID on tls_vehicle_alarm_status (ORGANIZATION_ID);

create index IDX_VEHICLE_GROUP_ORGID on tls_vehicle_group (ORGANIZATION_ID);

create index IDX_VEHICLE_MODEL_TID on tls_vehicle_model (TENANT_ID);
create index IDX_VEHICLE_MODEL_CODE on tls_vehicle_model (PUBLISH_CODE);

create index IDX_VEHICLE_TCHANGE_VIN on tls_vehicle_terminal_change (VIN);

create index IDX_VEHICLE_TRAVEL_STIME on tls_vehicle_travel (START_TIME);
