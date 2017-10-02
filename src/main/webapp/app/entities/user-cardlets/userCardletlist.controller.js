(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('UserCardletListController', UserCardletListController);



    UserCardletListController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService',  'paginationConstants', '$http', '$timeout', '$uibModal', '$location', '$window', '$rootScope'];

    function UserCardletListController ($scope, $state, CardletList, ParseLinks, AlertService,  paginationcardletConstants ,$http, $timeout, $uibModal, $location, $window, $rootScope) {
        var vm = this;


        $scope.time = Date.now()

        vm.currentSlide = 0;
        vm.showCarousel = true;
        $scope.showLink = true;

        $scope.isCopyTiEmail = false;
        $scope.fieldTable = [{
            field: "1",
            title: "Gmail"
        }, {
            field: "2",
            title: "Outlook"
        }, {
            field: "3",
            title: "Airmail"
        }, {
            field: "4",
            title: "Yahoo"
        }];

        $scope.showSpinner = true;

        $scope.showConent = true;

        $scope.hasChanged = function(id) {
            $scope.instructions = id.field;
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
                    $scope.addBanner();
                })
                .error(function (data, status, headers, config) {
                    $scope.showSpinner = false;
                });


        }

        var fileSelected=function(evt) {
            var file=evt.currentTarget.files[0];
            if(file) {
                $scope.saveBanner("banner", file, true);
            }
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

        $scope.isAddBanner = false;

        $scope.addBanner = function(){

            $scope.selectSignature($scope.selectSignatureId)
        }

        $scope.banner ="";
        $scope.tabsImage = '';

        $scope.gerPreviewLink = function(id){
            return ($location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/previewCardlet?cardletId='+ window.btoa(id))
        }


        $scope.testSelect = function(){
            if (window.getSelection) {
                var selection = window.getSelection();
                if (selection.rangeCount > 0) {
                    window.getSelection().removeAllRanges();
                }
            } else if (document.selection) {
                // Internet Explorer
                document.selection.empty();
            }
        }

        $scope.copyToEmail = function(id, cardId, sigId) {
            $scope.showSpinner = true;
            $scope.selected = $scope.fieldTable[0];
            $scope.isAddBanner = false;

            $scope.croppedImageUrl = null;

            $scope.isCopyTiEmail = true;
            $window.scrollTo(0, 0);
            $scope.segnatureId = id;
            $scope.banner = ''
            for (var i = 0; i < $scope.userCardlets[id].tabs.length; i++) {
                if ($scope.userCardlets[id].tabs[i].tabType == '1'){
                    $scope.firstBusinessCardCopyed = $scope.userCardlets[id].tabs[i];
                    $scope.firstBusinessCardId = i;
                break
                }else{
                    $scope.firstBusinessCardCopyed = "";
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

            $scope.testSelect();
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

                });


            setTimeout(function() {
                $scope.showSpinner = false;
            }, 100)
        };

        $scope.tabNames ={
            "cardletName": "Contact Info",
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

        $scope.userCard();



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
            $scope.instructions = 1;
            $scope.signatureSelected = undefined;
            angular.element('#fileInput3').val(null);
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

        $scope.signatureSelected;
        $scope.selectSignatureId;

        $scope.selectedSignature = true;

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


        $scope.selectSignature = function(id){
            $scope.selectSignatureId = id;
            if(id == 1){

                $scope.selectedSignature = true;
                $scope.signatureSelected = 1;
                var imgageData =  $scope.getCanvas.toDataURL("image/png");
                // Now browser starts downloading it instead of just showing it
                $scope.newData = imgageData.replace(/^data:image\/png/, "data:application/octet-stream");
                $scope.saveImage("signature",imgageData)

            }else if(id == 2){
                $scope.isAddIcons = true;
            }else if(id == 3){
                $scope.selectedSignature = false;
                $scope.signatureSelected = 3;
                var urlField = document.getElementById('thirdSignature');

                if($scope.isAddBanner == true){
                    $scope.coptToEmailText = urlField.outerHTML+ '<div style=\"text-transform: scale(0.59);text-align: left\"><img style="text-transform: scale(0.59); width:430px" src="' + $scope.banner + '"></div>'
                }else{
                    $scope.coptToEmailText = urlField.outerHTML;
                }
                document.getElementById("gmailDiv").innerHTML = $scope.coptToEmailText
            }else if(id = 4){
                $scope.isShowMailClientWindow = true;
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

        $scope.addColors = function(id, colorMain, colorSecond, index, linkId){
            var cyrrentEl = document.getElementById(id);
            var link = document.getElementById(linkId);
            if(cyrrentEl) {
                cyrrentEl.style.background = "#F9F9F9";
                cyrrentEl.style.borderTop = "1px solid #D0D8D9"
                cyrrentEl.style.borderBottom = "1px solid #D0D8D9";
                link.style.color = "#7F8C8C"
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    link.style.color = "#"+colorSecond;
                    cyrrentEl.style.background = "#FFFFFF";
                    cyrrentEl.style.borderTop = "2px solid #" + colorSecond;
                    cyrrentEl.style.borderBottom = "0px";
                }

                if ($scope.tabNames) {
                    if ($scope.userCardlets[index].tabs.length == 1) {
                        cyrrentEl.style.width = "540px"
                        link.style.width = "535px";
                        link.style.maxWidth = "535px";
                    }

                    if ($scope.userCardlets[index].tabs.length == 2) {
                        cyrrentEl.style.width = "270px"
                        link.style.width = "265px";
                        link.style.maxWidth = "265px";
                    }
                    if ($scope.userCardlets[index].tabs.length == 3) {
                        cyrrentEl.style.width = "180px";
                        link.style.width = "175px";
                        link.style.maxWidth = "175px";
                    }
                    if ($scope.userCardlets[index].tabs.length == 4) {
                        cyrrentEl.style.width = "135px";
                        link.style.width = "130px";
                        link.style.maxWidth = "130px";
                    }
                }
            }
        }


        $scope.getLink = function(link){
            if( link.startsWith('http')){
                return link;
            }else{
                return 'http://'+link;
            }
        }

        $scope.myInterval = 0;


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
                $scope.signatureSelected = 1;
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
                            $scope.coptToEmailText = '<a href="' + $scope.signatureLink + '"> <img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a>';
                            if ($scope.isAddBanner == true) {
                                $scope.coptToEmailText = '<div style="width: 430px;"><a href="' + $scope.signatureLink + '"> <img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a><div><div style=\"text-transform: scale(0.59);\"><img style="text-transform: scale(0.59); width:427px" src="' + $scope.banner + '"></div></div>'
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
                    $scope.coptToEmailText = '<a href="' + $scope.signatureLink + '"> <img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a>';
                    if ($scope.isAddBanner == true) {
                        $scope.coptToEmailText = '<div style="width: 430px;"><a href="' + $scope.signatureLink + '"><img style="text-transform: scale(0.59);width:430px" src="' + $scope.croppedImageUrl + '"></a><div><div style=\"text-transform: scale(0.59);\"><img style="text-transform: scale(0.59); width:427px" src="' + $scope.banner + '"></div></div>'
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
