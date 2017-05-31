(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ActivationController', ActivationController);

    ActivationController.$inject = ['$stateParams', 'Auth', 'LoginService', '$scope', '$http', '$location'];

    function ActivationController ($stateParams, Auth, LoginService, $scope, $http, $location) {
        var vm = this;



        $scope.saveCardlet = function(){
            console.log($scope.userAccount.id)
            $http.post("/api/cardletFirst?id="+$scope.userAccount.id,  $scope.tabNames)
                .success(function (data, status, headers, config) {
                });
        }

        $scope.tabNames ={
            "cardletName": "Business cards",
            "tabs":
                [{
                    "name": "My info",
                    'position': 0,
                    "display": "block",
                    "tabType": 1,
                    "layout":{
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2",
                        "tabId": 1

                    },
                    "photo": "/content/images/avatar_img.png",


                },
                    {
                        "name": "Items",
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
                                "image3": "/content/images/img/portfolio_img_03.png"

                            }
                        ]
                    }
                ]
        };

        $scope.userAccount;

        $scope.getUserProfile = function(){
            $http.get("/api/accountActivate?key="+ $stateParams.key)
                .success(function(response, status, headers) {

                    $scope.userAccount = response;
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

                });
            Auth.activateAccount({key: $stateParams.key}).then(function () {
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        };
        $scope.getUserProfile();

        vm.login = LoginService.open;
    }
})();
