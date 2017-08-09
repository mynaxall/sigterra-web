(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('AddressBookDeleteController',AddressBookDeleteController);

    AddressBookDeleteController.$inject = ['$uibModalInstance', 'entity', 'AddressBook'];

    function AddressBookDeleteController($uibModalInstance, entity, AddressBook) {
        var vm = this;

        vm.addressBook = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AddressBook.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
