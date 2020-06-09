pageQuery
===
SELECT
@pageTag(){
	sim.*,
	DATE_FORMAT(v.SERVICE_START_DATE,'%Y-%m-%d') AS serviceStartDateFormat,
	DATE_FORMAT(v.SERVICE_END_DATE,'%Y-%m-%d') AS serviceEndDateFormat,
	di.ITEM_NAME card_way
@}
FROM
	`t_simcard` sim
LEFT JOIN base_dic_item di ON di.id = sim.CARD_WAY_ID
LEFT JOIN t_terminal t ON t.SIMCARD_ID = sim.id
LEFT JOIN v_vehicle v ON v.TERMINAL_ID = t.id
where 1=1
@if(isNotEmpty(code)){
    and sim.code like #code#
@}
@if(isNotEmpty(cardOwner)){
    and sim.CARD_OWNER like #cardOwner#
@}
@if(isNotEmpty(orderNo)){
    and sim.ORDER_NO like #orderNo#
@}
@if(isNotEmpty(status)){
    and sim.STATUS = #status#
@}
@if(isNotEmpty(cardWayId)){
    and sim.CARD_WAY_ID = #cardWayId#
@}
@if(isNotEmpty(department)){
    and sim.DEPARTMENT like #department#
@}
@if(isNotEmpty(operator)){
    and sim.OPERATOR = #operator#
@}
@if(isNotEmpty(cardType)){
    and length(sim.code) = #cardType#
@}
order by sim.id desc