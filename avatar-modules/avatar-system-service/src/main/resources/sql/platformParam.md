pageQuery
===
SELECT
@pageTag() {
bt.ID tenant_id,
org.ORG_NAME tenant_name,
pp.ID,
pp.ACCOUNT,
pp.`PASSWORD`
@}
FROM
base_tenant bt
LEFT JOIN base_platform_param pp ON pp.TENANT_ID = bt.ID
INNER JOIN base_organization org ON org.ID = bt.ROOT_ORGANIZATION_ID
WHERE bt.ENABLE_FLAG = 0
AND bt.DEL_FLAG = 0
@if(!isEmpty(tenantName)){
AND org.ORG_NAME like #tenantName#
@}