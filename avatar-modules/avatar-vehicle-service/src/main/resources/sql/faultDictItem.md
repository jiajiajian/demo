pageQuery
===
SELECT
@pageTag() {
    i.id,
	o.ORG_NAME,
	v.TLA,
	CONCAT( i.SPN, '.', i.FMI ) AS spn_fmi,
	i.SPN_NAME,
	i.FMI_NAME 
@}
FROM
	v_fault_dict_item i
    @if(!isEmpty(organizationId)){
	    INNER JOIN 
    	    ( SELECT 
    	    id FROM base_organization 
    	    WHERE path LIKE CONCAT( ( SELECT path FROM base_organization WHERE id = #organizationId# ), '%' ) 
    	    ) temp
    	    ON i.ORGANIZATION_ID = temp.id
    @}
	LEFT JOIN v_tla v ON v.id = i.TLA_ID
	LEFT JOIN base_organization o ON o.ID = i.ORGANIZATION_ID
	where 1=1
    @if(!isEmpty(organizationId)){
        and i.ORGANIZATION_ID = #organizationId#
    @}
    @if(!isEmpty(tlaId)){
        and i.tla_id = #tlaId#
    @}
    @if(!isEmpty(spnFmi)){
        and CONCAT( i.SPN, '.', i.FMI ) like #spnFmi#
    @}
    @if(!isEmpty(spnName)){
        and i.SPN_NAME like #spnName#
    @}
    @if(!isEmpty(fmiName)){
        and i.FMI_NAME like #fmiName#
    @}
    

spnFmiTlaQueryByRootOrg
===
SELECT
	d.SPN,
	d.FMI,
	t.TLA 
FROM
	v_fault_dict_item d
	LEFT JOIN v_tla t ON d.TLA_ID = t.id
	WHERE d.ORGANIZATION_ID = #rootOrgId#	
	
tlaList
===
SELECT
TLA
FROM
v_tla
WHERE
ORGANIZATION_ID = #rootOrgId#

tlaId
===
SELECT
id
FROM
v_tla
WHERE
ORGANIZATION_ID = #rootOrgId#
and tla = #tla#