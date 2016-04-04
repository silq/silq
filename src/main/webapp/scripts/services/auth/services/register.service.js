'use strict';

angular.module('silq2App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


