'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('measurement', {
                parent: 'admin',
                url: '/measurement?initialThreshold&finalThreshold&step&limit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/measurement/measurement.html',
                        controller: 'MeasurementController'
                    }
                },
                resolve: {

                }
            });
    });
