(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('InputPropertiesDialogController', InputPropertiesDialogController);

    InputPropertiesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InputProperties'];

    function InputPropertiesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InputProperties) {
        var vm = this;

        vm.inputProperties = entity;
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
            if (vm.inputProperties.id !== null) {
                InputProperties.update(vm.inputProperties, onSaveSuccess, onSaveError);
            } else {
                InputProperties.save(vm.inputProperties, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:inputPropertiesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
