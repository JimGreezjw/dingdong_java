// 注册模块
var smsAppModule = angular.module('smsAppModule', []);

/**
 * $scope中存放相应的数据
 */
smsAppModule.controller('SmsValiController', function($scope) {
	// 显示的数据
	$scope.smsValiObj = {
		mobileNo : 18500357952,
		msgCode : "1234",
		imgCode : "1212"
	};

	// 按钮动作
	$scope.testClick = function() {
		var mobileNo = $scope.smsValiObj.mobileNo;
		var msgCode = $scope.smsValiObj.msgCode;

		alert(mobileNo + ", " + msgCode);
	};

	$scope.getValidNum = function() {
		alert("获取验证码");
	};
});
