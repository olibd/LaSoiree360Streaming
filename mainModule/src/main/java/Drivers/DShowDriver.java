package Drivers;

import Devices.FFMPEGAVDevice;
import Exceptions.FailedToIOWithFFMPEGError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by olivier on 16-12-16.
 */
public class DShowDriver extends FFMPEGAVDriver {
    private Logger logger = LoggerFactory.getLogger(DShowDriver.class);
    public DShowDriver() throws IOException {
        super();
        driverInputFormat = "dshow";
        updateAVdevices();
    }

    /**
     * Start ffmpeg process and return the process
     *
     * @return
     * @throws IOException
     */
    public Process startFFMPEGProcess() {
        ProcessBuilder builder = new ProcessBuilder(ffmpegFilepath, "-list_devices", "true", "-f", "dshow", "-i", "dummy");
        builder.redirectErrorStream(true);
        try {
            return builder.start();
        } catch (IOException e) {
            logger.error("IOException while activating the FFMPEG process", e);
            e.printStackTrace();
            throw new FailedToIOWithFFMPEGError();
        }
    }

    /**
     * @param line
     * @return AVdevice from supplied output line
     */
    public FFMPEGAVDevice getDeviceFromOutputLine(String line) {

        String deviceName = line.split("\"")[1];
        return new FFMPEGAVDevice(deviceName);
    }

    /**
     * @return ffmpeg input command
     */
    @Override
    public String getFFMPEGInputCommand() {

        String output = "";

        output += getFFMPEGVideoInputCommand();

        if (selectedVideoDevice != null && selectedAudioDevice != null)
            output += ":";

        output += getFFMPEGAudioInputCommand();

        return output;
    }

    /**
     * @return FFmpeg video input command
     */
    @Override
    public String getFFMPEGVideoInputCommand() {
        if (selectedVideoDevice != null)
            return "video=" + selectedVideoDevice.toString();
        else return "";
    }

    /**
     * @return FFmpeg audio input command
     */
    @Override
    public String getFFMPEGAudioInputCommand() {

        if (selectedAudioDevice != null)
            return "audio=" + selectedAudioDevice.toString();
        else return "";
    }
}
