pageQuery
===
SELECT
	@pageTag() {
        u.*, ref.role_name, o.org_name organization_name, f.name finance_name
    @} 
FROM base_user u
@if(!isEmpty(organizationId)){
    INNER JOIN(
        SELECT id
        FROM base_organization
        WHERE path like CONCAT((SELECT path FROM base_organization WHERE id = #organizationId#), '%')  
    )org ON u.organization_Id = org.id
@} 
left join (SELECT t.user_id, GROUP_CONCAT(r.role_name)  role_name
           FROM base_role_user t 
           INNER JOIN base_role r ON r.id = t.role_id
           GROUP BY t.user_id) ref on ref.user_id = u.id
@if(!isEmpty(roleId)){
    left join base_role_user roleUser on u.id = roleUser.user_id
@} 
left join base_organization o on o.id = u.organization_id
left join base_finance f on f.id = u.finance_id
where 1=1 and u.enable_flag=1 and u.del_flag=0
@if(!isEmpty(name)){
    and (u.realname like #name# or u.login_name like #name#)
@}
@if(!isEmpty(roleId)){
    and roleUser.role_id = #roleId#
@}  
@if(!isEmpty(financeId)){
    and u.finance_id = #financeId#
@}  
@if(!isEmpty(userType)){
    and u.user_type = #userType#
@}  

exportQuery
===
SELECT
    u.*, ref.role_name
FROM base_user u
@if(!isEmpty(organizationId)){
    INNER JOIN(
        SELECT id
        FROM base_organization
        WHERE path like CONCAT((SELECT path FROM base_organization WHERE id = #organizationId#), '%')  
    )org ON u.organization_Id = org.id
@} 
left join (SELECT t.user_id, GROUP_CONCAT(r.role_name, ',')  role_name
           FROM base_role_user t 
           INNER JOIN base_role r ON r.id = t.role_id
           GROUP BY t.user_id) ref on ref.user_id = u.id
@if(!isEmpty(roleId)){
    left join base_role_user roleUser on u.id = roleUser.user_id
@} 
left join base_organization o on o.id = u.organization_id
left join base_finance f on f.id = u.finance_id
where 1=1 and u.enable_flag=1 and u.del_flag=0
@if(!isEmpty(name)){
    and (u.realname like #name# or u.login_name like #name#)
@}
@if(!isEmpty(roleId)){
    and roleUser.role_id = #roleId#
@}  
@if(!isEmpty(financeId)){
    and u.finance_id = #financeId#
@} 
order by u.id desc 

getRolesByUserId
===
SELECT
	role_name from base_role role
LEFT JOIN base_role_user roleUser ON role.id = roleUser.role_id
@if(!isEmpty(orgId)){
    INNER JOIN base_organization temp  ON temp.id = role.ORGANIZATION_ID
@}
WHERE
    1=1
@if(!isEmpty(userId)){
     and
    	roleUser.user_id = #userId#  
@}
@if(!isEmpty(orgId)){
    and role.ORGANIZATION_ID = #orgId#
@}

getRoleIdByUserId
===
SELECT
	role_id from base_role_user roleUser
WHERE
    1=1
@if(!isEmpty(userId)){
     and
    	roleUser.user_id = #userId#  
@}


optionsByOrgId
===
SELECT
	u.id,
	u,REALNAME as name 
FROM
	base_user u
@if(!isEmpty(organizationId)){
	INNER JOIN 
	( SELECT id 
	FROM base_organization 
	WHERE path LIKE 
	CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp 
	ON u.ORGANIZATION_ID = temp.id 
  @}
WHERE
	1 = 1 
	AND u.enable_flag = 1 
	AND u.del_flag =0