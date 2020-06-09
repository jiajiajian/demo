getCountByProvince
===
SELECT
	vvr.GPS_PROVINCE province,
	count( 1 ) count
FROM
	v_vehicle_realtime vvr
LEFT JOIN v_vehicle vv ON vv.VIN = vvr.VIN
@if(!isEmpty(organizationId)){
	    INNER JOIN 
	    ( SELECT 
	    id FROM base_organization 
	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
	    ) temp
	    ON vv.ORGANIZATION_ID = temp.id
    @}
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
where
    1=1 and vvr.GPS_PROVINCE is not null
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE_CODE = #province#
    @}	  
GROUP BY
	vvr.GPS_PROVINCE 
	
getCountByCity
===
SELECT
	vvr.GPS_CITY province,
	count( 1 ) count
FROM
	v_vehicle_realtime vvr
LEFT JOIN v_vehicle vv ON vv.VIN = vvr.VIN
@if(!isEmpty(organizationId)){
	    INNER JOIN 
	    ( SELECT 
	    id FROM base_organization 
	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
	    ) temp
	    ON vv.ORGANIZATION_ID = temp.id
    @}
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
where
    1=1 and vvr.GPS_PROVINCE is not null
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE_CODE = #province#
    @}	  
GROUP BY
	vvr.GPS_CITY 	  
	
getCountByVehicleType
===
SELECT
	vvt.`NAME` vehicleTypeName,
	count( 1 ) count
FROM
	v_vehicle vv
LEFT JOIN v_vehicle_realtime vvr ON vv.VIN = vvr.VIN
@if(!isEmpty(organizationId)){
	    INNER JOIN 
	    ( SELECT 
	    id FROM base_organization 
	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
	    ) temp
	    ON vv.ORGANIZATION_ID = temp.id
    @}
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
where
    1=1
    and vv.VEHICLE_TYPE_ID is not null
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}  
	GROUP BY vvt.`NAME`
	
getTotalWorkTimeByProvince
===
SELECT
	vvr.GPS_PROVINCE province,
	IFNULL(ROUND(SUM(vvr.TOTAL_WORK_TIME)/3600,2),0) totalWorkTime
FROM
	v_vehicle_realtime vvr 
LEFT JOIN v_vehicle vv ON vv.VIN = vvr.VIN
   @if(!isEmpty(organizationId)){
    	INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) temp
    	    ON vv.ORGANIZATION_ID = temp.id
        @}
WHERE
	vvr.GPS_PROVINCE IS NOT NULL 
	@if(!isEmpty(vehicleTypeId)){
                and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
                and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
                and vvr.GPS_PROVINCE_CODE = #province#
    @}
GROUP BY
	vvr.GPS_PROVINCE

getTotalWorkTimeByCity
===
SELECT
	vvr.GPS_CITY province,
	IFNULL(ROUND(SUM(vvr.TOTAL_WORK_TIME)/3600,2),0) totalWorkTime
FROM
	v_vehicle_realtime vvr 
LEFT JOIN v_vehicle vv ON vv.VIN = vvr.VIN
   @if(!isEmpty(organizationId)){
    	INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) temp
    	    ON vv.ORGANIZATION_ID = temp.id
        @}
WHERE
	vvr.GPS_PROVINCE IS NOT NULL 
	@if(!isEmpty(vehicleTypeId)){
                and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
                and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
                and vvr.GPS_PROVINCE_CODE = #province#
    @}
GROUP BY
	vvr.GPS_CITY		

getWorkTime
===
SELECT
	vvr.VIN,
	vvr.TOTAL_WORK_TIME,
	IFNULL( t.workTime, 0 ) workTime,
	vvt.`NAME` vehicleTypeName,
	vvm.`NAME` vehicleModelName,
	org.ORG_NAME orgName,
	FROM_UNIXTIME(vvr.DATA_UPDATE_TIME/1000,'%Y-%m-%d %H:%i:%S') AS updateTime,
	vvr.GPS_ADDRESS 
