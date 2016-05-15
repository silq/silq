'use strict';

angular.module('silq2App')
    .factory('Avaliacao', function ($resource, $http) {
        return {
            avaliar: function(curriculumId, avaliacaoForm) {
                return $http.post('api/avaliar/curriculum/' + curriculumId, avaliacaoForm);
            }
        };
    });
