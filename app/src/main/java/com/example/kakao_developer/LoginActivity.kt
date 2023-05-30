package com.example.kakao_developer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kakao_developer.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    // 카카오 계정으로 로그인 callback
    // 카카오톡으로 로그인 할 수 없는 경우 -> 앱이 없음 -> 카카오 계정으로 로그인
    private val callback : (OAuthToken?,Throwable?) -> Unit  = { token,error->
        if (error != null){
            Log.d("카카오 계정 로그인 실패 ",error.toString())
        } else if (token != null){
            Log.d("카카오 계정 로그인 성공 ",token.accessToken)
            getUserInfo()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 해쉬 키 얻기
        val keyHash = Utility.getKeyHash(this)
        Log.d("hash : ",keyHash)

        binding.kakaoLogin.setOnClickListener {
            if (!checkLogin()){
                kaKaoLogin()
            }
        }

    }

    private fun kaKaoLogin(){
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)){
            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity){token, error ->
                if (error != null){
                    Log.d("카카오톡 앱 로그인 실패",error.toString())

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인 취소의 경우
                    // 의도적 로그인 취소로 판단. 카카오 계정으로 로그인 시도 없이 로그인 취소 처리 (아무런 동작 x)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오 계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback =  callback)
                } else if (token != null){
                    Log.d("카카오톡 앱 로그인 성공 ",token.accessToken)
                    getUserInfo()

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
        }
    }

    private fun checkLogin() : Boolean{
        // 이미 로그인 상태
        var check = false
        if (AuthApiClient.instance.hasToken()){
            UserApiClient.instance.accessTokenInfo {_,error->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        check = false
                    } else {
                        // 기타 에러 발생
                        Log.d("kakao Login error : ", error.toString())
                        check = false
                    }
                } else {
                    // 토큰 유효성 체크 성공  ( 필요 시 토큰 재 갱신)
                    check = true
                }
            }
        } else {
            // 토큰이 없음
            check = false
        }
        return check
    }

    private fun getUserInfo(){
        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
            if (error != null){
                Log.d("토큰 정보 불러오기 실패",error.toString())
            }
            else if (tokenInfo != null){
                val userId = tokenInfo.id?.toInt()
                val expiresIn = tokenInfo.expiresIn.toInt()
                Log.d("토큰 정보 입니다 : ",
                    "회원 아이디 : $userId \n" +
                        "만료시간: $expiresIn")

                val intent =  Intent(applicationContext,MainActivity::class.java)
                intent.putExtra("tokenInfo_id",userId)
                intent.putExtra("tokenInfo_expiresIn",expiresIn)
                startActivity(intent)
            }
            else {
                Log.d("토큰 정보 불러오기 실패", "기타 오류")
            }
        }
    }
}