pageQuery
===
SELECT
@pageTag() {
	vvm.ID,
	vvm.ITEM_NAME,
	vvm.ITEM_DETAIL,
	org.ORG_NAME,
	vvm.CREATE_USER_ACCOUNT,
	FROM_UNIXTIME( vvm.CREATE_TIME / 1000, '%Y-%m-%d %H:%i:%S' ) AS createTimeStr 
 @}	
FROM
	v_vehicle_maintenance vvm
LEFT JOIN base_organization org ON vvm.ORGANIZATION_ID = org.ID
@if(!isEmpty(organizationId)){
            INNER JOIN 
            	( SELECT 
            	  id FROM base_organization 
            	  WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
            	) temp
            	   ON vvm.ORGANIZATION_ID = temp.id
    @}
where
    1=1
    @if(!isEmpty(itemName)){
        and vvm.item_name like #itemName#	
    @}