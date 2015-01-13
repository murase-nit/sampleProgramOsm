package mySrc.panel.mapPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import mySrc.coordinate.ConvertLngLatAppletCoordinate;

/**
 * 地図上に描画するクラス
 * @author murase
 *
 */
public class MapPanelPaint {
	private MapPanel _mapPanel;
	private Graphics _mapPanelGraphics;
	private Graphics2D _mapPanelGraphics2d;
	private int _markerSize;
	private ConvertLngLatAppletCoordinate _convert;

	public MapPanelPaint(MapPanel aMapPanel) {
		_mapPanel = aMapPanel;
	}
	
	// 初期処理.
	public void init(int aMarkerSize, Graphics aG, ConvertLngLatAppletCoordinate aConvert){
		_markerSize = aMarkerSize;
		_mapPanelGraphics = aG;
		_mapPanelGraphics2d = (Graphics2D)aG;
		_convert = aConvert;
	}
	
	// 地図の描画.
	public void paintMap(Image image, BufferedImage bufferedImage){
			//_mapPanelGraphics2d.drawImage(image,0,0,_mapPanel);	// 地図の再描画?.
			_mapPanelGraphics2d.drawImage(bufferedImage, 0, 0, _mapPanel);
	}
	// 中心座標の描画.
	public void paintCenterPoint(Point2D lnglat, ConvertLngLatAppletCoordinate _convert){
		Point point = _convert.convertLngLatToAppletCoordinate((Point2D.Double)lnglat);
		Rectangle2D.Double rectangle = new Rectangle2D.Double(
				point.x - _markerSize/2/2,
				MapPanel.WINDOW_HEIGHT - point.y - _markerSize/2/2,
				_markerSize/2,
				_markerSize/2);
		_mapPanelGraphics2d.setPaint(Color.green);
		_mapPanelGraphics2d.fill(rectangle);
		_mapPanelGraphics2d.setPaint(Color.black);
		_mapPanelGraphics2d.draw(rectangle);
	}
	
}
