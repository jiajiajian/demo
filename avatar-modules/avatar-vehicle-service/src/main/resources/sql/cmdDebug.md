pageQuery
===
SELECT
@pageTag() {
    cd.ID,
    org.ORG_NAME,
    vm.`NAME` vehicle_type_name,
    cd.CMD,
    cd.UPDATE_USER_ACCOUNT,
    cd .UPDATE_TIME
@}
FROM
v_cmd_debug cd
LEFT JOIN base_organization org ON org.ID = cd.ORGANIZATION_ID
LEFT JOIN v_vehicle_type vm ON vm.id = cd.VEHICLE_TYPE_ID
where 1=1
@if(!isEmpty(organizationId)){
    and cd.ORGANIZATION_ID = #organizationId#
@} 
@if(!isEmpty(vehicleTypeId)){
    and cd.VEHICLE_TYPE_ID = #vehicleTypeId#
@}

cols
===
	ID,ORGANIZATION_ID,VEHICLE_TYPE_ID,VEHICLE_MODEL_ID,CMD,REMARK,CREATE_TIME,CREATE_USER_ACCOUNT,CREATE_USER_REALNAME

updateSample
===
	
	ID=#id#,ORGANIZATION_ID=#organizationId#,VEHICLE_TYPE_ID=#vehicleTypeId#,VEHICLE_MODEL_ID=#vehicleModelId#,CMD=#cmd#,REMARK=#remark#,CREATE_TIME=#createTime#,CREATE_USER_ACCOUNT=#createUserAccount#,CREATE_USER_REALNAME=#createUserRealname#

condition
===

	1 = 1  
	@if(!isEmpty(id)){
	 and ID=#id#
	@}
	@if(!isEmpty(organizationId)){
	 and ORGANIZATION_ID=#organizationId#
	@}
	@if(!isEmpty(vehicleTypeId)){
	 and VEHICLE_TYPE_ID=#vehicleTypeId#
	@}
	@if(!isEmpty(vehicleModelId)){
	 and VEHICLE_MODEL_ID=#vehicleModelId#
	@}
	@if(!isEmpty(cmd)){
	 and CMD=#cmd#
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
	
	