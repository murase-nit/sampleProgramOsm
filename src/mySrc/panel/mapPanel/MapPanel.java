package mySrc.panel.mapPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.security.auth.callback.LanguageCallback;
import javax.swing.JApplet;
import javax.swing.JPanel;

import mySrc.coordinate.ConvertLngLatAppletCoordinate;
import mySrc.coordinate.GetLngLatOsm;
import mySrc.db.getData.OsmRoadDataGeom;
import mySrc.panel.MyMap;
import mySrc.panel.inputPanel.InputPanel;
import mySrc.panel.outputPanel.OutputPanel;
import mySrc.yahooAPI.ContentsGeocorder;
import mySrc.yahooAPI.LocalSearch;

/**
 * 左下の画面(地図表示)
 * @author murase
 *
 */
public class MapPanel extends JPanel{
	/** 地図パネルの横幅. */
	public static  int WINDOW_WIDTH = 700;
	/** 地図パネルの高さ. */
	public static  int WINDOW_HEIGHT = 700;
	/** 右側のパネルの幅(出力用パネル). */
	public static final int RIGHT_AREA_WIDTH = 100;
	/** 上側のパネルの高さ(入力用パネル). */
	public static final int UPPER_AREA_HEIGHT = 150;
	
	/** 初期の経度. */
	private static final double DEFAULT_LNG = 136.93391783413753;//
	/** 初期の緯度. */
	private static final double DEFAULT_LAT = 35.15452766675943;//
	/** 初期のスケール. */
	private static final int DEFAULT_SCALE = 16;
	/***/
	private static final String DEFAULT_MAPSTYLE = "mapnik";
	/** デフォルトのマーカーサイズ */
	public static final int DEFAULT_MARKER_SIZE = 10;
	/** 小さいマーカーサイズ */
	public static final int SMALL_MARKER_SIZE = 4;
	
	// 地図データのURL.
	public static final String HOSTNAME = "http://rain.elcom.nitech.ac.jp/OsmStaticMap2/staticmap.php?";
	public static final String PARAM_CENTER = "center=";
	public static final String PARAM_ZOOM = "&zoom=";
	public static final String PARAM_SIZE = "&size=";
	public static final String PARAM_MAPTYPE = "&maptype=";
	
	// 各種インスタンス変数.
	public InputPanel _inputPanel;
	public MapPanelPaint _mapPanelPaint;
	public MapPanelEvent _mapPanelEvent;
	public ConvertLngLatAppletCoordinate _convert;
	
	// 地図基本データ.
	/** 現在の緯度経度 */
	public Point2D _lngLat = new Point2D.Double(DEFAULT_LNG, DEFAULT_LAT);
	/** 現在のスケール*/
	public int _scale = DEFAULT_SCALE;
	/** 右上の緯度経度 */
	public Point2D _upperLeftLngLat = new Point2D.Double();
	/** 左下の緯度経度 */
	public Point2D _lowerRightLngLat = new Point2D.Double();
	
	/** 地図画像 */
	public BufferedImage _bufferedImage;
	public Image _image;
	
	// 道路基本データ.
	/** リンクID */
	public ArrayList<Integer> _linkId;
	/** (sourcePoint, targetPoint)の組 */
	public ArrayList<Line2D> _link;
	/** source id */
	public ArrayList<Integer> _sourceId;
	/** target id */
	public ArrayList<Integer> _targetId;
	/** km */
	public ArrayList<java.lang.Double> _length;
	/** 道路のクラス */
	public ArrayList<Integer> _clazz;
	/** 道路の形状を表す */
	public ArrayList<ArrayList<Point2D>> _arc;
	public boolean _roadDataFlg = false;
	
	
	// 施設データ関係.
	// Yahoo施設データ.
	/** 周辺建物の緯度経度 */
	public ArrayList<Point2D> _lnglatDataArrayList = new ArrayList<Point2D>();
	/** 建物の名前. */
	protected ArrayList<String> _pointDataNameArrayList = new ArrayList<String>(); 
	/** マーカーのサイズ */
	public boolean _markFlg = false;
	// OSM施設データ.
	
	
	
	
	public MapPanel(OutputPanel aOutputPanel, MyMap aMyMap) {
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		_mapPanelPaint = new MapPanelPaint(this);
		_mapPanelEvent = new MapPanelEvent(this);
		
		addMouseListener(_mapPanelEvent);
		
		makeMap();
		
	}
	
