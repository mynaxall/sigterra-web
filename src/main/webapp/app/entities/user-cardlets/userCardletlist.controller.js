(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('UserCardletListController', UserCardletListController);



    UserCardletListController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService',  'paginationConstants', '$http', '$timeout', '$uibModal', '$location'];

    function UserCardletListController ($scope, $state, CardletList, ParseLinks, AlertService,  paginationcardletConstants ,$http, $timeout, $uibModal, $location) {
        var vm = this;


        $scope.saveBanner = function(){
            console.log($scope.banner )
            $scope.showCropDialog = false
        }

        $scope.closeCropDialog = function(){
            $scope.showCropDialog = false;
        }

        $scope.showCropDialog = false;
        $scope.myImage ='';
        $scope.myCroppedImage = '';

        $scope.handleFileSelect=function(evt) {
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

        vm.loadPage = loadPage;
        vm.transition = transition;

        $scope.tabNames ={
            "cardletName": "catdlet",
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

            document.getElementById(cityName).style.display = "block";
            document.getElementById(tabId).className += " active";
        }

        $scope.isCopyTiEmail = false;
        $scope.isAddBanner = false;

        $scope.addBanner = function(){
            $scope.isAddBanner = true;
        }

        $scope.banner =""
        $scope.copyToEmail = function(id, cardId, sigId) {

            $scope.isCopyTiEmail = true;
            $scope.segnatureId = id;
            $scope.userCardlets[id].tabs;
            $scope.banner = '/app/cardlets/img/banner.png'
            for (var i = 0; i < $scope.userCardlets[id].tabs.length; i++) {
                if ($scope.userCardlets[id].tabs[i].tabType = 1){
                    $scope.firstBusinessCardId = i;
                break
                }
            }
            console.log(sigId)
            $scope.signatureLink = $location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/previewCardlet?cardletId='+ sigId.toString();
            $scope.element = $("#"+cardId); // global variable
            $scope.getCanvas;
            html2canvas($scope.element, {
                useCORS: true,
                onrendered: function (canvas) {
                    $("#previewImage").append(canvas);
                    $scope.getCanvas = canvas;
                }
            });
        };

        $scope.convertImage = function(image){
            $scope.encoded = $base64.encode(image);
            console.log($scope.encoded)
            return $scope.encoded;
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

        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {
                    $scope.signatures = response;
                });
        }
        $scope.copText = function(){
            var urlField = document.getElementById('copyedText');
            // select the contentscopyToEmail
            copyToClipboard('bobo')
        }


        $scope.userCard= function(){
            $http.get("/api/userCardlets")
                .success(function(response, status, headers) {
                    $scope.userCardlets = response;

                });
        }

        $scope.userCard();

        $scope.copyToClipboard = function(id) {
           var code =  '<iframe style="width: 555px; height: 280px;border: 0px!important" src="http://localhost:8080/#/copyToWeb?cardletId='+ id +'"></iframe>';
            window.prompt("Copy to clipboard: Ctrl+C, Enter", code);
        }

         $scope.showImageDialogBanner = function(){
             $scope.showCropDialog = true;
        };




        $scope.isShowModal = false;

        $scope.closeDialog = function(){
            $scope.isShowModal = false;
            $scope.isCopyTiEmail = false;
            $scope.showBannerDialog = false;
            $scope.isShowMailClientWindow = false;
            $scope.isAddIcons = false;

            $("canvas").remove();
        };


        $scope.testimg2= function() {
            var imgageData =  $scope.getCanvas.toDataURL("image/png");
            console.log(imgageData)

            // Now browser starts downloading it instead of just showing it
            $scope.newData = imgageData.replace(/^data:image\/png/, "data:application/octet-stream");
            $("#btn-Convert-Html2Image").attr("download", "your_pic_name.png").attr("href", newData);
        };



        $scope.coptToEmailText;
        $scope.isShowMailClientWindow = false;
        $scope.sigImage0 = "asdasd"

        $scope.isAddIcons = false;

        $scope.selectSignature = function(id){
            $scope.isAddBanner = false;
            $scope.isCopyTiEmail = false;
            if(id == 1){
                $scope.isShowMailClientWindow = true;
                var imgageData =  $scope.getCanvas.toDataURL("image/png");
                // Now browser starts downloading it instead of just showing it
                $scope.newData = imgageData.replace(/^data:image\/png/, "data:application/octet-stream");
                $scope.coptToEmailText = '<a href="'+$scope.signatureLink+'"> <img style="text-transform: scale(0.59)" src="'+imgageData+'"></a>'
                    console.log(imgageData)
            }
            else if(id == 2){
                $scope.isAddIcons = true;
                var urlField = document.getElementById('secondSignature');
                $scope.coptToEmailText = urlField.outerHTML;
            }else if(id == 3){
                $scope.isShowMailClientWindow = true;
                var urlField = document.getElementById('thirdSignature');
                $scope.coptToEmailText = urlField.outerHTML;
            }else if(id = 4){
                $scope.isShowMailClientWindow = true;
                $scope.isAddIcons = false;
                var urlField = document.getElementById('secondSignature');
                $scope.coptToEmailText = urlField.outerHTML;
            }
        };


        $scope.testimg = function(){
            $scope.element = $("#cardlet-signature"); // global variable
            $scope.getCanvas;
            html2canvas($scope.element, {
                useCORS: true,
                onrendered: function (canvas) {
                    $("#previewImage").append(canvas);
                    $scope.getCanvas = canvas;
                }
            });
        };


         $scope.openModal= function(id, syncData) {
             $scope.cardletLink = '<iframe style="width: 555px; height: 280px;border: 0px!important" src="'+$location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/copyToWeb?cardletId='+ id +'"></iframe>'
             $scope.isShowModal = true;
        };

        /* @ngInject */


        $scope.getTabTypes = function(){
            $http.get("/api/tab-types-by-type/1")
                .success(function(response, status, headers) {
                    $scope.tabTypes = response;
                    $scope.tabNames.tabs[0].layout.tabId = $scope.tabTypes[0].id;
                    $scope.tabNames.tabs[0].layout.url = $scope.tabTypes[0].path;

                });
        };

        $scope.getItemTypes = function(){
            $http.get("/api/tab-types-by-type/2")
                .success(function(response, status, headers) {
                    $scope.itemTypes = response;
                    $scope.tabNames.tabs[1].layout.tabId = $scope.itemTypes[0].id;
                    $scope.tabNames.tabs[1].layout.url = $scope.itemTypes[0].path;
                });
        }

        $scope.delteCardlet = function(id){
            $http.get("/api/cardletDelete/"+id)
                .success(function(response, status, headers) {
                    $scope.userCardlets = response;
                });
        }

        $scope.getItemTypes();
        $scope.getTabTypes();

        $scope.isNewTab = true;





        $scope.addItem = function() {
            if ($scope.tabNames.tabs.length <= 3) {
                var newTab = {"name":"Item"+$scope.tabNames.tabs.length,
                    "position": $scope.tabNames.tabs.length,
                    "tabType": 2,
                    "layout":{
                        "id": $scope.itemTypes[0].tabId,
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
                $scope.tabNames.tabs[tabId].items.splice((index-2), 1);
                for (var i = 0; i < $scope.tabNames.tabs[tabId].items.length; i++) {
                    $scope.tabNames.tabs[tabId].items[i].index = i + 2;
                    $scope.tabNames.tabs[tabId].items[i].name  =  (i+1)+" item";
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

        $scope.showBannerDialog = false;

        $scope.removeTab = function(index) {

            if($scope.tabNames.tabs.length >1) {
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
            $scope.tabNames.tabs[id].layout.url = url;
            $scope.tabNames.tabs[id].layout.tabId = tabId;

        }


        $scope.addColors = function(id, colorMain, colorSecond, index){
            var cyrrentEl = document.getElementById(id);
            if(cyrrentEl) {
                cyrrentEl.style.background = "#" + colorMain;
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    cyrrentEl.style.background = "#" + colorSecond;
                }
            }
            if($scope.userCardlets[index].tabs.length <= 2){
                cyrrentEl.style.width = "268px"
            }
            if($scope.userCardlets[index].tabs.length == 3){
                cyrrentEl.style.width = "177px"
            }
            if($scope.userCardlets[index].tabs.length == 4){
                cyrrentEl.style.width = "131px"
            }
        }

        $scope.myInterval = 3000;


        $scope.saveCardlet = function(){
            $http.post("/api/cardlet",  $scope.tabNames)
                .success(function (data, status, headers, config) {

                });
        }
    }


})();
