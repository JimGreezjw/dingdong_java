var ddpatientRoutes = angular.module("ddpatientRoutes", []);
ddpatientRoutes.config(function (e, t, r) {
    e.state("tab", {
        url: "/tab",
        "abstract": !0,
        templateUrl: "templates/tabs.html",
        controller: "TabsCtrl"
    }).state("tab.main", {
        url: "/main",
        views: {"tab-main": {templateUrl: "templates/tab-main.html", controller: "MainCtrl"}}
    }).state("tab.doctorsFan", {
        url: "/doctorsFan",
        views: {"tab-doctorsFan": {templateUrl: "templates/tab-doctorsFan.html", controller: "DoctorsFanCtrl"}}
    }).state("tab.account", {
        url: "/account",
        views: {"tab-account": {templateUrl: "templates/tab-account.html", controller: "accountCtrl"}}
    }), e.state("newpatitent", {
        url: "/newpatient/:scheduleId/:doctorId/:type",
        templateUrl: "templates/newpatient.html",
        controller: "newPatientCtrl"
    }), e.state("doctorList", {
        url: "/doctorList",
        templateUrl: "templates/doctorList.html",
        controller: "DoctorsCtrl"
    }).state("doctorListShow", {
        url: "/doctorListShow",
        templateUrl: "templates/doctor/doctorListShow.html",
        controller: "DoctorsCtrl"
    }).state("hospitalDoctorList", {
        url: "/hospitalDoctorList/:hospitalId",
        templateUrl: "templates/hospital/hospitalDoctorList.html",
        controller: "hospitalDoctorsCtrl"
    }).state("hospitalsAll", {
        url: "/hospitalsAll",
        templateUrl: "templates/hospital/hospitals.html",
        controller: "HospitalsAllCtrl"
    }).state("doctorSearchList", {
        url: "/doctorSearchList/:filterText",
        templateUrl: "templates/doctor/doctorSearchList.html",
        controller: "DoctorsSearchListCtrl"
    }).state("doctor-detail", {
        url: "/doctors/:doctorId/hospital/:hospitalId",
        templateUrl: "templates/doctor-detail.html",
        controller: "DoctorDetailCtrl"
    }).state("tab.doctor-detail-history", {
        url: "/doctor-detail-history/:doctorId",
        views: {"tab-account": {templateUrl: "templates/doctor-detail.html", controller: "DoctorDetailCtrl"}}
    }).state("doctor-infor", {
        url: "/doctors/:doctorId/doctorInfor",
        templateUrl: "templates/doctor-infor.html",
        controller: "DoctorInforCtrl"
    }).state("user-patient-account", {
        url: "/userPatientAccount/:type",
        templateUrl: "templates/patient/user-patient-account.html",
        controller: "UserPatientCtrl"
    }).state("user-patient-add-main", {
        url: "/userPatientAddMain",
        templateUrl: "templates/patient/user-patient-add.html",
        controller: "UserPatientAddCtrl"
    }).state("user-patient-add-account", {
        url: "/userPatientAddAccount/:type",
        templateUrl: "templates/patient/user-patient-add.html",
        controller: "UserPatientAddCtrl"
    }).state("doctor-infor-show", {
        url: "/doctors/:doctorId/doctorInforShow",
        templateUrl: "templates/doctor/doctor-infor-show.html",
        controller: "DoctorInforCtrl"
    }).state("tab.hospital-infor", {
        url: "/hospitals/:doctorId",
        views: {"tab-main": {templateUrl: "templates/hospitallist.html", controller: "hospitalCtrl"}}
    }).state("tab.hospital-introduction", {
        url: "/hospitals/:hospitalId/hospitalIntro",
        views: {
            "tab-main": {
                templateUrl: "templates/hospital/hospitals_infor_show.html",
                controller: "hospitalIntroCtrl"
            }
        }
    }).state("doctorHospitals", {
        url: "/doctorHospitals/:doctorId",
        templateUrl: "templates/doctor/doctorHospitalList.html",
        controller: "doctorHospitalsCtrl"
    }).state("tab.doctor-detail-funs", {
        url: "/doctor-detail-funs/:doctorId",
        views: {
            "tab-doctorsFan": {
                templateUrl: "templates/doctorFun/doctor-detail-funs.html",
                controller: "DoctorDetailCtrl"
            }
        }
    }).state("tab.hospital-doctorsFan", {
        url: "/hospitals-doctorsFan/:doctorId",
        views: {"tab-doctorsFan": {templateUrl: "templates/hospitallist.html", controller: "hospitalCtrl"}}
    }).state("tab.doctor-infor-funs", {
        url: "/doctors-funs/:doctorId/doctorInfor",
        views: {
            "tab-doctorsFan": {
                templateUrl: "templates/doctorFun/doctor-infor-funs.html",
                controller: "DoctorInforCtrl"
            }
        }
    }).state("tab.userQueue", {
        url: "/user/userQueue",
        views: {"tab-account": {templateUrl: "templates/userQueueList.html", controller: "userQueueCtrl"}}
    }).state("tab.userRegister", {
        url: "/user/userRegister",
        views: {"tab-account": {templateUrl: "templates/userRegisterList.html", controller: "userRegisterCtrl"}}
    }).state("tab.userAfterRegister", {
        url: "/user/userAfterRegister",
        views: {"tab-main": {templateUrl: "templates/userRegisterList.html", controller: "userRegisterCtrl"}}
    }).state("tab.registerUploadFiles", {
        url: "/registerUploadFiles/:registerId",
        views: {
            "tab-account": {
                templateUrl: "templates/register/userRegisterFiles.html",
                controller: "userRegisterFilesCtrl"
            }
        }
    }).state("tab.userRegisterHistory", {
        url: "/user/userRegisterHistory",
        views: {
            "tab-account": {
                templateUrl: "templates/userRegisterHistoryList.html",
                controller: "userRegisterHistoryCtrl"
            }
        }
    }).state("tab.doctorEvaluationList", {
        url: "/user/doctorEvaluationList/:doctorId",
        views: {
            "tab-main": {
                templateUrl: "templates/doctor/doctorEvaluationListShow.html",
                controller: "doctorEvaluationListCtrl"
            }
        }
    }).state("tab.myEvaluationList", {
        url: "/user/myEvaluationList/:userId/:registerId/:doctorId",
        views: {
            "tab-account": {
                templateUrl: "templates/evaluation/evaluation-list.html",
                controller: "myEvaluationListCtrl"
            }
        }
    }).state("tab.needEvaluationList", {
        url: "/user/needEvaluationList",
        views: {
            "tab-account": {
                templateUrl: "templates/evaluation/tab-evaluation-list.html",
                controller: "needEvaluationListCtrl"
            }
        }
    }).state("tab.addEvaluation", {
        url: "/user/addEvaluation/:registerId",
        views: {
            "tab-account": {
                templateUrl: "templates/evaluation/add-evaluation.html",
                controller: "addEvaluationCtrl"
            }
        }
    }).state("tab.userRegisterConfirm", {
        url: "/user/register/userRegisterConfirm",
        views: {
            "tab-account": {
                templateUrl: "templates/register/userRegisterConfirmList.html",
                controller: "UserRegisterConfirmListCtrl"
            }
        }
    }).state("tab.score", {
        url: "/user/score",
        views: {"tab-account": {templateUrl: "templates/account/tab-score.html", controller: "ScoreCtrl"}}
    }).state("tab.terms", {
        url: "/user/terms",
        views: {"tab-account": {templateUrl: "templates/terms.html", controller: "TermsCtrl"}}
    }).state("userTransfer", {
        url: "/userTransfer/:transferType",
        templateUrl: "templates/transfers/user-transfers.html",
        controller: "UserTransferCtrl"
    }).state("tab.smsValid", {
        url: "/user/smsValid",
        templateUrl: "templates/smsValid.html",
        controller: "SmsValidCtrl"
    }).state("wxpay", {
        url: "/user/wxpay",
        templateUrl: "templates/wxpay/tab-wxpay.html",
        controller: "WxpayCtrl"
    }).state("userInformation", {
        url: "/user/userInformation",
        templateUrl: "templates/user-information.html",
        controller: "UserInformationCtrl"
    }).state("user-patient-main", {
        url: "/userPatientMain/:type",
        templateUrl: "templates/patient/user-patient-main.html",
        controller: "UserPatientCtrl"
    }).state("tab.user-patient-main-confirm", {
        url: "/userPatientMainConfirm/:type",
        views: {"tab-account": {templateUrl: "templates/patient/user-patient-main.html", controller: "UserPatientCtrl"}}
    }).state("tab.patients-history-detail", {
        url: "/patientHistory/:patientId/:registerId",
        views: {
            "tab-account": {
                templateUrl: "templates/patient/patient-history-detail.html",
                controller: "PatientDetailCtrl"
            }
        }
    }).state("tab.enchashment", {
        url: "/enchashment",
        views: {"tab-account": {templateUrl: "templates/wxpay/enchashment.html", controller: ""}}
    }).state("userSetting", {
        url: "/user/setting",
        templateUrl: "templates/userSetting.html",
        controller: "UserSettingCtrl"
    }).state("userChat", {
        url: "/userChat/:ddUserId/:doctorId",
        templateUrl: "templates/chat/chat-user.html",
        controller: "ChatMessagesCtrl"
    }).state("userChatRoom", {
        url: "/tab/userChat",
        templateUrl: "templates/chat/chatRoom.html",
        controller: "ChatRoomCtrl"
    }), t.otherwise("/tab/main"), r.backButton.text("").icon("ion-chevron-left"), r.tabs.style("standard"), r.tabs.position("bottom")
});
var ddpatientDirectives = angular.module("ddpatientDirectives", ["ui.rCalendar"]);
ddpatientDirectives.directive("compare", ["$ngModel", function (e) {
    return {
        link: function (e, t, r, s) {
            s.$parsers.unshift(function (e) {
                return "" == e || "" == r.compare || e == r.compare ? s.$setValidity("compare", !0) : s.$setValidity("compare", !1), e
            })
        }
    }
}]), ddpatientDirectives.directive("ngScroll", function () {
    return {
        link: function (e, t, r, s) {
            $(t).mCustomScrollbar({scrollButtons: {enable: !0}})
        }
    }
}), ddpatientDirectives.directive("ddexpander", function () {
    return {
        restrict: "EA",
        replace: !0,
        transclude: !0,
        scope: {expanderTitle: "=expanderTitle"},
        template: '<div><div class="item item-divider" ng-click="toggle()">{{expanderTitle}}</div><div class="body" ng-show="showMe" ng-transclude></div></div>',
        link: function (e, t, r) {
            e.showMe = !0, e.toggle = function () {
                e.showMe = !e.showMe
            }
        }
    }
}), ddpatientDirectives.directive("errSrc", function () {
    return {
        restrict: "EA", replace: !0, link: function (e, t, r) {
            r.ngSrc || r.$set("src", r.errSrc)
        }
    }
}), ddpatientDirectives.directive("ddbackbutton", ["$ionicHistory", function (e) {
    return {
        restrict: "EA",
        replace: !0,
        transclude: !0,
        template: '<a  class="button button-icon icon ion-chevron-left" ng-click="goBack()"></a>',
        link: function (t, r, s, a) {
            t.goBack = function () {
                e.goBack(-1)
            }
        }
    }
}]), ddpatientDirectives.directive("hscroller", ["$timeout", function (e) {
    return {
        restrict: "E",
        template: '<div class="hscroller" ng-transclude></div>',
        replace: !0,
        transclude: !0,
        compile: function (e, t) {
            return function (e, t, r) {
                t[0];
                angular.element(t).bind("scroll", function () {
                    t[0].scrollLeft
                })
            }
        }
    }
}]), ddpatientDirectives.directive("hcard", ["$rootScope", function (e) {
    return {
        restrict: "E",
        template: '<div class="hscroller-card" ng-transclude></div>',
        replace: !0,
        transclude: !0,
        scope: {desc: "@", level: "@", location: "@", image: "@", index: "@", href: "@"},
        link: function (e, t, r) {
            var s = angular.element('<a href="' + r.href + '" style="padding-left: 22px"> <img class="hscroller-img" src="' + r.image + '"/></a>');
            t.append(s), t.append('<div class="hscroller-label">' + r.desc + r.level + "<br>" + r.location + "</div>")
        }
    }
}]), ddpatientDirectives.directive("stringToNumber", function () {
    return {
        require: "ngModel", link: function (e, t, r, s) {
            s.$parsers.push(function (e) {
                return "" + e
            }), s.$formatters.push(function (e) {
                return parseInt(e)
            })
        }
    }
}), ddpatientDirectives.directive("star", function () {
    return {
        restrict: "EACM",
        scope: {ratingValue: "=", max: "=", readonly: "@", onHover: "=", onLeave: "=", starstyle: "@", startext: "@"},
        controller: function (e) {
            e.ratingValue = e.ratingValue || 0, e.starstyle = e.starstyle || "rating", e.startext = e.startext || "", e.max = e.max || 5, e.click = function (t) {
                e.readonly && "true" === e.readonly || (e.ratingValue = t)
            }, e.over = function (t) {
                e.onHover(t)
            }, e.leave = function (t) {
                e.onLeave(t)
            }, e.getStarCss = function () {
                return e.starstyle
            }
        },
        template: '<ul ng-class="getStarCss()" ng-mouseleave="leave(val)"> {{startext}}<li ng-repeat="star in stars" style=" margin:1px; display:inline;  font:bold 20px arial; cursor:pointer"  ng-class="star" ng-click="click($index + 1)" ng-mouseover="over($index + 1)"   >★</li></ul>',
        link: function (e, t, r) {
            t.css("text-align", "center");
            var s = function () {
                e.stars = [];
                for (var t = 0; t < e.max; t++)e.stars.push({filled: t < e.ratingValue})
            };
            s(), e.$watch("ratingValue", function (e, t) {
                t && s()
            }), e.$watch("max", function (e, t) {
                t && s()
            })
        }
    }
});
var ddpatientServices = angular.module("ddpatientServices", ["ngResource"]), serverName = "www.yushansoft.com";
ddpatientServices.factory("userInforList", [function () {
    return {}
}]), ddpatientServices.factory("doctorInforList", [function () {
    return {}
}]), ddpatientServices.factory("doctorServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/doctors/:id", null, {
        queryAll: {
            method: "get",
            params: {size: "@size", page: "@number", orderBy: "@orderBy", order: "@order"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("doctorNewJoinServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/doctors/newJoinDoctors", null, {
        queryAll: {
            method: "get",
            params: {requireNum: "@requireNum"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("scheduleServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/doctors/:id/scheduleDates", null, {
        query: {
            method: "get",
            params: {beginDate: "@beginDate", endDate: "@endDate"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("scheduleOnedayServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/doctors/:doctorId/hospitals/:hospitalId/schedules", null, {
        query: {
            method: "get",
            params: {beginDate: "@beginDate", endDate: "@endDate"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("scheduleDatesServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/doctors/:doctorId/hospitals/:hospitalId/scheduleDates", null, {
        query: {
            method: "get",
            params: {beginDate: "@beginDate", endDate: "@endDate"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("scheduleUserServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/users/:userId/schedules/:scheduleId/registers", null, {
        getScheduleUser: {
            method: "get",
            params: {userId: "@userId", scheduleId: "@scheduleId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("cancelUserFansServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/users/:userId/doctorFans", null, {
        cancelUserFans: {
            method: "DELETE",
            params: {userId: "@userId", doctorId: "@doctorId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("userFansServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/users/:id/doctorFans", {id: "@id", doctorId: "@doctorId"})
}]), ddpatientServices.factory("userQueueServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/users/:id/queues", {
        id: "@id",
        doctorId: "@doctorId",
        hospitalId: "@hospitalId"
    })
}]), ddpatientServices.factory("userNewQueueServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/registers/queueUp", {}, {
        userQueueUp: {
            method: "POST",
            params: {
                userId: "@userId",
                patientId: "@patientId",
                doctorId: "@doctorId",
                hospitalId: "@hospitalId",
                revisit: "@revisit",
                phenomenon: "@phenomenon",
                attachNo: "@attachNo"
            },
            isArray: !1
        }
    })
}]), ddpatientServices.factory("userMultiQueueServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/users/:id/mutiQueueUp", {
        id: "@id",
        doctorId: "@doctorId",
        hospitalStr: "@hospitalStr"
    })
}]), ddpatientServices.factory("hospitalServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/docotors/:id/doctorHospitals", {id: "@id"})
}]), ddpatientServices.factory("registerUpdateServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/registers/:id", {}, {
        registerUpdate: {
            method: "PATCH",
            params: {id: "@id", phenomenon: "@phenomenon", attachNo: "@attachNo"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("hospitalDoctorsServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/hospitals/:hospitalId/doctorHospitals", {}, {
        getHospitalDoctors: {
            method: "GET",
            params: {hospitalId: "@hospitalId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("registerUnFinishedServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:id/registers/unfinished", {id: "@id"})
}]), ddpatientServices.factory("getRegisterByStatusServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/registers/status", {userId: "@userId"}, {
        getRegisterByStatus: {
            method: "GET",
            params: {status: "@status"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("cancelRegisterServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/registers/:id", {id: "@id"}, {
        cancelRegister: {
            method: "DELETE",
            params: {}
        }
    })
}]), ddpatientServices.factory("getRegisterByIdServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/registers/:id", {id: "@id"}, {})
}]), ddpatientServices.factory("ddDeleteQueueServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/registers/queues/:id", {id: "@id"}, {
        deleteQueue: {
            method: "DElETE",
            params: {}
        }
    })
}]), ddpatientServices.factory("ddSmsValiServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/sms/validNum", {}, {
        validNum: {
            method: "GET",
            params: {mobileNo: "@mobileNo"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddUpdateSmsValiServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/sms/validation", null, {
        updateSmsValid: {
            method: "PUT",
            params: {userId: "@userId", mobileNo: "@mobileNo", msgCode: "@msgCode", imgCode: "@imgCode"}
        }
    })
}]), ddpatientServices.factory("ddUserServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId", {userId: "@userId"}, {})
}]), ddpatientServices.factory("ddUserRegisterServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/registers", {userId: "@userId"}, {
        userRegister: {
            method: "POST",
            params: {patientId: "@patientId", scheduleId: "@scheduleId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddUpdateUserInformationServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:id", {id: "@id"}, {
        updateUserInformation: {
            method: "PUT",
            params: {
                name: "@name",
                gender: "@gender",
                address: "@address",
                certificateId: "@certificateId",
                headImgUrl: "@headImgUrl"
            }
        }
    })
}]), ddpatientServices.factory("ddUserDirectRegisterServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/users/:userId/directRegister", {
        userId: "@userId",
        patientId: "@patientId",
        scheduleId: "@scheduleId",
        phenomenon: "@phenomenon",
        revisit: "@revisit",
        attachNo: "@attachNo"
    })
}]), ddpatientServices.factory("ddUserConfirmRegisterServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/{userId}/confirmRegister", {
        registerId: "@registerId",
        patientId: "@patientId",
        phenomenon: "@phenomenon",
        revisit: "@revisit",
        attachNo: "@attachNo"
    })
}]), ddpatientServices.factory("ddGetRegistersByStatusServices", ["$resource", "$http", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/registers/status", {userId: "@userId"}, {
        getRegistersByStatus: {
            method: "GET",
            params: {status: "@status"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddGetDoctorsByNameOrSpecialty", ["$resource", "$http", function (e) {
    return e("http://" + serverName + "/dingdong/doctors", {}, {
        getDoctorsByNameOrSpecialty: {
            method: "GET",
            params: {filterText: "@filterText", size: "@size", page: "@page", order: "@order", orderBy: "@orderBy"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddGetDoctorsnsigned", ["$resource", "$http", function (e) {
    return e("http://" + serverName + "/dingdong/doctors/unsigned", {}, {
        getGetDoctorsnsigned: {
            method: "GET",
            params: {doctorName: "@doctorName"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddHospitalsServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/hospitals", {}, {
        getHospitals: {
            method: "GET",
            params: {filterText: "@filterText", size: "@size", page: "@page", orderBy: "@orderBy", order: "@order"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddHospitalsAllServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/hospitals", {}, {
        getAllHospitals: {
            method: "GET",
            params: {size: "@size", page: "@page", orderBy: "@orderBy", order: "@order"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddConfirmQueueServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/registers/:id/confirmQueue", {id: "@id"}, {
        ddConfirmQueue: {
            method: "POST",
            params: {},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddDoctorHospitalsServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/docotors/:doctorId/doctorHospitals", {}, {
        getDoctorHospitals: {
            method: "GET",
            params: {doctorId: "@doctorId", status: "@status"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddDoctorEvaluationsServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/docotors/:doctorId/doctorEvals", {}, {
        getDoctorEvaluations: {
            method: "GET",
            params: {doctorId: "@doctorId", size: "@size", page: "@page", order: "@order", orderBy: "@orderBy"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddMyEvaluationsServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/doctorEvals", {}, {
        getMyEvaluations: {
            method: "GET",
            params: {userId: "@userId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddUserPatientServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/patients", {}, {
        getUserPatient: {
            method: "GET",
            params: {userId: "@userId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("cancelEvaluationServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/doctorEvals/:id", {id: "@id"}, {
        cancelEvaluation: {
            method: "DELETE",
            params: {}
        }
    })
}]), ddpatientServices.factory("addEvaluationServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/users/:userId/doctorEval", {
        userId: "@userId",
        registerId: "@registerId",
        treatmentEffect: "@treatmentEffect",
        serviceAttitude: "@serviceAttitude",
        evalDesc: "@evalDesc",
        tags: "@tags"
    })
}]), ddpatientServices.factory("yusFilesServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/yusFiles/:id", {id: "@id"}, {})
}]), ddpatientServices.factory("ddAddYusFilesServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/yusFiles", {userId: "@userId", wxServerId: "@wxServerId"})
}]), ddpatientServices.factory("ddUserPatientAddServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/patients", {userId: "@userId"}, {
        userPatientAdd: {
            method: "POST",
            params: {
                patientName: "@patientName",
                gender: "@gender",
                certificateId: "@certificateId",
                userRelation: "@userRelation",
                phone: "@phone"
            },
            isArray: !1
        }
    })
}]), ddpatientServices.factory("deleteUserPatientServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/patients/:id", {id: "@id"}, {
        deleteUserPatient: {
            method: "DELETE",
            params: {}
        }
    })
}]), ddpatientServices.factory("getUserPatientServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/patients/:id", {id: "@id"}, {
        getUserPatient: {
            method: "GET",
            params: {},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddUserPatientUpdateServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/patients/:id", {id: "@id"}, {
        userPatientUpdate: {
            method: "PUT",
            params: {
                userId: "@userId",
                name: "@name",
                certificateId: "@certificateId",
                userRelation: "@userRelation",
                gender: "@gender",
                birthday: "@birthday",
                phone: "@phone",
                address: "@address"
            },
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddPatientHistoryServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/doctors/:id/registers/status", {id: "@id"}, {
        queryHistory_Registers: {
            method: "GET",
            params: {},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddGetDoctorQueuesServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/doctors/:doctorId/statQueue", {doctorId: "@doctorId"}, {
        getDoctorQueues: {
            method: "GET",
            params: {},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddJsConfig", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/ddmz/jsConfig", {}, {
        jsConfig: {
            method: "GET",
            params: {url: "@url"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddGetDoctorScheduleDateServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/doctors/:doctorId/hospitals/:hospitalId/schedules", {
        doctorId: "@doctorId",
        hospitalId: "@hospitalId"
    }, {getDoctorScheduleDate: {method: "GET", params: {beginDate: "@beginDate", endDate: "@endDate"}, isArray: !1}})
}]), ddpatientServices.factory("ddGetDoctorHospitalsWithRegisterInfoServices", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/docotors/:doctorId/doctorHospitalsWithRegisterInfo", {doctorId: "@doctorId"}, {
        getDoctorHospitalsWithRegisterInfo: {
            method: "GET",
            params: {userId: "@userId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("userChatService", ["$resource", "$http", "$q", function (e) {
    return e("http://" + serverName + "/dingdong/userChat/:fromId", {}, {})
}]), ddpatientServices.factory("ddUserTransfersServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/Transfers", {userId: "@userId"}, {
        getUserTransfers: {
            method: "GET",
            params: {type: "@type", size: "@size", page: "@page"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddUserChatAllServices", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/users/:userId/msgs/:endDate", {userId: "@userId"}, {
        method: "GET",
        params: {size: "@size", page: "@page"},
        isArray: !1
    })
}]), ddpatientServices.factory("ddUserTwoUserMsgs", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/msgs", {userId: "@userId"}, {
        method: "GET",
        params: {fromUserId: "fromUserId", toUserId: "toUserId", size: "@size", page: "@page"},
        isArray: !1
    })
}]), ddpatientServices.factory("ddUserReadMsgs", ["$resource", function (e) {
    return e("http://" + serverName + "/dingdong/msgs/:toId/read", {toId: "@toId"}, {
        readMsgs: {
            method: "PATCH",
            params: {},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("ddRegistersServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/registers/:id", {id: "@id"}, {})
}]), ddpatientServices.factory("ddPatientDetailServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/patients/:patientId", {patientId: "@patientId"}, {})
}]), ddpatientServices.factory("getDoctorEvalsByRegisterServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/registers/:registerId/doctorEvals", {registerId: "@registerId"}, {
        getDoctorEvalsByRegisterId: {
            method: "GET",
            params: {},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("hospitalDeptTreeServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/hospitalDepts/:hospitalId/tree", {registerId: "@registerId"}, {
        method: "GET",
        params: {hospitalId: "@hospitalId", size: "@size", page: "@page"},
        isArray: !1
    })
}]), ddpatientServices.factory("getDoctorsTagsServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/systag/tags", {}, {
        getDoctorsTags: {
            method: "GET",
            params: {},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("getHosipitalIntroServices", ["$resource", "$q", function (e, t) {
    return e("http://" + serverName + "/dingdong/hospitals/:hospitalId", {}, {
        getSingleHosiptal: {
            method: "GET",
            params: {hospitalId: "@hospitalId"},
            isArray: !1
        }
    })
}]), ddpatientServices.factory("showPopupServices", ["$ionicPopup", function (e) {
    var t = {
        showPopup: function () {
            var t = e.show({
                template: '<input type="password" ng-model="data.wifi">',
                title: "Enter Wi-Fi Password",
                subTitle: "Please use normal things",
                scope: $scope,
                buttons: [{text: "Cancel"}, {
                    text: "<b>Save</b>", type: "button-positive", onTap: function (e) {
                        return $scope.data.wifi ? $scope.data.wifi : void e.preventDefault()
                    }
                }]
            });
            t.then(function (e) {
                console.log("Tapped!", e)
            }), $timeout(function () {
                t.close()
            }, 3e3)
        }, showConfirm: function () {
            var t = e.confirm({title: "Consume Ice Cream", template: "Are you sure you want to eat this ice cream?"});
            t.then(function (e) {
                e ? console.log("You are sure") : console.log("You are not sure")
            })
        }, showAlert: function (t, r) {
            var s = e.alert({title: t, template: r, buttons: [{text: "确定"}]});
            s.then(function (e) {
            })
        }
    };
    return t
}]);
var ddpatientFilters = angular.module("ddpatientFilters", []);
ddpatientFilters.filter("titleCase", function () {
    var e = function (e) {
        for (var t = e.split(" "), r = 0; r < t.length; r++)t[r] = t[r].charAt(0).toUpperCase() + t[r].slice(1);
        return t.join(" ")
    };
    return e
}), ddpatientFilters.filter("dateTranfer", function () {
    var e = function (e) {
        var t = new Date(e), r = t.getMonth() + 1, s = t.getFullYear() + "年" + r + "月" + t.getDate() + "日星期" + "日一二三四五六".charAt(t.getDay()) + "<br/><br/>";
        return s
    };
    return e
}), ddpatientFilters.filter("dateForOrder", function () {
    var e = function (e) {
        for (var t = [], r = 0; r < e.length; r++) {
            var s = new Date(e[r]), a = new Date(s.getFullYear() + "-" + (s.getMonth() + 1) + "-" + (s.getDate() + 1));
            t.push({startTime: a, endTime: a, allDay: !0})
        }
        return t
    };
    return e
}), ddpatientFilters.filter("gender", function () {
    var e = function (e) {
        var t = "男";
        return "1" == e && (t = "女"), t
    };
    return e
}), ddpatientFilters.filter("dateFormat1", function () {
    var e = function (e) {
        var t = new Date(e), r = t.getFullYear(), s = t.getMonth() + 1, a = t.getDate(), o = r + "." + s + "." + a;
        return o
    };
    return e
}), ddpatientFilters.filter("ageFormat", function () {
    var e = function (e) {
        var t = new Date, r = new Date(e), s = t.getFullYear() - r.getFullYear();
        return s
    };
    return e
}), ddpatientFilters.filter("registerStatus", function () {
    var e = function (e) {
        var t = "排队中";
        return "0" == e ? t = "草稿" : "1" == e ? t = "已预约" : "2" == e ? t = "已挂号" : "3" == e ? t = "已诊疗" : "4" == e ? t = "已取消" : "5" == e && (t = "已评价"), t
    };
    return e
}), ddpatientFilters.filter("timeSlot", function () {
    var e = function (e) {
        var t = "全天";
        return "1" == e ? t = "上午" : "2" == e ? t = "下午" : "3" == e && (t = "晚上"), t
    };
    return e
}), ddpatientFilters.filter("namePart", function () {
    var e = function (e) {
        var t = e.substring(0, 1);
        return t ? t + "**" : "***"
    };
    return e
}), ddpatientFilters.filter("doctorHospitalsStatus", function () {
    var e = function (e) {
        var t = "号满排队";
        return "0" == e ? t = " 号满排队" : "1" == e ? t = "已经排队" : "2" == e ? t = "预约挂号" : "3" == e && (t = "已经挂号"), t
    };
    return e
}), ddpatientFilters.filter("fontColorFilter", function () {
    var e = function (e) {
        var t = "color:green";
        return t = "挂号诚意金" == e ? "color:black" : "诊疗后退款" == e ? "color:blue" : "color:green"
    };
    return e
}), ddpatientFilters.filter("cut", function () {
    return function (e, t, r, s) {
        if (!e)return "";
        if (r = parseInt(r, 10), !r)return e;
        if (e.length <= r)return e;
        if (e = e.substr(0, r), t) {
            var a = e.lastIndexOf(" ");
            -1 != a && (e = e.substr(0, a))
        }
        return e + (s || " …")
    }
});