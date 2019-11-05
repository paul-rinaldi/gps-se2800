package gps;


/**
 * @author demarsa and aleckm
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class TrackStats {

	private double avSpeedK;
	private double avSpeedM;
	private double distK;
	private double distM;
	private double maxElev;
	private double maxLat;
	private double maxLong;
	private double maxSpeedK;
	private double maxSpeedM;
	private double minElev;
	private double minLat;
	private double minLong;

	public TrackStats(){
		this.avSpeedK = 0;
		this.avSpeedM = 0;
		this.distK = 0;
		this.distM = 0;
		this.minElev = 0;
		this.maxElev = 0;
		this.minLat = 0;
		this.maxLat = 0;
		this.minLong = 0;
		this.maxLong = 0;
		this.maxSpeedK = 0;
		this.maxSpeedM = 0;
	}

	public double getAvgSpeedK() {
		return avSpeedK;
	}

	public void setAvgSpeedK(double avSpeedK) {
		this.avSpeedK = avSpeedK;
	}

	public double getAvgSpeedM() {
		return avSpeedM;
	}

	public void setAvgSpeedM(double avSpeedM) {
		this.avSpeedM = avSpeedM;
	}

	public double getDistK() {
		return distK;
	}

	public void setDistK(double distK) {
		this.distK = distK;
	}

	public double getDistM() {
		return distM;
	}

	public void setDistM(double distM) {
		this.distM = distM;
	}

	public double getMaxElev() {
		return maxElev;
	}

	public void setMaxElev(double maxElev) {
		this.maxElev = maxElev;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getMaxLong() {
		return maxLong;
	}

	public void setMaxLong(double maxLong) {
		this.maxLong = maxLong;
	}

	public double getMaxSpeedK() {
		return maxSpeedK;
	}

	public void setMaxSpeedK(double maxSpeedK) {
		this.maxSpeedK = maxSpeedK;
	}

	public double getMaxSpeedM() {
		return maxSpeedM;
	}

	public void setMaxSpeedM(double maxSpeedM) {
		this.maxSpeedM = maxSpeedM;
	}

	public double getMinElev() {
		return minElev;
	}

	public void setMinElev(double minElev) {
		this.minElev = minElev;
	}

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public double getMinLong() {
		return minLong;
	}

	public void setMinLong(double minLong) {
		this.minLong = minLong;
	}
}