pageQuery
===
SELECT
@pageTag() {	
    debug.ID,
	debug.VIN,
	debug.test_status,
	FROM_UNIXTIME(debug.debug_begin_time/1000,'%Y-%m-%d') debug_begin_time,
	FROM_UNIXTIME(debug.update_time/1000,'%Y-%m-%d') updateTime,
	debug.`STATUS`,
	FROM_UNIXTIME(debug.debug_end_time/1000,'%Y-%m-%d') debug_end_time,
	bu.LOGIN_NAME,
	org.ORG_NAME,
	ter.`CODE` terminalCode,
	ter.id terminalId,
	card.`CODE` cardCode,
	vt.`NAME` typeName,
	vm.`NAME` modelName,
	vr.ACC,
	vr.GPS,
	vr.`LOCK`,
	vr.last_lon lon,
    vr.last_lat lat,
	vr.ONE_LEVEL_LOCK,
    vr.TWO_LEVEL_LOCK,
    vr.THREE_LEVEL_LOCK,
	concat(vr.gps_province,vr.gps_city,vr.gps_area) GPS_ADDRESS,
	vr.data_update_time
 @}	
FROM
	v_vehicle_debug debug
	LEFT JOIN v_vehicle vehicle ON debug.VIN = vehicle.VIN
	@if(!isEmpty(organizationId)){
            INNER JOIN 
            	( SELECT 
            	  id FROM base_organization 
            	  WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
            	) temp
            	   ON vehicle.ORGANIZATION_ID = temp.id
    @}
	LEFT JOIN base_organization org ON vehicle.ORGANIZATION_ID=org.ID 
	LEFT JOIN t_terminal ter ON vehicle.TERMINAL_ID=ter.id
	LEFT JOIN t_simcard card ON ter.SIMCARD_ID=card.id
	LEFT JOIN v_vehicle_type vt ON vehicle.VEHICLE_TYPE_ID=vt.id
	LEFT JOIN v_vehicle_model vm ON vehicle.VEHICLE_MODEL_ID=vm.id
	LEFT JOIN v_vehicle_realtime vr ON debug.VIN=vr.VIN
	LEFT JOIN base_user bu ON debug.DEBUG_USER_ID=bu.ID
