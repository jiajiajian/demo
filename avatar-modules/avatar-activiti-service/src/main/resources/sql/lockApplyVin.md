sample
===
* 注释

	select #use("cols")# from r_lock_apply_vin  where  #use("condition")#

cols
===
	apply_id,vin

updateSample
===
	
	apply_id=#applyId#,vin=#vin#

condition
===

	1 = 1  
	@if(!isEmpty(applyId)){
	 and apply_id=#applyId#
	@}
	@if(!isEmpty(vin)){
	 and vin=#vin#
	@}
	
pageQuery
===
SELECT
@pageTag() {
	ra.apply_code,
	t.`CODE` terminal_code,
	s.`CODE` sim_card,
	org.ORG_NAME,
	vt.`NAME` vehicle_type,
	vm.`NAME` vehicle_model
@}
FROM
	r_lock_apply_vin rav
LEFT JOIN r_lock_apply ra on ra.id = rav.apply_id
LEFT JOIN v_vehicle v ON V.VIN = rav.VIN
LEFT JOIN v_vehicle_type vt ON vt.id = v.VEHICLE_TYPE_ID
LEFT JOIN v_vehicle_model vm ON vm.id = v.VEHICLE_MODEL_ID
LEFT JOIN t_terminal t ON t.id = v.TERMINAL_ID
LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
LEFT JOIN base_organization org ON v.ORGANIZATION_ID = org.id
where rav.apply_id = #applyId#
@if(!isEmpty(vin)){
    and (v.vin like #code# or t.`CODE` like #vin# or s.`CODE` like #vin#)
@}