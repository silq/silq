'use strict';

angular.module('silq2App')
    .factory('Similarity', function ($resource) {
        return $resource('api/compare/my', {}, {
                'compareMine': {
                    method: 'POST',
                    isArray: false,
                    transformResponse: function (data) {
                        if (data) {
                            return angular.fromJson(data);
                        }
                        return null;
                    }
                }
            });
        });
