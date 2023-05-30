package com.example.kakao_developer

import android.util.Log
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

interface MapEventListener  : MapView.MapViewEventListener{

    // 맵 뷰가 사용 가능한 형태
    override fun onMapViewInitialized(p0: MapView?) {
        Log.d("init view : ",p0.toString())
    }

    // 지도의 중심 좌표가 이동한 경우 호출
    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    // 지도 확대/축소 레벨이 변경 된 경우 호출
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    // 사용자가 지도를 터치한 경우
    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        Log.d("onMapViewSingleTapped view : ",p0.toString())
        val currentMarker = MapPOIItem()
        currentMarker.apply {
            itemName = "Current Touch"
            tag = 0
            mapPoint = p1
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType =MapPOIItem.MarkerType.RedPin
        }
        p0?.addPOIItem(currentMarker)
    }

    // 사용자가 지도를 더블 터치 한 경우
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    // 지도의 한 지점을 길게 누른 경우
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    // 드래그를 시작한 경우
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    // 드래그를 끝낸 경우
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    // 지도 이동이 완료 된 경우
    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }


}