WHERE
    1=1
    @if(!isEmpty(status)){
    and debug.`STATUS`=#status#	
    @}
    @if(!isEmpty(financeId)){
            and vehicle.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vin)){
     and (debug.vin like #vin# or ter.`CODE` like #vin# or card.`CODE` like #vin#) 
    @}
    order by debug.create_time desc

getVehicleDebugLogList
===
SELECT
@pageTag() {	
    debug.ID,
	debug.VIN,
	FROM_UNIXTIME(debug.debug_begin_time/1000,'%Y-%m-%d') debug_begin_time,
	FROM_UNIXTIME(debug.update_time/1000,'%Y-%m-%d %H:%i:%S') updateTime,
    debug.`STATUS`,
    FROM_UNIXTIME(debug.debug_end_time/1000,'%Y-%m-%d') debug_end_time,
	bu.LOGIN_NAME,
	org.ORG_NAME,
	ter.`CODE` terminalCode,
	ter.id terminalId,
	card.`CODE` cardCode,
	vt.`NAME` typeName,
	vm.`NAME` modelName,
	vr.ACC,
	vr.GPS,
	vr.`LOCK`,
	vr.last_lon lon,
	vr.last_lat lat,
	vr.ONE_LEVEL_LOCK,
    vr.TWO_LEVEL_LOCK,
    vr.THREE_LEVEL_LOCK,
	concat(vr.gps_province,vr.gps_city,vr.gps_area) GPS_ADDRESS,
	vr.data_update_time
 @}	
FROM
	v_vehicle_debug debug
	LEFT JOIN v_vehicle vehicle ON debug.VIN = vehicle.VIN
	@if(!isEmpty(organizationId)){
         INNER JOIN 
           ( SELECT 
                id FROM base_organization 
             WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
           ) temp
             ON vehicle.ORGANIZATION_ID = temp.id
    @}
	LEFT JOIN base_organization org ON vehicle.ORGANIZATION_ID=org.ID 
	LEFT JOIN t_terminal ter ON vehicle.TERMINAL_ID=ter.id
	LEFT JOIN t_simcard card ON ter.SIMCARD_ID=card.id
	LEFT JOIN v_vehicle_type vt ON vehicle.VEHICLE_TYPE_ID=vt.id
	LEFT JOIN v_vehicle_model vm ON vehicle.VEHICLE_MODEL_ID=vm.id
	LEFT JOIN v_vehicle_realtime vr ON debug.VIN=vr.VIN
	LEFT JOIN base_user bu ON debug.DEBUG_USER_ID=bu.ID
WHERE
    1=1
        and debug.`STATUS` <> 0
    @if(!isEmpty(financeId)){
        and vehicle.FINANCE_ID = #financeId#
    @}    
    @if(!isEmpty(status)){
        and debug.`STATUS`=#status#	
    @}
    @if(!isEmpty(vin)){
        and (debug.vin like #vin# or ter.`CODE` like #vin# or card.`CODE` like #vin#) 
    @}
    order by debug.create_time desc
    
getVehicleDebugById
===
SELECT
	debug.VIN,
	org.ORG_NAME,
	vehicle.ORGANIZATION_ID,
	vehicle.VEHICLE_MODEL_ID,
	ter.id,
	ter.`CODE` terminalCode,
	card.`CODE` cardCode
FROM
	v_vehicle_debug debug
	LEFT JOIN v_vehicle vehicle ON debug.VIN = vehicle.VIN
	LEFT JOIN base_organization org ON vehicle.ORGANIZATION_ID=org.ID 
	LEFT JOIN t_terminal ter ON vehicle.TERMINAL_ID=ter.id
	LEFT JOIN t_simcard card ON ter.SIMCARD_ID=card.id
	LEFT JOIN v_vehicle_model vm ON vehicle.VEHICLE_MODEL_ID=vm.id
WHERE
  1=1
  @if(!isEmpty(id)){
    and debug.id=#id#   
  @}
  
getCountByStatus
===
SELECT
	debug.`STATUS`,
	COUNT(1) count 
FROM
	v_vehicle_debug debug 
GROUP BY
	debug.`STATUS`


getCmdDebugByInfo
===
SELECT 
    vcd.id,
    vcd.cmd
FROM v_cmd_debug vcd 
WHERE 
 1=1
 @if(!isEmpty(orgId)){
  and vcd.ORGANIZATION_ID = #orgId#	
 @}
 @if(!isEmpty(vehicleModelId)){
  and vcd.vehicle_model_id = #vehicleModelId#	
 @}
 
getDebugLogByVin
===
 SELECT
 	log.id,
 	log.vin,
 	log.ITEM_KEY,
 	log.ITEM_NAME,
 	log.`STATUS`,
 	log.CONTENT,
 	log.DEBUG_USER_ID,
 	log.DEBUG_TIME 
 FROM
 	v_vehicle_debug_log log
 where
    1=1
  @if(!isEmpty(vin)){
    and log.vin = #vin#	
   @}  
   
getVehicleList
===
SELECT
    @pageTag() {
	vv.VIN,
	tt.`CODE` terminalCode,
	ts.`CODE` cardCode,
	org.ORG_NAME orgName,
	vvt.`NAME` typeName,
	vvm.`NAME` modelName 
	@}
FROM
	v_vehicle vv
	@if(!isEmpty(organizationId)){
    	    INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) temp
    	    ON vv.ORGANIZATION_ID = temp.id
     @}
	LEFT JOIN t_terminal tt ON vv.TERMINAL_ID = tt.id
	LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
	LEFT JOIN base_organization org ON vv.ORGANIZATION_ID = org.ID
	LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID = vvt.id
	LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID = vvm.id
where 
    1=1
    @if(!isEmpty(vehicleTypeId)){
       and vvt.id = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
       and vvm.id = #vehicleModelId#
    @}	
   @if(!isEmpty(vin)){
       and (vv.vin like #vin# or tt.`CODE` like #vin# or ts.`CODE` like #vin#) 
   @}
   @if(!isEmpty(vinList)){
       and vv.vin not in ( #join(vinList)#)
   @}   
   
getVinListInDebug
===
SELECT
	vvd.VIN 
FROM
	v_vehicle_debug vvd
LEFT JOIN v_vehicle vehicle ON vvd.VIN = vehicle.VIN	
@if(!isEmpty(organizationId)){
	INNER JOIN 
	( SELECT id 
	  FROM base_organization 
	  WHERE path LIKE 
	  CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON vehicle.ORGANIZATION_ID = temp.id
 @}
      
vehicleLockList
===
 SELECT
 	b.* 
 FROM
 	(
 SELECT
 	a.*,
 	d.ITEM_CODE,
 	d.ITEM_NAME
 FROM
 	(
 SELECT
 	v.VIN,
 	sv.id SOFT_VERSION_ID,
 	s.id FUNCTION_id 
 FROM
 	v_vehicle v
 	INNER JOIN t_terminal t ON t.id = v.TERMINAL_ID
 	INNER JOIN t_soft_version sv ON sv.id = t.SOFT_VERSION_ID
 	INNER JOIN t_function_set s ON sv.LOCK_FUNCTION_ID = s.id 
 	) a
 	INNER JOIN t_function_set_item_lock l ON l.FUNCTION_ID = a.FUNCTION_id 
 	INNER JOIN base_dic_item d ON d.id = l.DIC_ITEM_ID
 	) b
 	LEFT JOIN v_vehicle v ON b.VIN = v.VIN
 @if(!isEmpty(organizationId)){
 	    INNER JOIN 
 	    ( SELECT 
 	    id FROM base_organization 
 	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
 	    ) temp
 	    ON v.ORGANIZATION_ID = temp.id
     @}
 	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
 	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
 	LEFT JOIN v_vehicle_debug debug ON v.VIN=debug.VIN
 	where 1=1
 	    @if(!isEmpty(status)){
            and debug.`STATUS`=#status#	
        @}
        @if(!isEmpty(vin)){
            and (v.vin like #vin# or t.`CODE` like #vin# or s.`CODE` like #vin#) 
        @}
        and b.vin in ( #join(vinList)#)   	
        
getDebugLogList
===
SELECT
	vdl.VIN,
	vdl.ITEM_KEY,
	vdl.ITEM_NAME,
	vdl.`STATUS`,
	vdl.CONTENT 
FROM
	v_vehicle_debug_log vdl
where 
    vdl.vin=#vin#	 

getDebugIdByVehicleId
===    
SELECT
	vvd.ID 
FROM
	v_vehicle_debug vvd
	LEFT JOIN v_vehicle vv ON vvd.VIN = vv.VIN 
WHERE
	vv.id = #vehicleId#           