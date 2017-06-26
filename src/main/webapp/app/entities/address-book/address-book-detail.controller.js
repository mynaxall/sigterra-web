(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('AddressBookDetailController', AddressBookDetailController);

    AddressBookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AddressBook', 'User', 'Cardlet'];

    function AddressBookDetailController($scope, $rootScope, $stateParams, previousState, entity, AddressBook, User, Cardlet) {
        var vm = this;

        vm.addressBook = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sigterraWebApp:addressBookUpdate', function(event, result) {
            vm.addressBook = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
