findUserListByIds
===
SELECT
	u.ID,
	u.REALNAME AS NAME 
FROM
	base_user u 
WHERE
	u.ID IN (#join(userIdArr)#)
	
findRoleListByIds
===
SELECT
	u.ID,
	u.ROLE_NAME AS NAME 
FROM
	base_role u 
WHERE
	u.ID IN (#join(roleIdArr)#)

userOptions
===
SELECT
	REALNAME `NAME`,
	ID 
FROM
	base_user 
WHERE
	USER_TYPE = #userType# 
	and DEL_FLAG = 0
    @if(userType == 2){
        AND ORGANIZATION_ID = #organizationId#
    @}
    @if(userType == 3){
        AND FINANCE_ID = #organizationId#
    @}
	
roleOptions
===
SELECT
	ROLE_NAME `NAME`,
	ID 
FROM
	base_role 
WHERE
	ROLE_TYPE = #roleType# 
    @if(roleType == 2){
        AND ORGANIZATION_ID = #organizationId#
    @}
    @if(roleType == 3){
        AND FINANCE_ID = #financeId#
    @}
	
findStrategiesForFaultOrAlarm
===
SELECT
	* 
FROM
	v_notice_strategy n
	WHERE n.ORG_TYPE = 1
	AND n.`CODE` = #alarmType#
    AND n.organization_id in (#join(orgs)#)
	
findStrategiesForFence
===
SELECT
	* 
FROM
	v_notice_strategy n
	where
	n.ORG_TYPE = #orgType#
	and n.ORGANIZATION_ID = #organizationId#
	AND n.`CODE` = 'FENCE'
	
orgPath
===
SELECT path 
	FROM base_organization WHERE id = #orgId#
	
alarmItemName
===
select ITEM_NAME from base_dic_item where DIC_CODE = 'ALARM_ITEM' and ITEM_CODE = #code#