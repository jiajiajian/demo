pageQueryAlarm
===
SELECT
@pageTag() {
	h.VIN,
  @if(!isEmpty(alarmState) && alarmState == 1){
    0 as item_num,
  @}
  @if(isEmpty(alarmState) || alarmState == 0){
    a.count AS item_num,
  @}
	t.`CODE` terminal_code,
	s.`CODE` sim_card,
	vr.DATA_UPDATE_TIME,
	CONCAT(vr.GPS_PROVINCE,vr.GPS_CITY,vr.GPS_AREA) as ADDRESS,
	vr.LAST_LON,
	vr.LAST_LAT
@}
FROM
	( SELECT VIN FROM v_alarm_history 
	    @if(!isEmpty(organizationId)){
    	    INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON temp.id = ORGANIZATION_ID
    	@}
	    WHERE ALARM_TYPE = 1
	    @if(!isEmpty(alarmState)){
             AND ALARM_STATE = #alarmState#
         @}
         @if(!isEmpty(beginTime)){
             AND BEGIN_TIME >= #beginTime#
         @}
         @if(!isEmpty(endTime)){
             AND BEGIN_TIME <= #endTime#
         @}
	  GROUP BY VIN ) h
	INNER JOIN (
    SELECT
	VIN,
	COUNT( 0 ) count 
FROM
	( SELECT VIN, ALARM_CODE, COUNT( 0 ) FROM v_alarm_history WHERE ALARM_TYPE = 1
	  @if(!isEmpty(alarmState)){
        AND ALARM_STATE = #alarmState#
      @}
	 GROUP BY VIN, ALARM_CODE ) a 
GROUP BY
	a.VIN 
	) a ON h.VIN = a.VIN
	INNER JOIN v_vehicle v ON v.vin = h.vin
	INNER JOIN v_vehicle_realtime vr ON vr.vin = v.vin
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN ( SELECT vin FROM v_alarm_info where ALARM_TYPE = 1 GROUP BY VIN ) ai ON ai.vin = a.vin
	where 1=1
    @if(!isEmpty(code)){
        AND (h.VIN LIKE #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}
    @if(!isEmpty(alarmState) && alarmState == 0){
        AND ai.vin IS NOT NULL
    @}
    @if(!isEmpty(alarmState) && alarmState == 1){
        AND ai.vin IS NULL
    @}     

   
pageQuerySingleVehicleAlarm
===
SELECT
@pageTag() {
	d.ITEM_NAME alarm_name,
	h.ADDRESS,
	h.ALARM_TYPE,
	h.BEGIN_TIME,
	h.END_TIME,
	h.ID
@} 
FROM
	v_alarm_history h
	LEFT JOIN base_dic_item d ON d.ITEM_CODE = h.ALARM_CODE 
WHERE
    h.ALARM_TYPE = 1
	and h.VIN = #vin# 
    @if(!isEmpty(alarmState)){
        AND h.ALARM_STATE = #alarmState#
    @}
    @if(!isEmpty(beginTime)){
        AND h.BEGIN_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        AND h.BEGIN_TIME <= #endTime#
    @} 
    @if(!isEmpty(alarmCode)){
        AND d.ITEM_NAME like #alarmCode#
    @}
ORDER BY h.ID DESC
     
pageQueryFault
===
SELECT
@pageTag() {
	h.VIN,
  @if(!isEmpty(alarmState) && alarmState == 1){
       0 as spn_Fmi_Num,
  @}
  @if(isEmpty(alarmState) || alarmState == 0){
       b.spn_Fmi_Num,
  @}
	t.`CODE` terminal_code,
	s.`CODE` sim_card,
	vr.DATA_UPDATE_TIME,
	CONCAT(vr.GPS_PROVINCE,vr.GPS_CITY,vr.GPS_AREA) as ADDRESS,
    vr.LAST_LON,
    vr.LAST_LAT,
	d.mtu_count
@}
FROM
	(
SELECT
	VIN 
FROM
	v_alarm_history
@if(!isEmpty(organizationId)){	
	INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON temp.id = ORGANIZATION_ID 
@}
WHERE
	ALARM_TYPE = 2 
	    @if(!isEmpty(alarmState)){
             AND ALARM_STATE = #alarmState#
         @}
         @if(!isEmpty(beginTime)){
             AND BEGIN_TIME >= #beginTime#
         @}
         @if(!isEmpty(endTime)){
             AND BEGIN_TIME <= #endTime#
         @}
GROUP BY
	VIN 
	) h
	INNER JOIN (
SELECT
	VIN,
	COUNT( 0 ) AS spn_Fmi_Num 
FROM
	( SELECT VIN, spn_fmi FROM v_alarm_history WHERE ALARM_TYPE = 2 
	    @if(!isEmpty(alarmState)){
             AND ALARM_STATE = #alarmState#
         @}
	 GROUP BY VIN, spn_fmi ) a 
GROUP BY
	a.VIN 
	) b ON b.VIN = h.VIN
INNER JOIN (
    SELECT
	    h.VIN,
	    SUM( h.FREQUENCY ) AS mtu_count 
    FROM
	    v_alarm_history h 
    WHERE
	    ALARM_TYPE = 2 
	    AND TLA = 'MTU' 
        @if(!isEmpty(beginTime)){
           AND h.BEGIN_TIME >= #beginTime#
        @}
        @if(!isEmpty(endTime)){
           AND h.BEGIN_TIME <= #endTime#
        @}
    GROUP BY
	    h.VIN 
	    ) d ON d.vin = h.vin
    LEFT JOIN ( SELECT vin FROM v_alarm_info where ALARM_TYPE = 2 GROUP BY VIN ) ai ON ai.vin = b.vin
	INNER JOIN v_vehicle v ON v.vin = h.vin
	INNER JOIN v_vehicle_realtime vr ON vr.vin = v.vin
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	where 1=1
    @if(!isEmpty(code)){
        AND (h.VIN LIKE #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}
    @if(!isEmpty(alarmState) && alarmState == 0){
        AND ai.vin IS NOT NULL
    @}
    @if(!isEmpty(alarmState) && alarmState == 1){
        AND ai.vin IS NULL
    @}

pageQuerySingleVehicleFault
===
SELECT
@pageTag() {
    h.ID,
	h.VIN,
	h.BEGIN_TIME,
	h.END_TIME,
	h.ADDRESS,
	h.ALARM_TYPE,
	h.FREQUENCY,
	h.RECENTLY_CONDITION,
	h.SPN_FMI,
	a.SPN_NAME,
	a.FMI_NAME,
    h.TLA 
@} 
FROM
	v_alarm_history h
	LEFT JOIN (
        SELECT
            CONCAT( i.SPN, '.', i.FMI, '.', vt.TLA ) AS spn_fmi,
            SPN_NAME,
            FMI_NAME
        FROM
            v_fault_dict_item i
            INNER JOIN v_tla vt ON vt.id = i.TLA_ID 
        WHERE
            i.ORGANIZATION_ID = ( SELECT o.ROOT_ORG_ID FROM v_vehicle v INNER JOIN base_organization o ON v.ORGANIZATION_ID = o.ID WHERE v.VIN = #vin# )  
        GROUP BY
            SPN,
            FMI,
            vt.TLA_ID 
            ) a ON a.spn_fmi = CONCAT(h.spn_fmi,'.',h.tla) 
WHERE
    h.ALARM_TYPE = 2
	and h.VIN = #vin# 
    @if(!isEmpty(alarmState)){
        AND h.ALARM_STATE = #alarmState#
    @}
    @if(!isEmpty(beginTime)){
        AND h.BEGIN_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        AND h.BEGIN_TIME <= #endTime#
    @} 
    @if(!isEmpty(spnFmi)){
        AND h.SPN_FMI like #spnFmi#
    @}
    @if(!isEmpty(tla)){
        AND h.tla = #tla#
    @}
ORDER BY h.ID DESC
   
   
pageQuerySingleVehicleFault1
===
SELECT
@pageTag() {
    h.ID,
	h.VIN,
	h.BEGIN_TIME,
	h.END_TIME,
	h.ADDRESS,
	h.ALARM_TYPE,
	h.FREQUENCY,
	h.SPN_FMI,
	h.RECENTLY_CONDITION,
	a.SPN_NAME,
	a.FMI_NAME
@} 
FROM
	v_alarm_history h
	LEFT JOIN (
        SELECT
            CONCAT( i.SPN, '.', i.FMI ) AS spn_fmi,
            SPN_NAME,
            FMI_NAME
        FROM
            v_fault_dict_item i
        WHERE
            i.ORGANIZATION_ID = ( SELECT o.ROOT_ORG_ID FROM v_vehicle v INNER JOIN base_organization o ON v.ORGANIZATION_ID = o.ID WHERE v.VIN = #vin# )  
        GROUP BY
            SPN,
            FMI
            ) a ON a.spn_fmi = h.spn_fmi
WHERE
    h.ALARM_TYPE = 2
	and h.VIN = #vin# 
    @if(!isEmpty(alarmState)){
        AND h.ALARM_STATE = #alarmState#
    @}
    @if(!isEmpty(beginTime)){
        AND h.BEGIN_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        AND h.BEGIN_TIME <= #endTime#
    @} 
    @if(!isEmpty(spnFmi)){
        AND h.SPN_FMI like #spnFmi#
    @}
ORDER BY h.ID DESC   

getRootOrgIdBySelectId
===
SELECT
ROOT_ORG_ID
FROM
base_organization
WHERE
ID = #organizationId#      

pageQueryFence
===
SELECT
@pageTag() {
	h.vin,
	o.ORG_NAME,
	f.FENCE_TYPE,
	f.ALARM_TYPE,
	h.ADDRESS,
	h.BEGIN_TIME,
	h.END_TIME 
@}
FROM
	v_alarm_history h
	INNER JOIN (
SELECT
	MAX( a.ID ) id,
	a.VIN 
FROM
	( SELECT ID, VIN FROM v_alarm_history WHERE ALARM_TYPE = 3 ORDER BY ID DESC ) a 
GROUP BY
	a.VIN 
	) b ON h.VIN = b.VIN 
	AND h.id = b.id
	INNER JOIN v_fence f ON f.ID = h.FENCE_ID
	LEFT JOIN v_vehicle v ON v.vin = h.vin
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID	
	@if(!isEmpty(organizationId)){	
	INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON temp.id = v.ORGANIZATION_ID
	@}
	LEFT JOIN base_organization o ON o.ID = v.ORGANIZATION_ID
WHERE
	1=1
    @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(code)){
        AND (h.VIN LIKE #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}
    @if(!isEmpty(vehicleModelId)){
        AND v.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(vehicleTypeId)){
        AND v.VEHICLE_TYPE_ID = #vehicleTypeId#
    @} 
    @if(!isEmpty(fenceType)){
        AND f.FENCE_TYPE = #fenceType#
    @}
    @if(!isEmpty(alarmType)){
        AND f.ALARM_TYPE = #alarmType#
    @}                 	
    @if(!isEmpty(alarmState)){
        AND h.ALARM_STATE = #alarmState#
    @} 
    @if(!isEmpty(beginTime)){
        AND h.BEGIN_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        AND h.BEGIN_TIME <= #endTime#
    @}
 
pageQuerySingleVehicleFence
===   
SELECT
@pageTag() {
	h.vin,
	f.FENCE_TYPE,
	f.ALARM_TYPE,
	h.ADDRESS,
	h.BEGIN_TIME,
	h.END_TIME 
@}
FROM
	v_alarm_history h
	INNER JOIN v_fence f ON f.ID = h.FENCE_ID 
WHERE
	h.ALARM_TYPE = 3 
	AND h.vin = #vin#
 @if(!isEmpty(fenceType)){
        AND f.FENCE_TYPE = #fenceType#
    @}
    @if(!isEmpty(alarmType)){
        AND f.ALARM_TYPE = #alarmType#
    @}                 	
    @if(!isEmpty(beginTime)){
        AND h.BEGIN_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        AND h.BEGIN_TIME <= #endTime#
    @}
ORDER BY h.ID DESC
    
count
===   
SELECT
	a.alarm_type,
	COUNT( 0 ) count
FROM
	(
SELECT
	h.VIN,
	h.ALARM_TYPE,
	COUNT( 0 ) 
FROM
	v_alarm_info h
	INNER JOIN v_vehicle v ON v.VIN = h.vin
	@if(!isEmpty(organizationId)){	
	INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON temp.id = v.ORGANIZATION_ID 
	@}
GROUP BY
	h.vin,
	h.ALARM_TYPE 
	) a 
GROUP BY
	a.ALARM_TYPE 

getMaintenanceCount
===
SELECT
	count( 1 ) 
FROM
	v_maintenance_log log
inner JOIN v_vehicle vv ON log.VIN=vv.VIN		 
WHERE
	log.HANDLE_STATUS =0

countUnDealAlarmOrFault
===   
SELECT
	COUNT( 0 ) as count
FROM
	v_alarm_history h
	INNER JOIN v_vehicle v ON v.VIN = h.vin
    LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
    LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
@if(!isEmpty(organizationId)){	
	INNER JOIN ( SELECT id FROM base_organization 
	WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp 
	ON temp.id = h.ORGANIZATION_ID 
 @}
WHERE 
    h.ALARM_STATE=0
    @if(!isEmpty(financeId)){
        AND v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(beginTime)){
        AND h.BEGIN_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        AND h.END_TIME <= #endTime#
    @} 	
    @if(!isEmpty(code)){
        AND (h.VIN LIKE #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}


getVehicleFaultDetail
===
SELECT
	h.VIN,
	org.ORG_NAME,
    s.`CODE` sim_card,
    t.`CODE` terminal_code,
    h.SPN_FMI,
    h.TLA,
    h.LAT,
    h.LON,
	h.BEGIN_TIME,
	h.END_TIME,
	h.DURATION,
	h.ADDRESS,
	a.*
FROM
	v_alarm_history h
	LEFT JOIN (
        SELECT
            CONCAT( SPN, '.', FMI ) AS spn_fmi,
            SPN_NAME,
            FMI_NAME,
            TLA,
            SPN,
            FMI
        FROM
            v_fault_dict_item i
            INNER JOIN v_fault_dict d ON i.DICT_ID = d.id  
        GROUP BY
            SPN,
            FMI
	) a ON a.spn_fmi = h.spn_fmi 
	INNER JOIN v_vehicle v ON v.VIN = h.vin
    LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_organization org on org.ID = h.ORGANIZATION_ID
WHERE
    h.ALARM_TYPE = 2
	and h.ID = #id#
	
getVehicleAlarmDetail
===
SELECT
	h.VIN,
    s.`CODE` sim_card,
    t.`CODE` terminal_code,
    h.LAT,
    h.LON,
    h.TLA,
	h.BEGIN_TIME,
	h.END_TIME,
	h.DURATION,
	h.ADDRESS,
	h.ALARM_CODE,
	org.ORG_NAME,
	d.ITEM_NAME alarm_name
FROM
	v_alarm_history h
	INNER JOIN v_vehicle v ON v.VIN = h.vin
    LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_organization org on org.ID = h.ORGANIZATION_ID
	LEFT JOIN base_dic_item d ON d.ITEM_CODE = h.ALARM_CODE 
WHERE
    h.ALARM_TYPE = 1
	and h.ID = #id#
	
pageQueryAlarmFaultList
===
SELECT
@pageTag() {
    h.id,
   	h.ALARM_STATE,
   	h.vin,
   	h.TLA,
   	H.SPN_FMI,
   	o.ORG_NAME,
   	h.ADDRESS,
   	h.BEGIN_TIME,
   	h.END_TIME,
   	h.ALARM_TYPE,
   	d.ITEM_NAME alarm_name
@}
FROM
	v_alarm_history h
	LEFT JOIN v_vehicle v ON v.vin = h.vin
	LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID	
	@if(!isEmpty(organizationId)){	
	INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp ON temp.id = v.ORGANIZATION_ID
	@}
	LEFT JOIN base_organization o ON o.ID = v.ORGANIZATION_ID
	LEFT JOIN base_dic_item d ON d.ITEM_CODE = h.ALARM_CODE 
WHERE
	1=1
    @if(!isEmpty(financeId)){
        and v.FINANCE_ID = #financeId#
    @}
    @if(!isEmpty(code)){
        AND (h.VIN LIKE #code# or t.`CODE` like #code# or s.`CODE` like #code#)
    @}      	
    @if(!isEmpty(beginTime)){
        AND h.BEGIN_TIME >= #beginTime#
    @}
    @if(!isEmpty(endTime)){
        AND h.BEGIN_TIME <= #endTime#
    @}
ORDER BY ALARM_STATE ASC, BEGIN_TIME DESC
	log.HANDLE_STATUS =0


dTCEffectStatistic
===
select 
@pageTag() {
    *
@}
from (
    SELECT
        tem.SPN_FMI, 
        tem.SPN_NAME, 
        tem.FMI_NAME,
        tem.TLA,
        COUNT(DISTINCT tem.vin) num
    from ( 
        SELECT
            his.vin,
            his.SPN_FMI,
            tt.TLA,
            t.SPN_NAME,
            t.FMI_NAME
        FROM
            v_alarm_history his
        INNER JOIN v_vehicle v ON v.vin = his.VIN
        INNER JOIN base_organization org ON org.id = v.ORGANIZATION_ID
        LEFT JOIN v_tla tt ON his.TLA = tt.TLA_ID AND tt.ORGANIZATION_ID = org.ROOT_ORG_ID
        LEFT JOIN (
            SELECT
                CONCAT(fdi.SPN, '.', fdi.FMI) AS spn_fmi,
                fd.ORGANIZATION_ID,
                fdi.SPN_NAME,
                fdi.FMI_NAME
            FROM
                v_fault_dict_item fdi
            INNER JOIN v_fault_dict fd ON fd.id = fdi.DICT_ID
        ) t ON t.ORGANIZATION_ID = org.ROOT_ORG_ID
        AND t.spn_fmi = his.SPN_FMI
        @if(isNotEmpty(orgId)){
            INNER JOIN ( SELECT id FROM base_organization WHERE path LIKE CONCAT( 
                                    ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) 
                        ) temp ON temp.id = v.ORGANIZATION_ID
        @}
        WHERE
            his.ALARM_TYPE = 2
            @if(isNotEmpty(startTime)){
                and his.BEGIN_TIME >= #startTime# 
            @}
            @if(isNotEmpty(endTime)){
                and his.BEGIN_TIME <= #endTime# 
            @}
    ) tem
    GROUP BY tem.SPN_FMI, tem.SPN_NAME, tem.FMI_NAME, tem.TLA
) ch

	   	