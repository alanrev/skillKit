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
            if (data.role == "Developer"){
                var createP = document.getElementById('pm');
                if (createP != null){
                    createP.style.display = 'none';
                }
            }
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

function getDevSkillsController($scope, $http) {

    $scope.getDataFromServer = function (username) {
        $http({
            method: 'POST',
            url: 'getDevSkills?username=' + username
        }).success(function (data, status, headers, config) {
            $scope.skills = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

function getProjectsController($scope, $http) {

    $scope.getDataFromServer = function (username) {
        $http({
            method: 'POST',
            url: 'GetProjects?username=' + username
        }).success(function (data, status, headers, config) {
            $scope.projectData = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

function getFreeUsersController($scope, $http) {

    $scope.getDataFromServer = function (username) {
        $http({
            method: 'POST',
            url: 'GetFreeUsers?username=' + username
        }).success(function (data, status, headers, config) {
            $scope.users = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

function getProjectInfoController($scope, $http) {

    $scope.getDataFromServer = function (username, project) {
        $http({
            method: 'POST',
            url: 'projectInfo?username=' + username + '&project=' + project
        }).success(function (data, status, headers, config) {
            for (var i = 0; i < data.team.length; i++){
                var memberRole = data.team[i].role;
                if (memberRole == "1"){
                    data.team[i].role = "Project Manager";
                }
                if (memberRole == "2"){
                    data.team[i].role = "Developer";
                }
            }
            $scope.project = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

function GetProjectTasksController($scope, $http) {

    $scope.getDataFromServer = function (username, project) {
        $http({
            method: 'POST',
            url: 'GetProjectTasks?username=' + username + '&project=' + project
        }).success(function (data, status, headers, config) {
            $scope.tasks = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

function GetTaskInfoController($scope, $http) {

    $scope.getDataFromServer = function (username, project, id) {
        $http({
            method: 'POST',
            url: 'GetTaskInfo?username=' + username + '&project=' + project + '&id=' + id
        }).success(function (data, status, headers, config) {
            $scope.task = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

function GetRecommendedUsersController($scope, $http) {
    $scope.getDataFromServer = function (username, project, id) {
        $http({
            method: 'POST',
            url: 'GetRecommendedUsers?username=' + username + '&project=' + project + '&id=' + id
        }).success(function (data, status, headers, config) {
            $scope.users = data;
        }).error(function (data, status, headers, config) {

        });

    };
}
