/* Directives */
var dddoctorDirectives = angular.module('dddoctorDirectives', ['ui.rCalendar']);

dddoctorDirectives.directive('compare', ['$ngModel',function ($ngModel) {
    return {
        link:function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                console.log("viewValue:%s", viewValue);
                console.log("attrs.compare:%s", attrs.compare);
                if (viewValue == "" || attrs.compare == "" || viewValue == attrs.compare) {
                    ctrl.$setValidity('compare', true);
                } else {
                    ctrl.$setValidity('compare', false);
                }
                return viewValue;
            });
        }
    };
}]);

dddoctorDirectives.directive('ngScroll', function () {
    return {
        link:function (scope, elm, attrs, ctrl) {
            $(elm).mCustomScrollbar({
                scrollButtons:{
                    enable:true
                }
            });
        }
    };
});

dddoctorDirectives.directive('errSrc', function() {
    return {
        restrict: 'EA',
        replace: true,
        link: function(scope, element, attrs) {
                if (!attrs.ngSrc) {
                    attrs.$set('src', attrs.errSrc);
                };
        }
    }
});

//自定义展开搜索
dddoctorDirectives.directive('ddexpander', function () {
    return {
        restrict : 'EA',
        replace : true,
        transclude : true,
        scope : {
            expanderTitle : '=expanderTitle'
        },
        template : '<div>'
        + '<div class="item item-divider" ng-click="toggle()">{{expanderTitle}}</div>'
        + '<div class="body" ng-show="showMe" ng-transclude></div>'
        + '</div>',
        link : function(scope, element, attrs) {
            scope.showMe = true;
            scope.toggle = function toggle() {
                scope.showMe = !scope.showMe;
            }
        }
    }
});

