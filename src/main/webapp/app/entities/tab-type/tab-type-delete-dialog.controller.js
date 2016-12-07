(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('TabTypeDeleteController',TabTypeDeleteController);

    TabTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TabType'];

    function TabTypeDeleteController($uibModalInstance, entity, TabType) {
        var vm = this;

        vm.tabType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TabType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
