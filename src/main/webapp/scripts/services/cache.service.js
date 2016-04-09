'use strict';

angular.module('silq2App')
    .factory('Cache', function (CacheFactory) {
        return {
            /**
             * Invalida todos os itens da cache local.
             */
            invalidate: function() {
                CacheFactory.clearAll();
            }
        };
    });
