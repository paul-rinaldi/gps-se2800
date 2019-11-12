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

	/**
	 * Creates a Track Point with the given latitude, longitude, elevation, and time
	 * @param latitude the desired latitude
	 * @param longitude the desired longitude
	 * @param elevation the desired elevation
	 * @param time the desired time
	 */
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