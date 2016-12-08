(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ItemDetailController', ItemDetailController);

    ItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Item', 'Cardlet', 'TabType'];

    function ItemDetailController($scope, $rootScope, $stateParams, previousState, entity, Item, Cardlet, TabType) {
        var vm = this;

        vm.item = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:itemUpdate', function(event, result) {
            vm.item = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
