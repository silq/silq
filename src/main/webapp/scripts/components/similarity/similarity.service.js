'use strict';

angular.module('silq2App')
    .factory('Similarity', function ($resource) {
        return $resource('api/avaliar/atual', {}, {
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
