pageQuery
===
SELECT
@pageTag() {
	vm.id,
	vm.`NAME`,
	vm.TONNAGE,
	FROM_UNIXTIME( vm.UPDATE_TIME / 1000, '%Y-%m-%d %H:%i:%S' ) AS UPDATE_TIME,
	vm.UPDATE_USER_ACCOUNT,
	org.ORG_NAME,
	vt.`NAME` vehicle_type_name
 @}
FROM
	v_vehicle_model vm
@if(!isEmpty(organizationId)){
	    INNER JOIN 
        ( SELECT 
        id FROM base_organization 
        WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) 
        temp ON vm.ORGANIZATION_ID = temp.id
  @}
	LEFT JOIN base_organization org ON org.id = vm.ORGANIZATION_ID 
	LEFT JOIN v_vehicle_type vt ON vm.VEHICLE_TYPE_ID = vt.id
WHERE
	1 = 1
	@if(!isEmpty(name)){
    	and vm.`NAME` like #name#
    @}
    @if(!isEmpty(vehicleTypeId)){
       and vm.VEHICLE_TYPE_ID = #vehicleTypeId#
     @}

findByNameAndOrgId
===
SELECT
	vm.*
FROM
	v_vehicle_model vm
	INNER JOIN ( SELECT * FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) ) temp ON vm.ORGANIZATION_ID = temp.id 
WHERE
	vm.`NAME` = #name#
	limit 0,1
	
vehicleModelOptionsByOrg
===
SELECT
vm.`NAME`,
vm.id
FROM
v_vehicle_model vm
WHERE
vm.ORGANIZATION_ID = ( SELECT ROOT_ORG_ID FROM base_organization WHERE ID = #orgId# )


optionsByFinanceId
===
SELECT
	vm.id,
	vm.name 
FROM
    v_vehicle_model vm
	INNER JOIN ( SELECT fo.ORG_ID FROM base_finance_organization fo WHERE fo.FINANCE_ID = #financeId# ) a ON a.ORG_ID = vm.ORGANIZATION_ID
