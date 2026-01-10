package Media;

import Playback.CompositePlayable;
import Playback.Playable;

import java.util.ArrayList;

public class Folder implements Playable, CompositePlayable {
    String folderName;
    ArrayList<Playable> folder = new ArrayList<Playable>();

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public Folder(String folderName, ArrayList<Playable> folder) {
        this.folderName = folderName;
        this.folder = folder;
    }

    @Override
    public Song mostPlayedSong() {
        Song max = null;

        for (Playable p : folder) {
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


    @Override
    public void play() {
        System.out.println(this);
        for (Playable child : folder) {
            child.play();
        }
    }


    @Override
    public double getDuration() {
        double total = 0;
        for (Playable child : folder) {
            total = total + child.getDuration();
        }
        return total;
    }

    @Override
    public String toString() {
        return (this.folderName);
    }

    @Override
    public void addMedia(Playable item) {
        this.folder.add(item);
    }

    @Override
    public void removeMedia(Playable item) {
        this.folder.remove(item);
    }

    @Override
    public ArrayList<Playable> getChildren() {
        return folder;
    }


}
