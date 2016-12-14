package com.dingdong.sys.service.impl;

public class JsSDKConfig {

    public String appId;
    public long timestamp;
    public String nonceStr;
    public String signature;
    public static final String JS_API = "onMenuShareTimeline,onMenuShareAppMessage,onMenuShareQQ";

    // public static final String JS_API =
    // "onMenuShareTimeline,onMenuShareAppMessage,onMenuShareQQ,startRecord,stopRecord,onVoiceRecordEnd,playVoice,pauseVoice,stopVoice,onVoicePlayEnd,uploadVoice,downloadVoice,chooseImage,previewImage,uploadImage,downloadImage,translateVoice,getNetworkType,openLocation,getLocation,hideOptionMenu,showOptionMenu,hideMenuItems,showMenuItems,hideAllNonBaseMenuItem,showAllNonBaseMenuItem,closeWindow,scanQRCode,chooseWXPay,openProductSpecificView,addCard,chooseCard,openCard";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String[] getJsApiList() {
        return JS_API.split(",|，");
    }

    @Override
    public String toString() {
        return "appId：" + this.appId + "\n" + "timestamp：" + this.timestamp + "\n" + "nonceStr：" + this.nonceStr + "\n"
                + "signature：" + this.signature + "\n" + "JS_API：" + JS_API;
    }

}