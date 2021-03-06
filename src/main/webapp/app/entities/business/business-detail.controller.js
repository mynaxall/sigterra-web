(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('BusinessDetailController', BusinessDetailController);

    BusinessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Business', 'Cardlet', 'TabType', 'InputProperties'];

    function BusinessDetailController($scope, $rootScope, $stateParams, previousState, entity, Business, Cardlet, TabType, InputProperties) {
        var vm = this;

        vm.business = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:businessUpdate', function(event, result) {
            vm.business = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
