sample
===
* 注释

	select #use("cols")# from r_lock_apply  where  #use("condition")#

cols
===
	id,apply_code,reason,state,instance_id,create_time,apply_user,org_id

updateSample
===
	
	id=#id#,apply_code=#applyCode#,reason=#reason#,state=#state#,instance_id=#instanceId#,create_time=#createTime#,apply_user=#applyUser#,org_id=#orgId#

condition
===

	1 = 1  
	@if(!isEmpty(id)){
	 and id=#id#
	@}
	@if(!isEmpty(applyCode)){
	 and apply_code=#applyCode#
	@}
	@if(!isEmpty(reason)){
	 and reason=#reason#
	@}
	@if(!isEmpty(state)){
	 and state=#state#
	@}
	@if(!isEmpty(instanceId)){
	 and instance_id=#instanceId#
	@}
	@if(!isEmpty(createTime)){
	 and create_time=#createTime#
	@}
	@if(!isEmpty(applyUser)){
	 and apply_user=#applyUser#
	@}
	@if(!isEmpty(orgId)){
	 and org_id=#orgId#
	@}
	
pageQuery
===
SELECT
@pageTag() {
	ra.*
@}
FROM
	r_lock_apply ra
@if(isNotEmpty(orgId)){
    INNER JOIN ( SELECT id FROM base_organization 
                    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #orgId# ), '%' ) ) temp 
        ON temp.id = ra.org_id
@}
WHERE 1=1
@if(!isEmpty(state)){
      AND ra.state = #state#
@}
@if(!isEmpty(applyUser)){
      AND ra.apply_user = #applyUser#
@}
@if(!isEmpty(applyCode)){
   AND ra.apply_code  like #applyCode#
@}
@if(!isEmpty(beginTime)){
 AND ra.create_time >= #beginTime#
@}
@if(!isEmpty(endTime)){
 AND ra.create_time <= #endTime#
@}
ORDER BY
	id DESC
	
updateInstanceId
===
update r_lock_apply set instance_id = #instanceId# where apply_code=#applyCode#


getApplyByCode
===
SELECT
    ra.*
FROM
	r_lock_apply ra
where ra.apply_code=#applyCode#


updateStateByCode
===
update r_lock_apply set state = #state# where apply_code=#applyCode#


updateStateByInstanceId
===
update r_lock_apply set state = #state# where instance_id=#instanceId#