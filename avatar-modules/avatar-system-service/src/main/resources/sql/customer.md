pageQuery
===
select 
@pageTag() {
t.*, org.org_name
@}
from base_customer t
inner join base_organization org ON org.id = t.ORGANIZATION_ID
where 1=1
@if(!isEmpty(q)){
    and (t.name like #q# or t.PHONE_NUMBER like #q#)
@}
@if(!isEmpty(organizationId)){
    and t.ORGANIZATION_ID = #organizationId#
@}

exportQuery
===
select 
t.*, org.org_name
from base_customer t
inner join base_organization org ON org.id = t.ORGANIZATION_ID
where 1=1
@if(!isEmpty(q)){
    and (t.name like #q# or t.PHONE_NUMBER like #q#)
@}
@if(!isEmpty(organizationId)){
    and t.ORGANIZATION_ID = #organizationId#
@}
order by t.id desc