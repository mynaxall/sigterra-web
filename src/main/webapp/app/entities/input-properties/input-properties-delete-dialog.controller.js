(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('InputPropertiesDeleteController',InputPropertiesDeleteController);

    InputPropertiesDeleteController.$inject = ['$uibModalInstance', 'entity', 'InputProperties'];

    function InputPropertiesDeleteController($uibModalInstance, entity, InputProperties) {
        var vm = this;

        vm.inputProperties = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InputProperties.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
