(function() {
    'use strict';
    angular
        .module('sigterraWebApp')
        .factory('PreviewCardlet', PreviewCardlet);

    PreviewCardlet.$inject = ['$resource', 'DateUtils'];

    function  PreviewCardlet ($resource, DateUtils) {
        var resourceUrl =  'api/userCardlets';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                PreviewCardlet: function (data) {
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
