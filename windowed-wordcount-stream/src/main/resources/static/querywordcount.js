angular.module('querywordcount',[]).controller("QueryWordCount", function ($scope,$http,$interval){
    $scope.reload = function () {
        $http.get('windowedWordCount/120000/').
            then(function (response) {
            	$scope.wordList = response.data;
            });
    };
    $scope.reload();
    $interval($scope.reload, 2000);
});