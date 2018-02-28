(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .directive('pwCheck', [function () {
            return {
                require: 'ngModel',
                link: function (scope, elem, attrs, ctrl) {
                    var firstPassword = '#' + attrs.pwCheck;
                    elem.add(firstPassword).on('keyup', function () {
                        scope.$apply(function () {
                            var v = elem.val()===$(firstPassword).val();
                            ctrl.$setValidity('pwmatch', v);
                        });
                    });
                }
            }
        }])
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', '$timeout', 'Auth', 'LoginService', '$state', '$scope', '$location', '$http', 'CreateFirstCardlet', 'PHONE_PATTERN'];

    function UserManagementDialogController ($stateParams, $uibModalInstance, entity, User, $timeout, Auth, LoginService, $state, $scope, $location, $http, CreateFirstCardlet, PHONE_PATTERN) {
        var vm = this;

        vm.clear = clear;
        vm.isFirstStep = true;
        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.registerAccount = {};
        vm.success = null;
        vm.isDone = false;
        vm.skip = skip;
        vm.back = back;
        vm.PHONE_PATTERN = new RegExp(PHONE_PATTERN);

        vm.nextStep = nextStep;

        function nextStep (){
            vm.isFirstStep = false;
        }

        function back() {
            vm.isFirstStep = true;
        }

        $scope.trimValue = function(val){

            if(val && !val.trim()){
                return true
            }
            return false
        }



        $timeout(function (){angular.element('#login').focus();});

        function register () {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey =  'en' ;
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                Auth.createAccount(vm.registerAccount).then(function (response) {
                    vm.success = 'OK';
                    vm.isDone = true;
                    $uibModalInstance.dismiss('cancel');
                    vm.isSaving = true;

                }).catch(function (response) {
                    vm.isSaving = false;
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.isFirstStep = true;
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.errorEmailExists = 'ERROR';
                    }
                });
            }
        }

        function skip () {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey =  'en' ;
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                var skipRegister ={"username": vm.registerAccount.username,
                    "email": vm.registerAccount.email,
                    "password": vm.registerAccount.password

                };



                Auth.createAccount(skipRegister).then(function (response) {
                    vm.success = 'OK';
                    vm.isDone = true;
                    $scope.userAccount = response;
                    $uibModalInstance.dismiss('cancel');
                    vm.isSaving = true;

                }).catch(function (response) {
                    vm.success = null;
                    vm.isSaving = false;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.isFirstStep = true;
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.errorEmailExists = 'ERROR';
                    }
                });
            }
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
