updateFaultStatus
===
UPDATE v_vehicle_realtime 
SET FAULT_STATUS = #status# 
WHERE
	VIN = #vin#