pageQuery
===
SELECT
@pageTag(){
	co.*, 
	di.item_name,
	u.LOGIN_NAME username
@}
FROM
	t_command co
LEFT JOIN base_user u on u.ID = co.user_id
LEFT JOIN base_dic_item di ON di.dic_code = 'CMD_ID'
	AND di.item_code = concat(conv(LEFT(co.cmd_id, 4), 10, 16), '')
WHERE co.vin = #vin#
@if(isNotEmpty(beginTime)){
    AND co.operate_time >= #beginTime#
@}
@if(isNotEmpty(endTime)){
    AND co.operate_time <= #endTime#
@}
order by co.id desc

getMaxSerialNoByDay
===
SELECT max(serial_no) FROM `t_command` where date = #day#