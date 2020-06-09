getDashBoardList
===
SELECT
@pageTag(){
	vv.VIN,
	tt.`CODE` terminalCode,
	org.ORG_NAME orgName,
	vvt.`NAME` typeName,
	vvm.`NAME` modelName,
	vvr.GPS_TIME,
	vvr.DATA_UPDATE_TIME,
	vvr.GPS_ADDRESS
@}
FROM
	v_vehicle vv
LEFT JOIN t_terminal tt ON vv.TERMINAL_ID=tt.id	
LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
LEFT JOIN v_vehicle_realtime vvr ON vv.VIN = vvr.VIN
LEFT JOIN base_customer bc on vv.CUSTOMER_ID=bc.id
where
    1=1
    @if(!isEmpty(organizationId)){
            and vv.ORGANIZATION_ID = #organizationId#
    @}
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE = #province#
    @}
    @if(!isEmpty(city)){
            and vv.GPS_CITY = #city#
    @}
    @if(!isEmpty(area)){
            and vv.GPS_AREA = #area#
    @}
    @if(!isEmpty(lock)){
            and vvr.'LOCK' = #lock#
    @}
    @if(!isEmpty(acc)){
            and vvr.ACC = #acc#
    @}
    @if(!isEmpty(customerName)){
            and bc.'NAME' like #customerName#
    @}
    @if(!isEmpty(vin)){
            and (vv.vin like #vin# or ts.'CODE' like #vin# or tt.'CODE' like #vin#)
    @}    
    
getCountByProvince
===
SELECT
	vvr.GPS_PROVINCE,
	count( 1 ) 
FROM
	v_vehicle_realtime vvr
LEFT JOIN v_vehicle vv ON vv.VIN = vvr.VIN
LEFT JOIN t_terminal tt ON vv.TERMINAL_ID=tt.id	
LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
LEFT JOIN base_organization org ON vv.ORGANIZATION_ID=org.ID
LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID=vvt.id
LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID=vvm.id
LEFT JOIN base_customer bc on vv.CUSTOMER_ID=bc.id	
where
    1=1
    @if(!isEmpty(organizationId)){
            and vv.ORGANIZATION_ID = #organizationId#
    @}
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE = #province#
    @}
    @if(!isEmpty(city)){
            and vv.GPS_CITY = #city#
    @}
    @if(!isEmpty(area)){
            and vv.GPS_AREA = #area#
    @}
    @if(!isEmpty(lock)){
            and vvr.'LOCK' = #lock#
    @}
    @if(!isEmpty(acc)){
            and vvr.ACC = #acc#
    @}
    @if(!isEmpty(customerName)){
            and bc.'NAME' like #customerName#
    @}
    @if(!isEmpty(vin)){
            and (vv.vin like #vin# or ts.'CODE' like #vin# or tt.'CODE' like #vin#)
    @}	  
GROUP BY
	vvr.GPS_PROVINCE     
    
getCountByCity
===    
SELECT
	vvr.GPS_PROVINCE,
	vvr.GPS_CITY,
	count( 1 ) 
FROM
	v_vehicle_realtime vvr
	LEFT JOIN v_vehicle vv ON vv.VIN = vvr.VIN
	LEFT JOIN t_terminal tt ON vv.TERMINAL_ID = tt.id
	LEFT JOIN t_simcard ts ON tt.SIMCARD_ID = ts.id
	LEFT JOIN base_organization org ON vv.ORGANIZATION_ID = org.ID
	LEFT JOIN v_vehicle_type vvt ON vv.VEHICLE_TYPE_ID = vvt.id
	LEFT JOIN v_vehicle_model vvm ON vv.VEHICLE_MODEL_ID = vvm.id
	LEFT JOIN base_customer bc ON vv.CUSTOMER_ID = bc.id 
where
    1=1
    @if(!isEmpty(organizationId)){
            and vv.ORGANIZATION_ID = #organizationId#
    @}
    @if(!isEmpty(vehicleTypeId)){
            and vv.VEHICLE_TYPE_ID = #vehicleTypeId#
    @}
    @if(!isEmpty(vehicleModelId)){
            and vv.VEHICLE_MODEL_ID = #vehicleModelId#
    @}
    @if(!isEmpty(province)){
            and vvr.GPS_PROVINCE = #province#
    @}
    @if(!isEmpty(city)){
            and vvr.GPS_CITY = #city#
    @}
    @if(!isEmpty(area)){
            and vvr.GPS_AREA = #area#
    @}
    @if(!isEmpty(lock)){
            and vvr.'LOCK' = #lock#
    @}
    @if(!isEmpty(acc)){
            and vvr.ACC = #acc#
    @}
    @if(!isEmpty(customerName)){
            and bc.'NAME' like #customerName#
    @}
    @if(!isEmpty(vin)){
            and (vv.vin like #vin# or ts.'CODE' like #vin# or tt.'CODE' like #vin#)
    @}	
GROUP BY
	vvr.GPS_PROVINCE,
	vvr.GPS_CITY         