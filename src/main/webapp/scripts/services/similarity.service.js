'use strict';

angular.module('silq2App')
    .factory('Similarity', function ($resource, $http) {
        return {
            avaliarAtual: function(avaliarForm) {
                return $http.post('api/avaliar/atual/', avaliarForm, {
                    cache: true
                });
            },
            avaliar: function(avaliacaoLivreForm) {
                return $http.post('api/avaliar/', avaliacaoLivreForm);
            },
            result: function(cacheId) {
                return $http.get('api/avaliar/result/' + cacheId, {
                    cache: true
                });
            }
        };
    });
