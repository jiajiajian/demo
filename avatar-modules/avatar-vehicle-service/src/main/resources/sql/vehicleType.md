pageQuery
===
SELECT
@pageTag() {
    vt.id,
	vt.`NAME`,
	FROM_UNIXTIME(vt.UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS UPDATE_TIME,
	vt.UPDATE_USER_ACCOUNT,
	org.ORG_NAME 
 @} 
FROM
	v_vehicle_type vt
	@if(!isEmpty(organizationId)){
	    INNER JOIN 
	    ( SELECT 
	    id FROM base_organization 
	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
	    ) temp
	    ON vt.ORGANIZATION_ID = temp.id
    @}
	LEFT JOIN base_organization org ON org.id = vt.ORGANIZATION_ID 
WHERE 1=1
@if(!isEmpty(name)){
	and vt.`NAME` like #name#
@}


cols
===
	id,NAME,ORGANIZATION_ID,DESCRIPTION,CREATE_TIME,UPDATE_TIME,CREATE_USER_ACCOUNT,CREATE_USER_REALNAME,UPDATE_USER_ACCOUNT,UPDATE_USER_REALNAME

updateSample
===
	
	id=#id#,NAME=#name#,ORGANIZATION_ID=#organizationId#,DESCRIPTION=#description#,CREATE_TIME=#createTime#,UPDATE_TIME=#updateTime#,CREATE_USER_ACCOUNT=#createUserAccount#,CREATE_USER_REALNAME=#createUserRealname#,UPDATE_USER_ACCOUNT=#updateUserAccount#,UPDATE_USER_REALNAME=#updateUserRealname#

condition
===

	1 = 1  
	@if(!isEmpty(id)){
	 and id=#id#
	@}
	@if(!isEmpty(name)){
	 and NAME=#name#
	@}
	@if(!isEmpty(organizationId)){
	 and ORGANIZATION_ID=#organizationId#
	@}
	@if(!isEmpty(description)){
	 and DESCRIPTION=#description#
	@}
	@if(!isEmpty(createTime)){
	 and CREATE_TIME=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and UPDATE_TIME=#updateTime#
	@}
	@if(!isEmpty(createUserAccount)){
	 and CREATE_USER_ACCOUNT=#createUserAccount#
	@}
	@if(!isEmpty(createUserRealname)){
	 and CREATE_USER_REALNAME=#createUserRealname#
	@}
	@if(!isEmpty(updateUserAccount)){
	 and UPDATE_USER_ACCOUNT=#updateUserAccount#
	@}
	@if(!isEmpty(updateUserRealname)){
	 and UPDATE_USER_REALNAME=#updateUserRealname#
	@}
	
findByNameAndOrgId
===
SELECT
	vt.*
FROM
	v_vehicle_type vt
	INNER JOIN ( SELECT * FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) ) temp ON vt.ORGANIZATION_ID = temp.id 
WHERE
	vt.`NAME` = #name#
	limit 0,1
	
vehicleTypeOptionsByOrg
===
SELECT
	vt.id,
	vt.`NAME` 
FROM
	v_vehicle_type vt
	INNER JOIN ( SELECT ID FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) ) temp ON vt.ORGANIZATION_ID = temp.id
	LEFT JOIN base_organization org ON org.id = vt.ORGANIZATION_ID
	
optionsBySelectOrg
===
SELECT
	ID,
`NAME`	
FROM
	v_vehicle_type 
WHERE
	ORGANIZATION_ID = ( SELECT ROOT_ORG_ID FROM base_organization WHERE ID = #orgId# )
	
optionsByFinanceId
===
SELECT
	t.id,
	t.name 
FROM
	v_vehicle_type t
	INNER JOIN ( SELECT fo.ORG_ID FROM base_finance_organization fo WHERE fo.FINANCE_ID = #financeId# ) a ON a.ORG_ID = t.ORGANIZATION_ID