'use strict';

angular.module('silq2App')
    .directive('highlightText', function($parse, $interpolate, $timeout) {
        return {
            restrict: 'A',
            replace: false,
            compile: function(element, attrs) {
                var elementHtmlExpr = $interpolate(element.html());

                return function(scope, element, attrs) {
                    var originalHtml = elementHtmlExpr(scope);
                    var delayedHighlight;

                    var highlightText = function(term) {
                        if (!term) return element.html(originalHtml);
                        var regex = new RegExp('('+term+')', 'gi');
                        var text = originalHtml.replace(regex, '<span class="highlight">$1</span>');
                        element.html(text);
                    };

                    scope.$watch(attrs.highlightText, function(term) {
                        delayedHighlight && $timeout.cancel(delayedHighlight);
                        delayedHighlight = $timeout(function() {
                            highlightText(term);
                        }, 400);
                    });
                };
            }
        };
    });
