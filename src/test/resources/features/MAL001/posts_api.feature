@MAL001 @api
Feature: MAL001 - Posts service API

  Background:
    Given the API base URL is taken from the environment properties

  Scenario: Fetch an existing post
    When I send a GET request to "/posts/1"
    Then the response status code is 200
    And the response field "id" equals "1"
    And the response field "title" is not empty

  Scenario: List posts
    When I send a GET request to "/posts"
    Then the response status code is 200
    And the response list "$" has more than 10 items

  Scenario: Create a post
    When I send a POST request to "/posts" with body:
      """
      { "title": "regression", "body": "nightly", "userId": 1 }
      """
    Then the response status code is 201
    And the response field "title" equals "regression"
    And the response field "id" is not empty

  Scenario Outline: Fetch users by id
    When I send a GET request to "/users/<id>"
    Then the response status code is 200
    And the response field "id" equals "<id>"

    Examples:
      | id |
      | 1  |
      | 2  |
      | 3  |
