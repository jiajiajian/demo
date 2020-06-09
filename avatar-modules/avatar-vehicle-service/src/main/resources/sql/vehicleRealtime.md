pageQuery
===
select @pageTag() {
    vvr.vin,
    vvr.data_update_time 
    @} 
    from 
        v_vehicle_realtime vvr
    inner join v_vehicle vv on vvr.vin=vv.vin
    @if(!isEmpty(organizationId)){
        	INNER JOIN 
        	 ( SELECT 
        	   id FROM base_organization 
        	   WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
        	 ) temp
        	    ON vv.ORGANIZATION_ID = temp.id
    @}
         

sample
===
* 注释

	select #use("cols")# from v_vehicle_realtime  where  #use("condition")#

cols
===
	ID,VIN,TOTAL_WORK_TIME,ACC,GPS,GPS_TIME,GPS_PROVINCE,GPS_CITY,GPS_AREA,LON,LAT,GPS_ADDRESS,LOCK,DEBUG_START_TIME,DEBUG_END_TIME,DESCRIPTION,DATA_UPDATE_TIME

updateSample
===
	
	ID=#id#,VIN=#vin#,TOTAL_WORK_TIME=#totalWorkTime#,ACC=#acc#,GPS=#gps#,GPS_TIME=#gpsTime#,GPS_PROVINCE=#gpsProvince#,GPS_CITY=#gpsCity#,GPS_AREA=#gpsArea#,LON=#lon#,LAT=#lat#,GPS_ADDRESS=#gpsAddress#,LOCK=#lock#,DEBUG_START_TIME=#debugStartTime#,DEBUG_END_TIME=#debugEndTime#,DESCRIPTION=#description#,DATA_UPDATE_TIME=#dataUpdateTime#

condition
===

	1 = 1  
	@if(!isEmpty(id)){
	 and ID=#id#
	@}
	@if(!isEmpty(vin)){
	 and VIN=#vin#
	@}
	@if(!isEmpty(totalWorkTime)){
	 and TOTAL_WORK_TIME=#totalWorkTime#
	@}
	@if(!isEmpty(acc)){
	 and ACC=#acc#
	@}
	@if(!isEmpty(gps)){
	 and GPS=#gps#
	@}
	@if(!isEmpty(gpsTime)){
	 and GPS_TIME=#gpsTime#
	@}
	@if(!isEmpty(gpsProvince)){
	 and GPS_PROVINCE=#gpsProvince#
	@}
	@if(!isEmpty(gpsCity)){
	 and GPS_CITY=#gpsCity#
	@}
	@if(!isEmpty(gpsArea)){
	 and GPS_AREA=#gpsArea#
	@}
	@if(!isEmpty(lon)){
	 and LON=#lon#
	@}
	@if(!isEmpty(lat)){
	 and LAT=#lat#
	@}
	@if(!isEmpty(gpsAddress)){
	 and GPS_ADDRESS=#gpsAddress#
	@}
	@if(!isEmpty(lock)){
	 and LOCK=#lock#
	@}
	@if(!isEmpty(debugStartTime)){
	 and DEBUG_START_TIME=#debugStartTime#
	@}
	@if(!isEmpty(debugEndTime)){
	 and DEBUG_END_TIME=#debugEndTime#
	@}
	@if(!isEmpty(description)){
	 and DESCRIPTION=#description#
	@}
	@if(!isEmpty(dataUpdateTime)){
	 and DATA_UPDATE_TIME=#dataUpdateTime#
	@}
	
