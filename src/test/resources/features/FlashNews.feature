#Falta el test analysis
#Faltan los test schema
#Faltan los test 401

Feature: Squad
"""
    As a developer
    I want to test the flash new endpoint
    So that I can ensure they behave as expected
  """

  Scenario: Validate squad creation/deletion POST /fantasy-squad 200 OK
    Given I get the API access token through the login process
    When I send a GET request to get the news from feed