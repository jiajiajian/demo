sample
===
* 注释

	select #use("cols")# from v_alarm_info  where  #use("condition")#

cols
===
	ID,VIN,BEGIN_TIME,ALARM_TYPE,ALARM_CODE,FMI,SPN,FAULT_PARAMETER,LON,LAT,ADDRESS,PROVINCE,CITY,AREA

updateSample
===
	
	ID=#id#,VIN=#vin#,BEGIN_TIME=#beginTime#,ALARM_TYPE=#alarmType#,ALARM_CODE=#alarmCode#,FMI=#fmi#,SPN=#spn#,FAULT_PARAMETER=#faultParameter#,LON=#lon#,LAT=#lat#,ADDRESS=#address#,PROVINCE=#province#,CITY=#city#,AREA=#area#

condition
===

	1 = 1  
	@if(!isEmpty(id)){
	 and ID=#id#
	@}
	@if(!isEmpty(vin)){
	 and VIN=#vin#
	@}
	@if(!isEmpty(beginTime)){
	 and BEGIN_TIME=#beginTime#
	@}
	@if(!isEmpty(alarmType)){
	 and ALARM_TYPE=#alarmType#
	@}
	@if(!isEmpty(alarmCode)){
	 and ALARM_CODE=#alarmCode#
	@}
	@if(!isEmpty(fmi)){
	 and FMI=#fmi#
	@}
	@if(!isEmpty(spn)){
	 and SPN=#spn#
	@}
	@if(!isEmpty(faultParameter)){
	 and FAULT_PARAMETER=#faultParameter#
	@}
	@if(!isEmpty(lon)){
	 and LON=#lon#
	@}
	@if(!isEmpty(lat)){
	 and LAT=#lat#
	@}
	@if(!isEmpty(address)){
	 and ADDRESS=#address#
	@}
	@if(!isEmpty(province)){
	 and PROVINCE=#province#
	@}
	@if(!isEmpty(city)){
	 and CITY=#city#
	@}
	@if(!isEmpty(area)){
	 and AREA=#area#
	@}
	


pageQueryCurrentVehicleFault
===
SELECT
@pageTag() {
	h.VIN,
	h.BEGIN_TIME,
	h.ADDRESS,
	h.FREQUENCY,
	h.SPN_FMI,
	a.*
@} 
FROM
	v_alarm_info h
	INNER JOIN (
SELECT
	CONCAT( SPN, '.', FMI ) AS spn_fmi,
	SPN,
	FMI,
	SPN_NAME,
	FMI_NAME,
	TLA 
FROM
	v_fault_dict_item i
	INNER JOIN v_fault_dict d ON i.DICT_ID = d.id 
WHERE
	d.ORGANIZATION_ID = ( SELECT o.ROOT_ORG_ID FROM v_vehicle v INNER JOIN base_organization o ON v.ORGANIZATION_ID = o.ID WHERE v.VIN = #vin# )  
GROUP BY
	SPN,
	FMI 
	) a ON a.spn_fmi = h.spn_fmi
	LEFT JOIN v_vehicle v ON v.vin = h.vin
WHERE
    h.ALARM_TYPE = 2
	and h.VIN = #vin# 