package Drivers;

/**
 * Created by olivier on 2017-03-08.
 */
public interface IFFMPEGCompatibleDriver {
    /**
     * Returns FFmpeg input command
     *
     * @return
     */
    String getFFMPEGInputCommand();

    /**
     * Returns FFmpeg video input command
     *
     * @return
     */
    String getFFMPEGVideoInputCommand();

    /**
     * Returns FFmpeg audio input command
     *
     * @return
     */
    String getFFMPEGAudioInputCommand();

    /**
     * Return driver input format for ffmpeg
     *
     * @return
     */
    String getInputFormat();
}