FROM
	v_vehicle_realtime vvr
	LEFT JOIN v_vehicle vv ON vvr.VIN = vv.VIN
	@if(!isEmpty(organizationId)){
        INNER JOIN 
        	( SELECT 
        	    id FROM base_organization 
        	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
        	 ) temp
        	 ON vv.ORGANIZATION_ID = temp.id
    @}
	LEFT JOIN base_organization org ON vv.ORGANIZATION_ID = org.ID
	LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID = vvt.id
	LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID = vvt.id
	LEFT JOIN (
	SELECT
		wt.VIN,
		IFNULL( SUM( wt.WORKING_TIME )/( 1000 * 60 * 60 ), 0 ) workTime 
	FROM
		v_vehicle_working_time wt 
	WHERE
		1 = 1
	@if(!isEmpty(beginTime)){
              and wt.begin_time > #beginTime#
    @}
    @if(!isEmpty(endTime)){
              and wt.end_time < #endTime#
    @}    		 
	GROUP BY
	wt.VIN 
	) t ON vvr.VIN = t.VIN
	where 1=1
	@if(!isEmpty(vehicleTypeId)){
             and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
             and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(vin)){
             and (vv.vin like #vin# or tt.`CODE` like #vin# or ts.`CODE` like #vin#) 
    @}
    
getMachineWorkDetail
===
   SELECT
    	FROM_UNIXTIME( vwt.BEGIN_TIME / 1000, '%Y-%m-%d %H:%i:%S' ) beginTime,
    	FROM_UNIXTIME( vwt.END_TIME / 1000, '%Y-%m-%d %H:%i:%S' ) endTime,
    	ROUND(vwt.WORKING_TIME/3600000,2) workTime
   FROM
    	v_vehicle_working_time vwt
   WHERE 
    	1=1
   @if(!isEmpty(beginTime)){
        and vwt.begin_time > #beginTime#
   @}
   @if(!isEmpty(endTime)){
        and vwt.end_time < #endTime#
   @}
   @if(!isEmpty(vin)){
        and vwt.vin = #vin#
    @}
    
getTotalWorkTimeDetail
===
SELECT  DISTINCT
      		 (vd.DATE_VAL) dateStr,
      		 vd.vin,
      		 ROUND( vd.TOTAL_WORKING_TIME / 3600000, 2 ) workTime
      	FROM
      		tls_report_vehicle_daily vd
      		LEFT JOIN v_vehicle vv ON vd.VIN = vv.VIN
      	WHERE vd.vin = #vin#
      	@if(!isEmpty(beginTime)){
                  and vd.CREATE_TIME > #beginTime#
          @}
          @if(!isEmpty(endTime)){
                  and vd.CREATE_TIME+24*60*60*1000 < #endTime#
          @}

getWorkTimeDetail
===
SELECT
	t.beginTime dateStr,
	SUM( workTime ) workTime 
FROM
	(
    SELECT
		FROM_UNIXTIME( vwt.BEGIN_TIME / 1000, '%Y-%m-%d' ) beginTime,
		ROUND( vwt.WORKING_TIME / 3600000, 2 ) workTime 
	FROM
		v_vehicle_working_time vwt 
	WHERE
		1 = 1
	@if(!isEmpty(beginTime)){
        and vwt.begin_time > #beginTime#
    @}
    @if(!isEmpty(endTime)){
        and vwt.end_time < #endTime#
    @} 
    @if(!isEmpty(vin)){
        and vwt.vin = #vin#
    @} 
	) t 
GROUP BY
	t.beginTime 

getOnlineRate
===
SELECT
	COUNT(*) onlineCount,
	t.DATE_VAL dateStr 
FROM
	(
	SELECT DISTINCT
		vd.VIN,
		vd.DATE_VAL 
	FROM
		tls_report_vehicle_daily vd
		LEFT JOIN v_vehicle vv ON vd.VIN = vv.VIN
	@if(!isEmpty(organizationId)){
            INNER JOIN 
            	( SELECT 
            	    id FROM base_organization 
            	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
            	 ) temp
            	 ON vv.ORGANIZATION_ID = temp.id
        @}	 
	WHERE
	    1=1
       @if(!isEmpty(vehicleTypeId)){
		 AND vv.VEHICLE_TYPE_ID = #vehicleTypeId# 
	    @}
		@if(!isEmpty(vehicleModelId)){
		    AND vv.VEHICLE_MODEL_ID = #vehicleModelId# 
		@}
		@if(!isEmpty(beginTime)){
                and vd.CREATE_TIME > #beginTime#
        @}
        @if(!isEmpty(endTime)){
                and vd.CREATE_TIME < #endTime#
        @} 
	) t 
GROUP BY
	DATE_VAL	    	
    	