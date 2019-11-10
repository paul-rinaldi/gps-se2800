package gps;


import javax.xml.crypto.dsig.Transform;
import java.util.ArrayList;
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
		this.tracks = new ArrayList<>();
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
		tc = new TracksCalculator();
		tc.calculateMetrics(getTrack(name));
	}

	/**
	 *
	 * @param index
	 */
	public Track getTrack(int index) {
		return tracks.get(index);
	}

	/**
	 *
	 * @param name
	 */
	private Track getTrack(String name) {
		Track toReturn = null;
		for (Track t : tracks) {
			if (t.getName().equals(name)) {
				toReturn = t;
			}
		}
		return toReturn;
	}

	public int getTrackAmount() {
		return tracks.size();
	}

}