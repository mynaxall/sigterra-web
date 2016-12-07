(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('TabTypeDetailController', TabTypeDetailController);

    TabTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TabType'];

    function TabTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, TabType) {
        var vm = this;

        vm.tabType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:tabTypeUpdate', function(event, result) {
            vm.tabType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
