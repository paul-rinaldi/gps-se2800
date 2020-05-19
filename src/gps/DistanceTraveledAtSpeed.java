package gps;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DistanceTraveledAtSpeed{
    private String name;
    private double lessThanThree;
    private double threeAndSeven;
    private double sevenAndTen;
    private double tenAndFifteen;
    private double fifteenAndTwenty;
    private double greaterThanTwenty;
    private static final double DEG_TO_RAD = 0.0174533;
    private static final double RADIUS_OF_EARTH_M = 6371000;
    private static final double M_TO_MI = 0.000621371;
    private Track track;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public DistanceTraveledAtSpeed(Track track){
        this.track = track;
    }

    /**
     * calculates the distance in miles traveled at specific speeds, gets the data from the track that this class was
     * created with
     * @throws RuntimeException throws if the track has less than two points
     */
    public void calculateData() throws RuntimeException{
        name = track.getName();
        ArrayList<Double> speeds = track.getTrackStats().getSpeeds();
        if (track.getPointAmount() < 2){
            throw new RuntimeException("Track must have more than two points");
        }
        for (int i = 0; i < track.getPointAmount()-1; i++){
            double tempSpeed = speeds.get(i);
            double tempDist = calculateDist(track.getTrackPoint(i), track.getTrackPoint(i+1));
            if (tempSpeed < 3){
                lessThanThree += tempDist;
            } else if(tempSpeed >= 3 && tempSpeed < 7){
                threeAndSeven += tempDist;
            } else if(tempSpeed >= 7 && tempSpeed < 10){
                sevenAndTen += tempDist;
            } else if(tempSpeed >= 10 && tempSpeed < 15){
                tenAndFifteen += tempDist;
            } else if(tempSpeed >= 15 && tempSpeed < 20){
                fifteenAndTwenty += tempDist;
            } else {
                greaterThanTwenty += tempDist;
            }
        }
        lessThanThree = Double.parseDouble(df2.format(lessThanThree));
        threeAndSeven = Double.parseDouble(df2.format(threeAndSeven));
        sevenAndTen = Double.parseDouble(df2.format(sevenAndTen));
        tenAndFifteen = Double.parseDouble(df2.format(tenAndFifteen));
        fifteenAndTwenty = Double.parseDouble(df2.format(fifteenAndTwenty));
        greaterThanTwenty = Double.parseDouble(df2.format(greaterThanTwenty));
    }

    private double calculateDist(TrackPoint a, TrackPoint b) {
        double deltaX = (RADIUS_OF_EARTH_M + ((b.getElevation() + a.getElevation()) / 2)) *
                (b.getLongitude() * DEG_TO_RAD - a.getLongitude() * DEG_TO_RAD) *
                Math.cos((b.getLatitude() * DEG_TO_RAD + a.getLatitude() * DEG_TO_RAD) / 2);
        double deltaY = (RADIUS_OF_EARTH_M + (b.getElevation() + a.getElevation()) / 2) *
                (b.getLatitude() * DEG_TO_RAD - a.getLatitude() * DEG_TO_RAD);
        double deltaZ = b.getElevation() - a.getElevation();
        double distInMeters = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));
        return distInMeters * M_TO_MI;
    }

    //The following methods are all used by the table to get the data
    public String getName() {
        return name;
    }

    public double getLessThanThree() {
        return lessThanThree;
    }

    public double getThreeAndSeven() {
        return threeAndSeven;
    }

    public double getSevenAndTen() {
        return sevenAndTen;
    }

    public double getTenAndFifteen() {
        return tenAndFifteen;
    }

    public double getFifteenAndTwenty() {
        return fifteenAndTwenty;
    }

    public double getGreaterThanTwenty() {
        return greaterThanTwenty;
    }
}
