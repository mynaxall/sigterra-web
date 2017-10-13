(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ActivityController', ActivityController)
        .directive('accountActivityStatistics', function() {
            return {
                restrict: 'E',
                templateUrl: 'app/account/activity/account-activity-statistics.html'
            };
        })
        .directive('activityTopEngagements', function() {
            return {
                restrict: 'E',
                templateUrl: 'app/account/activity/activity-top-engagements.html'
            };
        })
        .directive('recentActivitySidebar', function() {
            return {
                restrict: 'E',
                templateUrl: 'app/account/activity/recent-activity-sidebar.html'
            };
        });

    ActivityController.$inject = ['$scope', '$http', '$timeout'];

    function ActivityController ( $scope, $http, $timeout) {
        var vm = this;
        vm.error = null;
        vm.success = null;
        vm.activityCounters = null;
        vm.activityPeriod = 'day';

        vm.setPeriod = function(period) {
            if (vm.runProcess) return;
            vm.activityPeriod = period;
            $scope.getActivityCounters();
        };

        vm.runProcess = false;

        $scope.showSpinner = false;

        $scope.getActivityCounters = function(){
            $scope.showSpinner = true;
            vm.runProcess = true;
            $http.get('/api/analytic/stat?period=' + vm.activityPeriod)
                .success(function (data, status, headers, config) {
                    vm.activityCounters = data;
                    $scope.showSpinner = false;
                    vm.runProcess = false;
                }).error(function (err) {
                    $scope.showSpinner = false;
                    vm.runProcess = false;
            });
        }
    }

})();
