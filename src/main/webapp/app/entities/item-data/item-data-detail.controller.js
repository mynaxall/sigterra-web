(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ItemDataDetailController', ItemDataDetailController);

    ItemDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ItemData', 'Item'];

    function ItemDataDetailController($scope, $rootScope, $stateParams, previousState, entity, ItemData, Item) {
        var vm = this;

        vm.itemData = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:itemDataUpdate', function(event, result) {
            vm.itemData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
