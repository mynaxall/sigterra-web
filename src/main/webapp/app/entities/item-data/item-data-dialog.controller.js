(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ItemDataDialogController', ItemDataDialogController);

    ItemDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ItemData', 'Item', 'InputProperties'];

    function ItemDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ItemData, Item, InputProperties) {
        var vm = this;

        vm.itemData = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.items = Item.query();
        vm.names = InputProperties.query({filter: 'itemdata-is-null'});
        $q.all([vm.itemData.$promise, vm.names.$promise]).then(function() {
            if (!vm.itemData.name || !vm.itemData.name.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.itemData.name.id}).$promise;
        }).then(function(name) {
            vm.names.push(name);
        });
        vm.descriptions = InputProperties.query({filter: 'itemdata-is-null'});
        $q.all([vm.itemData.$promise, vm.descriptions.$promise]).then(function() {
            if (!vm.itemData.description || !vm.itemData.description.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.itemData.description.id}).$promise;
        }).then(function(description) {
            vm.descriptions.push(description);
        });

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
