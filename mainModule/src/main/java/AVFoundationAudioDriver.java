import Exceptions.AVDeviceDoesNotExistException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by olivier on 2017-03-12.
 */
public class AVFoundationAudioDriver extends AVFoundationDriver {
    public AVFoundationAudioDriver() throws IOException {
        super();
    }

    /**
     * Return FFMPEGAVDevice from supplied output line
     *
     * @param line
     * @return
     */
    protected FFMPEGAVDevice getDeviceFromOutputLine(String line) {

        String deviceName = line.split("( \\[\\d\\] )+")[1];
        return new AVFoundationAudioDevice(deviceName);
    }

    /**
     * Update AV devices
     *
     * @return
     * @throws IOException
     */
    @Override
    public HashMap<String, LinkedList<AVDevice>> updateAVdevices() throws IOException {
        avDevices.put("video", new LinkedList<>());
        avDevices.put("audio", new LinkedList<>());
        BufferedReader reader = getRunAndGetFFMPEGBufferedReader();

        //parse the output for the devices
        String line;
        String type = "";
        while ((line = reader.readLine()) != null && !line.trim().equals("--EOF--")) {

            //search the output for the devices type section
            if (line.contains("video devices")) {
                type = "video";
                continue; //skip the section title, go straight to the device on the next line
            } else if (line.contains("audio devices")) {
                type = "audio";
                continue; //skip the section title, go straight to the device on the next line
            } else if (!line.contains(driverInputFormat)) {
                //if we are not in the device section, move to the next line
                type = "";
                continue;
            } else if(type == "video") //skip if we are in the video section
                continue;

            AVDevice device = getDeviceFromOutputLine(line);

            //Add the device to the list
            avDevices.get(type).add(device);
        }
        return avDevices;
    }

    /**
     * Select supplied audio device
     *
     * @param device
     * @throws AVDeviceDoesNotExistException
     */
    @Override
    public void selectAudioDevice(AVDevice device) throws AVDeviceDoesNotExistException {
        AVDevice previousAVDevice = getSelectedAudioDevice();

        super.selectAudioDevice(device);

        //deactivate previous active device
        deactivateDevice(previousAVDevice);

        //activate new device
        activateDevice(device);
    }

    /**
     * Return driver's ffmpeg input format
     *
     * @return
     */
    @Override
    public String getInputFormat() {
        return null;
    }

    /**
     * Returns FFMpeg input command
     *
     * @return
     */
    @Override
    public String getFFMPEGInputCommand() {
        return getFFMPEGAudioInputCommand();
    }

    /**
     * Returns FFmpeg video input command
     *
     * @return
     */
    @Override
    public String getFFMPEGVideoInputCommand() {
        return null;
    }

    /**
     * Returns FFmpeg audio input command
     *
     * @return
     */
    @Override
    public String getFFMPEGAudioInputCommand() {
        return selectedAudioDevice.toString();
    }
}
