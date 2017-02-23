(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('UserCardlet', UserCardlet);

    UserCardlet.$inject = ['$resource', 'DateUtils'];

    function UserCardlet ($resource, DateUtils) {
        var resourceUrl =  'api/userCardlets';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                UserCardlet: function (data) {
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
