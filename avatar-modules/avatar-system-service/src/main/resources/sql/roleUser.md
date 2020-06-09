selectByRoleId
===
SELECT t.* from 
base_role_user t
inner join base_user u on u.id = t.user_id and u.del_flag = 0
where t.role_id = #roleId#


findUserRoleName
===
SELECT
	role_name
FROM base_role role
LEFT JOIN base_role_user roleUser ON role.id = roleUser.role_id
WHERE roleUser.user_id = #userId#  
