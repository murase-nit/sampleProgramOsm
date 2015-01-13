package mySrc.panel.inputPanel;

import java.awt.Dimension;

import javax.swing.JPanel;

import mySrc.panel.mapPanel.MapPanel;
import mySrc.panel.outputPanel.OutputPanel;

/**
 * 上の画面(入力用)
 * @author murase
 *
 */
public class InputPanel  extends JPanel{
	// パネルの縦横大きさ.
	private static final int INPUT_PANEL_WINDOW_WIDTH = MapPanel.WINDOW_WIDTH + MapPanel.RIGHT_AREA_WIDTH;
	private static final int INPUT_PANEL_WINDOW_HEIGHT = MapPanel.UPPER_AREA_HEIGHT;
		
		
		
		
		
	public InputPanel(MapPanel aMapPanel, OutputPanel aOutputPanel) {
		this.setPreferredSize(new Dimension(INPUT_PANEL_WINDOW_WIDTH, INPUT_PANEL_WINDOW_HEIGHT));
		
	}

}
