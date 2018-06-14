

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
            updateViewer: updateViewer,
            updateWidgets: updateWidgets,
            getWidgets: getWidgets,
            deleteWidget: deleteWidget,
            deleteContentLibrary: deleteContentLibrary,
            deleteQuickbite: deleteQuickbite,
            uploadFile: uploadFile,
            likesContentLibrary: likesContentLibrary,
            viewsContentLibrary: viewsContentLibrary

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

        function updateWidgets(value) {
            var service = $http.put('api/cardlet-widgets/', value).then(function(result) {
                return result.data;
            });
            return service;
        }


        function getWidgets(id){
            var service = $http.get('api/cardlet/'+ id +'/widgets').then(function(result) {
                return result.data;
            });
            return service;
        }

        function deleteWidget(id){
            var service = $http.delete('api/cardlet-testimonial-widgets/' + id).then(function(result) {
                return result.data;
            });
            return service;
        }

        function deleteContentLibrary(id) {
            var service = $http.delete('api/cardlet-content-library-widgets/' + id).then(function(result) {
                return result.data;
            });
            return service;
        }

        function deleteQuickbite(id){
            var service = $http.delete('api/cardlet-quick-bites-widgets/' + id).then(function(result) {
                return result.data;
            });
            return service;
        }


        function uploadFile(id, fd) {
            var service =  $http.post('api/cardlet/' + id + '/widgets/images/upload',  fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            });
            return service;
        }

        function likesContentLibrary (id) {
            var service = $http.post('api/cardlet/content-library-widget/' + id + '/likes').then(function(result) {
            });
            return service
        }


        function viewsContentLibrary (id) {
            var service = $http.post('api/cardlet/content-library-widget/' + id + '/views').then(function(result) {
            });
            return service
        }






    }
})();


