(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('PreviewCardletController', PreviewCardletController);



    PreviewCardletController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location', 'LoginService', '$sce'];

    function PreviewCardletController ($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location, LoginService, $sce) {

        var vm = this;
        $scope.time = Date.now()

        vm.currentSlide = 0;
        vm.showCarousel = true;
        $scope.showLink = true;

        $scope.getCardlet = function(){
            var param1 = $location.search().cardletId;
            $http.get("/api/cardlet/"+param1)
                .success(function(response, status, headers) {
                    $scope.tabNames = response;


                    if($scope.tabNames.tabs[0].tabType == 1){
                        vm.tabType = 1;
                        $scope.currentName = $scope.tabNames.tabs[0].name;
                        if($scope.tabNames.tabs[0].site.value) {
                            $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[0].site.value);
                            $scope.currentLink = $scope.getLink($scope.tabNames.tabs[0].site.value);
                            vm.showSpinner = true;
                        }
                    }
                    else{
                        vm.tabType = $scope.tabNames.tabs[0].tabType

                        angular.forEach($scope.tabNames.tabs[0].items, function (item) {

                            if(item.position  === 0){
                                if(item.link) {
                                    $scope.currentUrl = $scope.createURL(item.link);
                                    $scope.currentLink = $scope.getLink(item.link);
                                    vm.showSpinner = true;
                                }
                                $scope.currentName = item.name.value;
                            }

                        });
                    }
                    for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                        if ($scope.tabNames.tabs[i].tabType == '1'){
                            $scope.firstBusinessCardId = i;
                            break
                        }else{
                            $scope.firstBusinessCardId = '';
                        }
                    }

                    $timeout(function() {vm.showSpinner = false; },4000)
                });
        };

        $scope.successfulyAdded = "";
        $scope.errorAddeding = "";

        $scope.addToAddressBook = function(){
            var param1 = $location.search().cardletId;
            $http.post("/api/address-book/"+param1)
                .success(function(response, status, headers) {

                    $scope.successfulyAdded = response.message;
                    $timeout(function() {
                        $scope.successfulyAdded = "";
                    }, 3000);
                })
                .error(function(response, status, headers) {
                    $scope.errorAddeding = response.message;
                    $timeout(function() {
                        $scope.errorAddeding = "";
                    }, 3000);

                });
        }

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };

        $scope.createURL = function(link){
            if(link.indexOf('https://') == -1 && link.indexOf('http://') == -1){
               link = "http://"+link
            }else if(link.indexOf('https://') != -1){
                link.replace("https://", "http://")

            }
            return link
        }


        vm.nextIndex = 1;

        $scope.prevSlide = function(index){
            $scope.currentUrl = undefined;
            if( vm.currentSlide == 0){
                vm.currentSlide = $scope.tabNames.tabs[index].items.length -1;
                vm.nextIndex = 0
            }else{
                vm.currentSlide = vm.currentSlide - 1;
                vm.nextIndex = vm.currentSlide+ 1
            }


                $timeout(function() {

                angular.forEach($scope.tabNames.tabs[index].items, function (item) {
                    if(item.position  === vm.currentSlide) {
                        $scope.currentUrl = "";
                        $scope.currentLink = "";
                        $scope.currentName = "";
                        if (item.link) {
                            $scope.currentUrl = $scope.createURL(item.link);
                            $scope.currentLink = $scope.getLink(item.link);
                        }
                        if (item.link) {
                            $scope.currentName = item.name.value;
                            vm.showSpinner = true;
                        }

                    }

                });

            },10);
            $timeout(function() {vm.showSpinner = false; },4000)
        };

        $scope.setCurrentSlide = function(index, parent){
            vm.currentSlide = index;
            $timeout(function() {

                angular.forEach($scope.tabNames.tabs[parent].items, function (item) {
                    if(item.position  === vm.currentSlide) {
                        $scope.currentUrl = "";
                        $scope.currentLink = "";
                        $scope.currentName = "";
                        if (item.link) {
                            $scope.currentUrl = $scope.createURL(item.link);
                            $scope.currentLink = $scope.getLink(item.link);
                        }
                        if (item.link) {
                            $scope.currentName = item.name.value;
                            vm.showSpinner = true;
                        }

                    }

                });

            },10);
            $timeout(function() {vm.showSpinner = false; },4000)
        }

        $scope.itemPosition = function(index){
            return index + 1;
        }

        $scope.nextSlide = function(index){
            $scope.currentUrl = undefined
            if( vm.currentSlide == $scope.tabNames.tabs[index].items.length -1){
                vm.currentSlide = 0;
            }else{
                vm.currentSlide = vm.currentSlide + 1;

            }

            vm.nextIndex = vm.currentSlide + 1;
            if(vm.nextIndex == $scope.tabNames.tabs[index].items.length ){
                vm.nextIndex = 0;
            }


            $timeout(function() {
                angular.forEach($scope.tabNames.tabs[index].items, function (item) {

                    if(item.position  === vm.currentSlide){
                        $scope.currentUrl = "";
                        $scope.currentLink = "";
                        $scope.currentName = "";
                        if(item.link){
                        $scope.currentUrl = $scope.createURL(item.link);
                        $scope.currentLink = $scope.getLink(item.link);
                        }
                        if(item.link){
                            $scope.currentName = item.name.value;
                            vm.showSpinner = true;
                        }
                    }

                });
            },10)
            $timeout(function() {vm.showSpinner = false; },4000)
        };

        $scope.getLink = function(link){
            var linkNew = link;
            if(link.indexOf('https://') != -1){
                linkNew = link.replace("https://", "");
            }else if(link.indexOf('http://') != -1){
                linkNew = link.replace("http://", "")
            }
            return linkNew;

        }

        vm.showSpinner = false;

        vm.hideFrame = false;

        $scope.closeFrame = function(){
            document.getElementById("myFrame").style.height = "800px"
        }

        $scope.sethide = function(){
            vm.hideFrame = false;
        }



        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        $scope.getFonts = function(bold, italic, underline){
            var textDecoration = "normal";

            var fontFamily = "Roboto-Regular";

            if(bold && italic){
                fontFamily = "Roboto-Bold-Italic";
            }
            else if(italic){
                fontFamily = "Roboto-Italic";
            }
            else if(bold){
                fontFamily = "Roboto-Bold";
            }

            if(underline){
                textDecoration = "underline";
            }

            return  {
                "font-family" : fontFamily,
                "text-decoration" : textDecoration,

            }
        }


        $scope.openCity = function(cardName, cardId, id) {
            $scope.currentUrl = "1";
            $scope.currentLink = "1";
            $scope.currentUrl = undefined;
            vm.currentSlide = 0;

            if ($scope.tabNames.tabs[id].tabType === 1) {
                vm.tabType = 1;
            }else{
                vm.tabType = 2;
            }
            vm.showSpinner = true;
            vm.nextIndex = 1;

            setTimeout(function() {
                if ($scope.tabNames.tabs[id].tabType === 1) {
                    vm.tabType = 1;
                    $scope.currentName = $scope.tabNames.tabs[id].name;
                    if($scope.tabNames.tabs[id].site.value) {
                        $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[id].site.value);
                        $scope.currentLink = $scope.getLink($scope.tabNames.tabs[id].site.value);

                        vm.showSpinner = true;
                    }else{
                        vm.showSpinner = false;
                        $scope.currentUrl = "";
                        $scope.currentLink = "";

                    }

                }
                else {
                    vm.tabType = 2;
                    vm.tabType = $scope.tabNames.tabs[id].tabType
                    angular.forEach($scope.tabNames.tabs[id].items, function (item) {

                        if(item.position  === 0){
                            if(item.name) {
                                $scope.currentName = item.name.value;
                            }else{
                                $scope.currentName  = ""
                            }
                            if(item.link) {
                                $scope.currentUrl = $scope.createURL(item.link);
                                $scope.currentLink = $scope.getLink(item.link);
                                vm.showSpinner = true;
                            }else{
                                vm.showSpinner = false;
                                $scope.currentUrl = "";
                                $scope.currentLink = "";

                            }

                        }

                    });
                }

            },500)

            $timeout(function() {vm.showSpinner = false; },4000)



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

        };


        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {
                    $scope.signatures = response;
                });
        };



        angular.element(document).ready(function () {
            document.getElementsByClassName("tabcontent2")[0].style.display = "block";
            document.getElementsByClassName("tabs2")[0].className += " active";
        });


        $scope.reloadFrame = function(){
            var url = $scope.currentUrl;
            $scope.currentUrl = null;
            vm.showSpinner = true;
            $timeout(function() {
                $scope.currentUrl = url
            },10)
            $timeout(function() {vm.showSpinner = false; },4000)

        }



        $scope.addColors = function(id, colorMain, colorSecond, linkId){

            var currentEl = document.getElementById(id);
            var link = document.getElementById(linkId);
            if(currentEl) {
                currentEl.style.background = "#F9F9F9";
                currentEl.style.borderTop = "1px solid #D0D8D9"
                currentEl.style.borderBottom = "1px solid #D0D8D9";
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    currentEl.style.background = "#FFFFFF";
                    currentEl.style.borderTop = "2px solid #" + colorSecond;
                    currentEl.style.borderBottom = "0px";
                }

                if ($scope.tabNames) {

                    if ($scope.tabNames.tabs.length == 1) {
                        currentEl.style.width = "540px";
                        link.style.width = "535px";
                        link.style.maxWidth = "535px";
                    }
                    if ($scope.tabNames.tabs.length === 2) {
                        currentEl.style.width = "270px";
                        link.style.width = "265px";
                        link.style.maxWidth = "265px";
                    }
                    if ($scope.tabNames.tabs.length === 3) {
                        currentEl.style.width = "180px";
                        link.style.width = "175px";
                        link.style.maxWidth = "175px";
                    }
                    if ($scope.tabNames.tabs.length === 4) {
                        currentEl.style.width = "135px";
                        link.style.width = "130px";
                        link.style.maxWidth = "130px";
                    }
                }
            }
        };


    }


})();
