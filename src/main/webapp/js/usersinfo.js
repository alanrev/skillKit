/**
 * Created by Allan on 12/09/2015.
 */
var app = angular.module('userInfo', []);

function userInfoController($scope, $http) {

    $scope.getDataFromServer = function(username) {
        $http({
            method : 'GET',
            url : 'usersInfo?username='+ username
        }).success(function(data, status, headers, config) {
            $scope.person = data;
        }).error(function(data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });

    };
};

function getSkillController($scope, $http) {

    $scope.getDataFromServer = function (username) {
        $http({
            method: 'POST',
            url: 'GetSkill?username=' + username
        }).success(function (data, status, headers, config) {
            $scope.skillData = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

function getNewRoleSkillController($scope, $http) {

    $scope.getDataFromServer = function (username) {
        $http({
            method: 'POST',
            url: 'GetUserRoleSkills?username=' + username
        }).success(function (data, status, headers, config) {
            $scope.skillData = data;
        }).error(function (data, status, headers, config) {

        });

    };
}