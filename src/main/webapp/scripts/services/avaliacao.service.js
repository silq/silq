'use strict';

angular.module('silq2App')
    .factory('Avaliacao', function ($resource, $http) {
        return {
            avaliar: function(curriculumId, avaliacaoForm) {
                return $http.post('api/avaliar/curriculum/' + curriculumId, avaliacaoForm);
            },
            avaliarLivre: function(avaliacaoLivreForm) {
                 return $http.post('api/avaliar/livre', avaliacaoLivreForm);
             },
            livreResult: function(cacheId) {
                return $http.get('api/avaliar/livre/result/' + cacheId, {
                    cache: true
                });
            }
        };
    });
