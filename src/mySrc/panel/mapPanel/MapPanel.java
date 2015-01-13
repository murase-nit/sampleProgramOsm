package mySrc.panel.mapPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import mySrc.coordinate.ConvertLngLatAppletCoordinate;
import mySrc.coordinate.GetLngLatOsm;
import mySrc.panel.MyMap;
import mySrc.panel.inputPanel.InputPanel;
import mySrc.panel.outputPanel.OutputPanel;

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
	
	public MapPanel(OutputPanel aOutputPanel, MyMap aMyMap) {
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		_mapPanelPaint = new MapPanelPaint(this);
		_mapPanelEvent = new MapPanelEvent(this);
		
		addMouseListener(_mapPanelEvent);
		
		makeMap();
		
	}
	
	/**
	 * 地図の描画
	 */
	public void makeMap(){
		URL url = null;
		try {
			url = new URL(HOSTNAME +
					PARAM_CENTER + _lngLat.getY() +","+_lngLat.getX() +
					PARAM_ZOOM + DEFAULT_SCALE +
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
		_mapPanelPaint.paintCenterPoint(_lngLat, _convert);
	}
	
	// setter関数.
	public void setInputPanel(InputPanel aInputPanel){
		_inputPanel = aInputPanel;
	}
}
