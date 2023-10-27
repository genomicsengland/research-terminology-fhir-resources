--two step process ,
-- 1. use this script to generate a drug_list_from_db.txt file from db
-- 2. run python script 'drug_codesystem_from_list.py' to generate nice Title case drug names in json (initcap() function not smart enough)
with drug_groups as (
select distinct (unnest((medication_codeable_concept).coding)).code as drug_names
from medication_administration ma
)
select distinct(regexp_replace(drug_names, E'[\\x00-\\x1F\\x7F]|\\ufffd', ' ', 'g')) as drug_name
from drug_groups
order by drug_name;

-- This code give a unique list of drugs but the initcap() fn isn't smart enough for our needs
--with drug_groups as (
--select distinct (unnest((medication_codeable_concept).coding)).code as drug_names
--from medication_administration ma -- e.g. where ((medication_codeable_concept).coding[1]).code like 'TRIFLURIDINE%'
--)
--select distinct(initcap(regexp_replace(drug_names, E'[\\x00-\\x1F\\x7F]|\\ufffd', ' ', 'g'))) as drug_name
--from drug_groups
--order by drug_name;