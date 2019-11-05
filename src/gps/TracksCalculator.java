package gps;


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

	public TracksCalculator(){

	}

	/**
	 * Calculates the 12 desired metrics for the specified track
	 * @param track the track whose metrics are being calculated
	 */
	public void calculateMetrics(Track track){
		TrackPoint a;
		TrackPoint b;
		double deltaX;
		double deltaY;
		double deltaZ;

		avgSpeedK = 0;
		avgSpeedM = 0;

		int pointNum = track.getPointAmount();

		for(int i = 0; i < pointNum; i++) {
			if(i != pointNum-1) {
				a = track.getTrackPoint(i);
				b = track.getTrackPoint(i + 1);

				deltaX = (RADIUS_OF_EARTH_M + (b.getElevation() + a.getElevation())/2) *
						(b.getLongitude()*DEG_TO_RAD - a.getLongitude()*DEG_TO_RAD) *
						Math.cos((b.getLatitude()*DEG_TO_RAD-a.getLatitude()*DEG_TO_RAD)/2);
				deltaY = (RADIUS_OF_EARTH_M + (b.getElevation() + a.getElevation())/2) *
						(b.getLatitude()*DEG_TO_RAD - a.getLatitude()*DEG_TO_RAD);
				deltaZ = b.getElevation() - a.getElevation();
				double deltaT = (b.getTime().getTime() - a.getTime().getTime())*MILLI_SEC_TO_HOURS;
				double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));

				calcTotalDistance(distance);
				calcAvgMaxSpeed(deltaT, distance, pointNum, i);
				calcMinMaxElev(a);
				calcMinMaxLat(a);
				calcMinMaxLong(a);
			} else {
				a = track.getTrackPoint(i);

				calcMinMaxElev(a);
				calcMinMaxLat(a);
				calcMinMaxLong(a);
			}
		}

		TrackStats stats = track.getTrackStats();
		stats.setAvgSpeedK(avgSpeedK);
		stats.setAvgSpeedM(avgSpeedM);
		stats.setMaxSpeedK(maxSpeedK);
		stats.setMaxSpeedM(maxSpeedM);
		stats.setDistK(totalDistanceK);
		stats.setDistM(totalDistanceM);
		stats.setMinElev(minElev);
		stats.setMaxElev(maxElev);
		stats.setMinLat(minLat);
		stats.setMaxLat(maxLat);
		stats.setMinLong(minLong);
		stats.setMaxLong(maxLong);
	}

	/**
	 * Calculates the average and max speeds in KPH and MPH for the track
	 *
	 * @param deltaT the change in time between the current two track points
	 * @param distance the distance between the current two track points
	 * @param pointNum the total number of track points
	 * @param counter the current iteration of the loop
	 */
	private void calcAvgMaxSpeed(double deltaT, double distance, int pointNum, int counter) {
		double speedK = distance*M_TO_KM/deltaT;
		double speedM = distance*M_TO_MI/deltaT;
		avgSpeedK += speedK;
		avgSpeedM += speedM;
		if(counter == pointNum-1) {
			avgSpeedK = avgSpeedK/pointNum-1;
			avgSpeedM = avgSpeedM/pointNum-1;
		}
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
	private void calcMinMaxElev(TrackPoint point) {
		double elevation = point.getElevation();
		if(elevation > maxElev) {
			maxElev = elevation;
		}
		if(point.getElevation() < minElev) {
			minElev = elevation;
		}
	}


	/**
	 * Calculates the max and min latitudes of the track
	 *
	 * @param point the current track point
	 */
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
	private void calcTotalDistance(double distance) {
		totalDistanceK += distance*M_TO_KM;
		totalDistanceM += distance*M_TO_MI;
	}
}