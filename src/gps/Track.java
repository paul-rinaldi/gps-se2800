package gps;


import java.util.ArrayList;

/**
 * @author demarsa and aleckm
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class Track {

	private String name;
	private TrackStats stats;
	private ArrayList<TrackPoint> trackPoints;


	/**
	 * Creates a track object with String name and Array list Track points
	 * @param name the name of the track
	 * @param trackPoints the array list of track points
	 */
	public Track(String name, ArrayList<TrackPoint> trackPoints){
		this.name = name;
		this.trackPoints = trackPoints;
		this.stats = null;
	}

	public int getPointAmount() {
		return trackPoints.size();
	}



	public String getName(){
		return name;
	}

	public TrackPoint getTrackPoint(int index) {
		return trackPoints.get(index);
	}

	public TrackStats getTrackStats() {
		return stats;
	}

	public void setStats(TrackStats stats) {
		this.stats = stats;
	}

}