(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('AddressBookDialogController', AddressBookDialogController);

    AddressBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AddressBook', 'User', 'Cardlet'];

    function AddressBookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AddressBook, User, Cardlet) {
        var vm = this;

        vm.addressBook = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.cardlets = Cardlet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.addressBook.id !== null) {
                AddressBook.update(vm.addressBook, onSaveSuccess, onSaveError);
            } else {
                AddressBook.save(vm.addressBook, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:addressBookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
