(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('CardletDialogController', CardletDialogController);

    CardletDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cardlet', 'User'];

    function CardletDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cardlet, User) {
        var vm = this;

        vm.cardlet = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cardlet.id !== null) {
                Cardlet.update(vm.cardlet, onSaveSuccess, onSaveError);
            } else {
                Cardlet.save(vm.cardlet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:cardletUpdate', result);
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
