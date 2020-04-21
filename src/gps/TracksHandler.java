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
	 * Adds the desired track to the tracks array list
	 *
	 * @param track the track being added to the array list
	 */
	public void addTrack(Track track) {
		if(getTrack(track.getName()) == null) {
			tracks.add(track);
		} else {
			throw new UnsupportedOperationException("Selected Track has already been loaded: " + track.getName());
		}
	}

	/**
	 * Creates a track calculator to calculate the track stats
	 *
	 * @param name the name of the track to calculate the stats for
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
	 * Returns the track at the index in tracks
	 *
	 * @param index the desired position of the list to return
	 */
	public Track getTrack(int index) {
		return tracks.get(index);
	}

	/**
	 * Returns the track with the desired name but returns null if not in tracks
	 *
	 * @param name the name of the desired track
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

	/**
	 * Returns the index that the given track is located at
	 *
	 * @param name the index of the desired track or -1 if it is not in the tracks handler
	 */
	public int getTrackIndex(String name) {
		int toReturn = -1;
		for (int t = 0; t < tracks.size(); t++) {
			if (tracks.get(t).getName().equals(name)) {
				toReturn = t;
			}
		}
		return toReturn;
	}


	/**
	 * Gets the number of track points in tracks
	 * @return the number of track points in tracks
	 */
	public int getTrackAmount() {
		return tracks.size();
	}

}