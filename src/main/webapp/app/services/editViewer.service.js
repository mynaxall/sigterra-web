

(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .factory('EditViewerService', EditViewerService);

    EditViewerService.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$resource', '$http'];

    function EditViewerService ($rootScope, $state, $sessionStorage, $q, $resource, $http) {
        var service = {
            getBackgrounds: getBackgrounds,
            getListIcons: getListIcons,
            saveViewer: saveViewer,
            getPreview: getPreview,
            updateViewer: updateViewer

        };

        return service;

        function getBackgrounds(){
            var service = $http.get('api/cardlet/pageview/listbackgrounds').then(function(result) {
                return result.data;
            });
            return service;
        }

        function getListIcons(){
            var service = $http.get('api/cardlet/pageview/listicons').then(function(result) {
                return result.data;
            });
            return service;
        }

        function getPreview(id) {
            var service = $http.get('/api/cardlet/pageview/'+ id).then(function(result) {
                return result.data;
            });
            return service;

        }

        function saveViewer(value){
            var service = $http.post('api/cardlet/pageview/', value).then(function(result) {
                return result.data;
            });
            return service;
        }

        function updateViewer(value){
            var service = $http.put('api/cardlet/pageview/', value).then(function(result) {
                return result.data;
            });
            return service;
        }
    }
})();


