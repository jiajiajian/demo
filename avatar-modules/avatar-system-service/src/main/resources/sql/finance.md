pageQuery
===
select 
@pageTag() {
t.*, ref.org_name
@}
from base_finance t
left join (SELECT t.finance_id, GROUP_CONCAT(org.org_name) org_name
                              FROM base_finance_organization t 
                              INNER JOIN base_organization org ON org.id = t.org_id
                              GROUP BY t.finance_id) ref on ref.finance_id = t.id
@if(!isEmpty(organizationId)){
	INNER JOIN 
	( SELECT fo.FINANCE_ID FROM base_finance_organization fo
	 WHERE fo.ORG_ID = (SELECT ROOT_ORG_ID FROM base_organization WHERE ID = #organizationId# ) )
	 temp ON temp.FINANCE_ID = t.ID
@}
where 1=1
@if(!isEmpty(name)){
    and t.name like #name#
@}
@if(!isEmpty(financeId)){
    and t.id = #financeId#
@}

exportQuery
===
select 
t.*, ref.org_name
from base_finance t
left join (SELECT t.finance_id, GROUP_CONCAT(org.org_name, ',') org_name
                              FROM base_finance_organization t 
                              INNER JOIN base_organization org ON org.id = t.org_id
                              GROUP BY t.finance_id) ref on ref.finance_id = t.id
@if(!isEmpty(organizationId)){
inner join base_finance_organization ref2 on ref2.finance_id = t.id
inner join base_organization org ON org.id = ref2.org_id
@}
where 1=1
@if(!isEmpty(name)){
    and t.name like #name#
@}
@if(!isEmpty(organizationId)){
    and ref2.org_id = #organizationId#
@}

findOptionsByOrgId
===
SELECT
	bf.ID,
	bf.`NAME` 
FROM
	base_finance bf
@if(!isEmpty(organizationId)){
	INNER JOIN 
	( SELECT fo.FINANCE_ID FROM base_finance_organization fo
	 WHERE fo.ORG_ID = (SELECT ROOT_ORG_ID FROM base_organization WHERE ID = #organizationId# ) )
	 temp ON temp.FINANCE_ID = bf.ID
@}