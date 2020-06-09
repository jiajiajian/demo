pageQuery
===
SELECT
@pageTag() {
	a.* 
@}
FROM
	(
SELECT
    f.id,
	f.`NAME`,
	f.VEHICLE_NUM,
	f.UPDATE_USER_ACCOUNT,
	f.UPDATE_TIME,
	org.ORG_NAME,
	f.ORG_TYPE,
	f.ORGANIZATION_ID 
FROM
	v_fence f
	LEFT JOIN base_organization org ON f.ORGANIZATION_ID = org.id 
WHERE
	f.ORG_TYPE = 1 
UNION ALL
SELECT
    f.id,
	f.`NAME`,
	f.VEHICLE_NUM,
	f.UPDATE_USER_ACCOUNT,
	f.UPDATE_TIME,
	fi.`NAME` ORG_NAME,
	f.ORG_TYPE,
	f.ORGANIZATION_ID 
FROM
	v_fence f
	LEFT JOIN base_finance fi ON f.ORGANIZATION_ID = fi.id 
WHERE
	f.ORG_TYPE = 2 
	) a
WHERE
1=1 
    @if(!isEmpty(fenceName)){
       and  a.`NAME` LIKE #fenceName#
    @}
    @if(!isEmpty(orgType)){
       and a.ORG_TYPE = #orgType#
    @}	
    @if(!isEmpty(organizationId)){
       and a.ORGANIZATION_ID = #organizationId#
    @}

pageQueryUnRelatedVehicles
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
	@if(!isEmpty(organizationId)){
	INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON v.ORGANIZATION_ID = temp.id
	@}
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_organization org ON v.ORGANIZATION_ID = org.id 
WHERE
1=1
    @if(!isEmpty(code)){
        and (v.vin like #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}
    @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleModelId)){
        and v.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(vehicleTypeId)){
        and v.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}        
    and	NOT EXISTS ( SELECT VIN FROM v_fence_vehicle WHERE VIN = v.VIN AND FENCE_ID = #fenceId# )
    
pageQueryRelatedVehicles    
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
	v_fence_vehicle fv
	LEFT JOIN v_vehicle v ON v.VIN = fv.VIN
	LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
	LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_organization org ON v.ORGANIZATION_ID = org.id 
WHERE
	fv.FENCE_ID = #fenceId#
    @if(!isEmpty(organizationId)){
        and v.ORGANIZATION_ID = #organizationId#
    @}
    @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(vehicleModelId)){
        and v.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(vehicleTypeId)){
        and v.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(code)){
        and (v.vin like #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}   