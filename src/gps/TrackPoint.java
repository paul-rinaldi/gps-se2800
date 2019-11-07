package gps;


import java.util.Date;

/**
 * @author demarsa and aleckm
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class TrackPoint {

	private double elevation;
	private double latitude;
	private double longitude;
	private Date time;

	public TrackPoint() {
		this.elevation = 0;
		this.latitude = 0;
		this.longitude = 0;
		this.time = null;
	}

	public TrackPoint(double latitude, double longitude, double elevation, Date time) {
		this.elevation = elevation;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
	}

	public double getElevation(){
		return this.elevation;
	}

	public double getLatitude(){
		return this.latitude;
	}

	public double getLongitude(){
		return this.longitude;
	}

	public Date getTime(){
		return this.time;
	}

}