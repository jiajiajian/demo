pageQuery
===
SELECT
@pageTag() {
	l.vin,
	ter.`CODE` terminalCode,
	sim.`CODE` simCode,
	FROM_UNIXTIME(co.res_time / 1000, '%Y-%m-%d %H:%i:%S') AS responseTime,
    FROM_UNIXTIME(co.send_time / 1000, '%Y-%m-%d %H:%i:%S') AS executeTime,
	org.`ORG_NAME` orgName,
	vt.`NAME` typeName,
	vm.`NAME` modelName,
	co.state,
	di.ITEM_NAME,
	co.ip_address,
	u.login_name operateUsername
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
LEFT JOIN base_user u on u.id = co.user_id
@if(isNotEmpty(organizationId)){
    INNER JOIN ( SELECT id FROM base_organization 
                    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp 
        ON temp.id = v.ORGANIZATION_ID
@}
where l.flag = 1
    @if(!isEmpty(vehicleTypeId)){
        and vt.id = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
        and vm.id = #vehicleModelId#
    @}
    @if(!isEmpty(order)){
        and l.DIC_ITEM_ID = #order#	
    @}
    @if(!isEmpty(status)){
        and co.state = #status#	
    @}
    @if(isNotEmpty(keyword)){
        and (l.VIN like #keyword# or ter.CODE like #keyword# or sim.code like #keyword#)
    @}
    @if(!isEmpty(startDate)){
        and co.operate_time >= #startDate#
    @}
    @if(!isEmpty(endDate)){
        and co.operate_time <= #endDate#
    @}
