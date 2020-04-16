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
	private double maxElevM;
	private double maxLat;
	private double maxLong;
	private double maxSpeedK;
	private double maxSpeedM;
	private double minElevM;
	private double minLat;
	private double minLong;
	private double maxElevFt;
	private double minElevFt;

	/**
	 * Creates a TrackStats with all values initialized to 0
	 */
	public TrackStats(){
		this.avSpeedK = 0;
		this.avSpeedM = 0;
		this.distK = 0;
		this.distM = 0;
		this.minElevM = 0;
		this.maxElevM = 0;
		this.minLat = 0;
		this.maxLat = 0;
		this.minLong = 0;
		this.maxLong = 0;
		this.maxSpeedK = 0;
		this.maxSpeedM = 0;
		this.minElevFt = 0;
		this.maxElevFt = 0;
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

	public double getMaxElevM() {
		return maxElevM;
	}

	public void setMaxElevM(double maxElevM) {
		this.maxElevM = maxElevM;
	}

	public double getMaxElevFt() {
		return maxElevFt;
	}

	public void setMaxElevFt(double maxElevFt) {
		this.maxElevFt = maxElevFt;
	}

	public double getMinElevFt() {
		return minElevFt;
	}

	public void setMinElevFt(double minElevFt) {
		this.minElevFt = minElevFt;
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

	public double getMinElevM() {
		return minElevM;
	}

	public void setMinElevM(double minElevM) {
		this.minElevM = minElevM;
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