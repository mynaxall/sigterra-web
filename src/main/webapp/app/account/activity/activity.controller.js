(function () {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ActivityController', ActivityController)
        .directive('accountActivityStatistics', function () {
            return {
                restrict: 'E',
                templateUrl: 'app/account/activity/account-activity-statistics.html'
            };
        })
        .directive('activityTopEngagements', function () {
            return {
                restrict: 'E',
                templateUrl: 'app/account/activity/activity-top-engagements.html'
            };
        })
        .directive('recentActivitySidebar', function () {
            return {
                restrict: 'E',
                templateUrl: 'app/account/activity/recent-activity-sidebar.html'
            };
        });

    ActivityController.$inject = ['$scope', '$http', '$timeout', 'TopEngagementsService'];

    function ActivityController($scope, $http, $timeout, TopEngagementsService) {
        var vm = this;
        vm.error = null;
        vm.success = null;
        vm.activityCounters = null;
        vm.activityPeriod = 'day';
        vm.engagementsPeriod = 'day';
        vm.engagements = [];
        vm.setPeriod = setPeriod;
        vm.getTopEngagements = getTopEngagements;


        vm.onLoad = onLoad;
        vm.onLoad();
        function onLoad() {
            vm.runProcess = false;
            $scope.showSpinner = false;
            $scope.showEngagementsSpinner = false;
            vm.getTopEngagements();
        }


        /** Activity Counters Block */
        function setPeriod(period) {
            if (vm.runProcess) return;
            vm.activityPeriod = period;
            $scope.getActivityCounters();
        }
        $scope.getActivityCounters = function () {
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
        };


        /** Top Engagements Block */
        vm.setEngagementsPeriod = function (period) {
            if (vm.runProcess) return;
            vm.engagementsPeriod = period;
            vm.getTopEngagements();
        };
        function getTopEngagements() {
            $scope.showEngagementsSpinner = true;
            TopEngagementsService.getTopEngagements(vm.engagementsPeriod, function (res) {
                vm.engagements = res.data;
                vm.runProcess = false;
                $scope.showEngagementsSpinner = false;
            }, function (err) {
                $scope.showEngagementsSpinner = false;
                console.log(err);
                vm.runProcess = false;
                $scope.showEngagementsSpinner = false;
            });
        }
    }

})();
