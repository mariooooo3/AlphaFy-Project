package Media;

import Playback.Playable;

public class Song implements Playable {
    public String songName;
    public String artistName;
    public double songDuration;
    int count = 0;

    public Song(String songName, String artistName, double songDuration) {
        this.songName = songName;
        this.artistName = artistName;
        this.songDuration = songDuration;
    }

    @Override
    public String toString() {
        return (this.songName + ":" + this.getDuration());
    }

    @Override
    public double getDuration() {
        return this.songDuration;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public void play() {
        System.out.println(this);
        this.count++;
    }

}
