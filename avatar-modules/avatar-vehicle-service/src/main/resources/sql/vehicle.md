pageQuery
===
SELECT
@pageTag() {
    v.id,
	v.VIN,
	t.`CODE` terminal_code,
	s.`CODE` sim_card,
	org.ORG_NAME,
	f.`NAME` FINANCE_NAME,
	vt.`NAME` vehicle_type,
	vm.`NAME` vehicle_model,
	vr.TOTAL_WORK_TIME,
	vr.ACC,
	vr.GPS,
	vr.LON,
	vr.LAT,
	FROM_UNIXTIME(vr.DATA_UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS DATA_UPDATE_TIME,
	FROM_UNIXTIME(vr.GPS_TIME/1000,'%Y-%m-%d %H:%i:%S') AS GPS_TIME,
	CONCAT(vr.GPS_PROVINCE,vr.GPS_CITY,vr.GPS_AREA) as GPS_ADDRESS,
	vr.`LOCK`,
	vr.ONE_LEVEL_LOCK,
	vr.TWO_LEVEL_LOCK,
	vr.THREE_LEVEL_LOCK,
	v.SERVICE_STATUS,
	v.SALE_STATUS,
	DATE_FORMAT(v.SERVICE_START_DATE,'%Y-%m-%d') AS SERVICE_START_DATE,
	DATE_FORMAT(v.SERVICE_END_DATE,'%Y-%m-%d') AS SERVICE_END_DATE,
	v.SERVICE_PERIOD,
	v.CONTRACT_NUMBER,
	c.`NAME` customer_name,
	c.ALARM_NAME,
	c.ALARM_NUMBER,
	c.PHONE_NUMBER,
	vd.TEST_STATUS debug_status,
	FROM_UNIXTIME(vd.DEBUG_BEGIN_TIME/1000,'%Y-%m-%d') AS DEBUG_BEGIN_TIME,
    FROM_UNIXTIME(vd.DEBUG_END_TIME/1000,'%Y-%m-%d') AS DEBUG_END_TIME,
	DATE_FORMAT(v.REGIST_DATE ,'%Y-%m-%d') AS REGIST_DATE
 @} 
FROM
	v_vehicle v
@if(!isEmpty(organizationId)){
	    INNER JOIN 
	    ( SELECT 
	    id FROM base_organization 
	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
	    ) temp
	    ON v.ORGANIZATION_ID = temp.id
    @}
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_finance f ON f.ID = v.FINANCE_ID
	LEFT JOIN base_customer c ON c.ID = v.CUSTOMER_ID
	LEFT JOIN v_vehicle_realtime vr ON v.VIN = vr.VIN
	LEFT JOIN v_vehicle_debug vd ON vd.VIN = v.VIN
	LEFT JOIN base_organization org ON v.ORGANIZATION_ID = org.id
	where 1=1
    @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleTypeId)){
    	and vt.id = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
    	and vm.id = #vehicleModelId#
    @}
    @if(!isEmpty(saleStatus)){
    	and v.SALE_STATUS = #saleStatus#
    @}
    @if(!isEmpty(serviceStatus)){
    	and v.SERVICE_STATUS = #serviceStatus#
    @}
    @if(!isEmpty(code)){
    	and (v.vin like #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}
    @if(!isEmpty(serviceStartDate1)){
    	and v.SERVICE_START_DATE >= #serviceStartDate1#
    @}
    @if(!isEmpty(serviceStartDate2)){
    	and v.SERVICE_START_DATE <= #serviceStartDate2#
    @}
    @if(!isEmpty(serviceEndDate1)){
    	and v.SERVICE_END_DATE >= #serviceEndDate1#
    @}
    @if(!isEmpty(serviceEndDate2)){
    	and v.SERVICE_END_DATE <= #serviceEndDate2#
    @}    
    @if(!isEmpty(vinList)){
    	and v.vin in ( #join(vinList)#)
    @}
    order by v.id desc
    
batchQuery
===
SELECT
    v.id,
	v.VIN,
	t.`CODE` terminal_code,
	s.`CODE` sim_card,
	org.ORG_NAME,
	f.`NAME` FINANCE_NAME,
	vt.`NAME` vehicle_type,
	vm.`NAME` vehicle_model,
	vr.TOTAL_WORK_TIME,
	vr.ACC,
	vr.GPS,
	FROM_UNIXTIME(vr.DATA_UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS DATA_UPDATE_TIME,
	FROM_UNIXTIME(vr.GPS_TIME/1000,'%Y-%m-%d %H:%i:%S') AS GPS_TIME,
	vr.GPS_ADDRESS,
	vr.`LOCK`,
	vr.ONE_LEVEL_LOCK,
	vr.TWO_LEVEL_LOCK,
	vr.THREE_LEVEL_LOCK,
	v.SERVICE_STATUS,
	DATE_FORMAT(v.SERVICE_START_DATE,'%Y-%m-%d') AS SERVICE_START_DATE,
	DATE_FORMAT(v.SERVICE_END_DATE,'%Y-%m-%d') AS SERVICE_END_DATE,
	v.SERVICE_PERIOD,
	v.CONTRACT_NUMBER,
	c.`NAME` customer_name,
	c.ALARM_NAME,
	c.ALARM_NUMBER,
	vd.`STATUS` debug_status,
	FROM_UNIXTIME(vd.DEBUG_BEGIN_TIME/1000,'%Y-%m-%d') AS DEBUG_BEGIN_TIME,
    FROM_UNIXTIME(vd.DEBUG_END_TIME/1000,'%Y-%m-%d') AS DEBUG_END_TIME,
	DATE_FORMAT(v.REGIST_DATE ,'%Y-%m-%d') AS REGIST_DATE
FROM
	v_vehicle v
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_finance f ON f.ID = v.FINANCE_ID
	LEFT JOIN base_customer c ON c.ID = v.CUSTOMER_ID
	LEFT JOIN v_vehicle_realtime vr ON v.VIN = vr.VIN
	LEFT JOIN v_vehicle_debug vd ON vd.VIN = v.VIN
	LEFT JOIN base_organization org ON v.ORGANIZATION_ID = org.id
	where 1=1
    @if(!isEmpty(vinList)){
    	and v.vin in ( #join(vinList)#)
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
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	where 1=1
    @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleTypeId)){
    	and vt.id = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
    	and vm.id = #vehicleModelId#
    @}
    @if(!isEmpty(saleStatus)){
    	and v.SALE_STATUS = #saleStatus#
    @}
    @if(!isEmpty(serviceStatus)){
    	and v.SERVICE_STATUS = #serviceStatus#
    @}
    @if(!isEmpty(code)){
    	and (v.vin like #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}
    @if(!isEmpty(serviceStartDate1)){
    	and v.SERVICE_START_DATE >= #serviceStartDate1#
    @}
    @if(!isEmpty(serviceStartDate2)){
    	and v.SERVICE_START_DATE <= #serviceStartDate2#
    @}
    @if(!isEmpty(serviceEndDate1)){
    	and v.SERVICE_END_DATE >= #serviceEndDate1#
    @}
    @if(!isEmpty(serviceEndDate2)){
    	and v.SERVICE_END_DATE <= #serviceEndDate2#
    @}  
   and b.vin in ( #join(vinList)#)

baseInfoByVin
===
SELECT
v.VIN,
t.`CODE` terminal_code,
s.`CODE` sim_card,
o.ORG_NAME,
o.ROOT_ORG_ID
FROM
v_vehicle v
LEFT JOIN t_terminal t ON v.TERMINAL_ID = t.id
LEFT JOIN t_simcard s ON t.SIMCARD_ID = s.id
LEFT JOIN base_organization o ON o.id = v.ORGANIZATION_ID 
where v.vin = #vin#

findTerminalById
===
SELECT
t.id terminal_id,
t.`CODE` terminal_code,
s.`CODE` sim_card
FROM
t_terminal t
LEFT JOIN t_simcard s ON t.SIMCARD_ID = s.id
WHERE
t.ID = #id#

findTerminalByCode
===
SELECT
t.id terminal_id,
t.`CODE` terminal_code,
s.`CODE` sim_card
FROM
t_terminal t
LEFT JOIN t_simcard s ON t.SIMCARD_ID = s.id
WHERE
t.`CODE` = #code#

terminalTypeByVin
===
SELECT
	d.ITEM_CODE 
FROM
	v_vehicle v
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN base_dic_item d ON d.ID = t.PROTOCOL_ID 
WHERE
	v.VIN = #vin#
	
vinListByOrgIdOrFinanceId
===
SELECT
	v.VIN 
FROM
	v_vehicle v
@if(!isEmpty(organizationId)){
	INNER JOIN 
	( SELECT id 
	  FROM base_organization 
	  WHERE path LIKE 
	  CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON v.ORGANIZATION_ID = temp.id
    @}
@if(!isEmpty(financeId)){
     v.FINANCE_ID = #financeId#
    @} 
    
vehicleListByVinList
===
SELECT
*
FROM
v_vehicle
WHERE
VIN IN ( #join(vinList)#)

findOrgList
===
SELECT
	id ,
	ORG_NAME
FROM
	base_organization 
WHERE
1=1
@if(!isEmpty(organizationId)){
	and path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' )
@}	
	
vehicleTerminalListByOrgId
===
SELECT
    v.id,
	v.ORGANIZATION_ID,
	v.VIN,
	t.`CODE` terminal_code,
	s.`CODE` sim_card 
FROM
	v_vehicle v
	@if(!isEmpty(organizationId)){
    	INNER JOIN 
    	( SELECT id 
    	  FROM base_organization 
    	  WHERE path LIKE 
    	  CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON v.ORGANIZATION_ID = temp.id
        @} 
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID

findAllVin
===
SELECT
	VIN 
FROM
	v_vehicle
	
findVehicleByTerminalCode
===
SELECT
	v.* 
FROM
	v_vehicle v
	LEFT JOIN t_terminal t ON v.TERMINAL_ID = t.id 
WHERE
	t.`CODE` = #terminalCode#
	
vehicleAndTerminalInfoByVin
===
SELECT
v.VIN,
t.`CODE` terminal_code,
s.`CODE` sim_card,
o.ORG_NAME,
v.ORGANIZATION_ID
FROM
v_vehicle v
LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
LEFT JOIN base_organization o ON o.ID = v.ORGANIZATION_ID
where v.vin = #vin#


getVehicleMonitorList
===
SELECT
@pageTag() {
	vv.ID,
	vv.VIN,
	tt.`CODE` terminalCode,
	ts.`CODE` cardCode,
	org.ORG_NAME,
	vvt.`NAME` typeName,
	vvm.`NAME` modelName,
	vvr.ACC,
	ROUND(vvr.TOTAL_WORK_TIME/3600,2) TOTAL_WORK_TIME,
	vvr.GPS,
	FROM_UNIXTIME(vvr.DATA_UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS updateTime,
	vvr.DATA_UPDATE_TIME,
	concat(vvr.gps_province,vvr.gps_city,gps_area) GPS_ADDRESS,
	vvr.`LOCK`,
	vvr.ONE_LEVEL_LOCK,
    vvr.TWO_LEVEL_LOCK,
    vvr.THREE_LEVEL_LOCK,
	vv.SALE_STATUS,
	vvr.last_lat lat,
	vvr.last_lon lon,
	bc.`NAME` customerName,
	bc.PHONE_NUMBER
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
	LEFT JOIN v_vehicle_realtime vvr ON vv.VIN = vvr.VIN
	LEFT JOIN t_terminal tt ON vv.TERMINAL_ID=tt.id
	LEFT JOIN t_simcard ts ON tt.SIMCARD_ID=ts.id
	LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
	LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
	LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
	LEFT JOIN base_customer bc ON vv.CUSTOMER_ID=bc.ID
where 1=1
    @if(!isEmpty(vehicleTypeId)){
        and vv.VEHICLE_TYPE_ID=#vehicleTypeId#
    @}
    @if(!isEmpty(financeId)){
              and vv.FINANCE_ID = #financeId#
        @}
    @if(!isEmpty(vehicleModelId)){
        and vv.VEHICLE_TYPE_ID=#vehicleModelId#
    @}
    @if(!isEmpty(acc)){
        and vvr.ACC=#acc#
    @}
    @if(!isEmpty(lock)){
            @if(lock == 1){
                and (vvr.`LOCK`=#lock# or vvr.ONE_LEVEL_LOCK=#lock# or vvr.TWO_LEVEL_LOCK=#lock# or vvr.THREE_LEVEL_LOCK=#lock#)
            @}
            @if(lock == 0){
                and (vvr.`LOCK`=#lock# and vvr.ONE_LEVEL_LOCK=#lock# and vvr.TWO_LEVEL_LOCK=#lock# and vvr.THREE_LEVEL_LOCK=#lock#)
            @}
    @}
    @if(!isEmpty(gps)){
        and vvr.gps=#gps#
    @}
    @if(!isEmpty(saleStatus)){
        and vv.SALE_STATUS=#saleStatus#
    @}
    @if(!isEmpty(province)){
        and vvr.GPS_PROVINCE_CODE=#province#
    @}
    @if(!isEmpty(city)){
        and vvr.GPS_CITY_CODE=#city#
    @}
    @if(!isEmpty(vin)){
        and (vv.vin like #vin# or tt.`CODE` like #vin# or ts.`CODE` like #vin#) 
    @}
    @if(!isEmpty(days)){
        and vvr.DATA_UPDATE_TIME < #days#
    @}
    @if(!isEmpty(faultStatus)){
            and vvr.FAULT_STATUS = #faultStatus#
        @}
    @if(!isEmpty(online)){
        and vvr.DATA_UPDATE_TIME > #dataUpdateTime#
    @}
    order by vv.create_time desc
getProtocolByVin
===
SELECT
	bdi.ITEM_CODE 
FROM
	base_dic_item bdi
	LEFT JOIN t_terminal tt ON bdi.id = tt.PROTOCOL_ID
	LEFT JOIN v_vehicle vv ON vv.TERMINAL_ID = tt.id    
where 
    vv.vin=#vin#

getItemListByVin
===
SELECT
	item.`CODE`,
	item.`NAME`,
	item.unit,
	item.bit_start,
	item.EN_NAME,
	item.description,
	item.SEPARATOR,
	item.sort_num
FROM
	t_function_set_item_collect item
	LEFT JOIN t_soft_version tsv ON item.FUNCTION_ID = tsv.COLLECT_FUNCTION_ID
	LEFT JOIN t_terminal tt ON tt.SOFT_VERSION_ID = tsv.id
	LEFT JOIN v_vehicle vv ON vv.TERMINAL_ID = tt.id 
WHERE
	vv.VIN = #vin#
	@if(!isEmpty(itemCode)){
         and item.`CODE` = #itemCode#
    @}   

getDebugDate
===   
SELECT
d.DEBUG_BEGIN_TIME,
d.DEBUG_END_TIME
FROM
v_vehicle v
INNER JOIN
v_vehicle_debug  d ON v.VIN = d.VIN
WHERE v.VIN = #vin#

getVehicleLockList
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
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	where 1=1
    @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleTypeId)){
    	and vt.id = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
    	and vm.id = #vehicleModelId#
    @}
    @if(!isEmpty(saleStatus)){
    	and v.SALE_STATUS = #saleStatus#
    @}
   and b.vin in ( #join(vinList)#)

findTerminalInfoByVin
===
SELECT
v.VIN,
t.`CODE` terminal_code,
s.`CODE` sim_card
FROM
v_vehicle v
INNER JOIN t_terminal t
ON t.id = v.TERMINAL_ID
INNER JOIN t_simcard s ON s.id = t.SIMCARD_ID
where v.vin = #vin#

monitorVehicleInfoByVin
===
SELECT
	v.id,
	v.VIN,
	DATE_FORMAT( v.REGIST_DATE, '%Y-%m-%d' ) AS REGIST_DATE,
	t.`CODE` terminal_code,
	f.`NAME` FINANCE_NAME,
	vt.`NAME` vehicle_type,
	vm.`NAME` vehicle_model,
	c.`NAME` customer_name,
	c.PHONE_NUMBER,
	vd.TEST_STATUS debug_status,
	v.SERVICE_STATUS,
	v.SALE_STATUS,
	CONCAT( DATE_FORMAT( v.SERVICE_START_DATE, '%Y-%m-%d' ), ' ~ ', DATE_FORMAT( v.SERVICE_END_DATE, '%Y-%m-%d' ) ) AS service_date,
	v.SERVICE_PERIOD,
	v.CONTRACT_NUMBER,
	c.ALARM_NAME,
	c.ALARM_NUMBER,
	CONCAT( FROM_UNIXTIME( vd.DEBUG_BEGIN_TIME / 1000, '%Y-%m-%d' ), ' ~ ', FROM_UNIXTIME( vd.DEBUG_END_TIME / 1000, '%Y-%m-%d' ) ) AS debug_date 
FROM
	v_vehicle v
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_finance f ON f.ID = v.FINANCE_ID
	LEFT JOIN base_customer c ON c.ID = v.CUSTOMER_ID
	LEFT JOIN v_vehicle_debug vd ON vd.VIN = v.VIN 
WHERE
	v.VIN = #vin#

vehicleLockInfoByVin
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
WHERE
	b.VIN = #vin#

frameVehicleInfo
===
SELECT
	v.VIN,
	vt.`NAME` vehicle_type,
	vm.`NAME` vehicle_model,
	t.`CODE` terminal_code,
	s.`CODE` sim_card,
	vr.ACC,
	vr.GPS_TIME,
	vr.last_lon,
	vr.last_lat,
	vr.TOTAL_WORK_TIME 
FROM
	v_vehicle v
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON v.TERMINAL_ID = t.id
	LEFT JOIN t_simcard s ON t.SIMCARD_ID = s.id
	LEFT JOIN v_vehicle_realtime vr ON vr.vin = v.vin 
WHERE
	v.vin = #vin#
	
findRootOrgByOrg
===
SELECT
ROOT_ORG_ID
FROM
base_organization
WHERE
ID = #orgId#

checkVinByOrgAndVin
===
SELECT
	v.VIN 
FROM
	v_vehicle v
	INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) ) temp ON v.ORGANIZATION_ID = temp.id 
WHERE
	VIN = #vin#
	
vehicleAndTerminalInfoListByVinList
===
SELECT
v.VIN,
t.`CODE` terminal_code,
s.`CODE` sim_card,
o.ORG_NAME,
v.ORGANIZATION_ID
FROM
v_vehicle v
LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
LEFT JOIN base_organization o ON o.ID = v.ORGANIZATION_ID
where v.vin in ( #join(vinList)#)

findVehicleByCode
===
SELECT
	v.* 
FROM
	v_vehicle v
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID 
WHERE
	( v.VIN = #code# OR t.`CODE` = #code# OR s.`CODE` = #code# ) 
	LIMIT 0,1
	
findProtocolTypeAndApiKey
===
SELECT
	d.ITEM_CODE PROTOCOL_type,
	d.REMARK api_key 
FROM
	v_vehicle v
	INNER JOIN t_terminal t ON t.id = v.TERMINAL_ID
	INNER JOIN base_dic_item d ON d.ID = t.PROTOCOL_ID
	where v.vin = #vin#

findProtocolType
===
SELECT
d.ITEM_CODE
FROM
v_vehicle v
INNER JOIN t_terminal t ON v.TERMINAL_ID = t.id
INNER JOIN base_dic_item d ON d.ID = t.PROTOCOL_ID
WHERE v.vin = #vin#

rootOrgIdByVin
===
SELECT
o.ROOT_ORG_ID
FROM
v_vehicle v
INNER JOIN base_organization o
ON o.ID = v.ORGANIZATION_ID
where v.vin = #vin#
	

getVehicleMonitorOtherList
===
SELECT
@pageTag() {
	vv.ID,
	vv.VIN,
	tt.`CODE` terminalCode,
	ts.`CODE` cardCode,
	org.ORG_NAME,
	vvt.`NAME` typeName,
	vvm.`NAME` modelName,
	vvr.ACC,
    vvr.`LOCK`,
    vvr.ONE_LEVEL_LOCK,
    vvr.TWO_LEVEL_LOCK,
    vvr.THREE_LEVEL_LOCK,
	vvr.TOTAL_WORK_TIME,
	vvr.GPS,
	FROM_UNIXTIME(vvr.DATA_UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS updateTime,
	vvr.DATA_UPDATE_TIME,
	vvr.GPS_ADDRESS,
    vvr.SIGNAL_STRENGTH,
	vvd.TEST_STATUS,
	vvd.STATUS
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
	LEFT JOIN v_vehicle_realtime vvr ON vv.VIN = vvr.VIN
	LEFT JOIN t_terminal tt ON vv.TERMINAL_ID=tt.id
	LEFT JOIN t_simcard ts ON tt.SIMCARD_ID=ts.id
	LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
	LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
	LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
	LEFT JOIN base_customer bc ON vv.CUSTOMER_ID=bc.ID
	LEFT JOIN v_vehicle_debug vvd on vv.VIN= vvd.VIN
where 1=1
   @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(acc)){
        and vvr.ACC=#acc#
    @}
    @if(!isEmpty(lock)){
        @if(lock == 1){
            and (vvr.'lock'=#lock# or vvr.ONE_LEVEL_LOCK=#lock# or vvr.TWO_LEVEL_LOCK=#lock# or vvr.THREE_LEVEL_LOCK=1)
        @}
        @if(lock == 0){
            and (vvr.'lock'=#lock# and vvr.ONE_LEVEL_LOCK=#lock# and vvr.TWO_LEVEL_LOCK=#lock# and vvr.THREE_LEVEL_LOCK=#lock#)
        @}
    @}
    @if(!isEmpty(vin)){
        and (vv.vin like #vin# or tt.`CODE` like #vin# or ts.`CODE` like #vin#) 
    @}
    @if(!isEmpty(online)){
        @if(online == 1){
            and vvr.DATA_UPDATE_TIME > #dataUpdateTime#
        @}
        @if(online == 0){
            and vvr.DATA_UPDATE_TIME <= #dataUpdateTime#
        @}
    @}
ORDER BY vvr.DATA_UPDATE_TIME DESC
    
avgOilConsumption
=== 
SELECT
	v.VIN,
	a.`CODE`,
	t.`CODE` terminal_code,
	vt.`NAME` vehicle_type,
	vm.`NAME` vehicle_model 
FROM
	v_vehicle v
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_soft_version sv ON sv.id = t.SOFT_VERSION_ID
	LEFT JOIN t_function_set s ON sv.COLLECT_FUNCTION_ID = s.id
	LEFT JOIN ( SELECT FUNCTION_ID, CODE FROM t_function_set_item_collect WHERE VAR_ADDRESS = 'FFF458' ) a ON a.FUNCTION_ID = s.id
	where 1 = 1
   @if(!isEmpty(organizationId)){
        and v.ORGANIZATION_ID = #organizationId#
    @}	
   @if(!isEmpty(vehicleTypeId)){
        and v.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
   @if(!isEmpty(vehicleModelId)){
        and v.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
        and a.`CODE` is not null
 

avgModelOilConsumption
===
SELECT
	vm.`NAME` vehicle_model,
	temp.num,
	a.`CODE`,
	v.VIN
FROM
	v_vehicle v
INNER JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
LEFT JOIN t_soft_version sv ON sv.id = t.SOFT_VERSION_ID
LEFT JOIN t_function_set s ON sv.COLLECT_FUNCTION_ID = s.id
LEFT JOIN ( SELECT FUNCTION_ID, CODE FROM t_function_set_item_collect WHERE VAR_ADDRESS = 'FE4D' ) a ON a.FUNCTION_ID = s.id
LEFT JOIN (
    select count(0) as num,v.VEHICLE_MODEL_ID model_id
    from v_vehicle v 
    group by v.VEHICLE_MODEL_ID
) temp on temp.model_id = vm.ID
WHERE 1 = 1
@if(!isEmpty(organizationId)){
    and v.ORGANIZATION_ID = #organizationId#
@}

monthTonnageVehicleNum
===
SELECT tm.MONTH_VAL month,tm.TONNAGE,
elt(interval(round(tm.TOTAL_WORKING_TIME/3600000,2),0,30,240,500),'less30','30to240','240to500','more500') type ,
count(0) as num
from tls_report_vehicle_monthly tm 
left join v_vehicle v on v.VIN = tm.VIN
where  1=1
    AND tm.MONTH_VAL >= #beginMon# 
	AND tm.MONTH_VAL <= #endMon#  
    AND tm.TONNAGE = #tonnage#
    @if(!isEmpty(organizationId)){
        and v.ORGANIZATION_ID = #organizationId#
    @}
group by tm.MONTH_VAL,tm.TONNAGE,elt(interval(round(tm.TOTAL_WORKING_TIME/3600000,2),0,30,240,500),'less30','30to240','240to500','more500')

tlaCount
===
select
count(0)
from
v_tla
where
organization_id = #rootOrgId#