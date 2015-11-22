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

function userController($scope, $http) {

    $scope.getDataFromServer = function(username) {
        $http({
            method : 'GET',
            url : 'GetUserData?user='+ username
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

function getProfileSkillsController($scope, $http) {

    $scope.getDataFromServer = function (username) {
        $http({
            method: 'POST',
            url: 'GetUserSkill?username=' + username
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
            if (data != ""){
                $scope.skillsSize = data.length;
            }else{
                var hideHtml = document.getElementById("newSkills");
                hideHtml.style.display = "none";
            }
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
        $http({
            method: 'POST',
            url: 'ProjectTasksStatus?username=' + username + '&project=' + project
        }).success(function (data, status, headers, config) {
            var chart = new CanvasJS.Chart("tasksStatus", data);
            chart.render();
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
            if ((data.evaluated != null)||(data.status != "Close")){
                var evaluationForm = document.getElementById("evaluateForm");
                evaluationForm.style.visibility="hidden";
                if (data.status == "Close"){
                    var updateStatus = document.getElementById("updateButton");
                    var assign  = document.getElementById("pm");
                    var commentButton  = document.getElementById("commentButton");
                    updateStatus.style.visibility = "hidden";
                    commentButton.style.visibility = "hidden";
                    if (assign != null){
                        assign.style.visibility = "hidden";
                    }
                }

            }
            if (data.resolvedBy != null){
                $scope.resolvedByLength = data.resolvedBy.length;
            }
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
            var recommend = document.getElementById("recommendedUser");
            var all = document.getElementById("allUsers");
            if (data !="") {
                if (data.UsersFromProject.length == 0) {
                    if (recommend != null) {
                        recommend.style.display = "none";
                    }
                } else {
                    if (all != null) {
                        all.style.display = "none";
                    }
                }
            }
        }).error(function (data, status, headers, config) {

        });

    };
}

function UpdateStatusController($scope, $http) {
    $scope.getDataFromServer = function (username, project, id) {
        $http({
            method: 'POST',
            url: 'GetTaskInfo?username=' + username + '&project=' + project + '&id=' + id
        }).success(function (data, status, headers, config) {
            var task_status = [{"name":"Created","value":"0"}, {"name":"In progress","value":"1"},
                {"name":"Finished","value":"2"}, {"name":"Close","value":"3"}];
            var taskStatus = data.status;
            if (taskStatus != null){
                for(var index=0; index < task_status.length; index++){
                    var statusMap = task_status[index];
                    var name = statusMap["name"];
                    if (name != null){
                        if (taskStatus == name){
                            statusMap["selected"] = "selected";
                        }
                    }
                }
            }
            $scope.status = task_status;
        }).error(function (data, status, headers, config) {

        });

    };

}

function CommentsController($scope, $http) {
    $scope.getDataFromServer = function (username, project, id) {
        $http({
            method: 'POST',
            url: 'GetComments?username=' + username + '&project=' + project + '&id=' + id
        }).success(function (data, status, headers, config) {
            $scope.comments = data;
        }).error(function (data, status, headers, config) {

        });

    };

}

function GetTasksController($scope, $http) {

    $scope.getDataFromServer = function (username) {
        $http({
            method: 'POST',
            url: 'GetTasks?username=' + username
        }).success(function (data, status, headers, config) {
            $scope.projects = data;
        }).error(function (data, status, headers, config) {

        });

    };
}

