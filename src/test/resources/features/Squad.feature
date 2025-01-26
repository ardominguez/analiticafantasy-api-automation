#Falta el test analysis
#Faltan los test schema
#Faltan los test 401

Feature: Squad
  """
    As a developer
    I want to test the Squad endpoints
    So that I can ensure they behave as expected
  """

  Scenario: Validate squad creation/deletion POST /fantasy-squad 200 OK
    Given I get the API access token through the login process
    When User creates a fantasy squad
    And Squad is created properly
    And User removes the created squad
    Then Squad was removed properly

  Scenario: Validate add player to squad POST /fantasy-squad/upsert 200 OK
    Given I get the API access token through the login process
    And I retrieve a fantasy squad "Add player to squad"
    When User adds new players to the fantasy squad "Add player to squad"
    Then Squad "Add player to squad" is updated without errors

  Scenario: Validate remove player from squad POST /fantasy-squad/upsert 200 OK
    Given I get the API access token through the login process
    And I retrieve a fantasy squad "Remove player from squad"
    When User adds new players to the fantasy squad "Remove player from squad"
    And Squad "Remove player from squad" is updated without errors
    And User removes players from the squad
    Then Squad "Remove player from squad" is updated without errors

  Scenario: Validate share squad POST /fantasy-squad/share 200 OK
    Given I get the API access token through the login process
    And I retrieve a fantasy squad "Share Squad"
    When I send a POST request to share a squad
    Then the squad link was generated successfully

  Scenario: Validate shared squad GET /fantasy-squad/shared 200 OK
    Given I get the API access token through the login process
    And I retrieve a fantasy squad "Share Squad"
    And I send a POST request to share a squad
    When I send a GET request to get the squad shared
    Then the squad is retrieved successfully
