package Drivers;

import Devices.FFMPEGAVDevice;
import Exceptions.FailedToIOWithFFMPEGError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by olivier on 16-12-16.
 */
public class AVFoundationDriver extends FFMPEGAVDriver {
    private Logger logger = LoggerFactory.getLogger(AVFoundationDriver.class);

    public AVFoundationDriver() throws IOException {
        super();
        driverInputFormat = "AVFoundation";
        updateAVdevices();
    }

    /**
     * Starts the FFMPEG process
     *
     * @return
     * @throws IOException
     */
    public Process startFFMPEGProcess() {
        ProcessBuilder builder = new ProcessBuilder(ffmpegFilepath, "-f", "avfoundation", "-list_devices", "true", "-i", "\"\"");
        new ProcessBuilder();
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
     * Return Devices.FFMPEGAVDevice from supplied output line
     *
     * @param line
     * @return
     */
    public FFMPEGAVDevice getDeviceFromOutputLine(String line) {

        String deviceName = line.split("( \\[\\d\\] )+")[1];
        return new FFMPEGAVDevice(deviceName);
    }

    /**
     * Returns FFMpeg input command
     *
     * @return
     */
    @Override
    public String getFFMPEGInputCommand() {
        String videoInput = selectedVideoDevice == null ? "none" : selectedVideoDevice.toString();
        String audioInput = selectedAudioDevice == null ? "none" : selectedAudioDevice.toString();

        return MessageFormat.format("{0}:{1}", videoInput, audioInput);
    }

    /**
     * Returns FFmpeg video input command
     *
     * @return
     */
    @Override
    public String getFFMPEGVideoInputCommand() {
        if(selectedVideoDevice == null)
            return "";
        else
            return selectedVideoDevice.toString() + ":";
    }

    /**
     * Returns FFmpeg audio input command
     *
     * @return
     */
    @Override
    public String getFFMPEGAudioInputCommand() {
        if(selectedAudioDevice == null)
            return "";
        else
            return ":" + selectedAudioDevice.toString();
    }
}
