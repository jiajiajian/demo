selectAuthorizesByUser
===
SELECT
	distinct sf.* 
FROM base_authorize a
inner join base_role_user ru on a.AUTHORIZE_OBJECT_ID = ru.role_id
inner join base_sys_function sf ON sf.ID = a.permission_Id
where ru.USER_ID = #userId# 
    