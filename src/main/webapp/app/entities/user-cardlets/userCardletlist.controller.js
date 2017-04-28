(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('UserCardletListController', UserCardletListController);



    UserCardletListController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService',  'paginationConstants', '$http', '$timeout', '$uibModal', '$location', '$window', '$rootScope'];

    function UserCardletListController ($scope, $state, CardletList, ParseLinks, AlertService,  paginationcardletConstants ,$http, $timeout, $uibModal, $location, $window, $rootScope) {
        var vm = this;


        $scope.fieldTable = [{
            field: "1",
            title: "Gmail"
        }, {
            field: "2",
            title: "Outlook"
        }, {
            field: "3",
            title: "Airmal"
        }, {
            field: "4",
            title: "Yahoo"
        }];

        $scope.selected = $scope.fieldTable[0];

        $scope.hasChanged = function() {
            $scope.instructions =$scope.selected.field;
        }
        $scope.closeCropDialog = function(){
            $scope.showCropDialog = false;
            $scope.showCropDialogTabs = false;
            $scope.banner = "";
        }
        $scope.showCropDialog = false;
        $scope.showCropDialogTabs = false;
        $scope.myImage ='';
        $scope.myCroppedImage = '';
        $scope.myImage2 ='';

        var handleFileSelect=function(evt) {
            var file=evt.currentTarget.files[0];
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage=evt.target.result;
                });
            };
            reader.readAsDataURL(file);
        };
        angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

        $scope.handleFileSelect=function(evt) {
            var file=evt.currentTarget.files[0];
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage=evt.target.result;
                });
            };
            reader.readAsDataURL(file);

        };

        $scope.saveBanner = function(name, image64, isFile){
            $scope.showSpinner = true;
            if (isFile) {
                var fd = new FormData();
                fd.append('file', image64);
            } else {
                var img_b64 = image64;
                var png = img_b64.split(',')[1];
                var file = b64toBlob(png, 'image/png')
                var fd = new FormData();
                fd.append('file', file);
            }


            $http.post("/api/signature/upload/icon/" + $scope.userCardlets[$scope.segnatureId].id + "/" + name, fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(function (data, status, headers, config) {
                    $scope.bannerImageUrl = data.url;
                    $scope.showCropDialog = false;
                    $scope.showCropDialogTabs = false;
                    $scope.banner = $scope.bannerImageUrl;
                    document.getElementById("gmailDiv").innerHTML = $scope.coptToEmailText

                    $scope.showSpinner = false;

                });

        }

        var fileSelected=function(evt) {
            var file=evt.currentTarget.files[0];
            $scope.saveBanner("banner", file, true);
        };
        angular.element(document.querySelector('#fileInput3')).on('change',fileSelected);


        $scope.fileSelected = function (evt) {
            var file=evt.currentTarget.files[0];
            $scope.saveImage("banner", file, true);
        };

        var handleFileSelect2=function(evt) {
            var file=evt.currentTarget.files[0];
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage2=evt.target.result;
                });
            };
            reader.readAsDataURL(file);
        };
        angular.element(document.querySelector('#fileInput2')).on('change',handleFileSelect2);

        $scope.handleFileSelect2=function(evt) {
            var file=evt.currentTarget.files[0];
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage2=evt.target.result;
                });
            };
            reader.readAsDataURL(file);

        };

        vm.loadPage = loadPage;
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

        $scope.instructions = 1;

        $scope.isCopyTiEmail = false;
        $scope.isAddBanner = false;

        $scope.addBanner = function(){

            $scope.selectSignature($scope.selectSignatureId)
        }

        $scope.banner ="";
        $scope.tabsImage = '';

        $scope.copyToEmail = function(id, cardId, sigId) {
            console.log($scope.userCardlets)
            console.log(id)
            console.log(cardId)
            console.log(sigId)

            $scope.isCopyTiEmail = true;
            $window.scrollTo(0, 0);
            $scope.segnatureId = id;
            $scope.banner = '/content/images/banner.png'
            for (var i = 0; i < $scope.userCardlets[id].tabs.length; i++) {
                if ($scope.userCardlets[id].tabs[i].tabType == '1'){
                    $scope.firstBusinessCardCopyed = $scope.userCardlets[id].tabs[i];
                    $scope.firstBusinessCardId = i;
                    console.log($scope.firstBusinessCardId)
                break
                }
            }
            $scope.signatureLink = $location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/previewCardlet?cardletId='+ sigId.toString();
            $scope.element = $("#"+cardId); // global variable
            $scope.getCanvas;
            html2canvas($scope.element, {
                useCORS: true,
                onrendered: function (canvas) {
                    $("#previewImage").append(canvas);
                    $scope.getCanvas = canvas;
                    $scope.selectSignature(1);
                }
            });
        };

        $scope.convertImage = function(image){
            $scope.encoded = $base64.encode(image);
            return $scope.encoded;
        };

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


        $scope.impersonate = function(){
            if(window.localStorage['impersonate'] === 'true'){
                $http.get("/api/logout/impersonate")
                    .success(function (response, status, headers) {
                        localStorage.setItem("impersonate", false);
                        $window.location.reload()
                    })
            }

        }

        $scope.showImpersonate = window.localStorage['impersonate'];
        $scope.userCard = function(){
            $http.get("/api/userCardlets")
                .success(function(response, status, headers) {
                    $scope.userCardlets = response;
                    if($scope.userCardlets.length === 0){
                        $scope.saveCardlet();

                    }
                });
        };

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

        $scope.getUserProfile = function(){
            $http.get("/api/account")
                .success(function(response, status, headers) {

                    $scope.userAccount = response;
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
                        $scope.tabNames.tabs[0].photo = $location.protocol() + '://' + $location.host() + ':' + $location.port()+"content/images/avatar_img.png"
                    }


                });
            $scope.userCard();
        }
        $scope.getUserProfile();


        $scope.copyToClipboard = function(id) {
           var code =  '<iframe style="width: 555px; height: 280px;border: 0px!important" src="http://localhost:8080/#/copyToWeb?cardletId='+ id +'"></iframe>';
            window.prompt("Copy to clipboard: Ctrl+C, Enter", code);
        };

         $scope.showImageDialogBanner = function(){
             $scope.showCropDialog = true;
        };

        $scope.showCropTabs = function(index){
            $scope.showCropDialogTabs = true;
            $scope.ImageTabIndex = index;
        };

        $scope.saveTabsImage = function(){
            $scope.saveImage($scope.ImageTabIndex, $scope.tabsImage)
        };


        $scope.isShowModal = false;

        $scope.closeDialog = function(){
            $scope.isShowModal = false;
            $scope.isCopyTiEmail = false;
            $scope.showBannerDialog = false;
            $scope.isShowMailClientWindow = false;
            $scope.isAddIcons = false;
            $scope.showDelteCardletDialog = false;

            $("canvas").remove();
        };


        $scope.testimg2= function() {
            var imgageData =  $scope.getCanvas.toDataURL("image/png");

            // Now browser starts downloading it instead of just showing it
            $scope.newData = imgageData.replace(/^data:image\/png/, "data:application/octet-stream");
            $("#btn-Convert-Html2Image").attr("download", "your_pic_name.png").attr("href", newData);
        };



        $scope.coptToEmailText;
        $scope.isShowMailClientWindow = false;


        $scope.isAddIcons = false;

        $scope.signatureSelected = 1;
        $scope.selectSignatureId;

        $scope.selectSignature = function(id){
            $scope.selectSignatureId = id;
            if(id == 1){
                $scope.signatureSelected = 1;
                var imgageData =  $scope.getCanvas.toDataURL("image/png");
                // Now browser starts downloading it instead of just showing it
                $scope.newData = imgageData.replace(/^data:image\/png/, "data:application/octet-stream");
                $scope.saveImage("signature",imgageData)

            }else if(id == 2){
                $scope.isAddIcons = true;
            }else if(id == 3){
                $scope.signatureSelected = 3;
                $window.scrollTo(0, 0);
                var urlField = document.getElementById('thirdSignature');
                $scope.coptToEmailText = urlField.outerHTML;
                document.getElementById("gmailDiv").innerHTML = $scope.coptToEmailText
            }else if(id = 4){
                $scope.isShowMailClientWindow = true;
                $window.scrollTo(0, 0);
                $scope.isAddIcons = false;
                var urlField = document.getElementById('secondSignaturePreview');
                $scope.coptToEmailText = urlField.outerHTML;
                document.getElementById("gmailDiv").innerHTML = $scope.coptToEmailText
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
        };

        $scope.delteCardlet = function(id){
            $scope.showDelteCardletDialog = false;
            $http.get("/api/cardletDelete/"+id)
                .success(function(response, status, headers) {
                    $scope.userCardlets = response;
                });
        };


        $scope.showDelteCardletDialog = false;
        $scope.delteCardletDialog = function(id){
            $scope.showDelteCardletDialog = true;
            $scope.delteCardletId = id;
        };

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
                            "image": "/content/images/portfolio_img_01.png",
                            "image2": "/content/images/portfolio_img_02.png",
                            "image3": "/content/images/portfolio_img_03.png"
                        }
                    ]

                };
                $scope.tabNames.tabs.push(newTab);
            }
            if($scope.tabNames.tabs.length == 4){
                $scope.isNewTab = false;
            }
        };

        $scope.addItems = function(tabId, index) {
            if($scope.tabNames.tabs[tabId].items.length <= 9){
                $scope.isDeleteItem = true;
                var newItem =  {
                    //"name":  ($scope.tabNames.tabs[tabId].items.length+1)+" item",
                    "index": index+2,
                    "position": $scope.tabNames.tabs[tabId].items.length,
                    "image": "/content/images/portfolio_img_01.png",
                    "image2": "/content/images/portfolio_img_02.png",
                    "image3": "/content/images/portfolio_img_03.png",
                };
                $scope.tabNames.tabs[tabId].items.push(newItem);

                $scope.slides2 = $scope.tabNames.tabs[tabId].items;

            }
        };


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

                };
                $scope.tabNames.tabs.push(newTab);

            }
            if($scope.tabNames.tabs.length == 4){
                $scope.isNewTab = false;
            }
        };

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
        };

        $scope.accordionActive = 1;

        $scope.changeAccordionActivity = function(id){
            $scope.accordionActive = id;
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
                cyrrentEl.style.background = "#F9F9F9";
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    cyrrentEl.style.background = "#FFFFFF" ;
                    cyrrentEl.style.borderTop = "2px solid #4BABE2"
                }
            }
            if($scope.userCardlets[index].tabs.length <= 2){
                cyrrentEl.style.width = "270px"
            }
            if($scope.userCardlets[index].tabs.length == 3){
                cyrrentEl.style.width = "180px"
            }
            if($scope.userCardlets[index].tabs.length == 4){
                cyrrentEl.style.width = "135px"
            }
        }

        $scope.myInterval = 3000;


        $scope.saveCardlet = function(){
            $http.post("/api/cardlet",  $scope.tabNames)
                .success(function (data, status, headers, config) {
                    $scope.userCard();
                });
        }


        function b64toBlob(b64Data, contentType, sliceSize) {
            contentType = contentType || '';
            sliceSize = sliceSize || 512;

            var byteCharacters = atob(b64Data);
            var byteArrays = [];

            for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                var slice = byteCharacters.slice(offset, offset + sliceSize);

                var byteNumbers = new Array(slice.length);
                for (var i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }

                var byteArray = new Uint8Array(byteNumbers);

                byteArrays.push(byteArray);
            }

            var blob = new Blob(byteArrays, {type: contentType});
            return blob;
        };


        $scope.saveImage = function(name, image64, isFile) {
            $scope.showSpinner = true;
            if (isFile) {
                var fd = new FormData();
                fd.append('file', image64);
            } else {
                var img_b64 = image64;
                var png = img_b64.split(',')[1];
                var file = b64toBlob(png, 'image/png')
                var fd = new FormData();
                fd.append('file', file);
            }


            if (!$scope.croppedImageUrl || $scope.croppedImageUrl === undefined) {

                $http.post("/api/signature/upload/icon/" + $scope.userCardlets[$scope.segnatureId].id + "/" + name, fd, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    })
                    .success(function (data, status, headers, config) {
                        $scope.croppedImageUrl = data.url;
                        $scope.showCropDialog = false;
                        $scope.showCropDialogTabs = false;
                        if (name == "signature") {
                            $window.scrollTo(0, 0);
                            $scope.coptToEmailText = '<a href="' + $scope.signatureLink + '"> <img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a>';
                            if ($scope.isAddBanner == true) {
                                $scope.coptToEmailText = '<div></div><a href="' + $scope.signatureLink + '"> <img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a></div><div><div style=\"max-height: 240px; overflow: hidden;text-transform: scale(0.59);\"><img style="text-transform: scale(0.59); width:430px" src="' + $scope.banner + '"></div></div>'
                                $scope.isAddBanner = false;
                            }
                        } else if (name == "banner") {
                            $scope.banner = $scope.croppedImageUrl;
                        } else {
                            $scope.userCardlets[$scope.segnatureId].tabs[$scope.ImageTabIndex].sigImage = $scope.croppedImageUrl;

                        }
                        document.getElementById("gmailDiv").innerHTML = $scope.coptToEmailText

                        $scope.showSpinner = false;

                    });
            }else{
                if (name == "signature") {
                    $window.scrollTo(0, 0);
                    $scope.coptToEmailText = '<a href="' + $scope.signatureLink + '"> <img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a>';
                    if ($scope.isAddBanner == true) {
                        $scope.coptToEmailText = '<div></div><a href="' + $scope.signatureLink + '"> <img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a></div><div><div style=\"max-height: 240px; overflow: hidden;text-transform: scale(0.59);\"><img style="text-transform: scale(0.59); width:430px" src="' + $scope.banner + '"></div></div>'
                    }
                } else if (name == "banner") {
                    $scope.banner = $scope.croppedImageUrl;
                } else {
                    $scope.userCardlets[$scope.segnatureId].tabs[$scope.ImageTabIndex].sigImage = $scope.croppedImageUrl;

                }
                $scope.showSpinner = false;
                document.getElementById("gmailDiv").innerHTML = $scope.coptToEmailText
            }
        };


    }


})();
