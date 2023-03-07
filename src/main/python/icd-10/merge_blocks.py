import json

#
# Merges ICD-10 chapter and block concepts into an ICD-10 FHIR code system containing only category concepts
# Categories are added to blocks based on the code ranges specified by block codes.
#
# Input 1: FHIR code system "concept" element including chapters and block concepts (nested) (e.g. output of extract_blocks.py)
# Input 2: ICD-10 FHIR code system (JSON) containing category concepts (nested)
# Output: FHIR code system containing chapters with nested block and category concepts (merged input 1 and 2)
#

input_file_blocks = './src/main/resources/who/icd-10/icd10end2016_chapters_blocks.json'
input_file_categories = './src/main/resources/who/icd-10/icd-10-uk-5.0.json'
output_file = './merged.json'

# return the list of concepts without children within the tree starting from the given concept
def get_leafs(concept):    
    if "concept" in concept:
        leafs = []
        for concept in concept["concept"]:
            leafs.extend(get_leafs(concept))
        return leafs
    else:
        return [concept]

with open(input_file_blocks) as input:
    blocks = json.load(input)

with open(input_file_categories) as input:
    icd10 = json.load(input)
    # dict for looking up categories by code
    index = {}
    for concept in icd10["concept"]:
        index[concept["code"]] = concept
    # lookup categories for each leaf block
    leafs = get_leafs(blocks)
    for leaf in leafs:
        block_code = leaf["code"]
        # parse block code, e.g. "X85-Y09"
        letter = ord(block_code[0])
        number = int(block_code[1:3])
        end_letter = ord(block_code[4])
        end_number = int(block_code[5:7])
        leaf["concept"] = []
        while (letter == end_letter and number <= end_number) or letter < end_letter:
            category_code = chr(letter) + "{:02d}".format(number)
            try:
                category = index.pop(category_code)
                leaf["concept"].append(category)
            except KeyError:
                # the block range included a non existing category (this is normal)
                print("warning: category " + category_code + " not found for block " + block_code)
                pass
            number += 1
            if number > 99:
                letter += 1
                number = 0

# print any categories not included in a block (indicates a bug or error in the input)     
for category_code in index.keys():
    print("error: category " + category_code + " was not included in any block")

# replace concepts in the icd10 code system with the merged concepts
icd10["concept"] = blocks["concept"]

with open(output_file, 'w') as output:
    json.dump(icd10, output, indent=4)
    
