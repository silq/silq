'use strict';

angular.module('silq2App')
    .filter('similarityClass', function () {
    	return function(similarity) {
            similarity = parseFloat(similarity);
            if (similarity === 1.0) return 'success';
            if (similarity >= 0.7) return 'info';
            if (similarity >= 0.4) return 'warning';
            return 'danger';
    	};
    });
