(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('TabTypeDialogController', TabTypeDialogController);

    TabTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TabType'];

    function TabTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TabType) {
        var vm = this;

        vm.tabType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tabType.id !== null) {
                TabType.update(vm.tabType, onSaveSuccess, onSaveError);
            } else {
                TabType.save(vm.tabType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:tabTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
