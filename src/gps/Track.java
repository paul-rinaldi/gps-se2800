package gps;


import java.util.List;

/**
 * @author demarsa
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class Track {

	private String name;
	private TrackStats stats;
	private List<TrackPoint> trackPoints;

	public Track(){

	}

	/**
	 * 
	 * @param name
	 * @param trackPoints
	 */
	public void Track(String name, List<TrackPoint> trackPoints){

	}

	public int getPointAmount(){
		return 0;
	}

	/**
	 * 
	 * @param index
	 */
	public TrackPoint getTrackPoint(int index){
		return null;
	}

	public TrackStats getTrackStats(){
		return null;
	}

}