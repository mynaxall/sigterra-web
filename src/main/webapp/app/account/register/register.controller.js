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
        .controller('RegisterController', RegisterController);


    RegisterController.$inject = [ '$timeout', 'Auth', 'LoginService', '$state', '$scope', '$location', '$http', 'CreateFirstCardlet'];

    function RegisterController ($timeout, Auth, LoginService, $state, $scope, $location, $http, CreateFirstCardlet) {
        var vm = this;


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
                    $scope.userAccount = response;
                    $scope.getUserProfile();
                }).catch(function (response) {
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.isFirstStep = true;
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
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
                    $scope.getUserProfile();
                }).catch(function (response) {
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.isFirstStep = true;
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }
                });
            }
        }

        $scope.tabNames ={
            "cardletName": "Your Sigterra Profile Name",
            "tabs":
                [{
                    "name": "Contact Info",
                    'position': 0,
                    "display": "block",
                    "tabType": 1,
                    "layout":{
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2",
                        "tabId": 1

                    },
                    "photo": $location.protocol() + '://' + $location.host() + ':' + $location.port()+"/content/images/avatar_img.png",


                },
                    {
                        "name": "Image Items",
                        "position": 1,
                        "tabType": 2,
                        "layout": {
                            "mainColor": "FFFFFF",
                            "secondaryColor": "4BABE2",
                            "tabId": 4
                        },
                        "items": [
                            {
                                //"name": "1 item",
                                "index": "2",
                                "position": "0",
                                "image": "/content/images/portfolio_img_01.png",
                                "image2": "/content/images/portfolio_img_02.png",
                                "image3": "/content/images/img/portfolio_img_03.png",
                                "name": {
                                    "value": "Item Header"
                                },
                                "description": {
                                    "value": "Item Description"
                                }

                            }
                        ]
                    }
                ]
        };

        $scope.getUserProfile = function(){

                    console.log($scope.userAccount)
                    $scope.tabNames.tabs[0].userName = {
                        "value": $scope.userAccount.username
                    };
                    $scope.tabNames.tabs[0].userEmail = {
                        "value": $scope.userAccount.email
                    };

                    $scope.tabNames.tabs[0].phone = {
                        "value": $scope.userAccount.phoneNumber
                    };

                    $scope.tabNames.tabs[0].address = {
                        "value": $scope.userAccount.address
                    };

                    $scope.tabNames.tabs[0].company = {
                        "value": $scope.userAccount.companyName
                    };

                    $scope.tabNames.tabs[0].site = {
                        "value": $scope.userAccount.companySite
                    };

                    $scope.tabNames.tabs[0].job = {
                        "value": $scope.userAccount.jobTitle
                    };
                    if($scope.userAccount.imageUrl){
                        $scope.tabNames.tabs[0].photo = $scope.userAccount.imageUrl
                    }else{
                        $scope.tabNames.tabs[0].photo = $location.protocol() + '://' + $location.host() + ':' + $location.port()+"/content/images/avatar_img.png"
                    }
                    $scope.saveCardlet();
        };

        $scope.saveCardlet = function(){
            console.log($scope.userAccount.id)
            CreateFirstCardlet.saveCardlet($scope.userAccount.id, $scope.tabNames).then(function (response) {
                console.log(response)
            }).catch(function (response) {
            })
        }
    }
})();
