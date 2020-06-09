pageQuery
===
SELECT
@pageTag() {
    ai.id,
	a.ID ORGANIZATION_ID ,
	a.ORG_NAME,
	ai.ALARM_ITEM,
	ai.UPDATE_USER_ACCOUNT,
	FROM_UNIXTIME(ai.UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS UPDATE_TIME
@}
FROM
@if(!isEmpty(organizationId)){
	( SELECT ID,ORG_NAME 
	    FROM base_organization 
	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) a
  @}   
@if(isEmpty(organizationId)){
    base_organization a
  @}   
	INNER JOIN v_alarm_item ai ON ai.ORGANIZATION_ID = a.id

cols
===
	ID,ORGANIZATION_ID,ALARM_ITEM,REMARK,CREATE_TIME,CREATE_USER_ACCOUNT,CREATE_USER_REALNAME

updateSample
===
	
	ID=#id#,ORGANIZATION_ID=#organizationId#,ALARM_ITEM=#alarmItem#,REMARK=#remark#,CREATE_TIME=#createTime#,CREATE_USER_ACCOUNT=#createUserAccount#,CREATE_USER_REALNAME=#createUserRealname#

condition
===

	1 = 1  
	@if(!isEmpty(id)){
	 and ID=#id#
	@}
	@if(!isEmpty(organizationId)){
	 and ORGANIZATION_ID=#organizationId#
	@}
	@if(!isEmpty(alarmItem)){
	 and ALARM_ITEM=#alarmItem#
	@}
	@if(!isEmpty(remark)){
	 and REMARK=#remark#
	@}
	@if(!isEmpty(createTime)){
	 and CREATE_TIME=#createTime#
	@}
	@if(!isEmpty(createUserAccount)){
	 and CREATE_USER_ACCOUNT=#createUserAccount#
	@}
	@if(!isEmpty(createUserRealname)){
	 and CREATE_USER_REALNAME=#createUserRealname#
	@}
findDictItem
===
SELECT
ITEM_CODE,
ITEM_NAME
FROM
base_dic_item
WHERE
DIC_CODE = #code#