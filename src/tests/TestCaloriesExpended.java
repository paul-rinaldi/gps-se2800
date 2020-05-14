package tests;

import gps_plotter.Plotter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCaloriesExpended {

    @Test
    public void testCaloriesExpended(){

        Plotter p = new Plotter(null, null);

        double distanceChanges[] = {15, 16, 14, 7.5, 0, 30, 5, 100, 0, 0};
        double timeChangesMin[] = {60, 50, 70, 30, 20, 60, 60, 0, 10, 60};
        double elevationGains[] = {0, 10, 5, 0, 20, 0, 100, 100, 100};
        double calories[] = {1000, 1300, 809.9999999999999, 1000, 40, 2000, 533.3333333333333, 0, 0, 200};

        for(int i = 0; i < 8; i++){
           double cal =  p.calculateCaloriesExpended(distanceChanges[i], timeChangesMin[i], elevationGains[i]);
           assertEquals(calories[i], cal);
        }

    }

}