	/**
	 * 描画関係
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	// アンチエイリアス処理.
		
		// 初期処理.
		_mapPanelPaint.init(DEFAULT_MARKER_SIZE, g, _convert);
		// 地図の描画.
		_mapPanelPaint.paintMap(_image, _bufferedImage);
		// 中心点の表示
		_mapPanelPaint.paintCenterPoint(_lngLat, _convert);
		// 道路データの描画.
		_mapPanelPaint.paintRoadData(_roadDataFlg, _link);
		// 周辺の建物の座標を描画.
		_mapPanelPaint.paintShopData(_markFlg, _lnglatDataArrayList);

	}
	
	/**
	 * 地図の描画
	 */
	public void makeMap(){
		URL url = null;
		try {
			url = new URL(HOSTNAME +
					PARAM_CENTER + _lngLat.getY() +","+_lngLat.getX() +
					PARAM_ZOOM + _scale +
					PARAM_SIZE + WINDOW_WIDTH +"x"+ WINDOW_HEIGHT +
					PARAM_MAPTYPE + DEFAULT_MAPSTYLE
					);
			System.out.println(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		revalidate();//遅延自動レイアウトをサポートします。  
		// bufferedimage を使った画像表示.
		try{
			_bufferedImage = ImageIO.read(url);
		}catch(Exception e){
			e.printStackTrace();
		}
//		_bufferedImage = reduce1(bufferedImage, (int)(WINDOW_WIDTH/_reduceRate), (int)(WINDOW_HEIGHT/_reduceRate));
		// imageを使った画像表示.
		_image = Toolkit.getDefaultToolkit().getImage(url);	// ? 画像ファイルのイメージオブジェクト.
		// 左上と右下の座標取得.
		GetLngLatOsm getLngLatOsm = new GetLngLatOsm(_lngLat, _scale, new Point(WINDOW_WIDTH, WINDOW_HEIGHT));
		_upperLeftLngLat = getLngLatOsm._upperLeftLngLat;
		_lowerRightLngLat = getLngLatOsm._lowerRightLngLat;
		// 緯度経度とアプレット座標の変換用インスタンス.
		this._convert = new ConvertLngLatAppletCoordinate((Point2D.Double)_upperLeftLngLat,
				(Point2D.Double)_lowerRightLngLat, new Point(WINDOW_WIDTH, WINDOW_HEIGHT));

//		_unsetFlg();
		
		repaint();
	}
	
	/**
	 * 入力した場所に移動する(移動ボタン移動したときに実行されるメソッド).
	 * @param aLnglat		緯度経度.
	 * @param aLocationName	地名に関するキーワード.
	 * @param aSelectedType	選択された地図移動タイプ:"lnglat" or "location" or "landmark".
	 * @param aScale		スケール.
	 */
	public void moveMap (Point2D.Double aLnglat, String aLocationName, String aSelectedType,int aScale) {
		// 値が入力されていないときは現在の座標.
		// 座標が選択されているときは緯度経度で指定地点へ移動.
		if (aSelectedType.equals("lnglat") && aLnglat.x != 0.0 && aLnglat.y != 0.0) {
			_lngLat = aLnglat;
		} else if ((aSelectedType.equals("address")||aSelectedType.equals("landmark"))&&
				!aLocationName.equals("")) {	// 地名からジオコーディングで指定地点へ移動.
			ContentsGeocorder contentsGeocorder = new ContentsGeocorder(aLocationName, aSelectedType);
			Point2D.Double lnglat = contentsGeocorder.getLnglatValue();
			_lngLat = lnglat;
		}
		this._scale = aScale;
		makeMap();
	}
	
	
	/**
	 * データベースからデータを取得し変数へ格納.
	 */
	public void insertRoadData(){
		if (_roadDataFlg == true) {
			_roadDataFlg = false;
		} else {
			OsmRoadDataGeom osmRoadDataGeom = new OsmRoadDataGeom();
			osmRoadDataGeom.startConnection();
			osmRoadDataGeom.insertOsmRoadData(_upperLeftLngLat, _lowerRightLngLat);
			osmRoadDataGeom.endConnection();
			_linkId = osmRoadDataGeom._linkId;
			_link = osmRoadDataGeom._link;
			_sourceId = osmRoadDataGeom._sourceId;
			_targetId = osmRoadDataGeom._targetId;
			_length = osmRoadDataGeom._length;
			_clazz = osmRoadDataGeom._clazz;
			_arc = osmRoadDataGeom._arc;
			_roadDataFlg = true;
		}
		repaint();
	}
	
	/**
	 * YahooローカルサーチAPIを使ったHTTPリクエストをしレスポンス(XMLデータ)をメンバ変数へ格納する.
	 * 格納される変数　_pointDataArray[] _pointInfoNum 周辺建物の緯度経度とその数.
	 * グループコードと範囲を指定する.
	 * InputPanelから呼び出される 1番目.
	 * @param aGroupCode	選択されたグループコード.
	 * @param aRadius		選択された半径(-1のときは矩形範囲選択　そうでないときは円形範囲選択).
	 */
	public void insertShopData(ArrayList<String> aGroupCode, String type){
		if(_markFlg == true){
			_markFlg = false;	// 点の描画消す.
			repaint();
			return;
		}
		_lnglatDataArrayList = new ArrayList<>();
		_pointDataNameArrayList = new ArrayList<String>();
		if(type == "yahoo"){	// yahooデータ.
			for (int i=0; i<aGroupCode.size(); i++) {
				LocalSearch localSearch;
				localSearch = new LocalSearch(new Point2D.Double(_upperLeftLngLat.getX(), _lowerRightLngLat.getY()),
						new Point2D.Double(_lowerRightLngLat.getX(), _upperLeftLngLat.getY()), aGroupCode.get(i));
				_lnglatDataArrayList.addAll(localSearch.getLatLngArrayList());
				_pointDataNameArrayList.addAll(localSearch.getNameArrayList());
			}
		}
		_markFlg = true;
		repaint();
	}
	
	
	
	// setter関数.
	public void setInputPanel(InputPanel aInputPanel){
		_inputPanel = aInputPanel;
	}
}
