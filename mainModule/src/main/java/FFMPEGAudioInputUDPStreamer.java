import Exceptions.FailedToIOWithFFMPEGError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by olivier on 2017-03-12.
 */
public class FFMPEGAudioInputUDPStreamer implements IInputPreprocessor{

    private String inputFormat;
    private FFMPEGAVDevice device;
    private final String destination = "udp://127.0.0.1:9999";
    private String ffmpegPath;
    private Process process;
    private Logger logger = LoggerFactory.getLogger(FFMPEGAudioInputUDPStreamer.class);

    public FFMPEGAudioInputUDPStreamer(String inputFormat, FFMPEGAVDevice device){
        this.inputFormat = inputFormat;
        this.device = device;
        ffmpegPath = PathFactory.getInstance().getFFMPEGPath(this.getClass());
    }

    /**
     * Allows activation, this will start an instance of FFMPEG which will begin to stream the given input to the
     * destination via UDP
     */
    @Override
    public void activate() {
        ProcessBuilder builder = new ProcessBuilder(ffmpegPath, "-f", inputFormat, "-i", ":" + device.getName(), "-c:a", "libfdk_aac", "-b:a", "320k", "-f", "mpegts", destination);

        builder.redirectErrorStream(true);
        try {
            process = builder.start();
        } catch (IOException e) {
            logger.error("IOException while activating the preprocessor", e);
            e.printStackTrace();
            throw new FailedToIOWithFFMPEGError();
        }
    }


    /**
     * Allows deactivation of the UDP stream and destroys the instance of FFMPEG
     */
    @Override
    public void deactivate() {
        process.destroy();
    }

    /**
     * @return the activation state
     */
    @Override
    public boolean isActive() {
        if(process != null)
            return process.isAlive();
        else
            return false;
    }

    /**
     * @return the destination where the output of the preprocessor is sent
     */
    @Override
    public String getDestination() {
        return destination;
    }
}
