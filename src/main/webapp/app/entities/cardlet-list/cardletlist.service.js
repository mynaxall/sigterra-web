(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('CardletList', CardletList);

    CardletList.$inject = ['$resource', 'DateUtils'];

    function CardletList ($resource, DateUtils) {
        var resourceUrl =  'api/userCardlets';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
                        data.modifiedDate = DateUtils.convertLocalDateFromServer(data.modifiedDate);
                    }
                    return data;
                }
            }
        });
    }
})();
