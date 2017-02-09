(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('CardletListController', CardletListController);

    CardletListController.$inject = ['$scope', '$state', 'CardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location'];

    function CardletListController ($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;

        $scope.tabNames ={
            "cardletName": "cardlet",
            "tabs":
                [{
                    "name": "card",
                    'position': 0,
                    "display": "block",
                    "tabType": 1,
                    "layout":{
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"

                    }
                },
                    {
                        "name": "portfolio",
                        "position": 1,
                        "tabType": 2,
                        "layout": {
                            "mainColor": "FFFFFF",
                            "secondaryColor": "4BABE2"
                        },
                        "items": [
                            {
                                //"name": "1 item",
                                "index": "2",
                                "position": "0",
                                "image": "/app/cardlets/img/portfolio_img_01.png",
                                "image2": "/app/cardlets/img/portfolio_img_02.png",
                                "image3": "/app/cardlets/img/portfolio_img_03.png"

                            }
                        ]
                    }
                ]
        };

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


        $scope.openCity = function(cityName, tabId, cardName, cardId) {
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

            document.getElementById(cityName).style.display = "block";
            document.getElementById(tabId).className += " active";

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
            for (i = 0; i < tabs2.length; i++) {
                tabs2[i].className = tabs2[i].className.replace(" active", "");

            }
            document.getElementById(cardName).style.display = "block";
            document.getElementById(cardId).className += " active";
        }

        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {
                    console.log(response);
                    $scope.signatures = response;
                });
        }


        $scope.userCard= function(){
            $http.get("/api/userCardlets")
                .success(function(response, status, headers) {
                    console.log(response);
                    $scope.signatures = response;
                });
        }

        $scope.userCard();




        $scope.getTabTypes = function(){
            $http.get("/api/tab-types-by-type/1")
                .success(function(response, status, headers) {
                    console.log(response);
                    $scope.tabTypes = response;
                    $scope.tabNames.tabs[0].layout.tabId = $scope.tabTypes[0].id;
                    $scope.tabNames.tabs[0].layout.url = $scope.tabTypes[0].path;

                });
        }

        $scope.getItemTypes = function(){
            $http.get("/api/tab-types-by-type/2")
                .success(function(response, status, headers) {
                    console.log(response);
                    $scope.itemTypes = response;
                    $scope.tabNames.tabs[1].layout.tabId = $scope.itemTypes[0].id;
                    $scope.tabNames.tabs[1].layout.url = $scope.itemTypes[0].path;
                    console.log($scope.tabNames)
                });
        }

        $scope.getItemTypes();
        $scope.getTabTypes();

        $scope.isNewTab = true;




        angular.element(document).ready(function () {
            document.getElementsByClassName("tabcontent2")[0].style.display = "block";
            document.getElementsByClassName("tabs2")[0].className += " active";
            document.getElementsByClassName("tabcontent")[0].style.display = "block";;
            document.getElementsByClassName("tabs")[0].className += " active";
        });


        $scope.addItem = function() {
            if ($scope.tabNames.tabs.length <= 3) {
                var newTab = {"name":"Item"+$scope.tabNames.tabs.length,
                    "position": $scope.tabNames.tabs.length,
                    "tabType": 2,
                    "layout":{
                        "tabId": $scope.itemTypes[0].id,
                        "url": $scope.itemTypes[0].path,
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"
                    },
                    "items": [
                        {
                            //"name": "1 item",
                            "index": "2",
                            "position": "0",
                            "image": "/app/cardlets/img/portfolio_img_01.png",
                            "image2": "/app/cardlets/img/portfolio_img_02.png",
                            "image3": "/app/cardlets/img/portfolio_img_03.png"
                        }
                    ]

                }
                $scope.tabNames.tabs.push(newTab);
            }
            if($scope.tabNames.tabs.length == 4){
                $scope.isNewTab = false;
            }
        }

        $scope.addItems = function(tabId, index) {
            if($scope.tabNames.tabs[tabId].items.length <= 9){
                $scope.isDeleteItem = true;
                var newItem =  {
                    //"name":  ($scope.tabNames.tabs[tabId].items.length+1)+" item",
                    "index": index+2,
                    "position": $scope.tabNames.tabs[tabId].items.length,
                    "image": "/app/cardlets/img/portfolio_img_01.png",
                    "image2": "/app/cardlets/img/portfolio_img_02.png",
                    "image3": "/app/cardlets/img/portfolio_img_03.png",
                }
                $scope.tabNames.tabs[tabId].items.push(newItem);

                $scope.slides2 = $scope.tabNames.tabs[tabId].items;

            }
        }


        $scope.addTab = function() {
            if ($scope.tabNames.tabs.length <= 3) {
                var newTab = {"name":"card"+$scope.tabNames.tabs.length,
                    "position": $scope.tabNames.tabs.length,
                    "tabType": 1,
                    "layout":{
                        "tabId": $scope.tabTypes[0].id,
                        "url": $scope.tabTypes[0].path,
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"


                    }

                }
                $scope.tabNames.tabs.push(newTab);

            }
            if($scope.tabNames.tabs.length == 4){
                $scope.isNewTab = false;
            }
        }

        $scope.isDeleteItem = false;

        $scope.deleteItems = function(tabId, index){
            if($scope.tabNames.tabs[tabId].items.length > 1){
                console.log(index)
                $scope.tabNames.tabs[tabId].items.splice((index-2), 1);
                for (var i = 0; i < $scope.tabNames.tabs[tabId].items.length; i++) {
                    $scope.tabNames.tabs[tabId].items[i].index = i + 2;
                    $scope.tabNames.tabs[tabId].items[i].position = i;
                }
            }
            if($scope.tabNames.tabs[tabId].items.length === 1){
                $scope.isDeleteItem = false;
            }
        }

        $scope.accordionActive = 1;

        $scope.changeAccordionActivity = function(id){
            $scope.accordionActive = id;
        }


        $scope.positionCheck = function(){


            for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                $scope.tabNames.tabs[i].position = i;
            }

            setTimeout(function(){

                setTimeout(function(){
                    var tabs2 = document.getElementsByClassName("tabs2");
                    var tabs = document.getElementsByClassName("tabs");

                    for (var i = 0; i < tabs.length; i++) {
                        if(angular.element(tabs[i]).hasClass('active')){
                            document.getElementsByClassName("tabcontent")[i].style.display = "none";;
                            tabs[i].className = tabs[i].className.replace(" active", "");
                        }

                    }

                    for (var i = 0; i < tabs2.length; i++) {
                        if(angular.element(tabs2[i]).hasClass('active')){
                            document.getElementsByClassName("tabcontent2")[i].style.display = "none";;
                            tabs2[i].className = tabs2[i].className.replace(" active", "");
                        }

                    }

                    document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                    document.getElementsByClassName("tabs2")[0].className += " active";
                    document.getElementsByClassName("tabcontent")[0].style.display = "block";;
                    document.getElementsByClassName("tabs")[0].className += " active";
                }, 500);

                document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                document.getElementsByClassName("tabs2")[0].className += " active";
                document.getElementsByClassName("tabcontent")[0].style.display = "block";;
                document.getElementsByClassName("tabs")[0].className += " active";
            }, 500);
        }



        $scope.removeTab = function(index) {

            if($scope.tabNames.tabs.length >1) {
                console.log(index)
                $scope.tabNames.tabs.splice(index, 1);
                for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                    $scope.tabNames.tabs[i].position = i;
                }

                setTimeout(function(){

                    var tabs2 = document.getElementsByClassName("tabs2");
                    var tabs = document.getElementsByClassName("tabs");

                    for (var i = 0; i < tabs.length; i++) {
                        if(angular.element(tabs[i]).hasClass('active')){
                            document.getElementsByClassName("tabcontent")[i].style.display = "none";;
                            tabs[i].className = tabs[i].className.replace(" active", "");
                        }

                    }

                    for (var i = 0; i < tabs2.length; i++) {
                        if(angular.element(tabs2[i]).hasClass('active')){
                            document.getElementsByClassName("tabcontent2")[i].style.display = "none";;
                            tabs2[i].className = tabs2[i].className.replace(" active", "");
                        }

                    }
                    document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                    document.getElementsByClassName("tabs2")[0].className += " active";
                    document.getElementsByClassName("tabcontent")[0].style.display = "block";;
                    document.getElementsByClassName("tabs")[0].className += " active";
                }, 500);
            }
            if($scope.tabNames.tabs.length < 4){
                $scope.isNewTab = true;
            }
        }

        $scope.chooseType = function(id, url, tabId) {
            console.log(tabId)
            console.log(url)
            $scope.tabNames.tabs[id].layout.url = url;
            $scope.tabNames.tabs[id].layout.tabId = tabId;

        }


        $scope.addColors = function(id, colorMain, colorSecond){

            var cyrrentEl = document.getElementById(id);
            if(cyrrentEl) {
                cyrrentEl.style.background = "#" + colorMain;
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    cyrrentEl.style.background = "#" + colorSecond;
                }
            }
            if($scope.tabNames.tabs.length ===2){
                cyrrentEl.style.width = "268px"
            }
            if($scope.tabNames.tabs.length ===3){
                cyrrentEl.style.width = "177px"
            }
            if($scope.tabNames.tabs.length ===4){
                cyrrentEl.style.width = "131px"
            }
        }

        $scope.myInterval = 3000;


        $scope.saveCardlet = function(){
            console.log($scope.tabNames)
            $http.post("/api/cardlet",  $scope.tabNames)
                .success(function (data, status, headers, config) {
                    $location.path('/user-cardlets')
                });
        }
    }


})();
