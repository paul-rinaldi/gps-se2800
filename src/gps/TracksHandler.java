package gps;

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

	/**
	 * Initializes tracks as an array list
	 */
	public TracksHandler() {
		this.tracks = new ArrayList<>();
	}


	/**
	 *
	 * @param track
	 */
	public void addTrack(Track track) {
		if(getTrack(track.getName()) == null) {
			tracks.add(track);
		} else {
			throw new UnsupportedOperationException("Selected Track has already been loaded: " + track.getName());
		}
	}

	/**
	 *
	 * @param name
	 */
	public void calculateTrackStats(String name) throws UnsupportedOperationException {
		tc = new TracksCalculator();
		try {
			tc.calculateMetrics(getTrack(name));
		} catch(UnsupportedOperationException uoe) {
			throw new UnsupportedOperationException();
		}
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
	public Track getTrack(String name) {
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