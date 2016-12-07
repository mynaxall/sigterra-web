(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ItemDataDeleteController',ItemDataDeleteController);

    ItemDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'ItemData'];

    function ItemDataDeleteController($uibModalInstance, entity, ItemData) {
        var vm = this;

        vm.itemData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ItemData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
