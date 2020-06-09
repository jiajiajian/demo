terminalTests
===
SELECT
	tt.terminal_code,
	tt.`code` itemKey,
	c.cmd_id,
	c.state,
	c.res_json_body content,
	c.operate_time testTime
FROM
	t_terminal_test tt
LEFT JOIN t_command c ON c.id = tt.command_id
where terminal_code = #terminalCode#