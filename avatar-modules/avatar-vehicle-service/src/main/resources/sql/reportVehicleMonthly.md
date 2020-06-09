monthAvgWorkTime
===
SELECT
	concat( substr( a.MONTH_VAL, 1, 4 ), '-', substr( a.MONTH_VAL, 5, 2 ) ) AS month,
	SUM( a.TOTAL_WORKING_TIME )  AS work_time
FROM
	(
SELECT
	rm.TOTAL_WORKING_TIME,
	rm.MONTH_VAL 
FROM
	tls_report_vehicle_monthly rm
	INNER JOIN v_vehicle v ON v.VIN = rm.VIN 
WHERE
1=1
    @if(!isEmpty(mergeFlag) && mergeFlag == 0){
      AND v.ORGANIZATION_ID = #organizationId# 
    @}
	AND rm.MONTH_VAL >= #beginMon# 
	AND rm.MONTH_VAL <= #endMon#  
    @if(!isEmpty(tonnage)){
    	AND rm.TONNAGE = #tonnage#  
    @}
	@if(!isEmpty(mergeFlag) && mergeFlag == 1){
    	and v.ORGANIZATION_ID in (#join(orgList)#)
    @}	
	) a 
GROUP BY
	a.MONTH_VAL
	
activeVehicleDistribute
===
SELECT
	VR.GPS_PROVINCE as PROVINCE,
	rm.TONNAGE,
	COUNT( 0 ) count 
FROM
	tls_report_vehicle_monthly rm
	INNER JOIN v_vehicle_realtime vr ON vr.VIN = rm.vin 
	INNER JOIN v_vehicle v ON v.VIN = rm.vin 
   @if(!isEmpty(tonnage)){
	INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON v.ORGANIZATION_ID = temp.id 
   @}
WHERE
	rm.MONTH_VAL = #month# 
GROUP BY
	VR.GPS_PROVINCE,
	RM.TONNAGE


vehicleModelAvgWorkTime
===
SELECT 
    m.id,
    m.`NAME` name,
    tls.YEAR_VAL year,
    tls.MONTH_VAL month,
    IFNULL(ROUND(SUM(tls.TOTAL_WORKING_TIME/3600000),2),0) workTime
FROM
    tls_report_vehicle_monthly tls
    INNER JOIN v_vehicle v ON v.VIN = tls.VIN
    @if(!isEmpty(organizationId)){
    INNER JOIN 
     ( SELECT 
       id FROM base_organization 
       WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
     ) temp
        ON v.ORGANIZATION_ID = temp.id
    @}
    INNER JOIN v_vehicle_model m on m.id = v.VEHICLE_MODEL_ID
WHERE
1=1 AND tls.YEAR_val IS NOT NULL AND tls.MONTH_val IS NOT NULL
@if(!isEmpty(financeId)){
     and v.FINANCE_ID = #financeId#
@}
@if(!isEmpty(year) && year != 0){
     and tls.YEAR_VAL = #year#
@}
GROUP BY m.id,tls.MONTH_VAL


orgAvgWorkTime
===
select 
    org.ORG_NAME name,
    tls.YEAR_VAL year,
    tls.MONTH_VAL month,
    IFNULL(ROUND(SUM(tls.TOTAL_WORKING_TIME/3600000),2),0) workTime
from base_organization org
INNER JOIN v_vehicle v ON v.ORGANIZATION_ID = org.ID
LEFT JOIN tls_report_vehicle_monthly tls on tls.VIN = v.VIN 
where LENGTH( org.path) - LENGTH(REPLACE( org.PATH,'/','' )) = 2
AND tls.YEAR_val IS NOT NULL AND tls.MONTH_val IS NOT NULL
GROUP BY org.ORG_NAME,tls.MONTH_VAL

orgVehicleCount
===
select 
    temp.name as name, 
    sum(1) as count
from v_vehicle v
RIGHT join 
(
    select 
    org.ID,
    org.ORG_NAME as name
    from base_organization org
    INNER JOIN v_vehicle v ON v.ORGANIZATION_ID = org.ID
    where LENGTH( org.path) - LENGTH(REPLACE( org.PATH,'/','' )) = 2
    GROUP BY org.ID
) temp on temp.ID=v.ORGANIZATION_ID
GROUP BY temp.name