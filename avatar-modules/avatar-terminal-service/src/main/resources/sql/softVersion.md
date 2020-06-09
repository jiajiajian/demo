pageQuery
===
select
@pageTag(){
    sv.*, 
 	fs.code collectName,
 	fs2.code lockName
 @}
from t_soft_version sv
LEFT JOIN t_function_set fs on fs.id = sv.COLLECT_FUNCTION_ID
LEFT JOIN t_function_set fs2 on fs2.id = sv.LOCK_FUNCTION_ID
WHERE 1=1
@if(!isEmpty(keywords)){
    AND (sv.`CODE` like #keywords#  or sv.`NAME` like #keywords#) 
@}
order by sv.id desc