package com.example.kakao_developer

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

// Redirect URI : http://127.0.0.1:8000/login
class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // kakao sdk 초기화
        KakaoSdk.init(this, "0e76ab2e79ca08872fbcdab67c475fae")
    }
}