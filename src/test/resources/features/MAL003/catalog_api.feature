@MAL003 @api
Feature: MAL003 - Product catalog and auth API

  Background:
    Given the API base URL is taken from the environment properties

  Scenario: Fetch a product
    When I send a GET request to "/products/1"
    Then the response status code is 200
    And the response field "id" equals "1"
    And the response field "title" is not empty

  Scenario: Search products
    When I send a GET request to "/products/search?q=phone"
    Then the response status code is 200
    And the response field "total" is greater than 0

  Scenario: Login with valid credentials
    When I send a POST request to "/auth/login" with body:
      """
      { "username": "emilys", "password": "emilyspass" }
      """
    Then the response status code is 200
    And the response field "username" equals "emilys"
