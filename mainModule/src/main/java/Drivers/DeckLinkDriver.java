package Drivers;

import Devices.AVDevice;
import Devices.DeckLinkDevice;
import Devices.FFMPEGAVDevice;
import Exceptions.FailedToIOWithFFMPEGError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by olivier on 16-12-16.
 */
public class DeckLinkDriver extends FFMPEGAVDriver {
    private Logger logger = LoggerFactory.getLogger(DeckLinkDriver.class);
    public DeckLinkDriver() throws IOException{
        super();
        driverInputFormat = "decklink";
        updateAVdevices();
    }

    /**
     * Start ffmpeg process and return the process
     *
     * @return
     */
    public Process startFFMPEGProcess(){
        ProcessBuilder builder = new ProcessBuilder(ffmpegFilepath, "-f", "decklink", "-list_devices", "1", "-i", "dummy");
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
     * Update AVDevices and return the updated map
     *
     * @return
     * @throws IOException
     */
    public HashMap<String, LinkedList<AVDevice>> updateAVdevices() throws IOException {
        avDevices.put("video", new LinkedList<>());
        avDevices.put("audio", new LinkedList<>());

        BufferedReader reader = getRunAndGetFFMPEGBufferedReader();

        //parse the output for the devices
        String line;
        while ((line = reader.readLine()) != null && !line.trim().equals("--EOF--")) {

            if (!line.contains("[decklink @") || line.contains("Blackmagic DeckLink devices:")) {
                //if we are not reading a device line, move to the next line
                continue;
            }

            AVDevice device = getDeviceFromOutputLine(line);

            //Add the device to the list
            avDevices.get("video").add(device);
        }
        return avDevices;
    }

    /**
     * Return AVdevice from supplied output line
     *
     * @param line
     * @return
     */
    @Override
    public FFMPEGAVDevice getDeviceFromOutputLine(String line) throws IOException {
        String deviceName = line.split("] \t")[1];
        //clean the string by removing surrounding quotes
        deviceName = deviceName.replaceAll("\'", "");
        return new DeckLinkDevice(deviceName);
    }

    /**
     * Return ffmpeg input command
     *
     * @return
     */
    @Override
    public String getFFMPEGInputCommand() {
        return getFFMPEGVideoInputCommand();
    }

    /**
     * Returns FFmpeg video input command
     *
     * @return
     */
    @Override
    public String getFFMPEGVideoInputCommand() {
        return MessageFormat.format("{0}@{1}", selectedVideoDevice, ((DeckLinkDevice) selectedVideoDevice).getResolutionIndex());
    }

    /**
     * Returns FFmpeg audio input command
     *
     * @return
     */
    @Override
    public String getFFMPEGAudioInputCommand() {
        return null;
    }
}
