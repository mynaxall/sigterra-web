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
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', '$scope'];

    function SettingsController (Principal, Auth, $scope) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */


        Principal.identity().then(function(account) {
            vm.settingsAccount = account;
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }


        vm.changePassword = changePassword;
        vm.doNotMatch = null;
        vm.errorPassword = null;
        vm.successPassword = null;

        Principal.identity().then(function(account) {
            vm.account = account;
        });

        function changePassword () {
            if (vm.password !== vm.confirmPassword) {
                vm.error = null;
                vm.successPassword = null;
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;
                Auth.changePassword(vm.password).then(function () {
                    vm.error = null;
                    vm.successPassword = 'OK';
                }).catch(function () {
                    vm.errorPassword = null;
                    vm.error = 'ERROR';
                });
            }
        }

        vm.clear = clear;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        $scope.myImage='';
        $scope.myCroppedImage = '';

        $scope.showCroppedImage = false;

        var handleFileSelect=function(evt) {
            var file=evt.currentTarget.files[0];
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage=evt.target.result;
                });
            };
            reader.readAsDataURL(file);
            console.log("asdas")
        };
        angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

        vm.isShowDialog = false;

        vm.showImageDialog = showImageDialog;

        function showImageDialog(){
            vm.isShowDialog = true;
        }

        vm.hideImageDialog = hideImageDialog;
        function hideImageDialog(){
            vm.isShowDialog = false;
        }

        vm.saveImage =saveImage;

        function saveImage(){
            $scope.showCroppedImage = true;
            vm.isShowDialog = false;
        }
    }
})();
