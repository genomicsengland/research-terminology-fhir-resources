SELECT distinct
'{',
'"code": "' || cancer_disease_type || '",',
'"display": "' || initcap(replace(cancer_disease_Type,'_',' ')) || '",',
'"designation": [{ "value": "Cancer Subtype: ' || initcap(replace(cancer_disease_Type,'_',' ')) || '" }]',
'},'
from cancer_participant_disease
order by 2;
