pageQuery
===
select 
@pageTag(){
    t.id,
    t.relation_id,
    t.name,
    t.link_amount,
    t.BATTERY_DATA_FLAG,
    t.CUSTOM_DATA_FLAG,
    t.ip,
    t.port,
    t.idcode,
    t.LINK_TYPE,
    t.ENCRYPT,
    t.description,
    t.vehicle_amount,
    t.active_status
@}
from fwp_task t
@if(isNotEmpty(organizationId)){
    inner join ( select id from base_organization 
                    where path like CONCAT((SELECT path FROM base_organization WHERE id = #organizationId#), '%') 
                    AND ENABLE_FLAG = 0 AND DEL_FLAG = 0 
                ) temp ON temp.id = t.ORGANIZATION_ID
@}
where 1 = 1
@if(isNotEmpty(name)){
    and t.name like #name#
@}
@if(isNotEmpty(activeStatus)){
    and t.active_status = #activeStatus#
@}
order by t.id desc


queryAllVin
===
select v.vin from tls_vehicle_base_info v
@if(isNotEmpty(orgId)){
    inner join ( select id from base_organization 
                    where path like CONCAT((SELECT path FROM base_organization WHERE id = #orgId#), '%') 
                    AND ENABLE_FLAG = 0 AND DEL_FLAG = 0 
                ) temp ON temp.id = v.org_id
@}

findBoundVinByTask
===
select vin from fwp_vehicle_data where task_id = #taskId#
	