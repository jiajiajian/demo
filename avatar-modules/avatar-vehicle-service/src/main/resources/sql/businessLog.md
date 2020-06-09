pageQuery
===
SELECT
@pageTag() {
    b.*,
    o.org_name
@}    
FROM
v_business_log b
@if(!isEmpty(organizationId)){
	    INNER JOIN 
	    ( SELECT 
	    id FROM base_organization 
	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
	    ) temp
	    ON b.ORGANIZATION_ID = temp.id
    @}
LEFT JOIN v_vehicle v ON v.vin = b.vin
LEFT JOIN base_organization o ON b.ORGANIZATION_ID = o.ID
where 1 = 1
    @if(!isEmpty(operateType)){
        and b.OPERATE_TYPE = #operateType#
    @}
    @if(!isEmpty(code)){
        and (b.VIN like #code# or b.TERMINAL like #code# or b.SIMCARD like #code#)
    @}
    @if(!isEmpty(beginTime)){
        and b.CREATE_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        and b.CREATE_TIME <= #endTime#
    @}  
order by b.id desc
    
history
===
select 
b.*,
o.org_name,
v.REGIST_DATE,
v.SERVICE_END_DATE
from      
v_business_log b
LEFT JOIN base_organization o ON b.ORGANIZATION_ID = o.ID
INNER JOIN v_vehicle v ON v.vin = b.vin
where b.vin = #vin#     
and b.OPERATE_TYPE = #operateType#
order by b.id desc