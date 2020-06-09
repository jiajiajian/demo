getLogInfo
===
SELECT 
@pageTag() {
log.*
@}
 FROM base_log log
@if(!isEmpty(organizationId)){
 INNER JOIN(
 	SELECT
 		*
 	FROM
 		base_organization
 	WHERE path like CONCAT((SELECT path FROM base_organization WHERE id = #organizationId#), '%')
 )org ON log.organization_Id = org.id
@}	
where 1=1
@if(!isEmpty(financeId)){
    and log.FINANCE_ID = #financeId#
@}	
@if(!isEmpty(operateAccount)){
    and log.operate_account like #operateAccount#
@}	
@if(!isEmpty(startDate)){
    and log.operate_time >= #startDate#
@}
@if(!isEmpty(endDate)){
    and log.operate_time <= #endDate#
@}	