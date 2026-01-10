package Playback;

import Media.Song;

import java.util.ArrayList;

public interface CompositePlayable {
    public void addMedia(Playable item);

    public void removeMedia(Playable item);

    public Song mostPlayedSong();

    ArrayList<Playable> getChildren();
}
