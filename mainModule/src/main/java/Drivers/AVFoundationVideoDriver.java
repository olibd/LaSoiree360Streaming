package Drivers;

import Devices.AVDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by olivier on 2017-03-12.
 */
public class AVFoundationVideoDriver extends AVFoundationDriver {
    public AVFoundationVideoDriver() throws IOException {
        super();
    }

    /**
     * Update AV devices lists
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
            } else if(type == "audio")
                continue;

            AVDevice device = getDeviceFromOutputLine(line);

            //Add the device to the list
            avDevices.get(type).add(device);
        }
        return avDevices;
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
