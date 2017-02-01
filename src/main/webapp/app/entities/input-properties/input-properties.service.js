(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('InputProperties', InputProperties);

    InputProperties.$inject = ['$resource'];

    function InputProperties ($resource) {
        var resourceUrl =  'api/input-properties/:id';

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
