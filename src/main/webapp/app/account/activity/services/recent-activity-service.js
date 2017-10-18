(function() {
    'use strict';

    angular
        .module('sigterraWebApp')

        .service('RecentActivityService', RecentActivityService)
        .constant('SIZE_RECENT', 11)
        .constant('NUMBER_RECENT', 0);

        RecentActivityService.$inject = ['$http', 'NUMBER_RECENT', 'SIZE_RECENT'];

        function RecentActivityService ($http, NUMBER_RECENT, SIZE_RECENT) {

            var service = {};
            service.getRecentActivity = getRecentActivity;
            service.appendRecentActivity = appendRecentActivity;

            return service;

            function getRecentActivity(cardletId, recentType, sc, ec) {
                var path;
                if(cardletId === 0) {
                    path = '/api/analytic/recent';
                } else {
                    path = '/api/analytic/recent/' + cardletId;
                }
                $http.get(path + '?page=' + NUMBER_RECENT + '&size=' + SIZE_RECENT +'&type=' + recentType).then(sc, ec);
            }

            function appendRecentActivity(cardletId, pageRecent, recentType, sc, ec) {
                var path;
                if(cardletId == '0') {
                    path = '/api/analytic/recent';
                } else {
                    path = '/api/analytic/recent/' + cardletId;
                }
                $http.get(path + '?page=' + pageRecent + '&size=' + SIZE_RECENT +'&type=' + recentType).then(sc, ec);
            }

        }

})();
