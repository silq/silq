'use strict';

angular.module('silq2App')
    .factory('errorHandlerInterceptor', function ($q, $rootScope, Flash) {
        return {
            responseError: function (response) {
                $rootScope.$emit('silq:httpError', response);
                return $q.reject(response);
            }
        };
    })
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('errorHandlerInterceptor');
    }]);
