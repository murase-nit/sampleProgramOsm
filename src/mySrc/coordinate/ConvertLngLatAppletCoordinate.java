package mySrc.coordinate;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Point;

import java.util.ArrayList;

import mySrc.panel.mapPanel.MapPanel;



/**
 * 緯度経度とアプレット座標の変換に関するクラス
 * @author murase
 *
 */
public class ConvertLngLatAppletCoordinate {
	/** 左上の経度, 左上の緯度. */
	private Point2D.Double _upperLeftLngLat;
	/** 右下の経度, 右下の緯度. */
	private Point2D.Double _lowerRightLngLat;
	/** ウインドウサイズ */
	private Point _windowSize;
	
	/**
	 * 
	 * @param aUpperLeftLngLat	// 左上の緯度経度.
	 * @param aLowerRightLngLat	// 右下の緯度経度.
	 * @param aWindowSize		// ウインドウサイズ(x,y 縦横の長さ).
	 */
	public ConvertLngLatAppletCoordinate(Point2D.Double aUpperLeftLngLat, Point2D.Double aLowerRightLngLat,
			Point aWindowSize){
		_upperLeftLngLat = aUpperLeftLngLat;
		_lowerRightLngLat = aLowerRightLngLat;
		_windowSize = aWindowSize;
	}
	
	/**
	 * 緯度経度からアプレット内座標に変換.
	 * @param aLngLat 緯度経度
	 * @return　アプレット座標
	 */
	public Point convertLngLatToAppletCoordinate(Point2D.Double aLngLat){
		double width = _lowerRightLngLat.x - _upperLeftLngLat.x;	// 右端から左端までの経度の差（経度であらわされている）.
		double hight = _upperLeftLngLat.y - _lowerRightLngLat.y ;	// 上端から下端までの緯度の差（緯度であらわされている）.
		double widthBase = _windowSize.x/width;				// widthBaseの逆数がアプレット1ドットあたりの経度の増加幅.
		double heightBase = _windowSize.y/hight;			// heightBaseの逆数がアプレット1ドットあたりの緯度の増加幅.
		
		int xtoi = (int)((aLngLat.x - _upperLeftLngLat.x)*widthBase);	// アプレット内のｘ座標.
		int ytoi = (int)((aLngLat.y - _lowerRightLngLat.y)*heightBase);	// アプレット内のy座標.
		return(new Point(xtoi, _windowSize.y - ytoi));	// 戻り値のY軸は反転させる必要がある.
	}
	
	/**
	 * 緯度経度からアプレット内座標に変換.
	 * @param aLngLatArray 緯度経度
	 * @return　アプレット座標
	 */
	public ArrayList<Point> convertLngLatToAppletCoordinate(ArrayList<Point2D.Double> aLngLatArray){
		Point lnglat;
		ArrayList<Point> appletCoordinateArray = new ArrayList<Point>();;
		for (int i=0; i<aLngLatArray.size(); i++) {
			lnglat = convertLngLatToAppletCoordinate(aLngLatArray.get(i));
			appletCoordinateArray.add(new Point(lnglat.x,lnglat.y));
		}
		return appletCoordinateArray;
	}
	
	/**
	 * 緯度経度からアプレット内座標に変換.
	 * @param aLine2Double
	 * @return
	 */
	public Line2D convertLngLatToAppletCoordinateLine2D(Line2D aLine2Double){
		Line2D line2d = new Line2D.Double(convertLngLatToAppletCoordinate((Point2D.Double)aLine2Double.getP1()),
				convertLngLatToAppletCoordinate((Point2D.Double)aLine2Double.getP2()));
		return line2d;
	}
	public ArrayList<Line2D> convertLngLatToAppletCoordinateLine2D(ArrayList<Line2D> aLine2dArrayList){
		ArrayList<Line2D> line2dArrayList = new ArrayList<>();
		for(int i=0; i<aLine2dArrayList.size(); i++){
			line2dArrayList.add(convertLngLatToAppletCoordinateLine2D(aLine2dArrayList.get(i)));
		}
		return line2dArrayList;
	}
	
	/**
	 * アプレット内座標から緯度経度に変換
	 * @param aAppletCoordinate　アプレット内座標
	 * @return 緯度経度
	 */
	public Point2D.Double convertAppletCoordinateToLngLat(Point aAppletCoordinate){
		//aAppletCoordinate.y = MapPanel.WINDOW_HEIGHT - aAppletCoordinate.y;	// Y軸の反転.
		int appletCoordinateX = aAppletCoordinate.x;
		int appletCoordinateY = _windowSize.y - aAppletCoordinate.y;	// Y軸の反転.
		double width = _lowerRightLngLat.x - _upperLeftLngLat.x;	// 右端から左端までの経度の差（経度であらわされている）.
		double hight = _upperLeftLngLat.y - _lowerRightLngLat.y ;	// 上端から下端までの緯度の差（緯度であらわされている）.
		double widthBase = _windowSize.x/width;			// widthBaseの逆数がアプレット1ドットあたりの経度の増加幅.
		double heightBase = _windowSize.y/hight;		// heightBaseの逆数がアプレット1ドットあたりの緯度の増加幅.
		
		return(new Point2D.Double((appletCoordinateX/widthBase)+_upperLeftLngLat.x,
				(appletCoordinateY/heightBase)+_lowerRightLngLat.y));
	}
	
	/**
	 * アプレット内座標から緯度経度に変換.
	 * @param aAppletCoordinateArray　アプレット座標
	 * @return 緯度経度
	 */
	public ArrayList<Point2D.Double> convertAppletCoordinateToLngLat(ArrayList<Point> aAppletCoordinateArray){
		Point2D.Double appletCoordinate;
		ArrayList<Point2D.Double> lnglatArray = new ArrayList<Point2D.Double>();
		for (int i=0; i<aAppletCoordinateArray.size(); i++) {
			appletCoordinate = convertAppletCoordinateToLngLat(aAppletCoordinateArray.get(i));
			lnglatArray.add(new Point2D.Double(appletCoordinate.x, appletCoordinate.y));
		}
		return lnglatArray;
	}
}
