(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('Item', Item);

    Item.$inject = ['$resource', 'DateUtils'];

    function Item ($resource, DateUtils) {
        var resourceUrl =  'api/items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
                        data.modifiDate = DateUtils.convertLocalDateFromServer(data.modifiDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
                    copy.modifiDate = DateUtils.convertLocalDateToServer(copy.modifiDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
                    copy.modifiDate = DateUtils.convertLocalDateToServer(copy.modifiDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
