package tests;

import gps_plotter.Plotter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;

public class ElevationGain {
/*
    @Test
    public void  testCalculateElevationGain(){

        Plotter plotter = new Plotter(null);

        double[] elevations = {300.20, 302.20, 302.20, 300.10, 310.2, 400.2, 399, 401.2};
        double[] gains = {0, 2.0, 0, 0, 8, 90, 0, 1};

        double highestElevation = elevations[0];

        for(int i = 0; i < elevations.length; i++){

            double currentElev = elevations[i];
            double gain = plotter.calculateElevationGain(currentElev, highestElevation);
            assertEquals(gains[i], gain);
            if(gain > 0){
                highestElevation = currentElev;
            }

        }


    }

    @Test
    public void  testCalculateElevationGain2() {

        Plotter plotter = new Plotter(null);

        double[] elevations = {62, 28, 47, 89, 94, 94, 103, 102, 102, 104};
        double[] gains = {0, 0, 0, 27, 5, 0, 9, 0, 0, 1};

        double highestElevation = elevations[0];

        for (int i = 0; i < elevations.length; i++) {

            double currentElev = elevations[i];
            double gain = plotter.calculateElevationGain(currentElev, highestElevation);
            assertEquals(gains[i], gain);
            if (gain > 0) {
                highestElevation = currentElev;
            }

        }
    }


    @Test
    public void testTimePassed(){
        Plotter plotter = new Plotter(null);

        ArrayList<Date> dates = new ArrayList<>();
        dates.add(new Date(0));
        dates.add(new Date(300000));
        dates.add(new Date(600000));
        dates.add(new Date(900000));
        dates.add(new Date(1200000));
        dates.add(new Date(1500000));

        Date firstDate = dates.get(0);

        double[] minPassed = {0, 5, 10, 15, 20, 25};

        for(int i = 0; i < dates.size(); i++){
            double minPassedActual = plotter.timePassedInMin(dates.get(i), firstDate);
            assertEquals(minPassed[i], minPassedActual);
        }

    }

    @Test
    public void testTimePassed2(){
        Plotter plotter = new Plotter(null);

        ArrayList<Date> dates = new ArrayList<>();
        dates.add(new Date(0));
        dates.add(new Date(150000));
        dates.add(new Date(300000));
        dates.add(new Date(420000));
        dates.add(new Date(480000));
        dates.add(new Date(600000));

        Date firstDate = dates.get(0);

        double[] minPassed = {0, 2.5, 5, 7, 8, 10};

        for(int i = 0; i < dates.size(); i++){
            double minPassedActual = plotter.timePassedInMin(dates.get(i), firstDate);
            assertEquals(minPassed[i], minPassedActual);
        }

    }
*/
}
