@MAL002 @ui
Feature: MAL002 - Customer portal login (UI)

  Scenario: Valid user can log in
    Given I open the application from the environment properties
    When I log in with username "standard_user" and password "secret_sauce"
    Then I land on the products page

  Scenario: Locked out user is rejected
    Given I open the application from the environment properties
    When I log in with username "locked_out_user" and password "secret_sauce"
    Then I see the login error "Epic sadface: Sorry, this user has been locked out."

  Scenario: Wrong password is rejected
    Given I open the application from the environment properties
    When I log in with username "standard_user" and password "wrong"
    Then I see the login error "Epic sadface: Username and password do not match any user in this service"
