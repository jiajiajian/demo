findAll
===
select t.*, i.item_name org_type_name
from base_organization t
left join base_dic_item i on i.id = t.org_type_id

getOrgChild
===
select t.*, i.item_name org_type_name
from base_organization t
left join base_dic_item i on i.id = t.org_type_id
where path like CONCAT((SELECT path FROM base_organization WHERE id = #orgId#), '%')

getChild
===
select * 
 from base_organization 
where path like CONCAT((SELECT path FROM base_organization WHERE id = #orgId#), '%')

getIdByOrgId
===
select id
 from base_organization 
 where path like CONCAT((SELECT path FROM base_organization WHERE id = #orgId#), '%')