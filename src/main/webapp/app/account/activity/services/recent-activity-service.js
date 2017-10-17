(function() {
    'use strict';

    angular
        .module('sigterraWebApp')

        .service('RecentActivityService', RecentActivityService)
        .constant('SIZE_RECENT', 10)
        .constant('NUMBER_RECENT', 0);

        RecentActivityService.$inject = ['$http', 'NUMBER_RECENT', 'SIZE_RECENT'];

        function RecentActivityService ($http, NUMBER_RECENT, SIZE_RECENT) {

            var service = {};
            service.getRecentActivity = getRecentActivity;
            service.appendRecentActivity = appendRecentActivity;

            return service;

            function getRecentActivity(recentType, sc, ec) {
                $http.get('/api/analytic/recent?page=' + NUMBER_RECENT + '&size=' + SIZE_RECENT +'&type=' + recentType).then(sc, ec);
            }

            function appendRecentActivity(pageRecent, recentType, sc, ec) {
                $http.get('/api/analytic/recent?page=' + pageRecent + '&size=' + SIZE_RECENT +'&type=' + recentType).then(sc, ec);
            }

        }

})();
