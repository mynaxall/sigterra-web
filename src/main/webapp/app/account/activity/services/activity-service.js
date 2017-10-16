(function() {
    'use strict';

    angular
        .module('sigterraWebApp')

        .service('TopEngagementsService', TopEngagementsService);

        TopEngagementsService.$inject = ['$http'];

        function TopEngagementsService ($http) {

            var service = {};
            service.getTopEngagements = getTopEngagements;

            return service;

            function getTopEngagements(engagementsPeriod, sc, ec) {
                $http.get('/api/analytic/top?period=' + engagementsPeriod).then(sc, ec);
            }

        }

})();
