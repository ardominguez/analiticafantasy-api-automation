#Falta el test analysis
#Faltan los test schema
#Faltan los test 401

Feature: TierList
"""
    As a developer
    I want to test the TierList endpoints
    So that I can ensure they behave as expected
  """

  Scenario: Validate TierList creation/deletion POST /tierlist 200 OK
    Given I get the API access token through the login process
    When I send a POST request to create a TierList
    Then TierList is created properly
    And I send a GET request to get the tierList info
    And I send a REMOVE request to eliminate the tierList

  Scenario: Validate visualize TierList GET /tierlist/slug 200 OK
    Given I get the API access token through the login process
    When I send a POST request to create a TierList
    And TierList is created properly
    And I send a GET request to get the tierList info
    Then I send a GET request to visualize the tierList
    And I delete the tierList