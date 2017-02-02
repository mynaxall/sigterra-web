'use strict';

describe('Controller Tests', function() {

    describe('Business Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBusiness, MockCardlet, MockTabType, MockInputProperties;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBusiness = jasmine.createSpy('MockBusiness');
            MockCardlet = jasmine.createSpy('MockCardlet');
            MockTabType = jasmine.createSpy('MockTabType');
            MockInputProperties = jasmine.createSpy('MockInputProperties');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Business': MockBusiness,
                'Cardlet': MockCardlet,
                'TabType': MockTabType,
                'InputProperties': MockInputProperties
            };
            createController = function() {
                $injector.get('$controller')("BusinessDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sigterraWebApp:businessUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
