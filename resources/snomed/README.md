# SNOMED CT UK Edition Concept Maps

## Code System

The SNOMED CT UK Edition code system is synchronised from the NHS terminology server.

## SNOMED to ICD-10 and OPCS-4 Concept Maps

The SNOMED CT UK distribution includes "Complex Map" reference sets mapping SNOMED codes to ICD-10 and OPCS-4 codes.

For details on the reference set format see:
https://confluence.ihtsdotools.org/display/DOCRELFMT/5.2.3.3+Complex+and+Extended+Map+from+SNOMED+CT+Reference+Sets

The latest distribution can be downloaded from: https://isd.digital.nhs.uk/trud/users/authenticated/filters/0/categories/26/items/101/releases
(this requires a TRUD login)

Within the distribution, the TSV file containing the mappings is (suffix depending on the release): 
SnomedCT_UKClinicalRF2_PRODUCTION_20230215T000001Z/Snapshot/Refset/Map/der2_iisssciRefset_ExtendedMapUKCLSnapshot_GB1000000_20230215.txt

The refSetId column indicates the reference set each row belongs to:
*  999002271000000101: SNOMED to ICD-10 5th edition (five character)
*  999002741000000101: SNOMED to OPCS 4.8 (retired)
*  1126441000000105: SNOMED to OPCS 4.9
*  1382401000000109: SNOMED to OPCS 4.10

Class uk.co.genomicsengland.re.fhir.tools.snomed.SCTUKMap2FHIR converts the reference set for a given ID to the FHIR Concept Map format.
Notes:
* Only mappings with a single "mapGroup" are included. Mappings with multiple groups are one-to-many mappings, i.e. requiring more than one target code for a single source code, which cannot be expressed in the FHIR format.
* Special mapTarget values such #NC (not classifiable), #NIS (not in scope) are excluded from the result.
* Any non-digit characters are removed from the target code, except for the first position.
* Optionally, a dot is inserted after the 3rd character in target codes longer than 3 characters.
 
Input parameters:
1. refset input file name (tsv)
2. refset ID (see above)
3. output file name (json)
4. dot-notation (true/false)

## SNOMED to HPO Concept Map

The SNOMED to HPO concept map was created by CSIRO, the Australian e-Health Research Centre, and provided as a FHIR concept map in https://genomics.ontoserver.csiro.au. The map was last updated in 2018 and has not been published externally, as far as we know.
