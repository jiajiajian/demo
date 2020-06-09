pageQuery
===
SELECT
@pageTag() {
    l.id,
	l.vin,
	ter.`CODE` terminalCode,
	sim.`CODE` simCode,
	vr.DATA_UPDATE_TIME,
	vr.LOCK,
	vr.ONE_LEVEL_LOCK,
	vr.TWO_LEVEL_LOCK,
	vr.THREE_LEVEL_LOCK,
	vr.ACC,
	vr.GPS,
	vr.DATA_UPDATE_TIME,
	FROM_UNIXTIME(co.res_time / 1000, '%Y-%m-%d %H:%i:%S') AS responseTime,
	FROM_UNIXTIME(co.send_time / 1000, '%Y-%m-%d %H:%i:%S') AS executeTime,
	org.`ORG_NAME` orgName,
	vt.`NAME` typeName,
	vm.`NAME` modelName,
	v.SALE_STATUS,
	bc.`NAME` customerName,
	bc.PHONE_NUMBER,
	co.state,
	di.ITEM_NAME
 @}
FROM
	r_lock l
INNER JOIN v_vehicle v ON v.VIN = l.VIN
LEFT JOIN t_command co ON co.id = l.COMMAND_ID
LEFT JOIN base_dic_item di on di.id = l.DIC_ITEM_ID
LEFT JOIN base_organization org ON v.ORGANIZATION_ID = org.ID
LEFT JOIN t_terminal ter ON v.TERMINAL_ID = ter.id
LEFT JOIN t_simcard sim ON ter.SIMCARD_ID = sim.id
LEFT JOIN v_vehicle_type vt ON v.VEHICLE_TYPE_ID = vt.id
LEFT JOIN v_vehicle_model vm ON v.VEHICLE_MODEL_ID = vm.id
LEFT JOIN v_vehicle_realtime vr ON l.VIN = vr.VIN
LEFT JOIN base_customer bc ON v.CUSTOMER_ID = bc.ID
@if(isNotEmpty(orgId)){
    INNER JOIN ( SELECT id FROM base_organization 
                    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) ) temp 
        ON temp.id = v.ORGANIZATION_ID
@}
where l.flag = 0
    @if(!isEmpty(orderId)){
        and l.DIC_ITEM_ID=#orderId#
    @}
    @if(!isEmpty(runState)){
        and co.state=#runState#
    @}
    @if(!isEmpty(keywords)){
        and (l.VIN like #keywords# or ter.`CODE` like #keywords# or sim.`CODE` like #keywords#) 
    @}

getLockFunctionSet
===
SELECT t.`CODE` terminalCode, di.ITEM_CODE from t_terminal t
INNER JOIN t_soft_version s on s.id = t.SOFT_VERSION_ID
INNER JOIN t_function_set f on f.id = s.LOCK_FUNCTION_ID
INNER JOIN t_function_set_item_lock fl on fl.FUNCTION_ID = f.id
INNER JOIN base_dic_item di on di.id = fl.DIC_ITEM_ID
where t.`CODE` in ( #join(terminalCodeList)#)
    
getVehicleList
===
SELECT
@pageTag() {
    v.VIN,
	t.`CODE` terminal_code,
	s.`CODE` sim_code,
	org.ORG_NAME,
	vt.`NAME` vehicle_type,
	vm.`NAME` vehicle_model
 @}
FROM
	v_vehicle v
LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
@if(isNotEmpty(orgId)){
    INNER JOIN ( SELECT id FROM base_organization 
                    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) ) temp 
        ON temp.id = v.ORGANIZATION_ID
@}
LEFT JOIN base_organization org ON v.ORGANIZATION_ID=org.ID 
where v.vin not in (SELECT vin FROM r_lock WHERE FLAG = 0)
@if(!isEmpty(vehicleTypeId)){
    and v.VEHICLE_TYPE_ID=#vehicleTypeId#
@}
@if(!isEmpty(vehicleModelId)){
    and v.VEHICLE_MODEL_ID=#vehicleModelId#
@}
@if(!isEmpty(keywords)){
    and (v.VIN like #keywords# or t.CODE like #keywords# or s.CODE like #keywords#) 
@}

queryControllerLockUnfinished
===
select id, vin from r_lock where RUN_STATE = 5


getVehicleLockList
===
SELECT
	b.ITEM_CODE,
	b.ITEM_NAME,
	b.VIN
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
where 1=1 and b.vin = #vin#