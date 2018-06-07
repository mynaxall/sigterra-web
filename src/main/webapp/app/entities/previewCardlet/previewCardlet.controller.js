(function () {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('PreviewCardletController', PreviewCardletController);


    PreviewCardletController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location', 'LoginService', '$sce', '$rootScope', '$window', 'EditViewerService'];

    function PreviewCardletController($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants, $http, $timeout, $location, LoginService, $sce, $rootScope, $window, EditViewerService) {

        var vm = this;
        vm.tabId = "";
        vm.bg = "/content/images/background.jpg";
        vm.title = 'Conversational Commerce is the biggest opportunity for brands to act like people in digital channels & build customer engagement and loyalty.'
        $rootScope.title = "";
        vm.toggleNavbar = toggleNavbar;
        vm.isNavbarCollapsed = true;
        $scope.time = Date.now();
        $scope.showViewDialog = false;


        $scope.getUser = function () {
            $http.get("/api/account")
                .success(function (response, status, headers) {
                    $scope.currentUser = response;
                    console.log($scope.currentUser)
                    $window.document.title = $scope.currentUser.username + " - Sigterra profile"
                });
        };
        $scope.getUser();

        $scope.time = Date.now()

        vm.currentSlide = 0;
        vm.showCarousel = true;
        $scope.showLink = true;

        $scope.testimonials = [];
        $scope.contentLibrary = [];
        $scope.activetTestimonial = 0;

        function  getId(id) {
          return  window.atob( window.atob( window.atob(window.atob(id))));
        }

        $scope.getCardlet = function () {
            var param1 = $location.search().cardletId;
            $http.get("/api/cardlet/" + getId(param1))
                .success(function (response, status, headers) {
                    $scope.tabNames = response;


                    if ($scope.tabNames.tabs[0].tabType == 1) {
                        vm.tabType = 1;
                        $scope.currentName = $scope.tabNames.tabs[0].name;
                        if ($scope.tabNames.tabs[0].site.value) {
                            $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[0].site.value);
                            $scope.currentLink = $scope.getLink($scope.tabNames.tabs[0].site.value);
                            vm.showSpinner = true;
                        }
                    }
                    else {
                        vm.tabType = $scope.tabNames.tabs[0].tabType

                        angular.forEach($scope.tabNames.tabs[0].items, function (item) {

                            if (item.position === 0) {
                                if (item.link) {
                                    $scope.currentUrl = $scope.createURL(item.link);
                                    $scope.currentLink = $scope.getLink(item.link);
                                    vm.showSpinner = true;
                                }
                                $scope.currentName = item.name.value;
                            }

                        });
                    }
                    for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                        if ($scope.tabNames.tabs[i].tabType == '1') {
                            $scope.firstBusinessCardId = i;
                            break
                        } else {
                            $scope.firstBusinessCardId = '';
                        }
                    }

                    $timeout(function () {
                        vm.showSpinner = false;
                    }, 4000)
                });

            EditViewerService.getPreview(getId(param1)).then(function (response) {
                $scope.cardletView = response;
                if($scope.cardletView.header){
                    $scope.header = $scope.cardletView.header;
                }
                if($scope.cardletView.background){
                    $scope.background = $scope.cardletView.background;
                }
                if($scope.cardletView.footer){
                    $scope.footer = $scope.cardletView.footer;
                }
                if($scope.cardletView.links) {
                    $scope.links = $scope.cardletView.links;
                }

            }).catch(function (response) {
            });

            getWidgets(getId(param1));
        };


        /* Add to Address Book */
        $scope.successfulyAdded = "";
        $scope.errorAddeding = "";

        $scope.addToAddressBook = function () {
            var param1 = $location.search().cardletId;
            $http.post("/api/address-book/" + getId(param1))
                .success(function (response, status, headers) {

                    $scope.successfulyAdded = response.message;
                    $timeout(function () {
                        $scope.successfulyAdded = "";
                    }, 3000);
                })
                .error(function (response, status, headers) {
                    $scope.errorAddeding = response.message;
                    $timeout(function () {
                        $scope.errorAddeding = "";
                    }, 3000);

                });
        };

        /* Reads counter */
        $scope.readsCounter = function (event, paramItemId, paramItemDataId) {
            $http.post("api/event/item/" + paramItemId + "/" + paramItemDataId)
                .success(function (response, status, headers) {
                    // Do nothing
                })
                .error(function (response, status, headers) {
                    // Do nothing
                });
        };

        /* Clicks counter */
        $scope.clicksCounter = function (event) {

            // clicks counter request
            var paramId = $location.search().cardletId;
            $http.post("api/event/cardlet/" + getId(paramId))
                .success(function (response, status, headers) {
                    // Do nothing
                })
                .error(function (response, status, headers) {
                    // Do nothing
                });

            event.stopPropagation();

        };


        $scope.trustSrc = function (src) {
            return $sce.trustAsResourceUrl(src);
        };

        $scope.createURL = function (link) {
            if (link) {

                if (link.indexOf('https://') == -1 && link.indexOf('http://') == -1) {
                    link = "http://" + link
                } else if (link.indexOf('https://') != -1) {
                    link.replace("https://", "http://")

                }
            }
            return link
        }


        vm.nextIndex = 1;

        $scope.prevSlide = function (index) {
            $scope.currentUrl = undefined;
            if (vm.currentSlide === 0) {
                vm.currentSlide = $scope.tabNames.tabs[index].items.length - 1;
                vm.nextIndex = 0;
            }else {
                vm.currentSlide = vm.currentSlide - 1;
                vm.nextIndex = vm.currentSlide + 1;
            }


            $timeout(function () {

                angular.forEach($scope.tabNames.tabs[index].items, function (item) {
                    if (item.position === vm.currentSlide) {
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

            }, 10);
            $timeout(function () {
                vm.showSpinner = false;
            }, 4000)
        };

        $scope.setCurrentSlide = function (index, parent) {
            vm.currentSlide = index;
            $timeout(function () {

                angular.forEach($scope.tabNames.tabs[parent].items, function (item) {
                    if (item.position === vm.currentSlide) {
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

            }, 10);
            $timeout(function () {
                vm.showSpinner = false;
            }, 4000)
        }

        $scope.itemPosition = function (index) {
            return index + 1;
        }

        $scope.nextSlide = function (index) {
            $scope.currentUrl = undefined
            if (vm.currentSlide == $scope.tabNames.tabs[index].items.length - 1) {
                vm.currentSlide = 0;
            } else {
                vm.currentSlide = vm.currentSlide + 1;

            }

            vm.nextIndex = vm.currentSlide + 1;
            if (vm.nextIndex == $scope.tabNames.tabs[index].items.length) {
                vm.nextIndex = 0;
            }


            $timeout(function () {
                angular.forEach($scope.tabNames.tabs[index].items, function (item) {

                    if (item.position === vm.currentSlide) {
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
            }, 10);
            $timeout(function () {
                vm.showSpinner = false;
            }, 4000)
        };

        $scope.getLink = function (link) {
            var linkNew = link;
            if (link.indexOf('https://') != -1) {
                linkNew = link.replace("https://", "");
            } else if (link.indexOf('http://') != -1) {
                linkNew = link.replace("http://", "")
            }
            return linkNew;

        }

        vm.showSpinner = false;

        vm.hideFrame = false;

        $scope.closeFrame = function () {
            document.getElementById("myFrame").style.height = "800px"
        }

        $scope.sethide = function () {
            vm.hideFrame = false;
        }


        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        $scope.getFonts = function (bold, italic, underline) {
            var textDecoration = "normal";

            var fontFamily = "Roboto-Regular";

            if (bold && italic) {
                fontFamily = "Roboto-Bold-Italic";
            }
            else if (italic) {
                fontFamily = "Roboto-Italic";
            }
            else if (bold) {
                fontFamily = "Roboto-Bold";
            }

            if (underline) {
                textDecoration = "underline";
            }

            return {
                "font-family": fontFamily,
                "text-decoration": textDecoration,

            }
        }

        $scope.openCity = function (cardName, cardId, id) {
            $scope.currentUrl = "1";
            $scope.currentLink = "1";
            $scope.currentUrl = undefined;
            vm.currentSlide = 0;

            if ($scope.tabNames.tabs[id].tabType === 1) {
                vm.tabType = 1;
            } else {
                vm.tabType = 2;
                vm.tabId = $scope.tabNames.tabs[id];
            }
            vm.showSpinner = true;
            vm.nextIndex = 1;

            setTimeout(function () {
                if ($scope.tabNames.tabs[id].tabType === 1) {
                    vm.tabType = 1;
                    $scope.currentName = $scope.tabNames.tabs[id].name;
                    if ($scope.tabNames.tabs[id].site.value) {
                        $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[id].site.value);
                        $scope.currentLink = $scope.getLink($scope.tabNames.tabs[id].site.value);

                        vm.showSpinner = true;
                    } else {
                        vm.showSpinner = false;
                        $scope.currentUrl = "";
                        $scope.currentLink = "";

                    }

                }
                else {
                    vm.tabType = 2;
                    vm.tabType = $scope.tabNames.tabs[id].tabType
                    angular.forEach($scope.tabNames.tabs[id].items, function (item) {

                        if (item.position === 0) {
                            if (item.name) {
                                $scope.currentName = item.name.value;
                            } else {
                                $scope.currentName = ""
                            }
                            if (item.link) {
                                $scope.currentUrl = $scope.createURL(item.link);
                                $scope.currentLink = $scope.getLink(item.link);
                                vm.showSpinner = true;
                            } else {
                                vm.showSpinner = false;
                                $scope.currentUrl = "";
                                $scope.currentLink = "";

                            }

                        }

                    });
                }

            }, 500)

            $timeout(function () {
                vm.showSpinner = false;
            }, 4000)


            var i, tabcontent2, tablinks2, tabs2;
            tabcontent2 = document.getElementsByClassName("tabcontent-preview");
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


        $scope.showSignature = function () {
            $http.get("/api/signatures")
                .success(function (response, status, headers) {
                    $scope.signatures = response;
                });
        };


        angular.element(document).ready(function () {
            document.getElementsByClassName("tabcontent-preview")[0].style.display = "block";
            document.getElementsByClassName("tabs2")[0].className += " active";
        });


        $scope.reloadFrame = function () {
            var url = $scope.currentUrl;
            $scope.currentUrl = null;
            vm.showSpinner = true;
            $timeout(function () {
                $scope.currentUrl = url
            }, 10)
            $timeout(function () {
                vm.showSpinner = false;
            }, 4000)

        }


        $scope.addColors = function (id, colorMain, colorSecond, linkId) {

            var currentEl = document.getElementById(id);
            var link = document.getElementById(linkId);
            if (currentEl) {

                if ($scope.tabNames) {

                    if ($scope.tabNames.tabs.length == 1) {
                        currentEl.style.width = "907px";
                        link.style.width = "907px";
                        link.style.maxWidth = "907px";
                    }
                    if ($scope.tabNames.tabs.length === 2) {
                        currentEl.style.width = "451px";
                        link.style.width = "451px";
                        link.style.maxWidth = "451px";
                    }
                    if ($scope.tabNames.tabs.length === 3) {
                        currentEl.style.width = "299px";
                        link.style.width = "299px";
                        link.style.maxWidth = "299px";
                    }
                    if ($scope.tabNames.tabs.length === 4) {
                        currentEl.style.width = "223px";
                        link.style.width = "223px";
                        link.style.maxWidth = "223px";
                    }
                }
            }
        };

        $scope.getUrl = function (url) {
            if (url === "app/cardlets/item3.html") {
                return "app/cardlets/itemPreview.html"
            }
            else if (url === "app/cardlets/pres1.html"){
                              return "app/cardlets/itemPreview.html"
            }else {
                return url;
            }
        }

        $scope.scrollToFooter = function (id) {
            var el = document.getElementById(id);
            console.log(el)
            el.scrollIntoView();
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function  getWidgets(id) {
            EditViewerService.getWidgets(id).then(function (response) {
                $scope.widgets = response;
                $scope.setWidgets()
            }).catch(function (response) {
            });
        }

        $scope.setWidgets = function () {
            $scope.testimonials = $scope.widgets.cardletTestimonialWidget;
            $scope.contentLibrary = $scope.widgets.cardletContentLibraryWidget;
        }


        $scope.prevTimonialSlide = function(index){
            $scope.currentUrl = undefined;
            if( $scope.activetTestimonial == 0){
                $scope.activetTestimonial = $scope.testimonials.length -1
            }else{
                $scope.activetTestimonial = parseInt($scope.activetTestimonial) - 1;
            }

        };

        $scope.nextTimonialSlide = function(index){
            $scope.currentUrl = undefined
            if( $scope.activetTestimonial == $scope.testimonials.length -1){
                $scope.activetTestimonial = 0
            }else{
                $scope.activetTestimonial = parseInt($scope.activetTestimonial) + 1;
            }

        };

        $scope.openPreviewDialog = function(index){
            $scope.isVideo = false;
            $scope.isPdf = false;
            $scope.isImage = false;
            $scope.showLink = false;
            $scope.showViewDialog = true;
            $scope.dialogContent= $scope.contentLibrary[index];
            $scope.viewingElement = $scope.contentLibrary[index].uploadFileUrl;
            if($scope.viewingElement.toLowerCase().includes('www.youtube.com') ) {
                $scope.isVideo = true;
                $scope.viewingElement = $scope.viewingElement.replace("watch?v=", "embed/");
            }else if($scope.viewingElement.toLowerCase().includes('.pdf')){
                $scope.isPdf = true;
            }else if($scope.viewingElement.toLowerCase().includes('.jpeg') || $scope.viewingElement.toLowerCase().includes('.png') || $scope.viewingElement.toLowerCase().includes('.jpg')){
                $scope.isImage = true;
            }else {
                $scope.showLink = true;
            }
            document.body.className += " modal-open";
            EditViewerService.viewsContentLibrary($scope.dialogContent.id).then(function () {
                getContentLibrary();
            }).catch(function (response) {
            });

        };

        $scope.closeDialog = function () {
            document.body.className = document.body.className.replace(" modal-open", "");
            $scope.showViewDialog = false;
        }

        $scope.likeContentLibrary = function () {
            EditViewerService.likesContentLibrary($scope.dialogContent.id).then(function () {
                getContentLibrary();
            }).catch(function (response) {
            });
        }


        function getContentLibrary() {
            var id = $location.search().cardletId;
            EditViewerService.getWidgets(getId(id)).then(function (response) {
                $scope.contentLibrary = response.cardletContentLibraryWidget;
            }).catch(function (response) {
            });
        }
    }



})();
