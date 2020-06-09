findAll
===
select et.* from fwp_export_task et
INNER JOIN fwp_task t on t.id = et.FWP_TASK_ID
@if(isNotEmpty(orgId)){
    inner join ( select id from base_organization 
                    where path like CONCAT((SELECT path FROM base_organization WHERE id = #orgId#), '%') 
                    AND ENABLE_FLAG = 0 AND DEL_FLAG = 0 
                ) temp ON temp.id = t.ORGANIZATION_ID
@}