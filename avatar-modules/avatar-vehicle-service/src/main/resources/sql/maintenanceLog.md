pageQuery
===
SELECT
@pageTag() {
	vml.ID,
	vml.VIN,
	org.ORG_NAME,
	vvt.`NAME` vehicleTypeName,
	vvm.`NAME` vehicleModelName,
	vml.REMIND_HOURS,
	vml.MAINTENANCE_TYPE,
	vml.INTERVAL_HOURS,
	vml.REMIND_TIME,
	vml.HANDLE_STATUS,
	vml.HANDLE_RESULT,
	vml.HANDLE_TIME,
	vml.HANDLE_USER_ACCOUNT 
	@}
FROM
	v_maintenance_log vml
inner JOIN v_vehicle vv ON vml.VIN=vv.VIN
@if(!isEmpty(organizationId)){
                INNER JOIN 
                	( SELECT 
                	  id FROM base_organization 
                	  WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
                	) temp
                	   ON vv.ORGANIZATION_ID = temp.id
        @}
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
where 
    1=1
    @if(!isEmpty(vehicleTypeId)){
         and vvt.id = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
         and vvm.id = #vehicleModelId#	
    @}
    @if(!isEmpty(handleStatus)){
         and vml.HANDLE_STATUS = #handleStatus#
    @}
    @if(!isEmpty(vin)){
         and vv.vin like #vin#	
    @}
order by vml.HANDLE_STATUS    

count
===
SELECT
	count( 1 ) 
FROM
	v_maintenance_log log
inner JOIN v_vehicle vv ON log.VIN=vv.VIN	 
WHERE
	log.HANDLE_STATUS =0 
	
getVehicleInfo
===
SELECT
	vv.VEHICLE_TYPE_ID,
	vv.VEHICLE_MODEL_ID,
	vv.VIN,
	IFNULL(ROUND(vvr.TOTAL_WORK_TIME/3600,2),0) TOTAL_WORK_TIME
FROM
	v_vehicle vv
	INNER JOIN v_vehicle_realtime vvr ON vv.VIN=vvr.VIN
where 		
     vv.vehicle_type_id is not null
 and vv.vehicle_model_id is not null
     
getMaintenanceInfo
===
SELECT
	vmi.MAINTENANCE_TYPE,
	vmi.MAINTENANCE_CONTENT,
	vmi.HOURS,
	vmi.TACTICS_ID 
FROM
	v_maintenance_info vmi
	LEFT JOIN v_maintenance_bind vmb ON vmi.TACTICS_ID = vmb.MAINTENANCE_TACTICS_ID
where 
    1=1
    @if(!isEmpty(vehicleTypeId)){
          and vmb.vehicle_type_id = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
          and vmb.vehicle_model_id = #vehicleModelId#	
    @}	
    
getMaintenanceInfoByInfo
===
SELECT
	vmi.* 
FROM
	v_maintenance_info vmi 
WHERE 
	  vmi.TACTICS_ID=#tacticsId#
  and vmi.MAINTENANCE_TYPE=#maintenanceType#
  and vmi.MAINTENANCE_CONTENT=#maintenanceContent#
 
getLogList
=== 
SELECT
	vml.* 
FROM
	v_maintenance_log vml
WHERE 
	  vml.vin=#vin#
  and vml.MAINTENANCE_TYPE=#maintenanceType#
  and vml.MAINTENANCE_CONTENT=#maintenanceContent#	 
ORDER BY
	vml.REMIND_TIME DESC  	        	    
         
         