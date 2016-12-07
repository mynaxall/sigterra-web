(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('BusinessDeleteController',BusinessDeleteController);

    BusinessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Business'];

    function BusinessDeleteController($uibModalInstance, entity, Business) {
        var vm = this;

        vm.business = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Business.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
