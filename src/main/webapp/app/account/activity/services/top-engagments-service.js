(function() {
    'use strict';

    angular
        .module('sigterraWebApp')

        .service('TopEngagementsService', TopEngagementsService)
        .constant('SIZE', 10)
        .constant('NUMBER', 0);

        TopEngagementsService.$inject = ['$http', 'NUMBER', 'SIZE'];

        function TopEngagementsService ($http, NUMBER, SIZE) {

            var service = {};
            service.getTopEngagements = getTopEngagements;
            service.appendTopEngagements = appendTopEngagements;

            return service;

            function getTopEngagements(cardletId, engagementsPeriod, sc, ec) {
                var path;
                if(cardletId === 0) {
                    path = '/api/analytic/top';
                } else {
                    path = '/api/analytic/top/' + cardletId;
                }
                $http.get(path + '?page=' + NUMBER + '&size=' + SIZE +'&period=' + engagementsPeriod).then(sc, ec);
            }

            function appendTopEngagements(cardletId, pageNumber, engagementsPeriod, sc, ec) {
                var path;
                if(cardletId === 0) {
                    path = '/api/analytic/top';
                } else {
                    path = '/api/analytic/top/' + cardletId;
                }
                $http.get(path + '?page=' + pageNumber + '&size=' + SIZE +'&period=' + engagementsPeriod).then(sc, ec);
            }

        }

})();
