var dddoctorControllers = angular.module('dddoctorControllers', ['ionic-datepicker']);

dddoctorControllers.controller('MainCtrl', function ($scope, $location, ddDoctorHospitalsServices) {

        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');

        ddDoctorHospitalsServices.getDoctorHospitals({doctorId: doctorId}, function (result) {
            $scope.hospitals = result.doctorHospitals;
        })
//    if (doctorId) {
//
//    }  else{
////        var navigationUrl = "/navigation";
////        $location.url(navigationUrl);
//
//        var smsValidHomePage = "/smsValidHomePage";
//        $location.url(smsValidHomePage);
//    }

})
    .controller('PatientsAllCtrl', function ($scope, dateFilter, $location, ddPatientServices, ddUserServices,doctorInforList) {
        var today = dateFilter(new Date(), 'yyyy-MM-dd');
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');

        $scope.ctrlScope = $scope;
        $scope.$on('$ionicView.enter', function (e) {
            $scope.haveNews = 'tab-red-point-patient';

            if(doctorId){
                ddPatientServices.queryMyPatient_Hos_Date({id: doctorId, scheduleDate: today}, function (result) {
                    $scope.patients = result.registers;
                });
                if ($scope.patients && $scope.patients.length > 0) {
                    $scope.patients.forEach(function (item, index, array) {
                        if (item.status < 3) {
                            $scope.haveNews = 'tab-red-point-patient';
                        } else {
                            $scope.haveNews = '';
                        }
                    });
                } else {
                    $scope.haveNews = '';
                }

                ddUserServices.get({userId: ddybUserId}, function (result) {
                    $scope.user = result.users[0];
                    $scope.ctrlScope.balance = result.users[0].balance;
                    $scope.ctrlScope.score = result.users[0].score;
                });
            }else{
                doctorId = doctorInforList.doctorId;
                if (doctorId) {
                    ddPatientServices.queryMyPatient_Hos_Date({id: doctorId, scheduleDate: today}, function (result) {
                        $scope.patients = result.registers;
                    });

                    if ($scope.patients && $scope.patients.length > 0) {
                        $scope.patients.forEach(function (item, index, array) {
                            if (item.status < 3) {
                                $scope.haveNews = 'tab-red-point-patient';
                            } else {
                                $scope.haveNews = '';
                            }
                        });
                    } else {
                        $scope.haveNews = '';
                    }

                    ddUserServices.get({userId: ddybUserId}, function (result) {
                        $scope.user = result.users[0];
                        $scope.ctrlScope.balance = result.users[0].balance;
                        $scope.ctrlScope.score = result.users[0].score;
                    });
                   var urlPath = '/tab/patientsAll';
                    $location.url(urlPath);
                }else{
                    var smsValidHomePage = "/smsValidHomePage";
                    $location.url(smsValidHomePage);
                }
            }


        });
    })


    .controller('HospitalDetailCtrl', function ($scope, $stateParams, $ionicPopup, $filter, ddHospitalServices, dateFilter) {
        $scope.hospital = ddHospitalServices.get($stateParams.hospitalId);
        $scope.expanderTitle = '医院简介';

        //弹出确认对话框
        $scope.showConfirm = function (str) {
            var information = '<h4 style=" text-align:center;">' + $scope.hospital.fullName + '</h4>';
            var confirmPopup = $ionicPopup.confirm({
                title: '<h4 >确定申请坐诊吗？</h4>',
                template: information,
                cancelText: '取消',
                okText: '确定'
            });
            confirmPopup.then(function (res) {
                if (res) {
                    console.log('确定申请');
                } else {
                    console.log('取消');
                }
            });
        };
    })

    .controller('DoctorInformationCtrl', function ($scope, $ionicPopup, $ionicPopover, $location, ddDoctorInformationServices, ddUpdateDoctorInformationServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;
        ddDoctorInformationServices.get({id: doctorId}, function (result) {
            $scope.doctor = result.doctors[0];
            $scope.ctrlScope.name = result.doctors[0].name;
            $scope.ctrlScope.gender = result.doctors[0].gender;
            $scope.ctrlScope.hospitalName = result.doctors[0].hospitalName;
            $scope.ctrlScope.level = result.doctors[0].level;
            $scope.ctrlScope.specialty = result.doctors[0].specialty;
            $scope.ctrlScope.introduction = result.doctors[0].introduction;
            $scope.ctrlScope.registerFee = result.doctors[0].registerFee;
        });
        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        //弹出确认对话框
        $scope.showConfirmeSave = function (str) {
            var information = '<h4 style=" text-align:center;">' + str + '</h4>';
            var confirmPopup = $ionicPopup.confirm({
                title: '<h4 >确定保存吗？</h4>',
                template: information,
                cancelText: '取消',
                okText: '确定'
            });
            confirmPopup.then(function (res) {
                if (res) {
                    var doctorInformation = null;
                    doctorInformation = {
                        "hospitalName": $scope.ctrlScope.hospitalName,
                        "level": $scope.ctrlScope.level,
                        "specialty": $scope.ctrlScope.specialty,
                        "introduction": $scope.ctrlScope.introduction
                    };
                    ddUpdateDoctorInformationServices.updateDoctorInformation({id: doctorId}, doctorInformation);
                    console.log('完成');
                    var urlPath;
                    urlPath = '/tab/accountAll';
                    showQueue("保存成功！");
                    $location.url(urlPath);
                } else {
                    console.log('取消');
                }
            });
        };

    })


    .controller('DoctorsCtrl', function ($scope, $rootScope, $ionicPopover, $location,$ionicHistory, $timeout, doctorServices, getUnSignedDoctorsServices,doctorInforList) {
        $scope.ctrlScope = $scope;
        doctorInforList.getDoctor = null;
        //叮咚名医列表
        $scope.$on('$ionicView.enter', function (e) {
            if (doctorInforList.getDoctor) {
                $ionicHistory.goBack(-1);
            }
        });

        $scope.getDoctor = function (doctor) {
            doctorInforList.getDoctor = doctor;
            $ionicHistory.goBack(-1);
        };

        vm = $scope.vm = {
            search: function () {

                if(vm.query){
                    getUnSignedDoctorsServices.getUnSignedDoctors({
                        doctorName: vm.query
                    }, function (result) {
                        vm.doctors = result.doctors;
                    })
                }else{
                    vm.doctors = null;
                }
            }
        };
        var ddUserId = localStorage.getItem('ddUserId');
    })


    .controller('navigationCtrl', function ($scope, $ionicPopup, $ionicPopover, $location, ddDoctorInformationServices, submitSignDoctorsterServices,doctorInforList) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;
        $scope.getDoctor = function () {
            var urlPath = "/doctorList";
            $location.url(urlPath);
        };

        $scope.$on('$ionicView.enter', function () {
//            alert(ddybUserId);
//            alert(localStorage.getItem('ddUserId'));
            if (doctorInforList.getDoctor) {
                $scope.ctrlScope.name = doctorInforList.getDoctor.name;
                $scope.ctrlScope.id = doctorInforList.getDoctor.id;
            }

            var alreadySubmitSign = sessionStorage.getItem('alreadySubmitSign');
            if(alreadySubmitSign=="y"){
                $scope.showsHide = true;
            }


        });

        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        //弹出确认对话框
        $scope.showConfirmeSave = function (str) {

            if ($scope.ctrlScope.name == null) {
                showQueue("请选择入驻人!");
            } else {
                var reg =  /^((0\d{2,3}))(-(\d{7,8}))(-(\d{3,}))?$/;
                if (reg.test($scope.ctrlScope.officeTele)) {
                    var information = '<h4 style=" text-align:center;">' + $scope.ctrlScope.name+'<br/>'+ $scope.ctrlScope.officeTele + '</h4>';
                    var confirmPopup = $ionicPopup.confirm({
                        title: '<h4 >确定提交申请吗？</h4>',
                        template: information,
                        cancelText: '取消',
                        okText: '确定'
                    });
                    confirmPopup.then(function (res) {
                        if (res) {
                            var doctorInformation = null;
                            doctorInformation = {
                                "id": $scope.ctrlScope.id,
                                "userId": ddybUserId,
                                "officeTele": $scope.ctrlScope.officeTele
                            };


                            submitSignDoctorsterServices.submitSign({id: $scope.ctrlScope.id,userId: ddybUserId,officeTele: $scope.ctrlScope.officeTele},
                                function (result) {
                                    if(result.responseDesc == "OK"){
                                        showQueue("申请提交成功,工作人员会及时与您联系！");
                                    }else{
                                        showQueue(result.responseDesc);
                                    }
                                $scope.ctrlScope.officeTele = null;
                                $scope.ctrlScope.name = null;
                                $scope.ctrlScope.id=null;
                            });

//                            submitSignDoctorsterServices.submitSign({}, doctorInformation);
                            console.log('完成');
//                            showQueue("申请提交成功,工作人员会及时与您联系！");
                        } else {
                            console.log('取消');
                        }
                    });
                }else{
                    showQueue("请检查科室固话是否输入正确！");
                }
            }
        };
    })



    .controller('SettingCtrl', function ($scope, $ionicPopup, $ionicPopover, $location, ddDoctorInformationServices, submitSignDoctorsterServices,doctorInforList) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;
        $scope.eliminate = function () {
            localStorage.removeItem("ddybUserId");
            localStorage.removeItem("doctorId");

            showQueue("成功清除缓存！");
        };

        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        $scope.aboutUs = function () {
            var urlPath = "/tab/aboutUs";
            $location.url(urlPath);
        };

    })

    .controller('QueueListCtrl', function ($scope, $timeout,$stateParams, $ionicPopover, ddDoctorQueuesListServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;
        $scope.$on('$ionicView.enter', function (e) {
            ddDoctorQueuesListServices.getDoctorQueuesList({doctorId: doctorId,hospitalId:$stateParams.hospitalId}, function (result) {
                $scope.queues = result.queues;
            })
        });

        $scope.doRefresh = function () {
            console.log('Refreshing!');
            $timeout(function () {
                //simulate async response
                ddDoctorQueuesListServices.getDoctorQueuesList({doctorId: doctorId,hospitalId:$stateParams.hospitalId}, function (result) {
                    $scope.queues = result.queues;
                })
                //Stop the ion-refresher from spinning
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };
    })



    .controller('DoctorQueuesCtrl', function ($scope, $timeout, $ionicPopover, ddGetDoctorQueuesServices,ddPatientHistoryServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;
        $scope.$on('$ionicView.enter', function (e) {
//            ddGetDoctorQueuesServices.getDoctorQueues({doctorId: doctorId}, function (result) {
//                $scope.queues = result.queues;
//                $scope.ctrlScope.queueNum = result.queueNum;
//                $scope.queue = result.queues[0];
//                if ($scope.queues && $scope.queues.length > 0) {
//                    $scope.showOrNot = false;
//                } else {
//                    $scope.showOrNot = true;
//                }
//            })
            ddPatientHistoryServices.queryHistory_Registers({id: doctorId,status:-1}, function (result) {
                $scope.queues = result.registers;
                $scope.ctrlScope.queueNum = 0;
//                $scope.queue = result.queues[0];
                if ($scope.queues && $scope.queues.length > 0) {
                    $scope.ctrlScope.queueNum =result.registers.length
                    $scope.showOrNot = false;
                } else {
                    $scope.showOrNot = true;
                }
            })

        });

        $scope.doRefresh = function () {
            console.log('Refreshing!');
            $timeout(function () {
                //simulate async response
//                ddGetDoctorQueuesServices.getDoctorQueues({doctorId: doctorId}, function (result) {
//                    $scope.queues = result.queues;
//                    $scope.ctrlScope.queueNum = result.queueNum;
//                });
                //Stop the ion-refresher from spinning

                ddPatientHistoryServices.queryHistory_Registers({id: doctorId,status:-1}, function (result) {
                    $scope.queues = result.registers;
                    $scope.ctrlScope.queueNum = 0;
                    $scope.queue = result.queues[0];
                    if ($scope.queues && $scope.queues.length > 0) {
                        $scope.ctrlScope.queueNum =result.registers.length
                        $scope.showOrNot = false;
                    } else {
                        $scope.showOrNot = true;
                    }
                })
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };
    })


    .controller('DoctorFunsCtrl', function ($scope, $timeout, $stateParams, $ionicPopover, ddUserServices, ddGetDoctorFunsServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');

        $scope.$on('$ionicView.enter', function (e) {
            var vm = $scope.vm = {
                moredata: false,
                doctors: [],
                pagination: {
                    perPage: 15,
                    currentPage: 1
                },
                init: function () {
                    ddGetDoctorFunsServices.getDoctorFuns({
                        doctorId: doctorId,
                        size: vm.pagination.perPage,
                        page: vm.pagination.currentPage,
                        order: 'id',
                        orderBy: 'ASC'
                    }, function (result) {
                        vm.doctorFansNum = result.doctorFans.length;
                        vm.doctorFans = result.doctorFans;
                        angular.forEach(vm.doctorFans, function (data, index, array) {
                            //data等价于array[index]
                            ddUserServices.get({userId: array[index].userId}, function (result) {
                                vm.patient = (result.users)[0];

                                vm.doctorFans[index].headImgUrl = (result.users)[0].headImgUrl;
                                vm.doctorFans[index].fansName = (result.users)[0].name;
                                vm.doctorFans[index].fansAddress = (result.users)[0].address;
                                vm.doctorFans[index].fansGender = (result.users)[0].gender;
                                vm.doctorFans[index].userId = array[index].userId;
                            });
                        });
                        if (vm.doctorFans && vm.doctorFans.length > 0) {
                            $scope.ctrlScope.showOrNot = false;
                        } else {
                            $scope.ctrlScope.showOrNot = true;
                        }
                    })


                },
                doRefresh: function () {
                    console.log('Refreshing!');
                    $timeout(function () {
                        //simulate async response
                        ddGetDoctorFunsServices.getDoctorFuns({
                            doctorId: doctorId,
                            size: 15,
                            page: 1,
                            order: 'id',
                            orderBy: 'ASC'
                        }, function (result) {
                            vm.doctorFans = result.doctorFans;
                            vm.doctorFansNum = result.doctorFans.length;
                            angular.forEach(vm.doctorFans, function (data, index, array) {
                                //data等价于array[index]
                                ddUserServices.get({userId: array[index].userId}, function (result) {
                                    vm.patient = (result.users)[0];
                                    vm.doctorFans[index].headImgUrl = (result.users)[0].headImgUrl;
                                    vm.doctorFans[index].fansName = (result.users)[0].name;
                                    vm.doctorFans[index].fansAddress = (result.users)[0].address;
                                    vm.doctorFans[index].fansGender = (result.users)[0].gender;
                                });
                            });
                        });
                        //Stop the ion-refresher from spinning
                        $scope.$broadcast('scroll.refreshComplete');
                    }, 1000);
                },
                loadMore: function () {
                    vm.pagination.currentPage += 1;
                    if (vm.moredata) {
                        ddGetDoctorFunsServices.getDoctorFuns({
                            doctorId: doctorId,
                            size: vm.pagination.perPage,
                            page: vm.pagination.currentPage,
                            order: 'id',
                            orderBy: 'ASC'
                        }, function (result) {
                            vm.doctorFans = vm.doctorFans.concat(result.doctorFans);
                            if (result.doctorFans && result.doctorFans.length == 0) {
                                vm.moredata = false;
                            }
                            if (result.pages <= vm.pagination.currentPage + 1) vm.moredata = false;
                            $scope.$broadcast('scroll.infiniteScrollComplete');
//                            vm.doctorFansNum = vm.doctorFans.length;
                            angular.forEach(vm.doctorFans, function (data, index, array) {
                                //data等价于array[index]
                                ddUserServices.get({userId: array[index].userId}, function (result) {
                                    vm.patient = (result.users)[0];
                                    vm.doctorFans[index].headImgUrl = (result.users)[0].headImgUrl;
                                    vm.doctorFans[index].fansName = (result.users)[0].name;
                                    vm.doctorFans[index].fansAddress = (result.users)[0].address;
                                    vm.doctorFans[index].fansGender = (result.users)[0].gender;
                                });
                            });
                        })

                    }
                }
            };
            vm.init();
        });

    })


    .controller('PatientsRegistersCtrl', function ($scope, $timeout, $ionicPopover, ddUserServices, ddPatientRegistersServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');

        $scope.$on('$ionicView.enter', function (e) {

            ddPatientRegistersServices.queryDoctor_Registers({id: doctorId}, function (result) {
                $scope.patients = result.registers;

                angular.forEach($scope.patients, function (data, index, array) {
                    //data等价于array[index]
                    ddUserServices.get({userId: array[index].userId}, function (result) {
                        $scope.patient = (result.users)[0];
                        $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
                    });
                });
                if ($scope.patients && $scope.patients.length > 0) {
                    $scope.showOrNot = false;
                } else {
                    $scope.showOrNot = true;
                }
            })
        });

        $scope.doRefresh = function () {
            console.log('Refreshing!');
            $timeout(function () {
                //simulate async response
                ddPatientRegistersServices.queryDoctor_Registers({id: doctorId}, function (result) {
                    $scope.patients = result.registers;
                    angular.forEach($scope.patients, function (data, index, array) {
                        //data等价于array[index]
                        ddUserServices.get({userId: array[index].userId}, function (result) {
                            $scope.patient = (result.users)[0];
                            $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
                        });
                    });
                });
                //Stop the ion-refresher from spinning
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };

    })

    .controller('PatientsTodayCtrl', function ($scope, $ionicPopover, $timeout, $stateParams, ddPatientServices,ddPatientRegistersServices, ddUserServices, dateFilter) {
        var today = dateFilter(new Date(), 'yyyy-MM-dd');
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        var type = "0";
        $scope.showPatient0 = "button  button-positive";
        $scope.showPatient1 = "button";
        $scope.$on('$ionicView.enter', function (e) {

            if(type == "0"){
                $scope.showPatient0 = "button  button-positive";
                $scope.showPatient1 = "button";
                ddPatientServices.queryMyPatient_Hos_Date({id: doctorId, scheduleDate: today}, function (result) {
                    $scope.patients = result.registers;

                    //用上patient表的话，患者头像就都缺省一样了，因为患者没有头像，用户不一定是患者
//                angular.forEach($scope.patients, function(data,index,array){
//                    //data等价于array[index]
//                    ddUserServices.get({userId:array[index].userId},function(result){
//                        $scope.patient=(result.users)[0];
//                        $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
//                    });
//                });
                    if ($scope.patients && $scope.patients.length > 0) {
                        $scope.showOrNot = false;
                    } else {
                        $scope.showOrNot = true;
                    }
                })
            }else{
                $scope.showPatient0 = "button";
                $scope.showPatient1 = "button button-positive";
                ddPatientRegistersServices.queryDoctor_Registers({id: doctorId}, function (result) {
                    $scope.patients = result.registers;

                    angular.forEach($scope.patients, function (data, index, array) {
                        //data等价于array[index]
                        ddUserServices.get({userId: array[index].userId}, function (result) {
                            $scope.patient = (result.users)[0];
                            $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
                        });
                    });
                    if ($scope.patients && $scope.patients.length > 0) {
                        $scope.showOrNot = false;
                    } else {
                        $scope.showOrNot = true;
                    }
                });

            }

        });


        $scope.showPatient = function (str) {

            if (str == "0") {
                type = "0"
                $scope.showPatient1 = "button";
                $scope.showPatient0 = "button button-positive";
                $scope.calendarHide = true;
                $scope.showsHide = true;
                ddPatientServices.queryMyPatient_Hos_Date({id: doctorId, scheduleDate: today}, function (result) {
                    $scope.patients = result.registers
                    if ($scope.patients && $scope.patients.length > 0) {
                        $scope.showOrNot = false;
                    } else {
                        $scope.showOrNot = true;
                    }
                });
            } else {
                type = "1"
                $scope.showPatient0 = "button";
                $scope.showPatient1 = "button button-positive";
                ddPatientRegistersServices.queryDoctor_Registers({id: doctorId}, function (result) {
                    $scope.patients = result.registers;

                    angular.forEach($scope.patients, function (data, index, array) {
                        //data等价于array[index]
                        ddUserServices.get({userId: array[index].userId}, function (result) {
                            $scope.patient = (result.users)[0];
                            $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
                        });
                    });
                    if ($scope.patients && $scope.patients.length > 0) {
                        $scope.showOrNot = false;
                    } else {
                        $scope.showOrNot = true;
                    }
                });
            }
        };



        $scope.doRefresh = function () {
            console.log('Refreshing!');
            $timeout(function () {
                if(type == "0"){
                    $scope.showPatient0 = "button  button-positive";
                    $scope.showPatient1 = "button";
                    ddPatientServices.queryMyPatient_Hos_Date({id: doctorId, scheduleDate: today}, function (result) {
                        $scope.patients = result.registers;

                        //用上patient表的话，患者头像就都缺省一样了，因为患者没有头像，用户不一定是患者
//                angular.forEach($scope.patients, function(data,index,array){
//                    //data等价于array[index]
//                    ddUserServices.get({userId:array[index].userId},function(result){
//                        $scope.patient=(result.users)[0];
//                        $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
//                    });
//                });
                        if ($scope.patients && $scope.patients.length > 0) {
                            $scope.showOrNot = false;
                        } else {
                            $scope.showOrNot = true;
                        }
                    })
                }else{
                    $scope.showPatient0 = "button";
                    $scope.showPatient1 = "button button-positive";
                    ddPatientRegistersServices.queryDoctor_Registers({id: doctorId}, function (result) {
                        $scope.patients = result.registers;

                        angular.forEach($scope.patients, function (data, index, array) {
                            //data等价于array[index]
                            ddUserServices.get({userId: array[index].userId}, function (result) {
                                $scope.patient = (result.users)[0];
                                $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
                            });
                        });
                        if ($scope.patients && $scope.patients.length > 0) {
                            $scope.showOrNot = false;
                        } else {
                            $scope.showOrNot = true;
                        }
                    });

                }
                //Stop the ion-refresher from spinning
                $scope.$broadcast('scroll.refreshComplete');
            }, 1000);
        };

    })

    .controller('PatientsHistoryCtrl', function ($scope, $timeout, $ionicHistory, $ionicPopover, ddUserServices, ddPatientHistoryServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');

        $scope.goBack = function () {
            $ionicHistory.goBack(-1);
        };
        $scope.$on('$ionicView.enter', function (e) {
            ddPatientHistoryServices.queryHistory_Registers({id: doctorId, status: 3}, function (result) {
                $scope.patients = result.registers;
//                angular.forEach($scope.patients, function(data,index,array){
//                    //data等价于array[index]
//                    ddUserServices.get({userId:array[index].userId},function(result){
//                        $scope.patients[index].headImgUrl = (result.users)[0].headImgUrl;
//                    });
//                });
                ddPatientHistoryServices.queryHistory_Registers({id: doctorId, status: 5}, function (result) {
                    $scope.patients = $scope.patients.concat(result.registers);
                    if ($scope.patients && $scope.patients.length > 0) {
                        $scope.showOrNot = false;
                    } else {
                        $scope.showOrNot = true;
                    }
                })
            });

        });

        $scope.doRefresh = function () {
            console.log('Refreshing!');
            $timeout(function () {
                ddPatientHistoryServices.queryHistory_Registers({id: doctorId, status: 3}, function (result) {
                    $scope.patients = result.registers;
//                angular.forEach($scope.patients, function(data,index,array){
//                    //data等价于array[index]
//                    ddUserServices.get({userId:array[index].userId},function(result){
//                        $scope.patients[indeqx].headImgUrl = (result.users)[0].headImgUrl;
//                    });
//                });
                    ddPatientHistoryServices.queryHistory_Registers({id: doctorId, status: 5}, function (result) {
                        $scope.patients = $scope.patients.concat(result.registers);
                        if ($scope.patients && $scope.patients.length > 0) {
                            $scope.showOrNot = false;
                        } else {
                            $scope.showOrNot = true;
                        }
                    })
                });
                //Stop the ion-refresher from spinning
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };
    })
    .controller('PatientDetailCtrl', function ($scope, $stateParams, $ionicHistory, $location, $ionicPopup, $filter, getDoctorEvalsByRegisterServices, ddPatientDetailServices, ddRegistersServices, ddUserServices, ddPatientCondServices, ddPatientFinishTreatmentServices, dateFilter) {

        ddPatientDetailServices.get({patientId: $stateParams.patientId}, function (result) {
            $scope.patient = (result.patients)[0];
        });

//        ddUserServices.get({userId:$stateParams.patientId},function(result){
//            $scope.patient=(result.users)[0];
//        });

//    	 ddPatientCondServices.getByRegisterId({registerId:$stateParams.registerId},function(result){
//    		 $scope.patientConds=(result.patientConds);
//    	 });

        if($stateParams.patientType == "patientsRegisters"){
            $scope.showsHideButton = true;
        }else{
            $scope.showsHideButton = false;
        }

        ddRegistersServices.get({id: $stateParams.registerId}, function (result) {
            $scope.register = (result.registers)[0];

            if ($scope.register) {
                var files = $scope.register.attachNo;
                var yusFiles = [];
                if (files != null && files != "") {
                    var str = [];
                    str = files.split(",");
                    $scope.showOrNot = false;
                    for (i = 0; i < str.length; i++) {
                        yusFiles.push(str[i]);
                    }
                } else {
                    $scope.showOrNot = true;
                }
                $scope.yusFiles = yusFiles;
            }
        });

        getDoctorEvalsByRegisterServices.getDoctorEvalsByRegisterId({registerId: $stateParams.registerId}, function (result) {
            $scope.doctorEvals = result.doctorEvals;
            if($scope.doctorEvals && $scope.doctorEvals.length>0){

            }else{
                $scope.notShowEvals = true;
            }
        });

        $scope.expanderTitle = '病情简介';
        $scope.goBack = function () {
            $ionicHistory.goBack(-1);
        };
        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };
        //弹出确认对话框
        $scope.showConfirm = function (str) {
            var information = '<h4 style=" text-align:center;">' + $scope.patient.name + '&nbsp;|&nbsp;' + $filter('gender')($scope.patient.gender) + '<br>' + $filter('dateFormatAge')($scope.patient.birthday)+ "岁" + '</h4>';
            var confirmPopup = $ionicPopup.confirm({
                title: '<h4 >确定已完成诊疗吗？</h4>',
                template: information,
                cancelText: '取消',
                okText: '确定'
            });
            confirmPopup.then(function (res) {
                if (res) {

                    ddPatientFinishTreatmentServices.finishTreatment({registerId: $stateParams.registerId}, function (result) {
                        var urlPath = 'tab/patientsToday';
                        $location.url(urlPath);
                        showQueue("完成诊疗,恭喜您获得10个咚咚币，医生辛苦了！");
                    });
                    console.log('完成');
                } else {
                    console.log('取消');
                }
            });
        };

        // 触发一个按钮点击，或一些其他目标
        $scope.showPopup = function () {
            $scope.data = {};
            // 一个精心制作的自定义弹窗
            var myPopup = $ionicPopup.show({
                template: '评分：<input type="number" ng-model="data.evaluationScore"><br>评语：<input type="text" ng-model="data.evaluationDesc">',
                title: '请输入评语',
                subTitle: '请公正客观的进行评价！',
                scope: $scope,
                buttons: [
                    {text: '取消'},
                    {
                        text: '<b>保存</b>',
                        type: 'button-positive',
                        onTap: function (e) {
                            if (!$scope.data.evaluationScore && !$scope.data.evaluationDesc) {
                                //不允许用户关闭，除非他键入wifi密码
                                e.preventDefault();
                            } else {
                                return $scope.data.evaluationScore + !$scope.data.evaluationDesc;
                            }
                        }
                    }
                ]
            });
        };

    })
    .controller('DoctorHospitalCtrl', function ($scope, $ionicPopover, $stateParams, $timeout, ddDoctorHospitalsServices, ddDeleteDoctorHospitalServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');


        $scope.$on('$ionicView.enter', function (e) {

            ddDoctorHospitalsServices.getDoctorHospitals({doctorId: doctorId}, function (result) {
                $scope.hospitals = result.doctorHospitals;
            })

        });

        $scope.doRefresh = function () {

            console.log('Refreshing!');
            $timeout(function () {
                //simulate async response
                ddDoctorHospitalsServices.getDoctorHospitals({doctorId: doctorId}, function (result) {
                    $scope.hospitals = result.doctorHospitals;
                });

                //Stop the ion-refresher from spinning
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };

        $scope.remove = function (doctorHospital) {
            ddDeleteDoctorHospitalServices.deleteDoctorHospital({id: doctorHospital.id}, function (result) {
                ddDoctorHospitalsServices.getDoctorHospitals({doctorId: doctorId}, function (result) {
                    $scope.hospitals = result.doctorHospitals;
                })
            });
            console.log('完成');
        };
    })


    .controller('HospitalListCtrl', function ($scope, $ionicPopover, $ionicHistory, $stateParams, $timeout, ddHospitalsServices, ddHospitalsByFilterTextServices, doctorInforList) {


        $scope.ctrlScope = $scope;
        //叮咚名医列表
        $scope.$on('$ionicView.enter', function (e) {
            doctorInforList.getDept = null;
        });
        var vm = $scope.vm = {
            moredata: true,
            hospitals: [],
            pagination: {
                perPage: 15,
                currentPage: 1
            },
            afterInit: false,
            init: function () {
                ddHospitalsServices.getHospitals({
                    size: vm.pagination.perPage,
                    page: vm.pagination.currentPage,
                    orderBy: 'name',
                    order: 'ASC'
                }, function (result) {
                    vm.hospitals = result.hospitals;
                    vm.afterInit = true;
                    if (result.pages <= vm.pagination.currentPage + 1) vm.moredata = false;
                })
            },
            doRefresh: function () {
                $timeout(function () {

                    $scope.$broadcast('scroll.refreshComplete');

                }, 1000);
            },
            loadMore: function () {
                vm.pagination.currentPage += 1;
                if (vm.moredata&& vm.afterInit) {
                    ddHospitalsServices.getHospitals({
                        size: vm.pagination.perPage,
                        page: vm.pagination.currentPage,
                        orderBy: 'name',
                        order: 'ASC'
                    }, function (result) {
                        vm.hospitals = vm.hospitals.concat(result.hospitals);
                        if (result.hospitals && result.hospitals.length == 0) {
                            vm.moredata = false;
                        }
                        if (result.pages <= vm.pagination.currentPage + 1) vm.moredata = false;
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    })
                }

            }
        };
        vm.init();

        var ddybUserId = localStorage.getItem('ddybUserId');


        $scope.vm.getHospital = function (doctorHospital) {
            doctorInforList.getHospital = doctorHospital;
            $ionicHistory.goBack(-1);
        };
    })


    .controller('HospitalDeptListCtrl', function ($scope, $ionicPopover, $ionicHistory, $stateParams, $timeout, ddHospitalDeptsServices, doctorInforList) {


        $scope.ctrlScope = $scope;
        //叮咚名医列表
        $scope.$on('$ionicView.enter', function (e) {
            doctorInforList.getDept = null;
            $scope.goBack = function () {
                $ionicHistory.goBack(-1);
            };
        });
        var vm = $scope.vm = {
            moredata: true,
            hospitalDepts: [],
            pagination: {
                perPage: 15,
                currentPage: 1
            },
            afterInit: false,
            init: function () {
                ddHospitalDeptsServices.getHospitalDepts({
                    id: $stateParams.hospitalId,
                    size: vm.pagination.perPage,
                    page: vm.pagination.currentPage
                }, function (result) {
                    vm.hospitalDepts = result.hospitalDepts;
                    vm.afterInit = true;
                    if (result.pages <= vm.pagination.currentPage + 1) vm.moredata = false;
                })
            },
            doRefresh: function () {
                $timeout(function () {

                    $scope.$broadcast('scroll.refreshComplete');

                }, 1000);
            },
            loadMore: function () {
                vm.pagination.currentPage += 1;
                if (vm.moredata && vm.afterInit) {
                    ddHospitalDeptsServices.getHospitalDepts({
                        id: $stateParams.hospitalId,
                        size: vm.pagination.perPage,
                        page: vm.pagination.currentPage
                    }, function (result) {
                        vm.hospitalDepts = vm.hospitalDepts.concat(result.hospitalDepts);
                        if (result.hospitalDepts && result.hospitalDepts.length == 0) {
                            vm.moredata = false;
                        }
                        if (result.pages <= vm.pagination.currentPage + 1) vm.moredata = false;
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    })

                }

            }
        };
        vm.init();

        var ddybUserId = localStorage.getItem('ddybUserId');

        $scope.vm.getDept = function (dept) {
            doctorInforList.getDept = dept;
            $ionicHistory.goBack(-1);
        };
    })


    .controller('DoctorHospitalAddCtrl', function ($scope, $location, $ionicPopover, $ionicPopup, ddAddDoctorHospitalServices, doctorInforList) {
        $scope.ctrlScope = $scope;
        //弹出确认对话框
        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        $scope.getHospital = function () {
            var urlPath = "/tab/hospitalList";
            $location.url(urlPath);
        };

        $scope.getDept = function (hospitalId) {
            if (hospitalId) {
                var urlPath = "/tab/hospitalDeptList/" + hospitalId;
                $location.url(urlPath);
            } else {
                showQueue("请先选择医院！");
            }
        };

        $scope.$on('$ionicView.enter', function () {

            $scope.ctrlScope.hospitalName = "";
            $scope.ctrlScope.hospitalId = "";
            $scope.ctrlScope.deptName = "";
            $scope.ctrlScope.deptId = "";

            if (doctorInforList.getHospital) {
                $scope.ctrlScope.hospitalName = doctorInforList.getHospital.name;
                $scope.ctrlScope.hospitalId = doctorInforList.getHospital.id;
            } else {

            }

            if (doctorInforList.getDept && doctorInforList.getHospital) {
                $scope.ctrlScope.deptName = doctorInforList.getDept.deptName;
                $scope.ctrlScope.deptId = doctorInforList.getDept.deptId;
            } else {

            }
        });


        $scope.showConfirmeAdd = function (str) {

            if ($scope.ctrlScope.hospitalName == null || $scope.ctrlScope.deptName == null || $scope.ctrlScope.minQueue == null || $scope.ctrlScope.registerFee == null) {
                showQueue("请补充完各项信息！");
            } else {
                var information = '<h4 style=" text-align:center;">' + $scope.ctrlScope.hospitalName + $scope.ctrlScope.deptName + '</h4>';
                var confirmPopup = $ionicPopup.confirm({
                    title: '<h4 >确定添加吗？</h4>',
                    template: information,
                    cancelText: '取消',
                    okText: '确定'
                });
                confirmPopup.then(function (res) {
                    if (res) {
                        var doctorHospital = null;
                        var doctorId = localStorage.getItem('doctorId');
                        var ddybUserId = localStorage.getItem('ddybUserId');
                        doctorHospital = {
                            "deptId": $scope.ctrlScope.deptId,
                            "deptName": $scope.ctrlScope.deptName,
                            "doctorId": doctorId,
                            "hospitalId": $scope.ctrlScope.hospitalId,
                            "hospitalName": $scope.ctrlScope.hospitalName,
                            "minQueue": $scope.ctrlScope.minQueue,
                            "registerFee": $scope.ctrlScope.registerFee,
                            "requestStatus": 0
                        };
                        ddAddDoctorHospitalServices.addDoctorHospital({}, doctorHospital);
                        console.log(doctorHospital);
                        var urlPath = 'tab/hospitals';
                        $location.url(urlPath);
                    } else {
                        console.log('取消');
                    }
                });
            }
        };
    })


    .controller('EnchashmentCtrl', function ($scope, $location, $ionicPopover, $ionicPopup, ddAddDoctorHospitalServices,ddDoctorCashApplyServices, doctorInforList) {
        $scope.ctrlScope = $scope;
        //弹出确认对话框
        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({
                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };


        $scope.getCard = function () {
                var urlPath = "/tab/accountList/getCard";
                $location.url(urlPath);
        };

        $scope.$on('$ionicView.enter', function () {

            $scope.ctrlScope.bankName = "";
            $scope.ctrlScope.cardNumber = "";
            $scope.ctrlScope.cardName = "";

            if (doctorInforList.card) {
                $scope.ctrlScope.bankName = doctorInforList.card.bankName;
                $scope.ctrlScope.cardNumber = doctorInforList.card.cardNumber;
                $scope.ctrlScope.cardName = doctorInforList.card.cardName;
            } else {

            }
        });


        $scope.cashApply = function (str) {

            if ($scope.ctrlScope.bankName == null || $scope.ctrlScope.cardNumber == null || $scope.ctrlScope.cardName == null) {
                showQueue("请补充完各项信息！");
            } else {
                var information = '<h4 style=" text-align:center;">' + $scope.ctrlScope.bankName +"<br/>"+ $scope.ctrlScope.cardNumber +"<br/>"+ $scope.ctrlScope.cardName+ '</h4>';
                var confirmPopup = $ionicPopup.confirm({
                    title: '<h4 >确定提交取现申请吗？</h4>',
                    template: information,
                    cancelText: '取消',
                    okText: '确定'
                });
                confirmPopup.then(function (res) {
                    if (res) {
                        ddDoctorCashApplyServices.postCashApply({accountId: doctorInforList.card.id,amount:  $scope.ctrlScope.amount}, function (result) {
                            showQueue(result.responseDesc);
                            var urlPath = '/tab/wxpay';
                            $location.url(urlPath);
                        });

                    } else {
                        console.log('取消');
                    }
                });
            }
        };
    })


    .controller('DoctorHospitalDetailCtrl', function ($scope, $stateParams, $ionicHistory, $location, $ionicPopup, $ionicPopover, $filter, doctorInforList, ddUpdateDoctorHospitalStatusServices, ddDoctorHospitalDetailServices, ddUpdateDoctorHospitalServices) {

        $scope.ctrlScope = $scope;

        ddybUserId = localStorage.getItem('ddybUserId');
        doctorId = localStorage.getItem('doctorId');
        ddDoctorHospitalDetailServices.get({id: $stateParams.id}, function (result) {
            $scope.hospital = result.doctorHospitals[0];
            $scope.ctrlScope.minQueue = result.doctorHospitals[0].minQueue;
            $scope.ctrlScope.registerFee = result.doctorHospitals[0].registerFee;
            $scope.ctrlScope.mainFlag = result.doctorHospitals[0].mainFlag;
            $scope.ctrlScope.deptId = result.doctorHospitals[0].deptId;
            $scope.ctrlScope.deptName = result.doctorHospitals[0].deptName;
            $scope.ctrlScope.requestDesc = result.doctorHospitals[0].requestDesc;
            $scope.ctrlScope.registerStatus = $filter('hospitalFilter')(result.doctorHospitals[0].status);
        });
        $scope.$on('$ionicView.enter', function () {


            if (doctorInforList.getDept) {
                $scope.ctrlScope.deptName = doctorInforList.getDept.deptName;
                $scope.ctrlScope.deptId = doctorInforList.getDept.deptId;
            } else {

            }

            $scope.goBack = function () {
                $ionicHistory.goBack(-1);
            };
        });


        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        /*
         //弹出确认对话框
         $scope.showConfirmOpen = function(str) {
         var information='<h4 style=" text-align:center;">'+ $scope.hospital.hospitalName+str+'</h4>';
         var confirmPopup = $ionicPopup.confirm({
         title: '<h4 >确定更开放排队吗？</h4>',
         template: information,
         cancelText:'取消',
         okText:'确定'
         });
         confirmPopup.then(function(res) {
         if(res) {

         ddUpdateDoctorHospitalStatusServices.updateDoctorHospitalStatus({id:$stateParams.id,status:1},function(result){
         showQueue("成功开放排队!");
         var   urlPath = '/tab/hospitals';
         $location.url(urlPath);
         });
         console.log("完成");
         } else {
         console.log('取消');
         }
         });
         };

         //弹出确认对话框
         $scope.showConfirmCancel = function(str) {
         var information='<h4 style=" text-align:center;">'+ $scope.hospital.hospitalName+str+'</h4>';
         var confirmPopup = $ionicPopup.confirm({
         title: '<h4 >确定取消排队吗？</h4>',
         template: information,
         cancelText:'取消',
         okText:'确定'
         });
         confirmPopup.then(function(res) {
         if(res) {
         ddUpdateDoctorHospitalStatusServices.updateDoctorHospitalStatus({id:$stateParams.id,status:0},function(result){
         showQueue("取消排队成功!");
         var   urlPath = '/tab/hospitals';
         $location.url(urlPath);
         });
         console.log('完成');
         } else {
         console.log('取消');
         }
         });
         };*/

        //弹出确认对话框
        $scope.showConfirmSave = function (str) {
//            var updatedHoctorHospital = null;
//            updatedHoctorHospital = {
//                "deptId": $scope.ctrlScope.deptId,
//                "deptName": $scope.ctrlScope.deptName,
//                "doctorId": doctorId,
//                "hospitalId": $scope.hospital.hospitalId,
//                "hospitalName": $scope.hospital.hospitalName,
//                "mainFlag":  $scope.ctrlScope.mainFlag,
//                "minQueue": $scope.ctrlScope.minQueue,
//                "registerFee": $scope.ctrlScope.registerFee,
//                "requestDesc": $scope.ctrlScope.requestDesc,
//                "requestStatus": $filter('hospitalRevFilter')($scope.ctrlScope.registerStatus)
//            };


//            if($scope.ctrlScope.registerStatus){
//                ddUpdateDoctorHospitalStatusServices.updateDoctorHospitalStatus({id:$stateParams.id,status:1},function(result){
//                    showQueue("成功开放排队!");
//                });
//            }else{
//                ddUpdateDoctorHospitalStatusServices.updateDoctorHospitalStatus({id:$stateParams.id,status:0},function(result){
//                    showQueue("取消排队成功!");
//                });
//            }


//            ddUpdateDoctorHospitalStatusServices.updateDoctorHospitalStatus({
//                id: $stateParams.id,
//                status: $filter('hospitalRevFilter')($scope.ctrlScope.registerStatus)
//            }, function (result) {
//                console.log($filter('hospitalRevFilter')($scope.ctrlScope.registerStatus));
//            });
            ddUpdateDoctorHospitalServices.updateDoctorHospital({
                id: $stateParams.id,
                deptName: $scope.ctrlScope.deptName,
                minQueue: $scope.ctrlScope.minQueue,
                registerFee: $scope.ctrlScope.registerFee,
                status: $filter('hospitalRevFilter')($scope.ctrlScope.registerStatus)
            }, function (result) {
                showQueue("保存成功!");
                $location.url('/tab/hospitals');
            });
        };

        $scope.getDept = function () {
            if ($stateParams.hospitalId) {
                var urlPath = "/tab/hospitalDeptList/" + $stateParams.hospitalId;
                $location.url(urlPath);
            } else {
                showQueue("请先选择医院！");
            }
        };

    })


    .controller('EvaluationsCtrl', function ($scope, $ionicPopover, ddEvaluationServices) {
        $scope.patients = ddEvaluationServices.getEvaluationsByDoctor();
        var promise = ddEvaluationServices.getEvaluationsByDoctor();
        promise.then(function (data) {
            $scope.evaluations = data;
        }, function (data) {
            $scope.evaluations = {error: '找不到评价信息！'};
        });
    })

    .controller('EvaluationDetailCtrl', function ($scope, $stateParams, $ionicPopup, $filter, ddEvaluationServices, dateFilter) {
        $scope.evaluation = ddEvaluationServices.get($stateParams.evaluationId);
        $scope.expanderTitle = '评价内容：';

        // 触发一个按钮点击，或一些其他目标
        $scope.showPopup = function () {
            $scope.data = {};

            // 一个精心制作的自定义弹窗
            var myPopup = $ionicPopup.show({
                template: '评分：<input type="number" ng-model="data.evaluationScore"><br>评语：<input type="text" ng-model="data.evaluationDesc">',
                title: '请输入评语',
                subTitle: '请公正客观的进行评价！',
                scope: $scope,
                buttons: [
                    {text: '取消'},
                    {
                        text: '<b>保存</b>',
                        type: 'button-positive',
                        onTap: function (e) {
                            if (!$scope.data.evaluationScore && !$scope.data.evaluationDesc) {

                                e.preventDefault();

                            } else {
                                return $scope.data.evaluationScore + !$scope.data.evaluationDesc;
                            }
                        }
                    }
                ]
            });
        };
    })

    .controller('ScheduleCtrl', function ($scope, $timeout, $location, $rootScope, $stateParams, $filter, $ionicPopup, dateFilter, ddPostponeScheduleByIdServices, ddDoctorSchedulesServices, ddDoctorScheduleDatesServices) {
        $scope.ctrlScope = $scope;
        var currentdate = new Date();
        $scope.calendar = {};
        $scope.calendar.mode = 'month';
        sessionStorage.setItem('currentDate', beginDate);
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        var scheduleList = $scope.schduleDateList = [];
        var nowTime = new Date();
        var beginDate = dateFilter(nowTime, 'yyyy-MM-dd');
        var endTime = new Date(nowTime.valueOf() + 1 * 24 * 60 * 60 * 1000 * 30);
        var endDate = dateFilter(endTime, 'yyyy-MM-dd');

        sessionStorage.setItem('currentDate', beginDate);
        $scope.shows = true;

        $scope.$on('$ionicView.enter', function (e) {

            $scope.scheduleType0 = "button";
            $scope.scheduleType1 = "button button-positive";
            $scope.calendarHide = false;
            ddDoctorScheduleDatesServices.getDoctorScheduleDates({
                doctorId: doctorId,
                beginDate: beginDate,
                endDate: endDate
            }, function (result) {
                $scope.schedules = $scope.schduleDateList = result.schduleDateList;
                $scope.showsHide = true;

                var current = sessionStorage.getItem('currentDate');
                if (current != 'undefined') {
                    ddDoctorSchedulesServices.getDoctorSchedules({
                        doctorId: doctorId,
                        beginDate: current,
                        endDate: current
                    }, function (result) {
                        $scope.showsHide = false;
                        $scope.schedules = result.schedules;
                    });
                }
            });
        });

        $scope.showSchedule = function (str) {

            if (str == "0") {
                $scope.scheduleType1 = "button";
                $scope.scheduleType0 = "button button-positive";
                $scope.calendarHide = true;
                $scope.showsHide = true;
                ddDoctorSchedulesServices.getDoctorSchedules({
                    doctorId: doctorId,
                    beginDate: beginDate,
                    endDate: endDate
                }, function (result) {
                    $scope.schedules = result.schedules;
                    $scope.showsHide = false;
                });
            } else {
                $scope.scheduleType0 = "button";
                $scope.scheduleType1 = "button button-positive";
                $scope.calendarHide = false;
                ddDoctorScheduleDatesServices.getDoctorScheduleDates({
                    doctorId: doctorId,
                    beginDate: beginDate,
                    endDate: endDate
                }, function (result) {
                    $scope.schedules = $scope.schduleDateList = result.schduleDateList;
                    $scope.showsHide = true;

                    var current = sessionStorage.getItem('currentDate');
                    if (current != 'undefined') {
                        ddDoctorSchedulesServices.getDoctorSchedules({
                            doctorId: doctorId,
                            beginDate: current,
                            endDate: current
                        }, function (result) {
                            $scope.showsHide = false;
                            $scope.schedules = result.schedules;
                        });
                    }
                });
            }
        };


        ddDoctorSchedulesServices.getDoctorSchedules({
            doctorId: doctorId,
            beginDate: beginDate,
            endDate: beginDate
        }, function (result) {
            $scope.schedules = result.schedules;
        });
        var selectedDateTime = nowTime;
        var selectedDate = dateFilter(selectedDateTime, 'yyyy-MM-dd');
        $scope.onTimeSelected = function (selectedTime) {
            selectedDateTime = selectedTime;
            selectedDate = dateFilter(selectedTime, 'yyyy-MM-dd');
            $scope.timeTitle = selectedDate;
            if (selectedDate >= beginDate) {
                ddDoctorSchedulesServices.getDoctorSchedules({
                    doctorId: doctorId,
                    beginDate: selectedDate,
                    endDate: selectedDate
                }, function (result) {
                    $scope.showsHide = false;
                    $scope.schedules = result.schedules;
                });
            }
            sessionStorage.setItem('currentDate', selectedDate);
        };
        $scope.onEventSelected = function (event) {
            console.log('Event selected:' + event.startTime + '-' + event.endTime + ',' + event.title);
        };
        $scope.onTimeTitleChanged = function (title) {
            $scope.timeTitle = title;
        };

        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        //弹出确认对话框
        $scope.showConfirmeAdd = function (str) {
            if ((selectedDate >= beginDate) && (selectedDateTime.valueOf() - nowTime.valueOf()) < 1 * 24 * 60 * 60 * 1000 * 30) {
                var urlPath = 'tab/scheduleDoctorHospitals';
                $location.url(urlPath);
            } else {
                showQueue("请添加30天以内的行程！");
            }
        };

        //弹出确认对话框
        $scope.delaychedule = function (schedule) {
//            var message = '<h4 style=" text-align:center;">' + schedule.hospitalName+'<br/>' +  schedule.scheduleDate+ $filter('timeSlot')(schedule.timeSlot) +'</h4>';
//            var confirmPopup = $ionicPopup.confirm({
//                title: '<h4 >您确定推迟如下行程吗？</h4>',
//                template: message,
//                cancelText: '取消',
//                okText: '确定'
//            });
//            confirmPopup.then(function (res) {
//                if (res) {
//                    ddPostponeScheduleByIdServices.postponeScheduleById({id:schedule.id},function(result){
//
//                    });
//                    ddDoctorScheduleDatesServices.getDoctorScheduleDates({doctorId:doctorId,beginDate:beginDate,endDate:endDate},function(result){
//                        $scope.schedules= $scope.schduleDateList =result.schduleDateList;
//                        $scope.showsHide = true;
//
//                        var current=sessionStorage.getItem('currentDate');
//                        ddDoctorSchedulesServices.getDoctorSchedules({doctorId:doctorId,beginDate:current,endDate:current},function(result){
//                            $scope.showsHide = false;
//                            $scope.schedules =result.scheduleList;
//                        });
//                    });
//                    showQueue("成功推迟本行程！");
//                } else {
//                    console.log('取消');
//                }
//            });

            var urlPath = 'tab/postponeSchedule/' + schedule.id;
            $location.url(urlPath);
        };
    })

    .controller('AddScheduleCtrl', function ($scope, $filter, $location, $ionicPopover, $ionicPopup, $stateParams, ddAddScheduleServices, ddDoctorHospitalDetailServices) {
        $scope.ctrlScope = $scope;
        $scope.timeSlotList = [{"name": "全天", "value": 0}, {"name": "上午", "value": 1},
            {"name": "下午", "value": 2}, {"name": "晚上", "value": 3}];

        $scope.datepickerObject = {
            setButtonType: 'button-assertive',  //Optional
            inputDate: sessionStorage.getItem('currentDate'),  //Optional
            mondayFirst: true,  //Optional
            templateType: 'modal', //Optional
            showTodayButton: 'true', //Optional
            modalHeaderColor: 'bar-positive', //Optional
            modalFooterColor: 'bar-positive', //Optional
            from: new Date(1910, 8, 2), //Optional
            to: new Date(2030, 8, 25),  //Optional
            callback: function (val) {    //Mandatory
                datePickerCallback(val);
            }
        };
        var doctorHospital = null;
        ddDoctorHospitalDetailServices.get({id: $stateParams.doctorHospitalId}, function (result) {
            $scope.hospital = result.doctorHospitals[0];
            $scope.ctrlScope.minQueue = result.doctorHospitals[0].minQueue;
            $scope.ctrlScope.registerFee = result.doctorHospitals[0].registerFee;
            $scope.ctrlScope.deptName = result.doctorHospitals[0].deptName;
            $scope.ctrlScope.hospitalName = result.doctorHospitals[0].hospitalName;
            $scope.ctrlScope.timeSlot = 0;
            doctorHospital = result.doctorHospitals[0];
        });

        //弹出确认对话框
        $scope.showConfirmeAdd = function (str) {
            var orderTime = '<h4 style=" text-align:center;">' + doctorHospital.hospitalName + '<br/>' + $filter('dateTranfer')(sessionStorage.getItem('currentDate')) + $filter('timeSlot')($scope.ctrlScope.timeSlot) + '</h4>';
            var confirmPopup = $ionicPopup.confirm({
                title: '<h4 >您确定添加如下行程吗？</h4>',
                template: orderTime,
                cancelText: '取消',
                okText: '确定'
            });
            confirmPopup.then(function (res) {
                if (res) {
                    //添加预约信息
                    var schedule = null;
                    var doctorId = localStorage.getItem('doctorId');
                    var ddybUserId = localStorage.getItem('ddybUserId');
                    var hospitalId = doctorHospital.hospitalId;

                    schedule = {
                        "doctorId": doctorId,
                        "hospitalId": hospitalId,
                        "issueNum": $scope.ctrlScope.minQueue,
                        "scheduleDate": (sessionStorage.getItem('currentDate')),
                        "timeSlot": $scope.ctrlScope.timeSlot
                    };
                    ddAddScheduleServices.addSchedule({}, schedule);
                    console.log(schedule);
                    var urlPath = 'tab/schedules';
                    $location.url(urlPath);

                } else {
                    console.log('取消');
                }
            });
        };
    })

    .controller('PostponeScheduleCtrl', function ($scope, $filter, $location, $ionicHistory, $ionicPopover, $ionicPopup, $stateParams, dateFilter, ddAddScheduleServices, ddDoctorHospitalDetailServices, ddPostponeScheduleServices) {
        $scope.ctrlScope = $scope;

        var data = new Date();

        $scope.goBack = function () {
            $ionicHistory.goBack(-1);
        };
        $scope.datepickerObject = {
            setButtonType: 'button-assertive',  //Optional
            inputDate: new Date(),  //Optional
            mondayFirst: true,  //Optional
            templateType: 'modal', //Optional
            showTodayButton: 'true', //Optional
            modalHeaderColor: 'bar-positive', //Optional
            modalFooterColor: 'bar-positive', //Optional
            from: new Date(1910, 8, 2), //Optional
            to: new Date(2030, 8, 25),  //Optional
            callback: function (val) {    //Mandatory
                datePickerCallback(val);
            }
        };
        var datePickerCallback = function (val) {
            if (typeof(val) === 'undefined') {
                console.log('No date selected');
            } else {
                $scope.datepickerObject.inputDate = val;
                console.log('Selected date is : ', val)
            }
        };
        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        $scope.postponeSchedule = function () {
            ddPostponeScheduleServices.postponeSchedule({
                id: $stateParams.scheduleId,
                scheduleDate: dateFilter($scope.datepickerObject.inputDate, 'yyyy-MM-dd')
            }, function (result) {
                showQueue("延迟成功！");

                var urlPath = 'tab/schedules';
                $location.url(urlPath);

            });
        }
    })


    .controller('ScheduleDetailCtrl', function ($scope, $timeout, $stateParams, $filter, $location, ddDoctorSchedulesServices) {

        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        ddDoctorSchedulesServices.getDoctorSchedules({
            doctorId: doctorId,
            beginDate: $stateParams.selectedDate,
            endDate: $stateParams.selectedDate
        }, function (result) {
            $scope.schedules = result.scheduleList;
        });

        $scope.doRefresh = function () {

            console.log('Refreshing!');
            $timeout(function () {
                //simulate async response
                ddDoctorSchedulesServices.getDoctorSchedules({
                    doctorId: doctorId,
                    beginDate: $stateParams.selectedDate,
                    endDate: $stateParams.selectedDate
                }, function (result) {
                    $scope.schedules = result.scheduleList;
                });

                //Stop the ion-refresher from spinning
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };

        //弹出确认对话框
        $scope.showConfirmeRgister = function (str) {
            var orderTime = '<h4 style=" text-align:center;">' + $filter('dateTranfer')(sessionStorage.getItem('currentDate')) + str + '</h4>';
            var urlPath = 'tab/scheduleDoctorHospitals';
            $location.url(urlPath);
        };
    })


    .controller('FunInformationCtrl', function ($scope, $timeout,$ionicHistory, $stateParams, $filter, $location, ddUserServices, getRegisterByStatusServices) {

        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.$on('$ionicView.enter', function (e) {
            $scope.goBack = function () {
                $ionicHistory.goBack(-1);
            };
            ddUserServices.get({userId: $stateParams.userId}, function (result) {
                $scope.user = result.users[0];
            });

            getRegisterByStatusServices.getRegisterByStatus({
                userId: $stateParams.userId,
                status: 5
            }, function (result) {
                $scope.registersHistorys = result.registers;
                if ($scope.registersHistorys && $scope.registersHistorys.length > 0) {
                    $scope.showOrNot = false;
                } else {
                    $scope.showOrNot = true;
                }
            })
        });
    })

    .controller('UserTransferCtrl', function ($scope, $timeout, $stateParams, $filter, $location,$ionicPopup, ddUserServices, ddUserTransfersServices) {

        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.expanderTitle = '账户明细';
        var vm = $scope.vm = {
            moredata: true,
            doctors: [],
            pagination: {
                perPage: 15,
                currentPage: 1
            },
            init: function () {
                ddUserTransfersServices.getUserTransfers({
                    userId: ddybUserId,
                    type: $stateParams.transferType,
                    size: vm.pagination.perPage,
                    page: vm.pagination.currentPage
                }, function (result) {
                    vm.transferList = result.transferList;
                    vm.transferType0 = "button";
                    vm.transferType1 = "button";
                    if ($stateParams.transferType == "0") {
                        vm.transferType = "余额账单";
                        vm.transferType0 = "button activated";
                    } else {
                        vm.transferType = "积分账单";
                        vm.transferType1 = "button activated";
                    }

                    angular.forEach(vm.transferList, function (data, index, array) {
                        //data等价于array[index]
                        ddUserServices.get({userId: array[index].fromUserId}, function (result) {
                            $scope.user = (result.users)[0];

                            if (array[index].fromUserId == "0") {
                                vm.transferList[index].headImgUrl = "http://www.yushansoft.com/dingdong/images/sys/money_bag.png";
                            } else {
                                vm.transferList[index].headImgUrl = (result.users)[0].headImgUrl;
                            }
                        });
                    });


                    if (vm.transferList && vm.transferList.length > 0) {
                        $scope.ctrlScope.showOrNot = false;
                    } else {
                        $scope.ctrlScope.showOrNot = true;
                    }
                })


            },
            doRefresh: function () {
                console.log('Refreshing!');
                $timeout(function () {
                    //simulate async response
                    ddUserTransfersServices.getUserTransfers({
                        userId: ddybUserId,
                        type: $stateParams.transferType,
                        size: 15,
                        page: 1
                    }, function (result) {
                        vm.transferList = result.transferList;
                        if (vm.transferList && vm.transferList.length > 0) {
                            $scope.ctrlScope.showOrNot = false;
                        } else {
                            $scope.ctrlScope.showOrNot = true;
                        }
                    });
                    //Stop the ion-refresher from spinning
                    $scope.$broadcast('scroll.refreshComplete');
                }, 1000);
            },
            loadMore: function () {
                vm.pagination.currentPage += 1;
                if (vm.moredata) {
                    ddUserTransfersServices.getUserTransfers({
                        userId: ddybUserId,
                        type: $stateParams.transferType,
                        size: vm.pagination.perPage,
                        page: vm.pagination.currentPage
                    }, function (result) {

                        vm.transferList = vm.transferList.concat(result.transferList);
                        if (result.transferList && result.transferList.length == 0) {
                            vm.moredata = false;
                        }
                        if (result.pages <= vm.pagination.currentPage + 1) vm.moredata = false;
                        $scope.$broadcast('scroll.infiniteScrollComplete');

                    })
                }
            }
        };
        vm.init();

        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        //弹出确认对话框
        $scope.showConfirme = function (str) {
            //弹出确认对话框
            showQueue("　1）什么是叮咚？<br/>        &nbsp;&nbsp;&nbsp;&nbsp;叮咚是叮咚医邦在线给予广大用户的虚拟货币的奖励，可以用于支付，直抵现金。                <br/>  &nbsp;&nbsp;&nbsp; 2）叮咚折抵现金比例是多少？<br/>        &nbsp;&nbsp;&nbsp;&nbsp; 叮咚折抵现金比例是100:1，100叮咚相当于1元人民币。");
        };

        $scope.showTransfer = function (str) {

            ddUserTransfersServices.getUserTransfers({
                userId: ddybUserId,
                type: str,
                size: vm.pagination.perPage,
                page: vm.pagination.currentPage
            }, function (result) {
                vm.transferList = result.transferList;
                vm.transferType0 = "button";
                vm.transferType1 = "button";
                if (str == "0") {
                    vm.transferType = "余额账单";
                    vm.transferType0 = "button activated";
                } else {
                    vm.transferType = "积分账单";
                    vm.transferType1 = "button activated";
                }

                angular.forEach(vm.transferList, function (data, index, array) {
                    //data等价于array[index]
                    ddUserServices.get({userId: array[index].fromUserId}, function (result) {
                        $scope.user = (result.users)[0];

                        if (array[index].fromUserId == "0") {
                            vm.transferList[index].headImgUrl = "http://www.yushansoft.com/dingdong/images/sys/money_bag.png";
                        } else {
                            vm.transferList[index].headImgUrl = (result.users)[0].headImgUrl;
                        }
                    });
                });


                if (vm.transferList && vm.transferList.length > 0) {
                    $scope.ctrlScope.showOrNot = false;
                } else {
                    $scope.ctrlScope.showOrNot = true;
                }
            })
        }


    })

    .controller('SmsValidCtrl', function ($scope, $filter, $ionicPopover, $location, $ionicPopup, $stateParams, ddUserServices, ddSmsValiServices, ddUpdateSmsValiServices) {

        $scope.ctrlScope = $scope;
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');


        ddUserServices.get({userId: ddybUserId}, function (result) {
            $scope.user = result.users[0];
            $scope.ctrlScope.phoneOld = result.users[0].phone;
        });
        var countdown = 60;

        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        $scope.ctrlScope.countdown = "获取验证码";
        $scope.showConfirmeGetValid = function (obj) {

            var mobileNo = $scope.ctrlScope.mobileNo;
            if (mobileNo != null) {
                var imgCode = 1;
                ddSmsValiServices.validNum({mobileNo: mobileNo}, function (result) {

                });

                setTimeout(function () {
                    clearInterval(myTime);
                    $scope.ctrlScope.countdown = "获取验证码";
                    countdown = 60;
                }, 61000);

                var myTime = setInterval(function () {

                    if (countdown == "1") {
                        $scope.ctrlScope.countdown = "获取验证码";
                        $scope.ctrlScope.getValidDisable = false;
                    } else {
                        countdown--;
                        $scope.ctrlScope.countdown = "重新发送（" + countdown + "）";
                        $scope.ctrlScope.getValidDisable = true;
                    }

                    $scope.ctrlScope.$digest(); // 通知视图模型的变化

                }, 1000);
            } else {
                showQueue("请输入要验证的手机号码！");
            }
        };


        $scope.showConfirmePutValid = function (str) {

            var mobileNo = $scope.ctrlScope.mobileNo;
            var msgCode = $scope.ctrlScope.msgCode;
            var imgCode = 1;
            if (mobileNo != null) {
                ddUpdateSmsValiServices.updateSmsValid({
                    userId: ddybUserId,
                    mobileNo: mobileNo,
                    msgCode: msgCode,
                    imgCode: imgCode
                }, function (result) {
                    if (result.retMap.ok == '1') {
                        var order = '<h4 style=" text-align:center;">' + mobileNo + '</h4>';
                        var confirmPopup = $ionicPopup.confirm({
                            title: '<h4 >验证通过：</h4>',
                            template: order,
                            cancelText: '取消',
                            okText: '确定'
                        });
                        confirmPopup.then(function (res) {
                            if (res) {
                                ddUserServices.get({userId: ddybUserId}, function (result) {
                                    $scope.user = result.users[0];
                                    $scope.ctrlScope.phoneOld = result.users[0].phone;
                                });

                                console.log('完成');
                                var urlPath = '/tab/smsValid';
                                $location.url(urlPath);

                            } else {
                                console.log('取消');
                            }
                        });
                    } else {
                        alert(result.retMap.msg);
                    }
                })
            }else{
                showQueue("请输入要验证的手机号码！");
            }

        }

    })

    .controller('SmsValidHomePageCtrl', function ($scope, $filter, $ionicPopover, $location, $ionicPopup, $stateParams, ddUserServices, ddSmsValiServices, ddUpdateSmsValiServices,doctorInforList) {

        $scope.ctrlScope = $scope;
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');


        ddUserServices.get({userId: ddybUserId}, function (result) {
            $scope.user = result.users[0];
            $scope.ctrlScope.phoneOld = result.users[0].phone;
        });
        var countdown = 60;

        showQueue = function (str) {
            var alertPopup = $ionicPopup.alert({

                template: '<h3>' + str + '</h3>',
                buttons: [
                    {text: '确定'}]
            });
            alertPopup.then(function (res) {
                console.log(str);
            });
        };

        $scope.ctrlScope.countdown = "获取验证码";
        $scope.showConfirmeGetValid = function (obj) {

            var mobileNo = $scope.ctrlScope.mobileNo;
            if (mobileNo != null) {
                var imgCode = 1;
                ddSmsValiServices.validNum({mobileNo: mobileNo}, function (result) {
                    if(result.retMap.ok == "0"){
                        showQueue(result.retMap.msg);
                    }else{
                        setTimeout(function () {
                            clearInterval(myTime);
                            $scope.ctrlScope.countdown = "获取验证码";
                            countdown = 60;
                        }, 61000);

                        var myTime = setInterval(function () {

                            if (countdown == "1") {
                                $scope.ctrlScope.countdown = "获取验证码";
                                $scope.ctrlScope.getValidDisable = false;
                            } else {
                                countdown--;
                                $scope.ctrlScope.countdown = "重新发送（" + countdown + "）";
                                $scope.ctrlScope.getValidDisable = true;
                            }

                            $scope.ctrlScope.$digest(); // 通知视图模型的变化

                        }, 1000);
                    }
                });
            } else {
                showQueue("请输入要验证的手机号码！");
            }
        };

        $scope.showConfirmePutValid = function (str) {

            var mobileNo = $scope.ctrlScope.mobileNo;
            var msgCode = $scope.ctrlScope.msgCode;
            var imgCode = 1;
            if (mobileNo != null) {
                ddUpdateSmsValiServices.updateSmsValid({
                    userId: ddybUserId,
                    mobileNo: mobileNo,
                    msgCode: msgCode,
                    imgCode: imgCode,
                    mode:1
                }, function (result) {
                    if (result.retMap.ok == '1' && result.retMap.doctorId) {
                        var doctorId = result.retMap.doctorId;
                        localStorage["doctorId"] = doctorId;
                        localStorage.setItem("doctorId",doctorId);
                        doctorInforList.doctorId = doctorId;
                        var urlPath = 'http://www.yushansoft.com/dingdong/v1/dddoctor/index.html#/tab/patientsAll';
                        $location.url(urlPath);
                        console.log('完成');

                    } else {
                        showQueue("您还未入驻，请与客服联系！");
                    }
                })
            }else{
                showQueue("请输入要验证的手机号码！");
            }
        }
    })




    .controller('DoctorCodeCtrl', function ($scope, $ionicPopup, $ionicPopover, ddDoctorInformationServices, ddUpdateDoctorInformationServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddUserId = localStorage.getItem('ddUserId');
        $scope.ctrlScope = $scope;
        ddDoctorInformationServices.get({id: doctorId}, function (result) {
            $scope.doctor = result.doctors[0];
            $scope.ctrlScope.name = result.doctors[0].name;
            $scope.ctrlScope.hospitalName = result.doctors[0].hospitalName;
            $scope.ctrlScope.level = result.doctors[0].level;
        });
    })

    .controller('AccountAllCtrl', function ($scope, $ionicPopup, $ionicPopover, $timeout, $q, ddUserServices,ddDoctorInformationServices, ddUpdateDoctorInformationServices, getUnSignedDoctorsServices, submitSignDoctorsterServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;
        var doctor = {
            name: ''
        };
        $scope.doctor = doctor;
        var confirmPopup;//确认医生信息对话框
        var myPopup;//输入医生姓名对话框
//        showPopup = $scope.showPopup = function () {
//            $scope.data = {};
//            // An elaborate, custom popup
//            myPopup = $ionicPopup.show({
//                template: '<input type="text" ng-model="doctor.name">',
//                title: '未加入叮咚医邦',
//                subTitle: '请输入医生姓名加入叮咚医邦',
//                scope: $scope,
//                buttons: [
//                    {text: '取消'},
//                    {
//                        text: '<b>加入</b>',
//                        onTap: function (e) {
//                            var promise = getDoctorInfor();
//                            promise.then(function (result) {
//                                var doctor = result.doctors[0];
//                                showDoctorInfor(doctor);
//                            }).then(function () {
//
//                            });
//                            e.preventDefault();
//
//                        }
//                    },
//                ]
//            });
//
//            $timeout(function () {
//                myPopup.close(); //close the popup after 3 seconds for some reason
//            }, 60000);
//        };

        getDoctorInfor = function () {
            if ($scope.doctor.name) {
                var deferred = $q.defer();
                getUnSignedDoctorsServices.get({doctorName: $scope.doctor.name}, function (result) {
                    deferred.resolve(result);
                });
                return deferred.promise;
            }
        };

//        showDoctorInfor = $scope.showDoctorInfor = function (doctor) {
//            confirmPopup = $ionicPopup.confirm({
//                title: '系统中您的信息为',
//                template: '<p>' + doctor.name + '</p>' + '<p>' + doctor.hospitalName + '</p>' + '<p>' + doctor.level + '</p>',
//                buttons: [
//                    {text: '取消'},
//                    {
//                        text: '<b>确认</b>',
//                        onTap: function (e) {
//                            submitSignDoctorsterServices.submitSign({
//                                id: doctor.id,
//                                userId: ddybUserId
//                            }, function (result) {
//                                if (result.responseDesc == 'OK') {
//                                    showAlert();
//
//                                }
//                            });
//                            e.preventDefault();
//                        }
//                    }]
//            });
//        };
        // An alert dialog
//        showAlert = $scope.showAlert = function () {
//            var alertPopup = $ionicPopup.alert({
//                title: '恭喜您',
//                template: '您已申请加入叮咚医邦，请等待系统确认！',
//                buttons: [
//                    {text: '确定'}]
//
//            });
//            alertPopup.then(function (res) {
//                confirmPopup.close();
//                myPopup.close();
//            });
//        };
        $scope.$on('$ionicView.enter', function (e) {
            if (doctorId) {
                ddDoctorInformationServices.get({id: doctorId}, function (result) {
                    $scope.doctor = result.doctors[0];
                    $scope.ctrlScope.name = result.doctors[0].name;
                    $scope.ctrlScope.gender = result.doctors[0].gender;
                    $scope.ctrlScope.level = result.doctors[0].level;

                    ddUserServices.get({userId: ddybUserId}, function (result) {
                        $scope.user = result.users[0];
                        $scope.ctrlScope.phoneOld = result.users[0].phone;
                    });

                    $scope.inforUrl = '#/tab/doctorInformation';
                });
            } else {
//                showPopup();
            }
        });
    })

    .controller('ScoreCtrl', function ($scope, $ionicPopup, $ionicPopover, ddUserServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;

        $scope.$on('$ionicView.enter', function (e) {
            ddUserServices.get({userId: doctorId}, function (result) {
                $scope.user = result.users[0];
                $scope.ctrlScope.score = result.users[0].score;
            });
        });

    })


    .controller('WxpayCtrl', function ($scope, $ionicPopup, $timeout, $stateParams, $ionicHistory, $ionicPopover, ddUserTransfersServices, $window, ddUserServices) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;
        $scope.expanderTitle = '财富明细';
        $scope.$on('$ionicView.enter', function (e) {
            $scope.goBack = function () {
                $ionicHistory.goBack(-1);
            };
            ddUserServices.get({userId: ddybUserId}, function (result) {
                $scope.user = result.users[0];
                $scope.ctrlScope.balance = result.users[0].balance;
                $scope.ctrlScope.score = result.users[0].score;
            });

            var vm = $scope.vm = {
                moredata: true,
                doctors: [],
                pagination: {
                    perPage: 15,
                    currentPage: 1
                },
                init: function () {
                    ddUserTransfersServices.getUserTransfers({
                        userId: ddybUserId,
                        type: $stateParams.transferType,
                        size: vm.pagination.perPage,
                        page: vm.pagination.currentPage
                    }, function (result) {
                        vm.transferList = result.transferList;
                        vm.transferType0 = "button";
                        vm.transferType1 = "button";
                        if ($stateParams.transferType == "0") {
                            vm.transferType = "余额账单";
                            vm.transferType0 = "button activated";
                        } else {
                            vm.transferType = "积分账单";
                            vm.transferType1 = "button activated";
                        }

                        angular.forEach(vm.transferList, function (data, index, array) {
                            //data等价于array[index]
                            ddUserServices.get({userId: array[index].fromUserId}, function (result) {
                                $scope.user = (result.users)[0];

                                if (array[index].fromUserId == "0") {
                                    vm.transferList[index].headImgUrl = "http://www.yushansoft.com/dingdong/images/sys/money_bag.png";
                                } else {
                                    vm.transferList[index].headImgUrl = (result.users)[0].headImgUrl;
                                }
                            });
                        });


                        if (vm.transferList && vm.transferList.length > 0) {
                            $scope.ctrlScope.showOrNot = false;
                        } else {
                            $scope.ctrlScope.showOrNot = true;
                        }
                    })


                },
                doRefresh: function () {
                    console.log('Refreshing!');
                    $timeout(function () {
                        //simulate async response
                        ddUserTransfersServices.getUserTransfers({
                            userId: ddybUserId,
                            type: $stateParams.transferType,
                            size: 15,
                            page: 1
                        }, function (result) {
                            vm.transferList = result.transferList;
                            if (vm.transferList && vm.transferList.length > 0) {
                                $scope.ctrlScope.showOrNot = false;
                            } else {
                                $scope.ctrlScope.showOrNot = true;
                            }
                        });
                        //Stop the ion-refresher from spinning
                        $scope.$broadcast('scroll.refreshComplete');
                    }, 1000);
                },
                loadMore: function () {
                    vm.pagination.currentPage += 1;
                    if (vm.moredata) {
                        ddUserTransfersServices.getUserTransfers({
                            userId: ddybUserId,
                            type: $stateParams.transferType,
                            size: vm.pagination.perPage,
                            page: vm.pagination.currentPage
                        }, function (result) {

                            vm.transferList = vm.transferList.concat(result.transferList);
                            if (result.transferList && result.transferList.length == 0) {
                                vm.moredata = false;
                            }
                            if (result.pages <= vm.pagination.currentPage + 1) vm.moredata = false;
                            $scope.$broadcast('scroll.infiniteScrollComplete');

                        })
                    }
                }
            };
            vm.init();


        });

        $scope.doRefresh = function () {
            console.log('Refreshing!');
            $timeout(function () {
                //simulate async response
                ddUserServices.get({userId: doctorId}, function (result) {
                    $scope.user = result.users[0];
                    $scope.ctrlScope.balance = result.users[0].balance;
                });
                //Stop the ion-refresher from spinning
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };

        //弹出确认对话框
        $scope.showConfirmeCharge = function (str) {
            //弹出确认对话框
            var urlPath = 'http://www.yushansoft.com/dingdong/sys/wxpay/tab-wxpay.html';
            $window.open(urlPath);
        };
    })

    .controller('AccountCtrl', function ($scope) {
        $scope.settings = {
            enableFriends: true
        };
    })


    .controller('AccountListCtrl', function ($scope, $ionicPopup, $timeout,$rootScope,$stateParams, $ionicPopover, $location,$ionicHistory,ddDeleteDoctorAccountServices, ddDoctorAccountsServices,doctorInforList) {
        var doctorId = localStorage.getItem('doctorId');
        var ddybUserId = localStorage.getItem('ddybUserId');
        $scope.ctrlScope = $scope;

        $scope.$on('$ionicView.enter', function (e) {
            ddDoctorAccountsServices.getDoctorAccounts({
                userId: ddybUserId
            }, function (result) {
                $scope.ctrlScope.accounts = result.accounts;
            });
            $scope.goBack = function () {
                $ionicHistory.goBack(-1);
            };
            $scope.ctrlScope.getAccount = function (account) {
                if($stateParams.operationType && $stateParams.operationType=="getCard"){
                    doctorInforList.card = account;
                    $ionicHistory.goBack(-1);
                }
            };

        });

        $scope.doRefresh = function () {
            console.log('Refreshing!');
            $timeout(function () {
                //simulate async response
                ddDoctorAccountsServices.getDoctorAccounts({
                    userId: ddybUserId
                }, function (result) {
                    $scope.ctrlScope.accounts = result.accounts;
                });
                $scope.$broadcast('scroll.refreshComplete');

            }, 1000);
        };

        $scope.showConfirmeAdd = function (str) {
            var urlPath = 'tab/addAccount';
            $location.url(urlPath);
        };


        $scope.remove = function (account) {
            ddDeleteDoctorAccountServices.deleteDoctorAccount({id: account.id}, function (result) {
                ddDoctorAccountsServices.getDoctorAccounts({
                    userId: ddybUserId
                }, function (result) {
                    $scope.ctrlScope.accounts = result.accounts;
                });
            });
            console.log('完成');
        };


    })



.controller('AddAccountCtrl', function ($scope, $filter, $location, $ionicPopover, $ionicPopup, $stateParams, ddAddDoctorAccountsServices) {
    $scope.ctrlScope = $scope;

    //弹出确认对话框
    $scope.showConfirmeAdd = function (str) {
        var message = '<h4 style=" text-align:center;">' + $scope.ctrlScope.bankName + '<br/>' + $scope.ctrlScope.cardNumber  + '<br/>' +$scope.ctrlScope.cardName +'</h4>';
        var confirmPopup = $ionicPopup.confirm({
            title: '<h4 >您确定添加如下账户吗？</h4>',
            template: message,
            cancelText: '取消',
            okText: '确定'
        });
        confirmPopup.then(function (res) {
            if (res) {

                var accounts = null;
                var ddybUserId = localStorage.getItem('ddybUserId');


               var request =  {
                    "accounts": [
                    {
                        "bankAddress": $scope.ctrlScope.bankAddress,
                        "bankBranchName": $scope.ctrlScope.bankBranchName,
                        "bankName": $scope.ctrlScope.bankName,
                        "cardName": $scope.ctrlScope.cardName,
                        "cardNumber": $scope.ctrlScope.cardNumber,
                        "userId": ddybUserId
                    }
                ],
                    "requestDesc": "string",
                    "requestStatus": 0
                };

//                accounts = [{
//                    "bankAddress": $scope.ctrlScope.bankAddress,
//                    "bankBranchName": $scope.ctrlScope.bankBranchName,
//                    "bankName": $scope.ctrlScope.bankName,
//                    "cardName": $scope.ctrlScope.cardName,
//                    "cardNumber": $scope.ctrlScope.cardNumber,
//                    "userId": ddybUserId
//                }];


                ddAddDoctorAccountsServices.addDoctorAccounts({}, request);
                console.log(request);
                var urlPath = 'tab/accountList';
                $location.url(urlPath);

            } else {
                console.log('取消');
            }
        });
    };
});