updateMsgRead
===
UPDATE v_user_alarm_info 
SET read_flag = 1
WHERE user_id = #userId# and read_flag = 0

pageQueryMsg
===
SELECT
@pageTag() {
    h.id,
    m.user_id,
    h.VIN,
    h.LAT,
    h.LON,
    h.ALARM_STATE,
    h.SPN_FMI,
    h.TLA,
    h.ADDRESS,
    h.BEGIN_TIME,
    h.END_TIME,
    h.ALARM_TYPE,
    d.ITEM_NAME alarm_name
@}
FROM
    v_user_alarm_info m
INNER JOIN v_alarm_history h ON h.ID = m.ALARM_ID
LEFT JOIN base_dic_item d ON d.ITEM_CODE = h.ALARM_CODE
LEFT JOIN v_vehicle v ON v.vin = h.vin
WHERE  m.user_id = #userId#
ORDER BY m.CREATE_TIME DESC