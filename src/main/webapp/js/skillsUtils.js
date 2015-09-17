/**
 * Created by Allan on 13/09/2015.
 */
var app = angular.module('skillsUtils', []);

function getSkillController($scope, $http) {

    $scope.getDataFromServer = function(username) {
        $http({
            method : 'POST',
            url : 'GetSkill?username='+ username
        }).success(function(data, status, headers, config) {
            $scope.skillData = data;
        }).error(function(data, status, headers, config) {

        });

    };
};