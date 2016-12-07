(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('SignatureDialogController', SignatureDialogController);

    SignatureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Signature'];

    function SignatureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Signature) {
        var vm = this;

        vm.signature = entity;
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
            if (vm.signature.id !== null) {
                Signature.update(vm.signature, onSaveSuccess, onSaveError);
            } else {
                Signature.save(vm.signature, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:signatureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
