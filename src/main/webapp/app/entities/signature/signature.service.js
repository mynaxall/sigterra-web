(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('Signature', Signature);

    Signature.$inject = ['$resource'];

    function Signature ($resource) {
        var resourceUrl =  'api/signatures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
