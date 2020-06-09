queryAll
===
SELECT
@pageTag() {
	jd.task_id,
	t. NAME,
	t.vehicle_amount,
	jd.rev_amount,
	jd.suc_amount,
	jd.fai_amount,
	jd.pat_amount
@} 	
FROM
	fwp_task_data jd
INNER JOIN fwp_task t ON t.id = jd.task_id
@if(isNotEmpty(organizationId)){
    inner join ( select id from base_organization 
                    where path like CONCAT((SELECT path FROM base_organization WHERE id = #organizationId#), '%') 
                    AND ENABLE_FLAG = 0 AND DEL_FLAG = 0 
                ) temp ON temp.id = t.ORGANIZATION_ID
@}
@if(!isEmpty(name)){
    where t.name like #name# 
@}


queryVehicleData
===
SELECT
@pageTag() {
    vd.id,
	v.vin,
	v.plate_number as plate_code,
	org.org_name,
	vd.rev_amount,
	vd.suc_amount,
    vd.reply_fai_amount,
    vd.timeout_fai_amount,
    vd.plat_resend_amount, 
    vd.terminal_resend_amount,
    a.area_name as province, 
    b.area_name as city
@}
FROM
	fwp_vehicle_data vd
INNER JOIN tls_vehicle_base_info v ON v.vin = vd.vin
LEFT JOIN base_organization org ON org.id = v.org_id
LEFT JOIN base_area a on a.area_code = v.province
LEFT JOIN base_area b on b.area_code = v.city
@if(isNotEmpty(organizationId)){
    inner join ( select id from base_organization 
                    where path like CONCAT((SELECT path FROM base_organization WHERE id = #organizationId#), '%') 
                    AND ENABLE_FLAG = 0 AND DEL_FLAG = 0 
                ) temp ON temp.id = v.org_id
@}
where vd.task_id = #taskId#
@if(!isEmpty(keyword)){
    and (v.vin like #keyword# or v.plate_number like #keyword# or org.org_name like #keyword#)
@}

queryNotRelatedVehicles
===
select 
@pageTag() {
    v.id, 
    v.vin, 
    v.plate_number as plate_code, 
    org.org_name
@} 
from tls_vehicle_base_info v
LEFT JOIN base_organization org ON org.id = v.org_id
LEFT JOIN fwp_vehicle_data vd ON vd.vin = v.vin and vd.task_id = #taskId#
@if(isNotEmpty(organizationId)){
    inner join ( select id from base_organization 
                    where path like CONCAT((SELECT path FROM base_organization WHERE id = #organizationId#), '%') 
                    AND ENABLE_FLAG = 0 AND DEL_FLAG = 0 
                ) temp ON temp.id = v.org_id
@}
where  vd.vin is null
@if(!isEmpty(keyword)){
    and (v.VIN like #keyword# or v.plate_number like #keyword# or org.org_name like #keyword#)
@}

queryApiKey
===
SELECT
	ot.API_KEY
FROM
	fwp_task t
INNER JOIN fwp_origin_relation re ON re.id = t.RELATION_ID
INNER JOIN fwp_origin_template ot ON ot.ID = re.TEMPLATE_ID
WHERE
	t.id = #id#
