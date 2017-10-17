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
        })
        .directive('whenScrollEnds', [function() {
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
                            scope.vm.nextPage();
                        }
                    });
                }
            }
        }]);

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
        vm.nextPage = nextPage;
        vm.appendTopEngagements = appendTopEngagements;
        vm.pageNumber = 0;

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
            vm.pageNumber = 0;
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

        function appendTopEngagements() {
            TopEngagementsService.appendTopEngagements( vm.pageNumber, vm.engagementsPeriod, function (res) {
                vm.engagements = vm.engagements.concat(res.data);
                vm.pageNumber ++;
                vm.runProcess = false;
                $scope.showEngagementsSpinner = false;
            }, function (err) {
                console.log(err);
                vm.runProcess = false;
                $scope.showEngagementsSpinner = false;
            });
        }

        function nextPage(event) {
            vm.appendTopEngagements();
        }
    }

})();
