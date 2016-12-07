(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('TabType', TabType);

    TabType.$inject = ['$resource'];

    function TabType ($resource) {
        var resourceUrl =  'api/tab-types/:id';

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
