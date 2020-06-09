getReportDailyData
===
SELECT
@pageTag() {
	t.*,
	vvt.`NAME` typeName,
	org.ORG_NAME,
	vvm.`NAME` modelName
@} 	 
FROM
	(
	SELECT
		vd.VIN,
		IFNULL( ROUND( SUM( vvr.TOTAL_WORK_TIME )/( 60 * 60 ), 2 ), 0 ) totalWorkingTime,
		IFNULL( ROUND( SUM( vd.HAMMER_TIME )/( 1000 * 60 * 60 ), 2 ), 0 ) hammerTime,
		IFNULL( ROUND( SUM( vd.ENGINE_RUN_TIME )/( 1000 * 60 * 60 ), 2 ), 0 ) engineRunTime,
		IFNULL( ROUND( SUM( vd.ENGINE_NOT_RUN_TIME )/( 1000 * 60 * 60 ), 2 ), 0 ) engineNotRunTime,
		MAX( vd.MAX_COOLANT_TEMP ) maxCoolantTemp,
		MAX( vd.MAX_H_OIL_TEMP ) maxHOilTemp,
		MAX( vd.MAX_EIM_TEMP ) maxEimTemp,
		AVG( vd.END_LF_CONSUMPTION ) endLfConsumption,
		AVG( END_DF_CONSUMPTION ) endDfConsumption 
	FROM
		tls_report_vehicle_daily vd
	LEFT JOIN v_vehicle_realtime vvr ON vd.VIN = vvr.VIN	 
	WHERE
		1 = 1
		 @if(!isEmpty(beginTime)){
             and vd.CREATE_TIME > #beginTime#
         @}
         @if(!isEmpty(endTime)){
             and vd.CREATE_TIME + 24*60*60*1000 < #endTime#
         @} 
	GROUP BY
		vd.VIN 
	) t
	inner JOIN v_vehicle vv ON t.VIN = vv.VIN
	@if(!isEmpty(organizationId)){
    	INNER JOIN 
    	   ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) temp
    	    ON vv.ORGANIZATION_ID = temp.id
    @}
	LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID = vvt.id
	left join base_organization org on vv.ORGANIZATION_ID=org.id
	LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID = vvm.id
	where 1=1
	 @if(!isEmpty(vehicleTypeId)){
             and vvt.id = #vehicleTypeId#
     @}
     @if(!isEmpty(vehicleModelId)){
             and vvm.id = #vehicleModelId#	
     @} 
 
getEngineData
===
SELECT
 	vd.ENGINE_SPEED_RATIO,
 	vd.ENGINE_NOT_RUN_TIME,
 	vd.ENGINE_RUN_TIME,
 	vd.DATE_VAL 
 FROM
 	tls_report_vehicle_daily vd     	     
 where 1=1
 	 @if(!isEmpty(startTime)){
          and vd.CREATE_TIME > #startTime#
     @}
     @if(!isEmpty(endTime)){
          and vd.CREATE_TIME + 24*60*60*1000 < #endTime#
     @}
     @if(!isEmpty(vin)){
          and vd.vin = #vin#
     @}	