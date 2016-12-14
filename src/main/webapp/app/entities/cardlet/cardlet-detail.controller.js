(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('CardletDetailController', CardletDetailController);

    CardletDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cardlet', 'User', 'Business', 'Item'];

    function CardletDetailController($scope, $rootScope, $stateParams, previousState, entity, Cardlet, User, Business, Item) {
        var vm = this;

        vm.cardlet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:cardletUpdate', function(event, result) {
            vm.cardlet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
