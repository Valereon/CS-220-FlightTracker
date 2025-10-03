package cs220.flighttracker;

import org.opensky.api.OpenSkyApi;
import org.opensky.model.StateVector;
import org.opensky.model.OpenSkyStates;

/**
 * Gets the current state of the flight tracker data
 * @author Maxwell Warren
 * @author Logan Surber
 * @author Malayna Vest
 * @author Litzy Garcia
 */
public class Main {
    /**
     * 
     */
    public static void main(String[] args) {
        OpenSkyApi api = new OpenSkyApi(); //anon access

        try {
            // Define parameters
            int time = 0; // 0 = current time
            String[] icao24 = null; // null = all aircrafts in hexcode
            OpenSkyApi.BoundingBox bbox = null; // null = no bounding box (aka geographical state vector)

            OpenSkyStates states = api.getStates(time, icao24, bbox);

            for (StateVector s : states.getStates()) {
                System.out.println("Flight Number (in hex): " + s.getIcao24());
                System.out.println("Latitude: " + s.getLatitude());
                System.out.println("Longitude: " + s.getLongitude());
                System.out.println("Altitude: " + s.getGeoAltitude());
                System.out.println("Velocity: " + s.getVelocity());
                System.out.println("------------------------");
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}