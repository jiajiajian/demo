getInfoListByTacticsId
===
SELECT
	vmi.ID,
	vmi.TACTICS_ID,
	vmi.MAINTENANCE_TYPE,
	vmi.MAINTENANCE_CONTENT,
	vmi.HOURS 
FROM
	v_maintenance_info vmi 
WHERE 
    vmi.TACTICS_ID = #tacticsId#
	
getInfoListByLogId
===
SELECT
	vmi.ID,
	vmi.MAINTENANCE_TYPE,
	vmi.HOURS,
	vmi.MAINTENANCE_CONTENT 
FROM
	v_maintenance_info vmi
	LEFT JOIN v_maintenance_bind vmb ON vmi.TACTICS_ID = vmb.MAINTENANCE_TACTICS_ID
	LEFT JOIN v_vehicle vv ON vv.VEHICLE_TYPE_ID = vmb.VEHICLE_TYPE_ID 
	AND vv.VEHICLE_MODEL_ID = vmb.VEHICLE_MODEL_ID
	LEFT JOIN v_maintenance_log vml ON vv.VIN = vml.VIN 
	AND vml.MAINTENANCE_TYPE = vmi.MAINTENANCE_TYPE 
	AND vml.INTERVAL_HOURS = vmi.HOURS
where 
    vml.id = #logId#		