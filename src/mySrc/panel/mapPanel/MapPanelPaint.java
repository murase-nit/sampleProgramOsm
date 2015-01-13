package mySrc.panel.mapPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	// 道路データの描画.
	public void paintRoadData(boolean _roadDataFlg, ArrayList<Line2D> _link){
		if(_roadDataFlg){
			for(Line2D line2d : _link){
				paint2dLine(_convert.convertLngLatToAppletCoordinateLine2D(line2d),
					Color.black, (float)_markerSize/3);
			}
		}
	}
	
	
	public void paintShopData(boolean _markFlg, ArrayList<Point2D> _lnglatDataArrayList){
		if(_markFlg){
			for(Point2D point2d: _lnglatDataArrayList){
				paint2dEllipse(_convert.convertLngLatToAppletCoordinate((Point2D.Double)point2d), Color.red, _markerSize);
			}
		}
	}
	
	
	
	
	
	
	
	
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	// 直線.
	private void paint2dLine(Point2D aPoint1, Point2D aPoint2, Color aColor, float aLineWidth){
		Line2D.Double linkLine = new Line2D.Double(aPoint1, aPoint2);
		// 線の幅.
		BasicStroke wideStroke = new BasicStroke(aLineWidth);
		_mapPanelGraphics2d.setStroke(wideStroke);
		_mapPanelGraphics2d.setPaint(aColor);
		_mapPanelGraphics2d.draw(linkLine);

	}
	private void paint2dLine(Line2D aLine, Color aColor, float aLineWidth){
		Line2D linkLine = aLine;
		// 線の幅.
		BasicStroke wideStroke = new BasicStroke(aLineWidth);
		_mapPanelGraphics2d.setStroke(wideStroke);
		_mapPanelGraphics2d.setPaint(aColor);
		_mapPanelGraphics2d.draw(linkLine);
	}
	// 円.
	private void paint2dEllipse(Point2D aCenterPointDouble, Color aColor, int aMarkerSize){
		_mapPanelGraphics2d.setPaint(aColor);
		Ellipse2D.Double ellipse = new Ellipse2D.Double(aCenterPointDouble.getX() - aMarkerSize/2,
				aCenterPointDouble.getY() - aMarkerSize/2, aMarkerSize, aMarkerSize);
		_mapPanelGraphics2d.fill(ellipse);	// 内部塗りつぶし.
		BasicStroke wideStroke = new BasicStroke(1.0f);
		_mapPanelGraphics2d.setStroke(wideStroke);
		_mapPanelGraphics2d.setPaint(Color.black);
		_mapPanelGraphics2d.draw(ellipse);	// 輪郭の描画.

	}
	// 多角形.
	private void paintPolygon(ArrayList<Point> aPointArrayList){
		int[] xPoints = new int[aPointArrayList.size()];
		int[] yPoints = new int[aPointArrayList.size()];
		
		for(int i=0; i<aPointArrayList.size(); i++){
			xPoints[i] = aPointArrayList.get(i).x;
			yPoints[i] = aPointArrayList.get(i).y; 
		}
		
		Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
		_mapPanelGraphics2d.setPaint(Color.black);
		_mapPanelGraphics2d.draw(polygon);
		_mapPanelGraphics2d.setPaint(new Color(0, 0, 0, 64));
		_mapPanelGraphics2d.fill(polygon);
	}
	// 正方形.
	// aPoint1は始点, aPoint2は終点.
	private void paint2dSquare(Point2D aPoint1, Point2D aPoint2, Color aColor){
		double width = Math.abs(aPoint1.getX() - aPoint2.getX());	// 横幅.
		double height = Math.abs(aPoint1.getY() - aPoint2.getY());	// 縦幅.
		double minWidth = width < height ? width : height;	// 縦横の小さい方を正方形の一片の長さとする.
		
		// aPoint2はaPoint1に対してどの位置にあるか.
		double x=0, y=0;
		if(aPoint1.getX() < aPoint2.getX() && aPoint1.getY() < aPoint2.getY()){	// aPoint2は右下.
			x = aPoint1.getX();
			y = aPoint1.getY();
		}else if(aPoint1.getX() < aPoint2.getX() && aPoint1.getY() > aPoint2.getY()){	// aPoint2は右上.
			x = aPoint1.getX();
			y = aPoint1.getY() - minWidth;
		}else if(aPoint1.getX() > aPoint2.getX() && aPoint1.getY() > aPoint2.getY()){	// aPoint2は左上.
			x = aPoint1.getX() - minWidth;
			y = aPoint1.getY() - minWidth;
		}else if(aPoint1.getX() > aPoint2.getX() && aPoint1.getY() < aPoint2.getY()){	// aPoint2は左下.
			x = aPoint1.getX() - minWidth;
			y = aPoint1.getY();
		}
//		double x = aPoint1.getX() < aPoint2.getX() ? aPoint1.getX() : aPoint2.getX();
//		double y = aPoint1.getY() < aPoint2.getY() ? aPoint1.getY() : aPoint2.getY();
		Point2D.Double startPoint = new Point2D.Double(x, y);			// 左上の座標.
		
		_mapPanelGraphics2d.setPaint(aColor);
		Rectangle2D.Double rectangle = new Rectangle2D.Double(startPoint.getX(), startPoint.getY(), minWidth, minWidth);
		_mapPanelGraphics2d.draw(rectangle);
	}
	
}
