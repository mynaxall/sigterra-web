(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('InputPropertiesDetailController', InputPropertiesDetailController);

    InputPropertiesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InputProperties'];

    function InputPropertiesDetailController($scope, $rootScope, $stateParams, previousState, entity, InputProperties) {
        var vm = this;

        vm.inputProperties = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:inputPropertiesUpdate', function(event, result) {
            vm.inputProperties = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
