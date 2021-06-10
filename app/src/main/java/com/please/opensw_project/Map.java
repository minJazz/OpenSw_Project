package com.please.opensw_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class Map extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    GpsTracker gpsTracker;
    Gps g;

    MapView mapView;
    double latitude;
    double longitude;

    ArrayList<MapPOIItem> mapPOIItemArrayList = new ArrayList<>();

    Toast mtoast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        g = new Gps();
        mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        gpsTracker = new GpsTracker(Map.this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();


        // this에 MapView.MapViewEventListener 구현.
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);



// 줌 레벨 변경
        mapView.setZoomLevel(7, true);

// 중심점 변경 + 줌 레벨 변경



// 줌 인
        mapView.zoomIn(true);

// 줌 아웃
        mapView.zoomOut(true);

    }
    double mLatitude;
    double mLongitude;
    @Override
    public void onMapViewInitialized(MapView mapView) {

        setMarkerMe();
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 9, true);

        mLatitude =getIntent().getDoubleExtra("latitude",0);
        mLongitude =getIntent().getDoubleExtra("longitude",0);

        if(mLongitude == 0.0){
            mLongitude = latitude;
            mLatitude = longitude;
            startToast(getApplicationContext(), "위치 정보가 없습니다." );
        }

        setMarkerAll();
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(mLongitude, mLatitude), 3, true);

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        drawLine(mapPOIItem);

        int i = mapPOIItemArrayList.indexOf(mapPOIItem);
        Product p = InfoActivity.information.get(i);
        startToast(getApplicationContext(), "가격 : "+ p.price + "원  거리 : " + (int)p.distance + "m" );
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    public void drawLine(MapPOIItem mapPOIItem) {
        mapView.removeAllPolylines();
        MapPoint myPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPoint mapPoint = mapPOIItem.getMapPoint();

        MapPolyline mapPolyline = new MapPolyline();
        mapPolyline.addPoint(mapPoint);
        mapPolyline.addPoint(myPoint);
        mapPolyline.setLineColor(Color.GREEN);

        mapView.addPolyline(mapPolyline);
    }

    public void setMarkerAll() {
        for (Product p : InfoActivity.information) {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(p.mLocation);
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(p.longitude, p.latitude));
            marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);

            if(mLongitude == p.longitude && mLatitude == p.latitude){
                drawLine(marker);
                startToast(getApplicationContext(), "가격 : "+ p.price + "원  거리 : " + (int)p.distance + "m" );
            }
            mapPOIItemArrayList.add(marker);
        }
    }

    public void setMarkerMe() {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName("내 위치");
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(InfoActivity.myLocation.getLatitude(), InfoActivity.myLocation.getLongitude()));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);


    }

    public void startToast(Context context, String msg){
        if (mtoast != null) {
            mtoast.cancel();
        }

        mtoast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mtoast.show();
    }


}