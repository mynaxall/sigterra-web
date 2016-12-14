'use strict';

describe('Controller Tests', function() {

    describe('Cardlet Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCardlet, MockUser, MockBusiness, MockItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCardlet = jasmine.createSpy('MockCardlet');
            MockUser = jasmine.createSpy('MockUser');
            MockBusiness = jasmine.createSpy('MockBusiness');
            MockItem = jasmine.createSpy('MockItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Cardlet': MockCardlet,
                'User': MockUser,
                'Business': MockBusiness,
                'Item': MockItem
            };
            createController = function() {
                $injector.get('$controller')("CardletDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sigterraWebApp:cardletUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
