package gps;


import java.text.DecimalFormat;

/**
 * @author demarsa and aleckm
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class TracksCalculator {

	private static final double DEG_TO_RAD = 0.0174533;
	private static final double M_TO_KM = 0.001;
	private static final double M_TO_MI = 0.000621371;
	private static final double RADIUS_OF_EARTH_M = 6371000;
	private static final double MILLI_SEC_TO_HOURS = 2.77778e-7;
	private double avgSpeedK;
	private double avgSpeedM;
	private double maxSpeedK;
	private double maxSpeedM;
	private double minElev;
	private double maxElev;
	private double minLat;
	private double maxLat;
	private double minLong;
	private double maxLong;
	private double totalDistanceK;
	private double totalDistanceM;
	private double totalTime;

	public TracksCalculator(){
		avgSpeedK = 0;
		avgSpeedM = 0;
		totalDistanceK = 0;
		totalDistanceM = 0;
		totalTime = 0;
		maxSpeedK = Double.MIN_VALUE;
		maxSpeedM = Double.MIN_VALUE;
		minElev = Double.MAX_VALUE;
		maxElev = Double.MAX_VALUE*-1;
		minLat = Double.MAX_VALUE;
		maxLat = Double.MAX_VALUE*-1;
		minLong = Double.MAX_VALUE;
		maxLong = Double.MAX_VALUE*-1;
	}

	/**
	 * Calculates the 12 desired metrics for the specified track
	 * @param track the track whose metrics are being calculated
	 */
	public void calculateMetrics(Track track) throws UnsupportedOperationException{
		track.setStats(new TrackStats());
		TrackPoint a;
		TrackPoint b;
		double deltaX;
		double deltaY;
		double deltaZ;

		int pointNum = track.getPointAmount();
		if(pointNum == 1) {
			a = track.getTrackPoint(0);
			calcMinMaxLat(a);
			calcMinMaxLong(a);
			calcMinMaxElev(a);
			throw new UnsupportedOperationException("Track only has one point");
		}

		for(int i = 0; i < pointNum; i++) {
			if(i != pointNum-1) {
				a = track.getTrackPoint(i);
				b = track.getTrackPoint(i + 1);

				deltaX = (RADIUS_OF_EARTH_M + ((b.getElevation() + a.getElevation())/2)) *
						(b.getLongitude()*DEG_TO_RAD - a.getLongitude()*DEG_TO_RAD) *
						Math.cos((b.getLatitude()*DEG_TO_RAD + a.getLatitude()*DEG_TO_RAD)/2);
				deltaY = (RADIUS_OF_EARTH_M + (b.getElevation() + a.getElevation())/2) *
						(b.getLatitude()*DEG_TO_RAD - a.getLatitude()*DEG_TO_RAD);
				deltaZ = b.getElevation() - a.getElevation();
				double deltaT = (b.getTime().getTime() - a.getTime().getTime())*MILLI_SEC_TO_HOURS;
				double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));

				totalTime += deltaT;
				calcTotalDistance(distance);
				calcMaxSpeed(deltaT, distance);
				calcMinMaxElev(a);
				calcMinMaxLat(a);
				calcMinMaxLong(a);
			} else {

				avgSpeedK = totalDistanceK/totalTime;
				avgSpeedM = totalDistanceM/totalTime;

				a = track.getTrackPoint(i);

				calcMinMaxElev(a);
				calcMinMaxLat(a);
				calcMinMaxLong(a);
			}
		}

		DecimalFormat format = new DecimalFormat("#.##");
		TrackStats stats = track.getTrackStats();
		stats.setAvgSpeedK(Double.parseDouble(format.format(avgSpeedK)));
		stats.setAvgSpeedM(Double.parseDouble(format.format(avgSpeedM)));
		stats.setMaxSpeedK(Double.parseDouble(format.format(maxSpeedK)));
		stats.setMaxSpeedM(Double.parseDouble(format.format(maxSpeedM)));
		stats.setDistK(Double.parseDouble(format.format(totalDistanceK)));
		stats.setDistM(Double.parseDouble(format.format(totalDistanceM)));
		stats.setMinElev(Double.parseDouble(format.format(minElev)));
		stats.setMaxElev(Double.parseDouble(format.format(maxElev)));
		stats.setMinLat(Double.parseDouble(format.format(minLat)));
		stats.setMaxLat(Double.parseDouble(format.format(maxLat)));
		stats.setMinLong(Double.parseDouble(format.format(minLong)));
		stats.setMaxLong(Double.parseDouble(format.format(maxLong)));
	}

	/**
	 * Calculates the average and max speeds in KPH and MPH for the track
	 *
	 * @param deltaT the change in time between the current two track points
	 * @param distance the distance between the current two track points
	 */
	//Don't execute anything if pointNum < 1
	private void calcMaxSpeed(double deltaT, double distance) {
		double speedK = (distance*M_TO_KM)/deltaT;
		double speedM = (distance*M_TO_MI)/deltaT;

		if(speedK > maxSpeedK) {
			maxSpeedK = speedK;
		}
		if(speedM > maxSpeedM) {
			maxSpeedM = speedM;
		}
	}

	/**
	 * Calculates the max and min elevations of the track
	 *
	 * @param point the current track point
	 */
	//If on first iteration must set max/min to current value
	//Then compare as normal
	private void calcMinMaxElev(TrackPoint point) {
		double elevation = point.getElevation();
		if(elevation > maxElev) {
			maxElev = elevation;
		}
		if(elevation < minElev) {
			minElev = elevation;
		}
	}


	/**
	 * Calculates the max and min latitudes of the track
	 *
	 * @param point the current track point
	 */
	//If on first iteration must set max/min to current value
	//Then compare as normal
	private void calcMinMaxLat(TrackPoint point) {
		double latitude = point.getLatitude();
		if(latitude > maxLat) {
			maxLat = latitude;
		}
		if(latitude < minLat) {
			minLat = latitude;
		}
	}

	/**
	 * Calculates the max and min longitudes of the track
	 *
	 * @param point the current track point
	 */
	//If on first iteration must set max/min to current value
	//Then compare as normal
	private void calcMinMaxLong(TrackPoint point){
		double longitude = point.getLongitude();
		if(longitude > maxLong) {
			maxLong = longitude;
		}
		if(longitude < minLong) {
			minLong = longitude;
		}
	}

	/**
	 * Calculates the total distance covered in kilometers and miles for the track
	 *
	 * @param distance the distance between the current two track points
	 */
	//Don't execute if pointNum < 1
	private void calcTotalDistance(double distance) {
		totalDistanceK += distance*M_TO_KM;
		totalDistanceM += distance*M_TO_MI;
	}
}