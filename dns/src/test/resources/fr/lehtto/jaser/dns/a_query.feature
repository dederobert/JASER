Feature: A query
    In order to determine IP address
    As a user
    I want to be able to query the IP address of a hostname
    So that I can determine the IP address of a hostname

  Scenario: Query a hostname
    Given I have "www.example.com" as hostname
    When I query the IP address of the hostname
    Then I should see "192.168.1.1" as IP address of the hostname