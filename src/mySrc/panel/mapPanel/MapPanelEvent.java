package mySrc.panel.mapPanel;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import mySrc.coordinate.ConvertLngLatAppletCoordinate;

/**
 * 地図のイベントに関するクラス
 * @author murase
 *
 */
public class MapPanelEvent implements MouseListener{
	
	public MapPanel _mapPanel;
	
	public MapPanelEvent(MapPanel aMapPanel) {
		_mapPanel = aMapPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2 && MouseEvent.BUTTON1 == e.getButton()) {	// ダブルクリックしたときはその点を中心点にする.
			System.out.println("getpoint"+e.getPoint());
			Point2D.Double lnglat = _mapPanel._convert.convertAppletCoordinateToLngLat(e.getPoint());
			_mapPanel._lngLat = lnglat;
//			_mapPanel._unsetFlg();
			_mapPanel.makeMap();
		} else if (e.getClickCount() == 1 && MouseEvent.BUTTON1 == e.getButton()) {	// クリックしたときはコンソールにデータを表示.
			int foundShopNum=0;			// クリックした上にあるノードの数.
			ArrayList<Point> _pointDataArrayList = _mapPanel._convert.convertLngLatToAppletCoordinate(_mapPanel._lnglatDataArrayList);
			ArrayList<String> shopNameArrayList = new ArrayList<String>();	// クリックした上にあるお店の名前.
			for (int i=0; i < _pointDataArrayList.size(); i++) {	// 建物の数だけループ.
				if (e.getPoint().x > _pointDataArrayList.get(i).x - 5 &&
				e.getPoint().x < _pointDataArrayList.get(i).x + 5 &&
				e.getPoint().y > _pointDataArrayList.get(i).y - 5 &&
				e.getPoint().y < _pointDataArrayList.get(i).y + 5) {	// クリックした点が建物のマーカー上であるか.
					foundShopNum++;
					shopNameArrayList.add(_mapPanel._pointDataNameArrayList.get(i));
				}
			}
			if(shopNameArrayList.size() > 0){
				System.out.println("found Shop Num "+foundShopNum);
				for(String string: shopNameArrayList){
					System.out.println(string);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

}
