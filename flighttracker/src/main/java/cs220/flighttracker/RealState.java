package cs220.flighttracker;
import org.opensky.api.OpenSkyApi;
public class RealState {
    private String name;
    private OpenSkyApi.BoundingBox bbox = null;

    public void SetName(String name){
        this.name = name;
    }
    public String GetName(){
        return name.toLowerCase();
    }

    public void SetBbox(double minLat, double maxLat, double minLong, double maxLong){
        bbox = new OpenSkyApi.BoundingBox(minLat, maxLat, minLong, maxLong);
    }

    public boolean isInsideBbox(double lat, double lon){
        if(lat < bbox.getMaxLatitude() && lat > bbox.getMinLatitude()){
            if(lon < bbox.getMaxLongitude() && lon > bbox.getMinLongitude()){
                return true;
            }
        }
        return false;
    }
}

