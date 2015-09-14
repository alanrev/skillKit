/**
 * Created by Allan on 13/09/2015.
 */
var app = angular.module('skillsUtils', []);

function addSkillController($scope, $http) {

    $scope.getDataFromServer = function(username, rate) {
        $http({
            method : 'GET',
            url : 'skillsHandler?username='+ username + "&rate=" + rate
        }).success(function(data, status, headers, config) {
            $scope.person = data;
        }).error(function(data, status, headers, config) {

        });

    };
};