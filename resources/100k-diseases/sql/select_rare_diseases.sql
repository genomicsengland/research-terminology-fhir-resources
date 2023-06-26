select
'{',
'"code": "' || upper(translate(disease_group,' -(),','__')) || '","display": "' || disease_group || '",',
'"concept": [' || group_concat(disease_sub_group) || ']',
'},'
from 
(
select
disease_group,
'{"code": "' || upper(translate(disease_sub_group,' -(),','__')) || '","display": "' || disease_sub_group ||
'","concept": [' || group_concat(specific_disease) || ']}' as disease_sub_group,
from (
SELECT
disease_group,
disease_sub_group,
'{"code": "' || upper(translate(specific_disease,' -(),','__')) || '","display": "' || specific_disease || '"}' as specific_disease
from (
   select
   	coalesce(normalised_disease_group, disease_group) as disease_group,
    coalesce(normalised_disease_sub_group, disease_sub_group) as disease_sub_group,
    normalised_specific_disease as specific_disease
   from rare_diseases_participant_disease ) c
group by disease_group, disease_sub_group, specific_disease
order by specific_disease
) a
group by disease_group, disease_sub_group
order by disease_sub_group
) b
group by disease_group
order by disease_group;
