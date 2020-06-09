pageQuery
===
SELECT
	@pageTag() {
	vmt.ID,
	vmt.TACTICS_NAME,
	org.ORG_NAME,
	vmt.CREATE_USER_ACCOUNT,
	vmt.CREATE_TIME
	@} 
FROM
	v_maintenance_tactics vmt
	@if(!isEmpty(organizationId)){
                INNER JOIN 
                	( SELECT 
                	  id FROM base_organization 
                	  WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
                	) temp
                	   ON vmt.ORGANIZATION_ID = temp.id
        @}
	LEFT JOIN base_organization org ON vmt.ORGANIZATION_ID = org.ID
where 1=1
  @if(!isEmpty(tacticsName)){
     and vmt.TACTICS_NAME like #tacticsName#	
  @}	
  
getBindListByTacticsId
===
SELECT
	vmb.id,
	vmb.MAINTENANCE_TACTICS_ID,
	vmb.VEHICLE_TYPE_ID,
	vmb.VEHICLE_MODEL_ID,
	vvt.`NAME` typeName,
    vvm.`NAME` modelName 
FROM
	v_maintenance_bind vmb
left join v_vehicle_type vvt on vmb.VEHICLE_TYPE_ID=vvt.id
left join v_vehicle_model vvm on vmb.VEHICLE_MODEL_ID=vvm.id	 
WHERE
	vmb.MAINTENANCE_TACTICS_ID = #tacticsId#  