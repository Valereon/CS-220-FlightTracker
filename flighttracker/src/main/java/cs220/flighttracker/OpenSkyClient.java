 import org.opensky.api.OpenSkyApi;
    import org.opensky.api.OpenSkyStates;
    import org.opensky.model.StateVector;

    public class OpenSkyClient {
        public static void main(String[] args) {
            // Replace with your OpenSky username and password if needed
            OpenSkyApi api = new OpenSkyApi("loganmsurber@gmail.com", "5mg4#3_x.gLG@Nm"); 

            try {
                // Retrieve current state vectors
                OpenSkyStates os = api.getStates(0, null, null, null); 

                if (os != null && os.getStates() != null) {
                    for (StateVector state : os.getStates()) {
                        System.out.println("ICAO24: " + state.getIcao24() + 
                                           ", Latitude: " + state.getLatitude() + 
                                           ", Longitude: " + state.getLongitude());
                    }
                } else {
                    System.out.println("No state vectors found.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
