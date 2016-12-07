(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('CardletDeleteController',CardletDeleteController);

    CardletDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cardlet'];

    function CardletDeleteController($uibModalInstance, entity, Cardlet) {
        var vm = this;

        vm.cardlet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cardlet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
