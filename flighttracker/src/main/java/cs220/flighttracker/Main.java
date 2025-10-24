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
public class Main {

    public static void main(String[] args) {
        OpenSkyApi api = new OpenSkyApi(); // anon access
        Scanner scanner = new Scanner(System.in);

        try {
            // Define parameters
            int time = 0; // 0 = current time
            String[] icao24 = null; // null = all aircrafts in hexcode
            OpenSkyApi.BoundingBox bbox = new OpenSkyApi.BoundingBox(25, 50, -125, -70); // null = no bounding box (aka
                                                                                         // geographical state vector)

            OpenSkyStates states = api.getStates(time, icao24, bbox);
            List<StateVector> stateVectors = (List<StateVector>) states.getStates();

            List<RealState> actualStates = GetStatesFromCSV("src/main/java/cs220/flighttracker/boundingBoxes.csv");

            UserSearchAndDisplay(scanner, stateVectors, actualStates);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void UserSearchAndDisplay(Scanner scanner, List<StateVector> stateVectors, List<RealState> actualStates) {
        String out = "0";
        while (!out.equals("1") && !out.equals("2")) {
            System.out.println("What would you like to search by:");
            System.out.println("1. Flight Number");
            System.out.println("2. Departing State / current state");
            out = GetUserInput("> ", scanner);
        }

        if (out.equals("1")) {
            String flightNumber = GetUserInput("Enter a flight number : ", scanner);
            GetAndDisplayFlightByNumber(stateVectors, flightNumber);

        } else {
            String realState = GetUserInput("Please enter one of the 50 us states", scanner);
            GetAndDisplayFlightByState(realState, stateVectors, actualStates);
        }
    }

    public static void GetAndDisplayFlightByNumber(List<StateVector> stateVectors, String flightNumber) {
        for (StateVector s : stateVectors) {
            if (Integer.parseInt(s.getIcao24(), 16) == Integer.parseInt(flightNumber)) {
                DisplayFlight(s);
                break;
            }
        }
    }

    public static void GetAndDisplayFlightByState(String realState, List<StateVector> stateVectors, List<RealState> actualStates) {
        realState = realState.toLowerCase();
        RealState correctState = null;

        for (RealState state : actualStates) {
            if (state.GetName().equals(realState)) {
                correctState = state;
            }
        }

        for (StateVector s : stateVectors) {
            if (correctState.isInsideBbox(s.getLatitude(), s.getLongitude())) {
                DisplayFlight(s);
            }
        }
    }

    public static void DisplayFlight(StateVector state) {
        System.out.println(Integer.parseInt(state.getIcao24(), 16) + " " + state.getCallsign());
        System.out.println("Latitude: " + state.getLatitude());
        System.out.println("Longitude: " + state.getLongitude());
        System.out.println("Altitude: " + state.getGeoAltitude());
        System.out.println("Velocity: " + state.getVelocity());
        System.out.println("------------------");
    }

    public static String GetUserInput(String messageToDisplay, Scanner scanner) {
        System.out.print(messageToDisplay);
        String input = scanner.nextLine();
        return input;
    }

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

}
