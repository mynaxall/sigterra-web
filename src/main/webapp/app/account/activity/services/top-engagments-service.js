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

            function getTopEngagements(engagementsPeriod, sc, ec) {
                $http.get('/api/analytic/top?page=' + NUMBER + '&size=' + SIZE +'&period=' + engagementsPeriod).then(sc, ec);
            }

            function appendTopEngagements(pageNumber, engagementsPeriod, sc, ec) {
                $http.get('/api/analytic/top?page=' + pageNumber + '&size=' + SIZE +'&period=' + engagementsPeriod).then(sc, ec);
            }

        }

})();
