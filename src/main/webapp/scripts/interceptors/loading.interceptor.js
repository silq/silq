'use strict';

angular.module('silq2App')
    .factory('loadingInterceptor', function ($rootScope, $q) {
        var loadingCount = 0;

        return {
            request: function (config) {
                if(++loadingCount === 1) $rootScope.loading = true;
                $rootScope.loading = true;
                return config || $q.when(config);
            },

            response: function (response) {
                if(--loadingCount === 0) $rootScope.loading = false;
                $rootScope.loading = false;
                return response || $q.when(response);
            },

            responseError: function (response) {
                if(--loadingCount === 0) $rootScope.loading = false;
                return $q.reject(response);
            }
        };
    })
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('loadingInterceptor');
    }]);
