(function () {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ActivityController', ActivityController)
        .directive('accountActivityStatistics', accountActivityStatistics)
        .directive('activityTopEngagements', activityTopEngagements)
        .directive('recentActivitySidebar', recentActivitySidebar)
        .directive('whenScrollEnds', [whenScrollEnds]);

    function accountActivityStatistics() {
        return {
            restrict: 'E',
            templateUrl: 'app/account/activity/account-activity-statistics.html'
        };
    }
    function activityTopEngagements() {
        return {
            restrict: 'E',
            templateUrl: 'app/account/activity/activity-top-engagements.html'
        };
    }
    function recentActivitySidebar() {
        return {
            restrict: 'E',
            templateUrl: 'app/account/activity/recent-activity-sidebar.html'
        };
    }
    function whenScrollEnds() {
        return {
            restrict: "A",
            link: function (scope, elem, attrs) {
                var visibleHeight = elem.height();
                var threshold = 105;
                elem.on('scroll', function () {
                    var scrollableHeight = elem.prop('scrollHeight');
                    var hiddenContentHeight = scrollableHeight - visibleHeight;
                    if (hiddenContentHeight - elem.scrollTop() <= threshold) {
                        // Scroll is almost at the bottom. Loading more rows
                        scope.vm.nextTopEngagements();
                    }
                });
            }
        }
    }

    ActivityController.$inject = ['$scope', '$http', '$timeout', 'TopEngagementsService', 'SIZE'];

    function ActivityController($scope, $http, $timeout, TopEngagementsService, SIZE) {
        var vm = this;
        vm.error = null;
        vm.success = null;
        vm.activityCounters = null;
        vm.activityPeriod = 'day';
        vm.engagementsPeriod = 'day';
        vm.engagements = [];
        vm.setPeriod = setPeriod;
        vm.getTopEngagements = getTopEngagements;
        vm.nextTopEngagements = nextTopEngagements;
        vm.appendTopEngagements = appendTopEngagements;
        vm.onCompleteRequest = onCompleteRequest;
        vm.pageNumber = 0;

        vm.onLoad = onLoad;
        vm.onLoad();
        function onLoad() {
            vm.getTopEngagements();
            vm.runProcess = false;
            $scope.showSpinner = false;
            $scope.showEngagementsSpinner = false;
        }

        function onCompleteRequest(spinner) {
            if(spinner == $scope.showSpinner) {
                $scope.showSpinner = false;
            } else if(spinner == $scope.showEngagementsSpinner){
                $scope.showEngagementsSpinner = false;
            }
            vm.runProcess = false;
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
                    vm.onCompleteRequest($scope.showSpinner);
                }).error(function (err) {
                    console.log(err);
                    vm.onCompleteRequest($scope.showSpinner);
            });
        };

        /** Top Engagements Block */
        vm.setEngagementsPeriod = function (period) {
            if (vm.runProcess) return;
            vm.engagementsPeriod = period;
            vm.pageNumber = 0;
            vm.getTopEngagements();
        };
        function getTopEngagements() {
            $scope.showEngagementsSpinner = true;
            TopEngagementsService.getTopEngagements(vm.engagementsPeriod, function (res) {
                vm.engagements = res.data;
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            }, function (err) {
                console.log(err);
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            });
        }
        function appendTopEngagements() {
            $scope.showEngagementsSpinner = true;
            TopEngagementsService.appendTopEngagements( vm.pageNumber, vm.engagementsPeriod, function (res) {
                vm.engagements = vm.engagements.concat(res.data);
                vm.pageNumber ++;
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            }, function (err) {
                console.log(err);
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            });
        }
        function nextTopEngagements() {
            vm.appendTopEngagements();
        }
    }

})();
