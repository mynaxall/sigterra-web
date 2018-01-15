

(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .factory('CreateFirstCardlet', CreateFirstCardlet);

    CreateFirstCardlet.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', '$resource'];

    function CreateFirstCardlet ($rootScope, $state, $sessionStorage, $q, $resource) {
        var service = {
            saveCardlet: saveCardlet
        };

        return service;

        function saveCardlet(id, cardlet, callback){
            var cb = callback || angular.noop;
            var Catdlet = $resource('/api/cardletFirst?id='+id);
            return Catdlet.save(cardlet, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }
    }
})();


