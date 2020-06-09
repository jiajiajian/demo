pageQuery
===
SELECT
@pageTag(){
	fs.*
@}
FROM
	t_function_set fs
WHERE
	1 = 1
@if(isNotEmpty(keyword)){
    AND (fs.`CODE` LIKE #keyword# OR fs.`NAME` LIKE #keyword#)
@}
@if(isNotEmpty(functionType)){
    AND fs.FUNCTION_TYPE = #functionType#
@}
order by fs.id desc
