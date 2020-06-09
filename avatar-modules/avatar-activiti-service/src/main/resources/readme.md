访问设计器：
1.http://localhost:8108/model/create   ====>返回 modelId
2.http://localhost:8108/modeler.html?modelId=modelId


activiti用户和组和系统管理整合数据库脚本
===
DROP VIEW  IF EXISTS act_id_membership;
DROP VIEW  IF EXISTS act_id_user;
DROP VIEW  IF EXISTS act_id_group;

DROP table  IF EXISTS act_id_membership;
DROP table  IF EXISTS act_id_user;
DROP table  IF EXISTS act_id_group;

CREATE VIEW act_id_user AS SELECT
	CONCAT(bu.id, '') AS ID_,
	NULL AS REV_,
	bu.login_name AS FIRST_,
	CONCAT(bu.del_flag, '') AS LAST_,
	bu.EMAIL_ADDRESS AS EMAIL_,
	bu.LOGIN_PASSWORD AS PWD_,
	NULL AS PICTURE_ID_
FROM
	base_user bu;

CREATE VIEW act_id_group AS SELECT
	CONCAT(br.id, '') AS ID_,
	NULL AS REV_,
	br.ROLE_NAME AS NAME_,
	NULL AS TYPE_
FROM
	base_role br;
	
CREATE VIEW act_id_membership AS SELECT
	(
		SELECT
			CONCAT(u.id, '')
		FROM
			base_user u
		WHERE
			u.id = ur.user_id
	) AS USER_ID_,
	(
		SELECT
			CONCAT(r.id, '')
		FROM
			base_role r
		WHERE
			r.id = ur.role_id
	) AS GROUP_ID_
FROM
	base_role_user ur