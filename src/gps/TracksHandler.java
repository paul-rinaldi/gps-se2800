package gps;


import javax.xml.crypto.dsig.Transform;
import java.util.List;

/**
 * @author demarsa and aleckm
 * @version 1.0
 * @created 04-Nov-2019 9:32:33 AM
 */
public class TracksHandler {

	private List<Track> tracks;
	private TracksCalculator tc;

	public TracksHandler() {

	}

	/**
	 * 
	 * @param track
	 */
	public void addTrack(Track track) {
		tracks.add(track);
	}

	/**
	 * 
	 * @param index
	 */
	//To Do
	public void calculateTrackStats(int index) {
		tc.calculateMetrics(tracks.get(index));
	}

	/**
	 * 
	 * @param name
	 */
	public void calculateTrackStats(String name) {

	}

	/**
	 * 
	 * @param index
	 */
	public Track getTrack(int index){
		return tracks.get(index);
	}

	/**
	 * 
	 * @param name
	 */
	public Track getTrack(String name){
		return null;
	}

	public int getTrackAmount(){
		return tracks.size();
	}

}