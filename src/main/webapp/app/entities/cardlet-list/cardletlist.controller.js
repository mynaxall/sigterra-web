(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('CardletListController', CardletListController);

    CardletListController.$inject = ['$scope', '$state', 'CardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout'];

    function CardletListController ($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;

        loadAll();

        function loadAll () {
            CardletList.query({
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.queryCount = vm.totalItems;
                $scope.cardlets = data;
                console.log(data);

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }


        $scope.openCity = function(cityName, tabId) {
            var i, tabcontent, tablinks, tabs;
            tabcontent = document.getElementsByClassName("tabcontent");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].style.display = "none";
            }
            tablinks = document.getElementsByClassName("tablinks");
            for (i = 0; i < tablinks.length; i++) {
                tablinks[i].className = tablinks[i].className.replace(" active", "");

            }
            tabs = document.getElementsByClassName("tabs");

            for (i = 0; i < tabs.length; i++) {
                tabs[i].className = tabs[i].className.replace(" active", "");

            }

            console.log(cityName)
            document.getElementById(cityName).style.display = "block";
            document.getElementById(tabId).className += " active";
        }

        $scope.openCard = function(cardName, tabId) {
            var i, tabcontent2, tablinks2, tabs2;
            tabcontent2 = document.getElementsByClassName("tabcontent2");
            for (i = 0; i < tabcontent2.length; i++) {
                tabcontent2[i].style.display = "none";
            }
            tablinks2 = document.getElementsByClassName("tablinks2");
            for (i = 0; i < tablinks2.length; i++) {
                tablinks2[i].className = tablinks2[i].className.replace(" active", "");

            }
            tabs2 = document.getElementsByClassName("tabs2");
            console.log("tabs2")
            console.log(tabs2)
            for (i = 0; i < tabs2.length; i++) {
                tabs2[i].className = tabs2[i].className.replace(" active", "");

            }

            console.log(cardName);
            console.log("cardName");

            console.log(tabId);
            document.getElementById(cardName).style.display = "block";
            document.getElementById(tabId).className += " active";
        }

        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {
                    console.log(response);
                    $scope.signatures = response;
                });
        }

        $scope.isNewTab = true;

        $scope.tabNames =
            [{
                "name": "card",
                'id': 0,
                "display": "block",
                "tybType": 1,
                "type":{ "url": "app/cardlets/busimessCard.html",
                    "mainColor": "FFFFFF",
                    "secondaryColor": "4BABE2"

                }
            },
            {
                "name": "portfolio",
                "id": 1,
                "tybType": 2,
                "type": {
                    "url": "app/cardlets/item.html",
                    "mainColor": "FFFFFF",
                    "secondaryColor": "4BABE2"
                },
                "items": [
                    {
                        "name": "1 item",
                        "index": "2",
                    }
                ]
            }
            ];

        $scope.tabTypes = [
            {"businessCard":  "app/cardlets/busimessCard.html"},
            {"businessCard":  "app/cardlets/businessCard2.html"},
            {"businessCard":  "app/cardlets/businessCard3.html"}
        ]
        $scope.itemTypes = [
            {"item":  "app/cardlets/item.html"},
            {"item":  "app/cardlets/item2.html"},
            {"item":  "app/cardlets/item3.html"}
        ]



        angular.element(document).ready(function () {
            document.getElementsByClassName("tabcontent2")[0].style.display = "block";
            document.getElementsByClassName("tabs2")[0].className += " active";
            document.getElementsByClassName("tabcontent")[0].style.display = "block";;
            document.getElementsByClassName("tabs")[0].className += " active";
        });


        $scope.addItem = function() {
            if ($scope.tabNames.length <= 3) {
                var newTab = {"name":"Item"+$scope.tabNames.length,
                    "id": $scope.tabNames.length,
                    "tybType": 2,
                    "type":{ "url": "app/cardlets/busimessCard.html",
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"
                    },
                    "items": [
                        {
                            "name": "1 item",
                            "index": "2",
                        }
                    ]

                }
                $scope.tabNames.push(newTab);
            }
            if($scope.tabNames.length == 4){
                $scope.isNewTab = false;
            }
        }

        $scope.addItems = function(tabId, index) {
            if($scope.tabNames[tabId].items.length <= 9){
                var newItem =  {
                    "name":  ($scope.tabNames[tabId].items.length+1)+" item",
                    "index": index+2
                }
                $scope.tabNames[tabId].items.push(newItem);

            }
        }


        $scope.addTab = function() {
            if ($scope.tabNames.length <= 3) {
                var newTab = {"name":"card"+$scope.tabNames.length,
                    "id": $scope.tabNames.length,
                    "tybType": 1,
                    "type":{ "url": "app/cardlets/busimessCard.html",
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"


                    }

                }
                $scope.tabNames.push(newTab);
            }
            if($scope.tabNames.length == 4){
                $scope.isNewTab = false;
            }
        }

        $scope.deleteItems = function(tabId, index){
            if($scope.tabNames[tabId].items.length > 1){
                console.log(index)
                $scope.tabNames[tabId].items.splice((index-2), 1);
                for (var i = 0; i < $scope.tabNames[tabId].items.length; i++) {
                    $scope.tabNames[tabId].items[i].index = i + 2;
                    $scope.tabNames[tabId].items[i].name  =  (i+1)+" item"
                }
                console.log($scope.tabNames[tabId].items)
            }
        }

        $scope.accordionActive = 1;

        $scope.changeAccordionActivity = function(id){
            $scope.accordionActive = id;
        }


        $scope.removeTab = function(index) {

            if($scope.tabNames.length >1) {
                console.log(index)
                $scope.tabNames.splice(index, 1);
                for (var i = 0; i < $scope.tabNames.length; i++) {
                    $scope.tabNames[i].id = i;
                }
                setTimeout(function(){
                    document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                    document.getElementsByClassName("tabs2")[0].className += " active";
                    document.getElementsByClassName("tabcontent")[0].style.display = "block";;
                    document.getElementsByClassName("tabs")[0].className += " active";
                }, 500);
            }
            if($scope.tabNames.length < 4){
                $scope.isNewTab = true;
            }
        }

        $scope.chooseType = function(id, url) {
            $scope.tabNames[id].type.url = url;
        }


        $scope.addColors = function(id, colorMain, colorSecond){
            var cyrrentEl =document.getElementById(id);
            cyrrentEl.style.background = "#"+colorMain;
            if(angular.element(document.getElementById(id)).hasClass('active')){
                cyrrentEl.style.background = "#"+colorSecond;
            }
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }


})();
