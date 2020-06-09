pageQuery
===
select
@pageTag() {
    t.*, org.org_name, f.name finance_name
@} 
from base_role t
    left JOIN base_organization org ON org.id = t.ORGANIZATION_ID
    left JOIN base_finance f  ON f.id = t.finance_id
where 1=1
@if(!isEmpty(organizationId)){
    and t.ORGANIZATION_ID = #organizationId#
@}
@if(!isEmpty(financeId)){
    and t.finance_id = #financeId#
@}
@if(!isEmpty(roleName)){
    and t.ROLE_NAME like #roleName#
@}
@if(!isEmpty(roleType)){
    and t.ROLE_TYPE = #roleType#
@}

getParentOrgId
===
SELECT ROOT_ORG_ID
FROM base_organization
WHERE id = #orgId#



