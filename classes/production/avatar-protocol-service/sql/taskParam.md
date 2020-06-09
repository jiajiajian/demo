deleteByTaskId
===
delete from FWP_TASK_PARAM where task_id = #taskId#

queryParamList
===
select 
    tp.var_code,
    hv.cn_name as  chineseName,
    hv.en_name as englishName
from fwp_task_param tp
inner JOIN base_heartbeat_variable hv on tp.var_code = hv.code 
where tp.task_id = #taskId#
	