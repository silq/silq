'use strict';

angular.module('silq2App')
    .factory('CurriculumLattes', function ($http, Cache) {
        var url = 'api/curriculum';
        
        return {
            url: url,
            get: function() {
                return $http.get(url, {
                    cache: true
                });
            },
            delete: function() {
                Cache.invalidate();
                return $http.delete(url);
            }
        };
    });
