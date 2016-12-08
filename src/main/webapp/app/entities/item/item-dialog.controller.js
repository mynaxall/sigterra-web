(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ItemDialogController', ItemDialogController);

    ItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Item', 'Cardlet', 'TabType'];

    function ItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Item, Cardlet, TabType) {
        var vm = this;

        vm.item = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.cardlets = Cardlet.query();
        vm.tabtypes = TabType.query({filter: 'item-is-null'});
        $q.all([vm.item.$promise, vm.tabtypes.$promise]).then(function() {
            if (!vm.item.tabType || !vm.item.tabType.id) {
                return $q.reject();
            }
            return TabType.get({id : vm.item.tabType.id}).$promise;
        }).then(function(tabType) {
            vm.tabtypes.push(tabType);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.item.id !== null) {
                Item.update(vm.item, onSaveSuccess, onSaveError);
            } else {
                Item.save(vm.item, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:itemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
