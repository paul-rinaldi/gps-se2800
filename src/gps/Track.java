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

<<<<<<< HEAD
	public int getPointAmount() {
		return trackPoints.size();
=======
	public int getPointAmount(){
		return trackPoints.size();
	}

	public String getName(){
		return name;
>>>>>>> 9d214b3003e3a11cfb68b9e4e738234aa817b2f4
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