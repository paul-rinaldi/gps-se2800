package gps;


import java.util.List;

/**
 * @author demarsa and aleckm
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
		this.name = name;
		this.trackPoints = trackPoints;
		this.stats = new TrackStats();
	}

	public int getPointAmount() {
		return trackPoints.size();
	}

<<<<<<< HEAD
	public String getName() {
=======

	public String getName(){
>>>>>>> 6445766aa200b11a7393a2ae11253b182eff8b97
		return name;
	}

	/**
	 * 
	 * @param index
	 */
	public TrackPoint getTrackPoint(int index) {
		return trackPoints.get(index);
	}

	public TrackStats getTrackStats() {
		return stats;
	}

}