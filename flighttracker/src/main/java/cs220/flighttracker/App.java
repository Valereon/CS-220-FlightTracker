package cs220.flighttracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.opensky.api.OpenSkyApi;
import org.opensky.model.StateVector;
import org.opensky.model.OpenSkyStates;

/**
 * Gets the current state of the flight tracker data
 * 
 * @author Maxwell Warren
 * @author Logan Surber
 * @author Malayna Vest
 * @author Litzy Garcia
 */
public class App {

    private List<StateVector> stateVectors;
    private List<RealState> actualStates;

    private View view;
/**
 * Loads initial flight data
 */
    public void Run() {
        OpenSkyApi api = new OpenSkyApi(); // anon access
        Scanner scanner = new Scanner(System.in);

        try {
            // Define parameters
            int time = 0; // 0 = current time
            String[] icao24 = null; // null = all aircrafts in hexcode
            OpenSkyApi.BoundingBox bbox = null; // null = no bounding box (aka geographical state vector)

            OpenSkyStates states = api.getStates(time, icao24, bbox);
            this.stateVectors = (List<StateVector>) states.getStates();
            this.actualStates = GetStatesFromCSV(
            "CS-220-FlightTracker/flighttracker/src/main/java/cs220/flighttracker/boundingBoxes.csv"
        );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a menu for the user to use, and then based on their answer will
     * display flights by flight number or by state
     * 
     * @param scanner
     * @param stateVectors
     * @param actualStates
     */
    

    /**
     * Gets and displays flight by number
     * 
     * @param stateVectors
     * @param flightNumber
     * @return string of the flight data
     */
    public String GetAndDisplayFlightByNumber(List<StateVector> stateVectors, String flightNumber) {
        boolean isFound = false;
        StringBuilder sb = new StringBuilder();
        for (StateVector s : stateVectors) {
            if (s.getCallsign() != null && s.getCallsign().trim().equalsIgnoreCase(flightNumber.trim())) {
                
            sb.append("Callsign: ").append(s.getCallsign()).append("\n");
            sb.append("Latitude: ").append(s.getLatitude()).append("\n");
            sb.append("Longitude: ").append(s.getLongitude()).append("\n");
            sb.append("Altitude: ").append(s.getGeoAltitude()).append("\n");
            sb.append("Velocity: ").append(s.getVelocity()).append("\n");
            sb.append("-------------------\n");

                isFound = true;
            }
        }

    if (!isFound) {
        return ("Could not find flight number " + flightNumber);
    }
    return sb.toString();
}

    /**
     * compares the state name with the states and when a match is found displays
     * all flights within its bounding box if none are found returns;
     * 
     * @param realState
     * @param stateVectors
     * @param actualStates
     * @return the string of the flights found
     */
    public String GetAndDisplayFlightByState(String realState, List<StateVector> stateVectors,
            List<RealState> actualStates) {
        realState = realState.toLowerCase();
        RealState correctState = null;
        StringBuilder sb = new StringBuilder();
        boolean isFound = false;
        for (RealState state : actualStates) {
            if (state.GetName().equals(realState)) {
                correctState = state;
                isFound = true;
            }
        }
        if (!isFound) {
            return("Could not find state " + realState);
        }

        for (StateVector s : stateVectors) {
            try{
                if (correctState.isInsideBbox(s.getLatitude(), s.getLongitude())) {
                    sb.append("ICAO: ").append(s.getIcao24()).append("\n");
                sb.append("FLight: ").append(s.getCallsign()).append("\n");
                sb.append("Latitude: ").append(s.getLatitude()).append("\n");
                sb.append("Longitude: ").append(s.getLongitude()).append("\n");
                sb.append("Altitude: ").append(s.getGeoAltitude()).append("\n");
                sb.append("Velocity: ").append(s.getVelocity()).append("\n");
                sb.append("-------------------\n");
                }
            }catch(NullPointerException e){
                continue;
            }

        }
        return sb.toString();
    }

    /**
     * Dispalys the given state Vector
     * 
     */
    public void DisplayFlight(StateVector state) {
         StringBuilder sb = new StringBuilder();
    sb.append("ICAO: ").append(state.getIcao24()).append("\n");
    sb.append("Callsign: ").append(state.getCallsign()).append("\n");
    sb.append("Latitude: ").append(state.getLatitude()).append("\n");
    sb.append("Longitude: ").append(state.getLongitude()).append("\n");
    sb.append("Altitude: ").append(state.getGeoAltitude()).append("\n");
    sb.append("Velocity: ").append(state.getVelocity()).append("\n");
    sb.append("------------------\n");

    if (view != null) {
        view.displayInView(sb.toString());
    }
}

   

    /**
     * reads file, and returns a list of RealState with bounding boxes and names
     * 
     * @param filePath
     * @return
     */
    public static List<RealState> GetStatesFromCSV(String filePath) {
        List<String> lines = ReadFromFile(filePath);
        List<RealState> states = new ArrayList<>();
        for (String string : lines) {
            String[] split = string.split(",");
            RealState currentState = new RealState();
            currentState.SetBbox(
                    Float.parseFloat(split[1].trim()),
                    Float.parseFloat(split[2].trim()),
                    Float.parseFloat(split[3].trim()),
                    Float.parseFloat(split[4].trim()));
            currentState.SetName(split[0]);
            states.add(currentState);
        }
        return states;
    }

    /**
     * reads from a file path and returns list of strings
     * 
     * @param filePath
     * @return
     */
    public static List<String> ReadFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                lines.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("an error occurred");
            e.printStackTrace();
        }
        return lines;
    }
    

    public List<StateVector> getStateVectors() {
        return stateVectors;
    }

    public List<RealState> getActualStates() {
        return actualStates;
    }

    public void setView(View view) {
        this.view = view;
    }
}
