/**
 * Created by Administrator on 2015/10/29.
 */
var dddoctorRoutes = angular.module('dddoctorRoutes', []);

dddoctorRoutes.config(function($stateProvider, $urlRouterProvider,$ionicConfigProvider) {

    $stateProvider
        .state('tab', {
            url: '/tab',
            abstract: true,
            templateUrl: 'templates/tabs.html',
            controller: 'PatientsAllCtrl'
        })

        // Each tab has its own nav history stack:


        .state('tab.patientsAll', {
            url: '/patientsAll',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/tab-patientsAll.html',
                    controller: 'PatientsAllCtrl'
                }
            }
        })


        .state('tab.main', {
            url: '/main',
            views: {
                'tab-main': {
                    templateUrl: 'templates/tab-main.html',
                    controller: 'MainCtrl'
                }
            }
        })

        .state('tab.hospitals', {
            url: '/hospitals',
            views: {
                'tab-hospitals': {
                    templateUrl: 'templates/hospital/doctor-hospital.html',
                    controller: 'DoctorHospitalCtrl'
                }
            }
        })

          .state('tab.doctor-hospital-detail', {
            url: '/doctorHospitals/:id/:hospitalId',
            views: {
                'tab-hospitals': {
                    templateUrl: 'templates/hospital/doctor-hospital-detail.html',
                    controller: 'DoctorHospitalDetailCtrl'
                }
            }
        })

        .state('tab.my-hospital', {
            url: '/myHospital',
            views: {
                'tab-hospitals': {
                    templateUrl: 'templates/hospital/doctor-hospital-add.html',
                    controller: 'DoctorHospitalAddCtrl'
                }
            }
        })

        .state('tab.hospital-list', {
            url: '/hospitalList',
            views: {
                'tab-hospitals': {
                    templateUrl: 'templates/hospital/hospital-list.html',
                    controller: 'HospitalListCtrl'
                }
            }
        })

        .state('tab.dept-list', {
            url: '/hospitalDeptList/:hospitalId',
            views: {
                'tab-hospitals': {
                    templateUrl: 'templates/hospital/dept-list.html',
                    controller: 'HospitalDeptListCtrl'
                }
            }
        })
        .state('tab.patients', {
            url: '/patientsRegisters',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/patient/tab-patients-registers.html',
                    controller: 'PatientsRegistersCtrl'
                }
            }
        })
        .state('tab.patients-today', {
            url: '/patientsToday',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/patient/tab-patients-today.html',
                    controller: 'PatientsTodayCtrl'
                }
            }
        })
         .state('tab.patient-detail', {
            url: '/patients/:patientId/:registerId/:patientType',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/patient/patient-detail.html',
                    controller: 'PatientDetailCtrl'
                }
            }
        })
        .state('tab.patients-history', {
            url: '/patientsHistory',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/patient/tab-patients-history.html',
                    controller: 'PatientsHistoryCtrl'
                }
            }
        })
         .state('tab.patients-history-detail', {
            url: '/patientHistory/:patientId/:registerId/:patientType',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/patient/patient-history-detail.html',
                    controller: 'PatientDetailCtrl'
                }
            }
        })
        
            
         .state('tab.evaluations', {
            url: '/evaluations',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/evaluation/tab-evaluations.html',
                    controller: 'EvaluationsCtrl'
                }
            }
        })
        
         .state('tab.evaluation-detail', {
            url: '/evaluation/:evaluationId',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/evaluation/evaluation-detail.html',
                    controller: 'EvaluationDetailCtrl'
                }
            }
        })

        .state('tab.doctorFuns', {
            url: '/doctorFuns/:doctorId',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/funs/tab-funs.html',
                    controller: 'DoctorFunsCtrl'
                }
            }
        })

        .state('tab.schedule-detail-edit', {
            url: '/schedule/scheduleDetailEdit/:id',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/schedule/schedule-detail-edit.html',
                    controller: 'ScheduleDetailEditCtrl'
                }
            }
        })


        .state('tab.queueNum', {
            url: '/queueNum',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/queue/tab-queues.html',
                    controller: 'DoctorQueuesCtrl'
                }
            }
        })

        .state('tab.funs', {
            url: '/funs',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/funs/tab-funs.html',
                    controller: 'DoctorFunsCtrl'
                }
            }
        })
        .state('tab.queues', {
            url: '/queues/:doctorId',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/queue/queues-hospital.html',
                    controller: 'DoctorQueuesCtrl'
                }
            }
        })


        .state('tab.postponeSchedule', {
            url: '/postponeSchedule/:scheduleId',
            views: {
                'tab-schedules': {
                    templateUrl: 'templates/schedule/postpone-schedule.html',
                    controller: 'PostponeScheduleCtrl'
                }
            }
        })

        .state('tab.schedules', {
            url: '/schedules',
            views: {
                'tab-schedules': {
                    templateUrl: 'templates/schedule/tab-schedules.html',
                    controller: 'ScheduleCtrl'
                }
            }
        })


        .state('tab.scheduleDoctorHospitals', {
            url: '/scheduleDoctorHospitals',
            views: {
                'tab-schedules': {
                    templateUrl: 'templates/schedule/schedule-doctor-hospital.html',
                    controller: 'DoctorHospitalCtrl'
                }
            }
        })

        .state('tab.addSchedule', {
            url: '/schedules/addSchedule/:doctorHospitalId',
            views: {
                'tab-schedules': {
                    templateUrl: 'templates/schedule/add-schedule.html',
                    controller: 'AddScheduleCtrl'
                }
            }
        })

        .state('tab.schedule-detail', {
            url: '/schedules/:selectedDate',
            views: {
                'tab-schedules': {
                    templateUrl: 'templates/schedule/schedule-detail.html',
                    controller: 'ScheduleDetailCtrl'
                }
            }
        })


        .state('tab.accountAll', {
            url: '/accountAll',
            views: {
                'tab-account': {
                    templateUrl: 'templates/account/tab-account.html',
                    controller: 'AccountAllCtrl'
                }
            }
        })


        .state('tab.account', {
            url: '/account',
            views: {
                'tab-account': {
                    templateUrl: 'templates/tab-account.html',
                    controller: 'AccountCtrl'
                }
            }
        })

        .state('tab.doctorInformation', {
            url: '/doctorInformation',
            views: {
                'tab-account': {
                    templateUrl: 'templates/doctor/doctor-information.html',
                    controller: 'DoctorInformationCtrl'
                }
            }
        })

        .state('tab.doctorRedPackets', {
            url: '/doctorRedPackets',
            views: {
                'tab-account': {
                    templateUrl: 'templates/doctor/doctor-registerFee.html',
                    controller: 'DoctorInformationCtrl'
                }
            }
        })

        .state('tab.doctorCode', {
            url: '/doctorCode',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/doctor/doctor-code.html',
                    controller: 'DoctorCodeCtrl'
                }
            }
        })

        .state('tab.queueList', {
            url: '/queueList/:hospitalId',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/queue/queue-list.html',
                    controller: 'QueueListCtrl'
                }
            }
        })

        .state('tab.funInformation', {
            url: '/funInformation/:userId',
            views: {
                'tab-patientsAll': {
                    templateUrl: 'templates/funs/fun-information.html',
                    controller: 'FunInformationCtrl'
                }
            }
        })

	   .state('tab.smsValid', {
	        url: '/smsValid',
	        views: {
	            'tab-account': {
	                templateUrl: 'templates/smsValid.html',
	                controller: 'SmsValidCtrl'
	            }
	        }
	    })

        .state('tab.wxpay', {
            url: '/wxpay',
            views: {
                'tab-account': {
                    templateUrl: 'templates/wxpay/tab-wxpay.html',
                    controller: 'WxpayCtrl'
                }
            }
        })
        .state('tab.score', {
            url: '/score',
            views: {
                'tab-account': {
                    templateUrl: 'templates/account/tab-score.html',
                    controller: 'ScoreCtrl'
                }
            }
        })
        .state('tab.userTransfer', {
            url: '/userTransfer/:transferType',
            views: {
                'tab-account': {
                    templateUrl: 'templates/transfers/user-transfers.html',
                    controller: 'UserTransferCtrl'
                }
            }
        })

        .state('tab.setting', {
            url: '/setting',
            views: {
                'tab-account': {
                    templateUrl: 'templates/setting/tab-setting.html',
                    controller: 'SettingCtrl'
                }
            }
        })

        .state('tab.enchashment', {
            url: '/enchashment',
            views: {
                'tab-account': {
                    templateUrl: 'templates/wxpay/enchashment.html',
                    controller: 'EnchashmentCtrl'
                }
            }
        })
        .state('tab.accountList', {
            url: '/accountList',
            views: {
                'tab-account': {
                    templateUrl: 'templates/transfers/accounts-list.html',
                    controller: 'AccountListCtrl'
                }
            }
        })

        .state('tab.getAccount', {
            url: '/accountList/:operationType',
            views: {
                'tab-account': {
                    templateUrl: 'templates/transfers/accounts-list.html',
                    controller: 'AccountListCtrl'
                }
            }
        })

        .state('tab.addAccount', {
            url: '/addAccount',
            views: {
                'tab-account': {
                    templateUrl: 'templates/transfers/add-account.html',
                    controller: 'AddAccountCtrl'
                }
            }
        })
        .state('tab.cashApply', {
            url: '/cashApply',
            views: {
                'tab-account': {
                    templateUrl: 'templates/wxpay/cashApply.html',
                    controller: ''
                }
            }
        })
        .state('tab.aboutUs', {
            url: '/aboutUs',
            views: {
                'tab-account': {
                    templateUrl: 'templates/setting/about-us.html',
                    controller:''
                }
            }
        })

        .state('tab.terms', {
        url: '/terms',
        views: {
            'tab-account': {
                templateUrl: 'templates/terms.html',
                controller:''
            }
        }
    })

        //叮咚名医列表
        .state('doctorList', {
            url: '/doctorList',
            templateUrl: 'templates/doctor/doctorList.html',
             controller: 'DoctorsCtrl'
        })

        .state('navigationTerms', {
            url: '/navigationTerms',
            templateUrl: 'templates/navigation/terms.html',
            controller: ''// UserMessagesCtrl
        })
     .state('navigation', {
            url: '/navigation',
            templateUrl: 'templates/navigation/doctor-navigation.html',
            controller: 'navigationCtrl'// UserMessagesCtrl
        })
    .state('smsValidHomePage', {
        url: '/smsValidHomePage',
        templateUrl: 'templates/smsValidHomePage.html',
        controller: 'SmsValidHomePageCtrl'
    })
    $stateProvider.state('login', {
        url: '/login',
        abstract: false,
        templateUrl: 'templates/login.html'
    });
    $stateProvider.state('signIn', {
        url: '/signIn',
        abstract: false,
        templateUrl: 'templates/signInDoctor.html'
    });

    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise('/tab/main');

    //ionic的基本配置
    $ionicConfigProvider.backButton.text('').icon('ion-chevron-left');
    $ionicConfigProvider.tabs.position('bottom');

});