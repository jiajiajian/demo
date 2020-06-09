pageQuery
===
select
@pageTag(){
 fsl.*,
 bdi.ITEM_NAME typeName
@}
from t_function_set_item_lock fsl
left join base_dic_item bdi on bdi.id = fsl.DIC_ITEM_ID
where fsl.FUNCTION_ID = #functionId#
order by fsl.id desc

getLockOptions
===
SELECT
	fl.*,
	d.ITEM_CODE
FROM
	t_terminal t 
INNER JOIN t_soft_version sv ON sv.id = t.SOFT_VERSION_ID
INNER JOIN t_function_set_item_lock fl on fl.FUNCTION_ID = sv.LOCK_FUNCTION_ID
LEFT JOIN base_dic_item d on d.ID = fl.DIC_ITEM_ID
WHERE t.`CODE` = #terminalCode#