(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('ItemData', ItemData);

    ItemData.$inject = ['$resource', 'DateUtils'];

    function ItemData ($resource, DateUtils) {
        var resourceUrl =  'api/item-data/:id';

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
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
                    copy.modifiedDate = DateUtils.convertLocalDateToServer(copy.modifiedDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
                    copy.modifiedDate = DateUtils.convertLocalDateToServer(copy.modifiedDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
