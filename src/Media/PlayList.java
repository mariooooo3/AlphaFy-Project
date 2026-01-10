package Media;

import Playback.CompositePlayable;
import Playback.Playable;

import java.util.ArrayList;

public class PlayList implements Playable, CompositePlayable {
    String playListName;
    ArrayList<Playable> playList = new ArrayList<Playable>();

    public PlayList(String playListName) {
        this.playListName = playListName;
    }

    public PlayList(String playListName, ArrayList<Playable> playList) {
        this.playListName = playListName;
        this.playList = playList;
    }

    @Override
    public void play() {
        System.out.println(this);
        for (Playable child : playList) {
            child.play();
        }
    }

    @Override
    public double getDuration() {
        double total = 0;
        for (Playable child : playList) {
            total = total + child.getDuration();
        }
        return total;
    }

    @Override
    public String toString() {
        return (this.playListName);
    }

    @Override
    public void addMedia(Playable item) {
        playList.add(item);
    }

    @Override
    public void removeMedia(Playable item) {
        playList.remove(item);
    }

    @Override
    public ArrayList<Playable> getChildren() {
        return playList;
    }

    @Override
    public Song mostPlayedSong() {
        Song max = null;

        for (Playable p : playList) {
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
