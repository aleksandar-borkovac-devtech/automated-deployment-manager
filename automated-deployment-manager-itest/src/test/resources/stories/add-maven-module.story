
Scenario: A developer logs into ADM and adds a Maven module with a dependency to another one 

Given a developer
When the user logs in
Then the ADM dashboard should be visible
When the user adds a Maven module called Shell Script Module with group name nl.tranquilizedquality.adm and artifact id shell-script-module of type TAR_GZIP
Then there should be 1 module(s) created
When the user adds a Maven module called Shell Script Module DB with group name nl.tranquilizedquality.adm and artifact id shell-script-module-db of type TAR_GZIP
Then there should be 2 module(s) created
When the user adds a dependency Shell Script Module DB to the Maven module called Shell Script Module
Then there should be 1 dependency created for the Maven module called Shell Script Module
When the user creates a release called S13.02
And adds an artifact called Shell Script Module DB with version 1.0.0 to the release S13.02
!--And adds an artifact called Shell Script Module with version 1.0.0 to the release S13.02
And the user deploys the release S13.02 to DEV
Then the deployment status of the release S13.02 should be FAILED
