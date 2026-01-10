package Media;

import Playback.CompositePlayable;
import Playback.Playable;

import java.util.ArrayList;

public class Album implements Playable, CompositePlayable {
    String albumName;
    String artistName;
    ArrayList<Playable> album = new ArrayList<Playable>();

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public Album(String albumName, String artistName) {
        this.albumName = albumName;
        this.artistName = artistName;
    }

    public Album(String albumName, String artistName, ArrayList<Playable> album) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.album = album;
    }

    @Override
    public void play() {
        System.out.println(this);
        for (Playable child : album) {
            child.play();
        }
    }

    @Override
    public double getDuration() {
        double total = 0;
        for (Playable child : album) {
            total = total + child.getDuration();
        }
        return total;
    }

    @Override
    public String toString() {
        return (this.albumName);
    }

    @Override
    public void addMedia(Playable item) {
        this.album.add(item);
    }

    @Override
    public void removeMedia(Playable item) {
        this.album.remove(item);
    }

    @Override
    public ArrayList<Playable> getChildren() {
        return album;
    }

    @Override
    public Song mostPlayedSong() {
        Song max = null;

        for (Playable p : album) {
            if (p instanceof Song s) {
                if (max == null || s.getCount() > max.getCount()) {
                    max = s;
                }
            } else if (p instanceof CompositePlayable composite) {
                Song candidate = composite.mostPlayedSong();
                if (candidate != null &&
                        (max == null || candidate.getCount() > max.getCount())) {
                    max = candidate;
                }
            }
        }
        return max;
    }


}