
Scenario: A developer logs into ADM and adds a Maven module and deploys it to a Windows machine

Given a developer with host management rights
When the user logs in
Then the ADM dashboard should be visible
When the user adds a host with the name iwp-windps on port 22 using SSHPS protocol logging in with svc-admdev and a private key MIIEoQIBAAKCAQEAtqrXPdnRPF+a2osAWzk1fyjhEhvWgAn3areLIA8ZC+HY49bf
Then there should be a host created with the name iwp-windps with the specified private key MIIEoQIBAAKCAQEAtqrXPdnRPF+a2osAWzk1fyjhEhvWgAn3areLIA8ZC+HY49bf