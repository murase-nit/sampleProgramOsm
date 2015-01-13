package mySrc.db.getData;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.postgis.PGgeometry;

import mySrc.db.GeometryParsePostgres;
import mySrc.db.HandleDbTemplateSuper;
import mySrc.panel.inputPanel.InputPanel;

/**
 * OSM道路データを扱う
 * @author murase
 *
 */
public class OsmRoadDataGeom extends HandleDbTemplateSuper {
	private static final String DBNAME = "osm_road_db";	// Database Name
	private static final String USER = "postgres";			// user name for DB.
	private static final String PASS = "usadasql";		// password for DB.
	private static final String URL = "rain2.elcom.nitech.ac.jp";
	private static final int PORT = 5432;
	private static final String DBURL = "jdbc:postgresql://"+URL+":"+PORT+"/" + DBNAME;
	
	/** リンクID */
	public ArrayList<Integer> _linkId;
	/** (sourcePoint, targetPoint)の組 */
	public ArrayList<Line2D> _link;
	public ArrayList<Integer> _sourceId;
	public ArrayList<Integer> _targetId;
	/** km */
	public ArrayList<Double> _length;
	/** cost */
	public ArrayList<Double> _length2;
	/** 道路のクラス */
	public ArrayList<Integer> _clazz;
	/** 道路の形状を表す */
	public ArrayList<ArrayList<Point2D>> _arc;
	
	public OsmRoadDataGeom(){
		super(DBNAME, USER, PASS, DBURL, HandleDbTemplateSuper.POSTGRESJDBCDRIVER_STRING);
	}
	
	/**
	 * 矩形範囲のデータを取り出す
	 */
	public void insertOsmRoadData(Point2D aUpperLeftLngLat, Point2D aLowerRightLngLat){
		_linkId = new ArrayList<>();
		_link = new ArrayList<>();
		_sourceId = new ArrayList<>();
		_targetId = new ArrayList<>();
		_length = new ArrayList<>();
		_length2 = new ArrayList<>();
		_clazz = new ArrayList<>();
		_arc = new ArrayList<>();
		
		try{
			String statement;
			// SRID=4326.
			statement = "select " +
					" id, osm_name,osm_source_id, osm_target_id, clazz, source, target, km, cost, x1, y1, x2, y2, geom_way " +
					" from osm_japan_car_2po_4pgr " +
					" where" +
					" st_intersects(" +
						"st_geomFromText(" +
							"'polygon(("+
								aUpperLeftLngLat.getX()+" "+aLowerRightLngLat.getY()+","+
								aLowerRightLngLat.getX()+" "+aLowerRightLngLat.getY()+","+
								aLowerRightLngLat.getX()+" "+aUpperLeftLngLat.getY()+","+
								aUpperLeftLngLat.getX()+" "+aUpperLeftLngLat.getY()+","+
								aUpperLeftLngLat.getX()+" "+aLowerRightLngLat.getY()+
							"))',"+WGS84_EPSG_CODE+
						"), "+
					"geom_way) " +
					"";
			System.out.println(statement);
			ResultSet rs = execute(statement);
			while(rs.next()){
				_linkId.add(rs.getInt("id"));
				_sourceId.add(rs.getInt("source"));
				_targetId.add(rs.getInt("target"));
				_link.add((Line2D)new Line2D.Double(rs.getDouble("x1"), rs.getDouble("y1"), rs.getDouble("x2"), rs.getDouble("y2")));
				_length.add(rs.getDouble("km"));
				_length2.add(rs.getDouble("cost"));
				_clazz.add(rs.getInt("clazz"));
//				System.out.println(GeometryParsePostgres.getLineStringMultiPoint((PGgeometry)rs.getObject("geom")));
				_arc.add(GeometryParsePostgres.getLineStringMultiPoint((PGgeometry)rs.getObject("geom_way")));
			}
			rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
}
