SELECT distinct
'{',
'"code": "' || cancer_disease_sub_type || '",',
'"display": "' || cancer_disease_sub_type || '",',
'"designation": [{ "value": "Cancer Subtype: ' || cancer_disease_sub_type || '" }]',
'},'
from cancer_participant_disease
order by 2;
