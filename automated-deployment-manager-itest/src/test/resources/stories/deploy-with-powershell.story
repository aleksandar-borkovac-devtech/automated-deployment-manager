
Scenario: A developer logs into ADM and adds a Maven module and deploys it to a Windows machine

Given a developer
When the user logs in
Then the ADM dashboard should be visible
When the user adds a Maven module called Power Shell Script Module with group name nl.tranquilizedquality.adm and artifact id power-shell-script-module of type ZIP
Then there should be 1 module(s) created
When the user creates a release called S13.02
And adds an artifact called Power Shell Script Module with version 1.0.0 to the release S13.02
And the user deploys the release S13.02 to DEV
Then the deployment status of the release S13.02 should be FAILED
