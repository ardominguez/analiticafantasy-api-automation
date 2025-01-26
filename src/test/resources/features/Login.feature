Feature: Login

  Scenario: Validate user valid login GET 200 OK
    Given I do login api authentication with credentials "valid"
    Then User is properly logged

  Scenario: Validate invalid login GET 401 unauthorized
    Given I do login api authentication with credentials "invalid"
    Then I get a login error message
