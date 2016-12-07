(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('SignatureDetailController', SignatureDetailController);

    SignatureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Signature'];

    function SignatureDetailController($scope, $rootScope, $stateParams, previousState, entity, Signature) {
        var vm = this;

        vm.signature = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:signatureUpdate', function(event, result) {
            vm.signature = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
