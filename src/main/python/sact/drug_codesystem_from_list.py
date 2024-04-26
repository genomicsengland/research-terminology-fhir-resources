#!python
#
# usage: python ./drug_codesystem_from_list.py
#
import json
from titlecase import titlecase

def abbreviations(word, all_caps):
    acronyms_to_leave_as_is = ['ADCT-301', 'ATG', 'AZD1775', 'CAR-T', 'PDR001']
    return word if word.upper() in acronyms_to_leave_as_is else titlecase(word)

input_file = './drug_list_from_db.txt'
output_file = './sact-drug-group.json'

drugs = []
with open(input_file) as input:
    for line in input:
        drugs.append(line.strip())

concept_structure = {
    "concept": [{"code": drug, "display": titlecase(drug, callback=abbreviations)} for drug in drugs]
}

with open(output_file, 'w') as output:
    json.dump(concept_structure, output, indent=4)