getDashBoardList
===
SELECT
@pageTag(){
	vv.VIN,
	tt.`CODE` terminalCode,
	org.ORG_NAME orgName,
	vvt.`NAME` typeName,
	vvm.`NAME` modelName,
	vvr.GPS_TIME,
	vvr.DATA_UPDATE_TIME,
	FROM_UNIXTIME(vvr.DATA_UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS UPDATE_TIME,
	vvr.last_lat lat,
	vvr.last_lon lon,
	vvr.GPS_ADDRESS
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
LEFT JOIN t_terminal tt ON vv.TERMINAL_ID=tt.id 	
LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
inner JOIN v_vehicle_realtime vvr ON vv.VIN = vvr.VIN
LEFT JOIN base_customer bc on vv.CUSTOMER_ID=bc.id
where
    1=1
    @if(!isEmpty(organizationId)){
            and vv.ORGANIZATION_ID = #organizationId#
    @}
    @if(!isEmpty(financeId)){
          and vv.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE_CODE = #province#
    @}
    @if(!isEmpty(city)){
            and vv.GPS_CITY_CODE = #city#
    @}
    @if(!isEmpty(area)){
            and vv.GPS_AREA_CODE = #area#
    @}
    @if(!isEmpty(lock)){
                @if(lock == 1){
                    and (vvr.`LOCK`=#lock# or vvr.ONE_LEVEL_LOCK=#lock# or vvr.TWO_LEVEL_LOCK=#lock# or vvr.THREE_LEVEL_LOCK=#lock#)
                @}
                @if(lock == 0){
                    and (vvr.`LOCK`=#lock# and vvr.ONE_LEVEL_LOCK=#lock# and vvr.TWO_LEVEL_LOCK=#lock# and vvr.THREE_LEVEL_LOCK=#lock#)
                @}
    @}
    @if(!isEmpty(acc)){
            and vvr.ACC = #acc#
    @}
    @if(!isEmpty(customerName)){
            and bc.`NAME` like #customerName#
    @}
    @if(!isEmpty(vin)){
            and (vv.vin like #vin# or ts.`CODE` like #vin# or tt.`CODE` like #vin#)
    @}    
    
getMapInfo
===
SELECT
	vv.VIN,
	vvr.DATA_UPDATE_TIME,
	vvr.last_LON lon,
	vvr.last_LAT lat
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
LEFT JOIN t_terminal tt ON vv.TERMINAL_ID=tt.id	
LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
LEFT JOIN v_vehicle_realtime vvr ON vv.VIN = vvr.VIN
LEFT JOIN base_customer bc on vv.CUSTOMER_ID=bc.id
where
    1=1
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(financeId)){
          and vv.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE_CODE = #province#
    @}
    @if(!isEmpty(city)){
            and vvr.GPS_CITY_CODE = #city#
    @}
    @if(!isEmpty(area)){
            and vvr.GPS_AREA_CODE = #area#
    @}
    @if(!isEmpty(lock)){
          @if(lock == 1){
             and (vvr.`LOCK`=#lock# or vvr.ONE_LEVEL_LOCK=#lock# or vvr.TWO_LEVEL_LOCK=#lock# or vvr.THREE_LEVEL_LOCK=#lock#)
          @}
          @if(lock == 0){
             and (vvr.`LOCK`=#lock# and vvr.ONE_LEVEL_LOCK=#lock# and vvr.TWO_LEVEL_LOCK=#lock# and vvr.THREE_LEVEL_LOCK=#lock#)
          @}
    @}
    @if(!isEmpty(acc)){
            and vvr.ACC = #acc#
    @}
    @if(!isEmpty(customerName)){
            and bc.`NAME` like #customerName#
    @}
    @if(!isEmpty(vin)){
            and (vv.vin like #vin# or ts.`CODE` like #vin# or tt.`CODE` like #vin#)
    @} 
        
getCountByProvince
===
SELECT
	vvr.GPS_PROVINCE province,
	count( 1 ) count
FROM
	v_vehicle_realtime vvr
inner JOIN v_vehicle vv ON vv.VIN = vvr.VIN
@if(!isEmpty(organizationId)){
    	INNER JOIN 
    	 ( SELECT 
    	   id FROM base_organization 
    	   WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	 ) temp
    	    ON vv.ORGANIZATION_ID = temp.id
        @}
LEFT JOIN t_terminal tt ON vv.TERMINAL_ID=tt.id	
LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
LEFT JOIN base_customer bc on vv.CUSTOMER_ID=bc.id	
where
    1=1
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(financeId)){
          and vv.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE_CODE = #province#
    @}
    @if(!isEmpty(city)){
            and vvr.GPS_CITY_CODE = #city#
    @}
    @if(!isEmpty(area)){
            and vvr.GPS_AREA_CODE = #area#
    @}
    @if(!isEmpty(lock)){
            @if(lock == 1){
                and (vvr.`LOCK`=#lock# or vvr.ONE_LEVEL_LOCK=#lock# or vvr.TWO_LEVEL_LOCK=#lock# or vvr.THREE_LEVEL_LOCK=#lock#)
            @}
            @if(lock == 0){
                and (vvr.`LOCK`=#lock# and vvr.ONE_LEVEL_LOCK=#lock# and vvr.TWO_LEVEL_LOCK=#lock# and vvr.THREE_LEVEL_LOCK=#lock#)
            @}
     @}
    @if(!isEmpty(acc)){
            and vvr.ACC = #acc#
    @}
    @if(!isEmpty(customerName)){
            and bc.`NAME` like #customerName#
    @}
    @if(!isEmpty(vin)){
            and (vv.vin like #vin# or ts.`CODE` like #vin# or tt.`CODE` like #vin#)
    @}	  
GROUP BY
	vvr.GPS_PROVINCE desc    
    
getCountByCity
===    
SELECT
	vvr.GPS_PROVINCE province,
	vvr.GPS_CITY city,
	count( 1 ) count
FROM
	v_vehicle_realtime vvr
	inner JOIN v_vehicle vv ON vv.VIN = vvr.VIN
	LEFT JOIN t_terminal tt ON vv.TERMINAL_ID = tt.id
	LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
	LEFT JOIN base_organization org ON vv.ORGANIZATION_ID = org.ID
	LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID = vvt.id
	LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID = vvm.id
	LEFT JOIN base_customer bc ON vv.CUSTOMER_ID = bc.id 
where
    1=1
    @if(!isEmpty(organizationId)){
            and vv.ORGANIZATION_ID = #organizationId#
    @}
    @if(!isEmpty(financeId)){
              and vv.FINANCE_ID = #financeId#
        @}
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE_CODE = #province#
    @}
    @if(!isEmpty(city)){
            and vv.GPS_CITY_CODE = #city#
    @}
    @if(!isEmpty(area)){
            and vv.GPS_AREA_CODE = #area#
    @}
    @if(!isEmpty(lock)){
                @if(lock == 1){
                    and (vvr.`LOCK`=#lock# or vvr.ONE_LEVEL_LOCK=#lock# or vvr.TWO_LEVEL_LOCK=#lock# or vvr.THREE_LEVEL_LOCK=#lock#)
                @}
                @if(lock == 0){
                    and (vvr.`LOCK`=#lock# and vvr.ONE_LEVEL_LOCK=#lock# and vvr.TWO_LEVEL_LOCK=#lock# and vvr.THREE_LEVEL_LOCK=#lock#)
                @}
    @}
    @if(!isEmpty(acc)){
            and vvr.ACC = #acc#
    @}
    @if(!isEmpty(customerName)){
            and bc.`NAME` like #customerName#
    @}
    @if(!isEmpty(vin)){
            and (vv.vin like #vin# or ts.`CODE` like #vin# or tt.`CODE` like #vin#)
    @}	
GROUP BY
	vvr.GPS_PROVINCE,
	vvr.GPS_CITY 
	
getVehicleInfoByVin
===
SELECT
vvr.VIN,
tt.`CODE` terminalCode,
ts.`CODE` cardCode,
vvt.`NAME` typeName,
vvm.`NAME` modelName,
FROM_UNIXTIME(vvr.GPS_TIME/1000,'%Y-%m-%d %H:%i:%S') AS gpsTime,
FROM_UNIXTIME(vvr.DATA_UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS updateTime,
vvr.GPS_ADDRESS,
round(vvr.total_Work_Time/3600,2) total_Work_Time,
vvr.last_lon,
vvr.last_lat 
FROM
v_vehicle_realtime vvr
LEFT JOIN v_vehicle vv ON vv.VIN = vvr.VIN
LEFT JOIN t_terminal tt ON vv.TERMINAL_ID = tt.id
LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID = vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID = vvm.id
where vvr.vin = #vin#	 

workTimeStatistic
===
SELECT vr.vin, vr.TOTAL_WORK_TIME 
from v_vehicle_realtime vr
inner join v_vehicle v on v.vin = vr.VIN
@if(!isEmpty(orgId)){
    INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( 
                                ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) 
                            ) temp ON temp.id = v.ORGANIZATION_ID
@}
WHERE 1 = 1
@if(!isEmpty(vehicleTypeId)){
    and v.VEHICLE_TYPE_ID = #vehicleTypeId#
@}
@if(!isEmpty(vehicleModelId)){
    and v.VEHICLE_MODEL_ID = #vehicleModelId# 	  	
@}
	
	