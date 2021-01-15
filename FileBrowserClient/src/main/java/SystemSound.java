import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SystemSound {

    private static final int SOUND_ERROR = 0;
    private static final int SOUND_LOGIN = 1;
    private static final int SOUND_LOGOUT = 2;
    private static final int SOUND_NAVIGATE = 3;
    private static final int SOUND_DOWNLOAD = 4;

    private URL url;

    public SystemSound(int soundID) {
        switch (soundID) {
            case SOUND_ERROR:
                url = this.getClass().getResource("sound_error.wav");
                break;
            case SOUND_LOGIN:
                url = this.getClass().getResource("sound_login.wav");
                break;
            case SOUND_LOGOUT:
                url = this.getClass().getResource("sound_logout.wav");
                break;
            case SOUND_NAVIGATE:
                url = this.getClass().getResource("sound_navigate.wav");
                break;
            case SOUND_DOWNLOAD:
                url = this.getClass().getResource("sound_download.wav");
                break;
        }
    }

    public void play() {
        try {
            // Open an audio input stream from resources
            AudioInputStream ain = AudioSystem.getAudioInputStream(url);

            // Get a sound clip resource
            Clip clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            clip.open(ain);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
