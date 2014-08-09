
Scenario: An administrator logs into ADM and logs out directly

Given an administrator
When the user logs in
Then the ADM dashboard should be visible
When the user logs out
Then the login page should be visible
