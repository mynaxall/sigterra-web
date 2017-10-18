(function () {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ActivityController', ActivityController)
        .filter('activityString', activityString)
        .filter('activityIcon', activityIcon)
        .directive('accountActivityStatistics', accountActivityStatistics)
        .directive('activityTopEngagements', activityTopEngagements)
        .directive('recentActivitySidebar', recentActivitySidebar)
        .directive('whenScrollEnds', [whenScrollEnds]);

    function activityString() {
        return function (type, title) {
            switch (type) {
                case 'view':
                    return 'viewed your "' + title + '" profile';
                    break;
                case 'add':
                    return 'has added you to his address book';
                    break;
                case 'read':
                    return 'opened your “' + title + '” link';
                    break;
                default:
                    return type;
            }
        }
    }
    function activityIcon() {
        return function (type) {
            switch (type) {
                case 'view':
                    return 'eye-open';
                    break;
                case 'add':
                    return 'phone-alt';
                    break;
                case 'read':
                    return 'list-alt';
                    break;
                default:
                    return 'eye-open';
            }
        }
    }

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
                var threshold = 100;
                elem.on('scroll', function () {
                    var scrollableHeight = elem.prop('scrollHeight');
                    var hiddenContentHeight = scrollableHeight - visibleHeight;
                    if (hiddenContentHeight - elem.scrollTop() <= threshold) {
                        // Scroll is almost at the bottom. Loading more rows
                        if(attrs.whenScrollEnds == 'nextTopEngagements'){
                            scope.vm.nextTopEngagements();
                        }
                        if(attrs.whenScrollEnds == 'nextRecentActivity'){
                            scope.vm.nextRecentActivity();
                        }
                    }
                });
            }
        }
    }

    ActivityController.$inject = ['$scope', '$http', '$timeout', 'TopEngagementsService', 'RecentActivityService'];

    function ActivityController($scope, $http, $timeout, TopEngagementsService, RecentActivityService) {
        var vm = this;
        vm.error = null;
        vm.success = null;
        vm.activityCounters = null;
        vm.cardlets = null;
        vm.toggleText = 'All Profiles';
        vm.cardletID = 0;
        vm.activityPeriod = 'day';
        vm.engagementsPeriod = 'day';
        vm.pageNumber = 0;
        vm.pageRecent = 0;
        vm.recentType = 'all';
        vm.engagements = [];
        vm.recents = [];
        vm.viewByCardletId = viewByCardletId;
        vm.setPeriod = setPeriod;
        vm.setEngagementsPeriod = setEngagementsPeriod;
        vm.getTopEngagements = getTopEngagements;
        vm.nextTopEngagements = nextTopEngagements;
        vm.appendTopEngagements = appendTopEngagements;
        vm.onCompleteRequest = onCompleteRequest;
        vm.setType = setType;
        vm.getRecentActivity = getRecentActivity;
        vm.appendRecentActivity = appendRecentActivity;
        vm.nextRecentActivity = nextRecentActivity;

        vm.onLoad = onLoad;
        vm.onLoad();

        function onLoad() {
            vm.getTopEngagements();
            vm.getRecentActivity();
            vm.runProcess = false;
            $scope.showSpinner = false;
            $scope.showEngagementsSpinner = false;
            $scope.showRecentSpinner = false;
        }

        function onCompleteRequest(spinner) {
            switch (spinner) {
                case $scope.showSpinner:
                    $scope.showSpinner = false;
                    break;
                case $scope.showEngagementsSpinner:
                    $scope.showEngagementsSpinner = false;
                    break;
                case $scope.showRecentSpinner:
                    $scope.showRecentSpinner = false;
                    break;
                case $scope.showProfileSpinner:
                    $scope.showProfileSpinner = false;
                    break;
                default:
                    $scope.showSpinner = false;
            }
            vm.runProcess = false;
        }

        /** Cardlets Dropdown Menu */
        function viewByCardletId(cardletId,toggleText) {
            vm.cardletID = cardletId;
            vm.toggleText = toggleText;
            console.log(vm.toggleText);
            vm.runProcess = true;
            $scope.showProfileSpinner = true;
            $scope.getActivityCounters();
            vm.onLoad();
            onCompleteRequest($scope.showProfileSpinner);
        }
        $scope.getCardletsItems = function () {
            $http.get('/api/userCardlets')
                .success(function (data) {
                    vm.cardlets = data;
                }).error(function (err) {
                    console.log(err);
            });
        };

        /** Activity Counters Block */
        function setPeriod(period) {
            if (vm.runProcess) return;
            vm.activityPeriod = period;
            $scope.getActivityCounters();
        }
        $scope.getActivityCounters = function () {
            $scope.showSpinner = true;
            vm.runProcess = true;
            var countersPath;
            if(vm.cardletID === 0) {
                countersPath = '/api/analytic/stat';
            } else {
                countersPath = '/api/analytic/stat/' + vm.cardletID;
            }
            $http.get(countersPath + '?period=' + vm.activityPeriod)
                .success(function (data, status, headers, config) {
                    vm.activityCounters = data;
                    vm.onCompleteRequest($scope.showSpinner);
                }).error(function (err) {
                console.log(err);
                vm.onCompleteRequest($scope.showSpinner);
            });
        };

        /** Top Engagements Block */
        function setEngagementsPeriod(period) {
            if (vm.runProcess) return;
            vm.engagementsPeriod = period;
            vm.pageNumber = 0;
            vm.getTopEngagements();
        }
        function getTopEngagements() {
            $scope.showEngagementsSpinner = true;
            TopEngagementsService.getTopEngagements(vm.cardletID, vm.engagementsPeriod, function (res) {
                vm.engagements = res.data;
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            }, function (err) {
                console.log(err);
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            });
        }
        function appendTopEngagements() {
            $scope.showEngagementsSpinner = true;
            TopEngagementsService.appendTopEngagements(vm.cardletID, vm.pageNumber, vm.engagementsPeriod, function (res) {
                vm.engagements = vm.engagements.concat(res.data);
                vm.pageNumber++;
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            }, function (err) {
                console.log(err);
                vm.onCompleteRequest($scope.showEngagementsSpinner);
            });
        }
        function nextTopEngagements() {
            vm.appendTopEngagements();
        }

        /** Recent Activity Block */
        function setType(type) {
            if (vm.runProcess) return;
            vm.recentType = type;
            vm.pageRecent = 0;
            vm.getRecentActivity();
        }
        function getRecentActivity() {
            $scope.showRecentSpinner = true;
            RecentActivityService.getRecentActivity(vm.cardletID, vm.recentType, function (res) {
                vm.recents = res.data;
                vm.onCompleteRequest($scope.showRecentSpinner);
            }, function (err) {
                console.log(err);
                vm.onCompleteRequest($scope.showRecentSpinner);
            });
        }
        function appendRecentActivity() {
            $scope.showRecentSpinner = true;
            RecentActivityService.appendRecentActivity(vm.cardletID, vm.pageRecent, vm.recentType, function (res) {
                vm.recents = vm.recents.concat(res.data);
                vm.pageRecent++;
                vm.onCompleteRequest($scope.showRecentSpinner);
            }, function (err) {
                console.log(err);
                vm.onCompleteRequest($scope.showRecentSpinner);
            });
        }
        function nextRecentActivity() {
            vm.appendRecentActivity();
        }


    }

})();
