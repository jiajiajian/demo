pageQuery
===
SELECT
@pageTag(){
	t.*,
	FROM_UNIXTIME(t.PRODUCE_DATE/1000,'%Y-%m-%d') AS produceDateFormat,
	FROM_UNIXTIME(t.CREATE_TIME/1000,'%Y-%m-%d') AS createTimeFormat,
	s.`CODE` simCode,
	sv.`NAME` softName,
	sv.COLLECT_FUNCTION_ID,
	fs.code collectName,
	sv.LOCK_FUNCTION_ID,
	fs2.code lockName,
	d.`ITEM_NAME` protocolName
@}
FROM
	t_terminal t
LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
LEFT JOIN t_soft_version sv ON sv.id = t.SOFT_VERSION_ID
LEFT JOIN t_function_set fs on fs.id = sv.COLLECT_FUNCTION_ID
LEFT JOIN t_function_set fs2 on fs2.id = sv.LOCK_FUNCTION_ID
LEFT JOIN base_dic_item d ON d.ID = t.PROTOCOL_ID
@if(isNotEmpty(organizationId)){
    INNER JOIN v_vehicle v on v.TERMINAL_ID = t.id
    INNER JOIN ( SELECT id FROM base_organization 
                    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) ) temp 
        ON temp.id = v.ORGANIZATION_ID
@}
where 1=1
@if(isNotEmpty(keyword)){
    and (t.CODE like #keyword# or s.code like #keyword#)
@}
@if(isNotEmpty(protocolId)){
    and t.PROTOCOL_ID = #protocolId#
@}
@if(isNotEmpty(softVersionId)){
    and t.SOFT_VERSION_ID = #softVersionId#
@}
@if(isNotEmpty(terminalModel)){
    and t.TERMINAL_MODEL like #terminalModel#
@}

getGeneralDataByCode
===
SELECT
	v.vin,
	t.`CODE` terminalCode,
	s.`CODE` sim,
	di.ITEM_VALUE protocol,
	di.item_code protocolType
FROM
	v_vehicle v
INNER JOIN t_terminal t ON v.TERMINAL_ID = t.id
INNER JOIN t_simcard s ON s.id = t.SIMCARD_ID
INNER JOIN base_dic_item di ON di.id = t.PROTOCOL_ID
WHERE
	t. CODE = #terminalCode#

getGeneralDataByVin
===
SELECT
	v.vin,
	t.`CODE` terminalCode,
	s.`CODE` sim,
	di.ITEM_VALUE protocol,
	di.item_code protocolType
FROM
	v_vehicle v
INNER JOIN t_terminal t ON v.TERMINAL_ID = t.id
INNER JOIN t_simcard s ON s.id = t.SIMCARD_ID
INNER JOIN base_dic_item di ON di.id = t.PROTOCOL_ID
WHERE
	v. VIN = #vin#
	
getGeneralDataByKeyword
===
SELECT
	v.vin,
	t.`CODE` terminalCode,
	s.`CODE` sim,
	di.ITEM_VALUE protocol,
	di.item_code protocolType
FROM
	v_vehicle v
INNER JOIN t_terminal t ON v.TERMINAL_ID = t.id
INNER JOIN t_simcard s ON s.id = t.SIMCARD_ID
INNER JOIN base_dic_item di ON di.id = t.PROTOCOL_ID
WHERE
	v. VIN = #keyword# or t.code = #keyword# or s.code = #keyword#
	
getOfUpdate
===
SELECT
	v.vin,
	t.*,
	s.`CODE` simCode,
	v.ORGANIZATION_ID,
	v.VEHICLE_TYPE_ID,
	org.ORG_NAME
FROM
	t_terminal t
LEFT JOIN v_vehicle v ON v.TERMINAL_ID = t.id
LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
LEFT JOIN base_organization org on org.id = v.ORGANIZATION_ID
WHERE
	t.id = #id#