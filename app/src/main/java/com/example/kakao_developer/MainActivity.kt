package com.example.kakao_developer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kakao_developer.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility
import net.daum.android.map.MapViewEventListener
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var userId : Int = -1
    private var expiresIn : Int = -1
    private lateinit var mapView : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getIntExtra("tokenInfo_id",-1)
        expiresIn = intent.getIntExtra("tokenInfo_expiresIn",-1)
        Log.d("userCode in mainActivity","$userId , $expiresIn")

        // map view setting
        MapView.setMapTilePersistentCacheEnabled(true)
        // map view
        mapView = MapView(this@MainActivity)
        binding.mapView.addView(mapView)

        // 클릭 이벤트
        mapView.setMapViewEventListener(object  : MapEventListener{
        })
    }

    // 현재 사용자 주소 얻기
    private fun getCurrentAddress(mapPoint: MapPoint?,paramFunc : (String?) -> Unit){
        mapPoint?.let{
            val currentMapPoint = MapPoint.mapPointWithGeoCoord(
                mapPoint.mapPointGeoCoord.latitude,
            mapPoint.mapPointGeoCoord.longitude)

            MapReverseGeoCoder(BuildConfig.APP_KEY,currentMapPoint,object : MapReverseGeoCoder.ReverseGeoCodingResultListener{

                // 주소 받기 성공
                override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
                    p1?.let {
                        Log.d("current click address : ",it)
                        paramFunc(it)
                    }
                }

                // 주소 받기 실패
                override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
                    paramFunc(null)
                }

            }, this@MainActivity).startFindingAddress()
        }
    }
}