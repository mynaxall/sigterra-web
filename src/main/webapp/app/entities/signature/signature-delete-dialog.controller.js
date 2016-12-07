(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('SignatureDeleteController',SignatureDeleteController);

    SignatureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Signature'];

    function SignatureDeleteController($uibModalInstance, entity, Signature) {
        var vm = this;

        vm.signature = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Signature.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
