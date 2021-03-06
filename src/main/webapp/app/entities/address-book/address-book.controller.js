(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('AddressBookController', AddressBookController);

    AddressBookController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService',  'paginationConstants', '$http', '$timeout', '$uibModal', '$location', '$window', '$rootScope'];

    function AddressBookController ($scope, $state, CardletList, ParseLinks, AlertService,  paginationcardletConstants ,$http, $timeout, $uibModal, $location, $window, $rootScope) {
        var vm = this;

        vm.currentSlide = null;
        vm.showCarousel = true;

        $scope.showSpinner = true;

        $scope.time = Date.now()
        vm.currentSlide = 0;
        $scope.showConent = true;
        $scope.showLink = true;
        $scope.hasChanged = function(id) {
            $scope.instructions = id.field;
        }

        $scope.openCard = function(cardName, tabId, id) {

            var i, tabcontent2, tablinks2, tabs2;
            tabcontent2 = document.getElementsByClassName("tabcontent2"+id);
            for (i = 0; i < tabcontent2.length; i++) {
                tabcontent2[i].style.display = "none";
            }
            tablinks2 = document.getElementsByClassName("tablinks2"+id);
            for (i = 0; i < tablinks2.length; i++) {
                tablinks2[i].className = tablinks2[i].className.replace(" active", "");

            }
            tabs2 = document.getElementsByClassName("tabs2"+id);
            for (i = 0; i < tabs2.length; i++) {
                tabs2[i].className = tabs2[i].className.replace(" active", "");

            }

            document.getElementById(cardName).style.display = "block";
            document.getElementById(tabId).className += " active";
        }

        $scope.impersonate = function(){
            if(window.localStorage['impersonate'] === 'true'){
                $http.get("/api/logout/impersonate")
                    .success(function (response, status, headers) {
                        localStorage.setItem("impersonate", false);
                        $window.location.reload()
                    })
            }

        }

        $scope.gerPreviewLink = function(id){
            var link =  "mailto:?subject=Link to the Sigterra Profile Page.&body=Hi,%0D%0A %0D%0A Take a look at this Sigterra Profile Page: "+ ($location.protocol() + '://' + $location.host() + ':' + $location.port() + '/%23/previewCardlet?cardletId=' +  window.btoa(id))
            return link.toString();
        }

        $scope.showImpersonate = window.localStorage['impersonate'];
        $scope.userCard = function(){
            $http.get("/api/user-address-books")
                .success(function(response, status, headers) {
                    $scope.userCardlets = response;

                });

            $scope.showSpinner = false;
        };

        $scope.searchText = "";

        $scope.tabNames ={};

        $scope.userCard();

        $scope.isShowModal = false;

        $scope.closeDialog = function(){
            $scope.showDelteCardletDialog = false;
        };

        $scope.isShowMailClientWindow = false;

        $scope.isAddIcons = false;

        $scope.openModal= function(id, syncData) {
            $scope.cardletLink = '<iframe style="width: 555px; height: 280px;border: 0px!important" src="'+$location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/copyToWeb?cardletId='+ id +'"></iframe>'
            $scope.isShowModal = true;
        };

        $scope.delteCardlet = function(id){
            $scope.showDelteCardletDialog = false;
            $http.delete("/api/address-books/"+id)
                .success(function(response, status, headers) {
                    $scope.userCardlets = response;
                });
        };

        $scope.showDelteCardletDialog = false;
        $scope.delteCardletDialog = function(id){
            $scope.showDelteCardletDialog = true;
            $scope.delteCardletId = id;
        };


        $scope.accordionActive = 1;

        $scope.changeAccordionActivity = function(id){
            $scope.accordionActive = id;
        };

        $scope.autoExpand = function(e) {
            var element = typeof e === 'object' ? e.target : document.getElementById(e);
            var scrollHeight = element.scrollHeight ; // replace 60 by the sum of padding-top and padding-bottom
            element.style.height =  scrollHeight + "px";
        };


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
        };

        $scope.chooseType = function(id, url, tabId) {
            $scope.tabNames.tabs[id].layout.url = url;
            $scope.tabNames.tabs[id].layout.tabId = tabId;

        }

        $scope.getUrl = function(url){
            if(url === "app/cardlets/item.html"){
                return "app/cardlets/item-editor.html"
            }else  if(url === "app/cardlets/item2.html"){
                return "app/cardlets/item2-editor.html"
            }else if(url === "app/cardlets/item3.html"){
                return "app/cardlets/item3-editor.html"
            }else if(url === "app/cardlets/info.html"){
                return "app/cardlets/info-editor.html"
            }else if(url === "app/cardlets/info2.html"){
                return "app/cardlets/info2-editor.html"
            }else if(url === "app/cardlets/info3.html"){
                return "app/cardlets/info3-editor.html"
            }else{

                return url;
            }
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

        $scope.addColors = function(id, colorMain, colorSecond, index, linkId){

            var cyrrentEl = document.getElementById(id);
            var link = document.getElementById(linkId);
            if(cyrrentEl) {

                for (var i = 0; i < $scope.userCardlets.length; i++) {
                    if($scope.userCardlets[i].id === index){
                        if ($scope.tabNames) {
                            if ($scope.userCardlets[i].tabs.length == 1) {
                                cyrrentEl.style.width = "538px";
                                link.style.width = "535px";
                                link.style.maxWidth = "535px";
                            }

                            if ($scope.userCardlets[i].tabs.length == 2) {
                                cyrrentEl.style.width = "268px";
                                link.style.width = "265px";
                                link.style.maxWidth = "265px";
                            }
                            if ($scope.userCardlets[i].tabs.length == 3) {
                                cyrrentEl.style.width = "178px";
                                link.style.width = "175px";
                                link.style.maxWidth = "175px";
                            }
                            if ($scope.userCardlets[i].tabs.length == 4) {
                                cyrrentEl.style.width = "133px";
                                link.style.width = "130px";
                                link.style.maxWidth = "130px";
                            }
                        }
                    }
                }


            }
        }

        $scope.myInterval = 0;

    }
})();
