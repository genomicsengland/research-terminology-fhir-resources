import json

#
# Extracts ICD-10 chapter and block concepts from the input FHIR code system
#
# Input: ICD-10 FHIR code system (JSON) including chapters, blocks and categories, with the hierarchy represented by parent/child properties
# Output: FHIR code system "concept" element containing only chapter and block concepts from the input, with the hierarchy represented by nesting
#

input_file = './src/main/resources/who/icd-10/icd10end2016.json'
output_file = 'blocks.json'

# return the list of values for the specified concept property
def get_property(concept, code):
    return [property["valueCode"] for property in concept["property"] if property["code"] == code]

with open(input_file) as input:
    icd10 = json.load(input)
    # dict for looking up parent concepts by code
    index = {}
    # dict containing only chapters with nested blocks
    tree = {}
    for concept in icd10["concept"]:
        code = concept["code"]
        kind = next(iter(get_property(concept, 'kind')), None)
        parent_code = next(iter(get_property(concept, 'parent')), None)
        del concept["property"] # remove properties (we don't need them in the result)
        if kind == 'chapter':
            index[code] = concept
            tree[code] = concept
        elif kind == 'block':
            index[code] = concept
            parent = index[parent_code]
            parent.setdefault("concept", [])
            parent["concept"].append(concept)

# write the chapters and blocks hierarchy to JSON file
code_system = {}        
code_system["concept"] = list(tree.values())       
with open(output_file, 'w') as output:
    json.dump(code_system, output, indent=4)
