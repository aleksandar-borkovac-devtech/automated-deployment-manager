
Scenario: A developer logs into ADM and adds a shell script based destination

Given a developer with host management rights
When the user logs in
Then the ADM dashboard should be visible
When the user adds a host with the name iwp-windps on port 22 using SSHPS protocol logging in with svc-admdev and a private key MIIEoQIBAAKCAQEAtqrXPdnRPF+a2osAWzk1fyjhEhvWgAn3areLIA8ZC+HY49bf
And the user adds a destination called Shell Scipt Destination
And adds a parameter with value test1
And adds a parameter with value test2
Then there should be 2 parameters configured for the destination
