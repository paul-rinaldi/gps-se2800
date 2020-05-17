package tests;

import gps_plotter.Plotter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCaloriesExpended {

    /**
     * Tests calculateCaloriesExpended
     * with only changes in distance
     */
    @Test
    public void testCaloriesExpendedDistanceChange(){

        Plotter p = new Plotter(null, null);

        double distanceChanges[] = {15, 7.5, 30, 45, 22.5, 0, 12, 6, 3, 21};
        double calories[] = {1000, 500, 2000, 3000, 1500, 0, 800, 400, 200, 1400};

        for(int i = 0; i < calories.length; i++){
           double cal =  p.calculateCaloriesExpended(distanceChanges[i], 0);
           assertEquals(calories[i], cal);
        }

    }

    /**
     * Tests calculateCaloriesExpended
     * with only elevation gain
     */
    @Test
    public void testCaloriesExpendedElevationGain(){
        Plotter p = new Plotter(null, null);

        double elevationGains[] = {0, 2, 1.5, 6, 10, 25, 60, 4.5, 45, 1};
        double calories[] = {0, 4, 3, 12, 20, 50, 120, 9, 90, 2};

        for(int i = 0; i < calories.length; i++){
            double cal =  p.calculateCaloriesExpended(0, elevationGains[i]);
            assertEquals(calories[i], cal);
        }
    }

    /**
     * Tests calculateCaloriesExpended
     * with distance changes and elevation gains
     */
    @Test public void testCaloriesExpendedBothChange(){
        Plotter p = new Plotter(null, null);

        double distanceChanges[] = {15, 7.5, 30, 45, 22.5, 0, 12, 6, 3, 21};
        double elevationGains[] = {0, 2, 1.5, 6, 10, 25, 60, 4.5, 45, 1};
        double calories[] = {1000, 504, 2003, 3012, 1520, 50, 920, 409, 290, 1402};

        for(int i = 0; i < calories.length; i++){
            double cal =  p.calculateCaloriesExpended(distanceChanges[i], elevationGains[i]);
            assertEquals(calories[i], cal);
        }
    }

}
