pageQuery
===
SELECT
@pageTag() {
	ten.ENABLE_FLAG,
	ten.id,
	u.REALNAME,
	u.LOGIN_NAME,
	r.ROLE_NAME,
	ten.CREATE_TIME,
	ten.REMARK
@}
FROM
	base_tenant ten
LEFT JOIN base_organization org ON org.id = ten.ROOT_ORGANIZATION_ID
LEFT JOIN base_user u ON u.id = ten.ADMIN_USER_ID
LEFT JOIN base_role_user ru ON ru.USER_ID = u.id
LEFT JOIN base_role r ON r.id = ru.ROLE_ID
WHERE
	ten.DEL_FLAG = 0
@if(isNotEmpty(name)){
    and u.REALNAME like #name#
@} 
@if(isNotEmpty(status)){
   and ten.ENABLE_FLAG = #status#
@} 
ORDER BY
	ten.id DESC