(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ItemDataDialogController', ItemDataDialogController);

    ItemDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ItemData', 'Item'];

    function ItemDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ItemData, Item) {
        var vm = this;

        vm.itemData = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.items = Item.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.itemData.id !== null) {
                ItemData.update(vm.itemData, onSaveSuccess, onSaveError);
            } else {
                ItemData.save(vm.itemData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:itemDataUpdate', result);
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
