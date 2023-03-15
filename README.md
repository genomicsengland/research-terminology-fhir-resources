# FHIR Terminology Resources (Research Environment)

Genomics England uses a FHIR terminology server to provide reference data for use with the clinical data sets available in the research environment.
This includes classifications such as ICD-10 and OPCS-4, as well as more complex vocabularies such as SNOMED CT and HPO. The Participant Explorer is an important consumer of this data, but researchers can also access it directly via the FHIR API.

The terminology server can be used to search for codes, lookup code descriptions, find equivalent codes in different code systems, and access the subsumption hierarchy (retrieve descendant or ancestor concepts). See https://build.fhir.org/terminology-module.html for the FHIR terminology specification.

The terminology server instance is provided as a managed service by CSIRO, accessed via https://ontoserver.aws.gel.ac. 
(Paper: https://jbiomedsem.biomedcentral.com/articles/10.1186/s13326-018-0191-z)

Our server is linked up to the NHSD national terminology server via a "content syndication" setup, through which the majority of content is synchronised.
(See https://digital.nhs.uk/services/terminology-servers)

Additional content may be uploaded to the server directly via the FHIR API, including:
* GEL-specific content (such as 100K recruited diseases)
* Content not currently available in the national terminology server (such as ICD-O-3, SNOMED concept maps, ICD-10 chapters and blocks)

Any source files, generated FHIR resources and related scripts/code for this additional content can be found in this repo.

See the individual /resources/\<name\>/README for further details on specific resources.
