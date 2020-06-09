pageQuery
===
SELECT
@pageTag() {
  o.ORG_NAME,
  c.*  
@}
FROM
v_charge c
LEFT JOIN base_organization o ON o.ID = c.ORGANIZATION_ID
where 1 = 1
@if(!isEmpty(organizationId)){
    and c.ORGANIZATION_ID = #organizationId#
  @}
  
pageQueryPrePaid
=== 
SELECT
@pageTag() {
	temp.*,
	t.TERMINAL_MODEL,
	s.`CODE` sim_card,
    o1.ROOT_ORG_ID,
CASE
	WHEN - 1 = o1.PARENT_ORG_ID THEN
	o1.ORG_NAME ELSE o2.ORG_NAME 
	END AS agent,
	o3.ORG_NAME AS root_org 
@}
FROM
	(
	SELECT
		v.VIN,
		v.SERVICE_STATUS,
		v.SERVICE_PERIOD,
		v.SERVICE_START_DATE,
		v.SERVICE_END_DATE,
		v.ORGANIZATION_ID,
		v.TERMINAL_ID,
		1 AS flag 
	FROM
		v_vehicle v 
	WHERE
	    v.SERVICE_STATUS = 1
		AND v.REGIST_DATE >= #beginDate# 
		AND v.REGIST_DATE <= #endDate# UNION ALL
	SELECT
		bl.VIN,
		bl.RENEWAL_MON AS SERVICE_PERIOD,
		v1.SERVICE_STATUS,
		v1.SERVICE_START_DATE,
		v1.SERVICE_END_DATE,
		v1.ORGANIZATION_ID,
		v1.TERMINAL_ID,
		2 AS flag 
	FROM
		(
		SELECT
			MAX( ID ) AS id,
			VIN 
		FROM
			v_business_log b 
		WHERE
			b.OPERATE_TYPE = 4 
			AND b.CREATE_TIME >= #beginTime# 
			AND b.CREATE_TIME <= #endTime#  
		GROUP BY
			VIN 
		) a
		INNER JOIN v_business_log bl ON bl.ID = a.ID
		INNER JOIN v_vehicle v1 ON v1.VIN = a.VIN 
	) temp
	@if(!isEmpty(organizationId)){
    	    INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) m
    	    ON temp.ORGANIZATION_ID = m.id
        @}
	LEFT JOIN t_terminal t ON t.id = temp.TERMINAL_ID
	LEFT JOIN t_simcard s ON s.id = t.SIMCARD_ID
	LEFT JOIN base_organization o1 ON o1.ID = temp.ORGANIZATION_ID
	LEFT JOIN base_organization o2 ON o2.ID = o1.PARENT_ORG_ID
    LEFT JOIN base_organization o3 ON o3.ID = o1.ROOT_ORG_ID
    
pageQueryAfterPaid
===
SELECT
@pageTag() {
	v.vin,
    s.code sim_card,
	v.REGIST_DATE,
	v.SERVICE_START_DATE,
	v.SERVICE_END_DATE,
	t.TERMINAL_MODEL,
	o.ROOT_ORG_ID,
	o1.ORG_NAME root_org_name
@}
FROM
	v_vehicle v
	@if(!isEmpty(organizationId)){
    	    INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) m
    	    ON v.ORGANIZATION_ID = m.id
        @}
	INNER JOIN t_terminal t ON t.id = v.TERMINAL_ID 
    INNER JOIN t_simcard s ON s.id = t.SIMCARD_ID
    INNER JOIN base_organization o ON o.ID = v.ORGANIZATION_ID
    INNER JOIN base_organization o1 ON o1.id = o.ROOT_ORG_ID
WHERE
	v.REGIST_DATE IS NOT NULL
    ORDER BY v.REGIST_DATE asc
    
pageQueryServiceWarn
===
SELECT
@pageTag() {
	v.vin,
	v.SERVICE_END_DATE,
	t.`CODE` terminal_code,
	s.`CODE` sim_card,
	o.ORG_NAME 
@}
FROM
	v_vehicle v
	@if(!isEmpty(organizationId)){
    	    INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) m
    	    ON v.ORGANIZATION_ID = m.id
        @}
	INNER JOIN t_terminal t ON t.id = v.TERMINAL_ID
	INNER JOIN t_simcard s on s.id = t.SIMCARD_ID
	INNER JOIN base_organization o ON o.ID = v.ORGANIZATION_ID 
WHERE
	v.SERVICE_END_DATE >= #beginDate#
	AND v.SERVICE_END_DATE <= #endDate# 
	
warnCount
===
SELECT
	count(0) 
FROM
	v_vehicle v
	@if(!isEmpty(organizationId)){
    	    INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) m
    	    ON v.ORGANIZATION_ID = m.id
        @}
WHERE
	v.SERVICE_END_DATE >= #beginDate#
	AND v.SERVICE_END_DATE <= #endDate#