Feature: Account

  Scenario: Update user data information
    Given I get the API access token through the login process
    When User update the data information
    Then User is data information is updated
