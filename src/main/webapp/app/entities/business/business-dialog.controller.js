(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('BusinessDialogController', BusinessDialogController);

    BusinessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Business', 'Cardlet', 'TabType'];

    function BusinessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Business, Cardlet, TabType) {
        var vm = this;

        vm.business = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.cardlets = Cardlet.query();
        vm.tabtypes = TabType.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.tabtypes.$promise]).then(function() {
            if (!vm.business.tabType || !vm.business.tabType.id) {
                return $q.reject();
            }
            return TabType.get({id : vm.business.tabType.id}).$promise;
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
            if (vm.business.id !== null) {
                Business.update(vm.business, onSaveSuccess, onSaveError);
            } else {
                Business.save(vm.business, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:businessUpdate', result);
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
