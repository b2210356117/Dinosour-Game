
import sun.audio.AudioStream;

import java.io.InputStream;

public class PlayAudio {
    public static AudioStream play(String path) {
        AudioStream audioStream = null;
        try {
            InputStream inputStream = PlayAudio.class.getResourceAsStream(path);
            audioStream = new AudioStream(inputStream);
            //AudioPlayer.player.start(audioStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioStream;
    }
}
