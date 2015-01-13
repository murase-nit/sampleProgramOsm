package mySrc.panel.mapPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

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
