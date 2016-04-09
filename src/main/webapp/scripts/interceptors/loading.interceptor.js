'use strict';

angular.module('silq2App')
    .factory('loadingInterceptor', function ($rootScope, $q) {
        $rootScope.loadingCount = 0;

        return {
            request: function (config) {
                if (config.loadingIndicator !== false) {
                    if(++$rootScope.loadingCount === 1) {
                        $rootScope.loading = true;
                    }
                }
                return config || $q.when(config);
            },

            response: function(response) {
                if (response.config.loadingIndicator !== false) {
                    if(--$rootScope.loadingCount === 0) {
                        $rootScope.loading = false;
                    }
                }
                return response || $q.when(response);
            },

            responseError: function(response) {
                if (response.config.loadingIndicator !== false) {
                    if(--$rootScope.loadingCount === 0) {
                        $rootScope.loading = false;
                    }
                }
                return $q.reject(response);
            }
        };
    })
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('loadingInterceptor');
    }]);
