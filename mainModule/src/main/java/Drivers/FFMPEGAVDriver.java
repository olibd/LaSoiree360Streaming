package Drivers;

import Devices.AVDevice;
import Devices.FFMPEGAVDevice;
import Platform.PathFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by olivier on 16-12-16.
 *
 * This class represents AVDevices which have native ffmpeg support (depends on the build of ffmpeg) and can be read
 * directly by FFMPEG.
 */
public abstract class FFMPEGAVDriver extends AVDriver{

    protected String ffmpegFilepath;

    public FFMPEGAVDriver() throws IOException{
        super();
        ffmpegFilepath = PathFactory.getInstance().getFFMPEGPath(this.getClass());

    }


    /**
     * Start the FFMPEG engine, returns the process
     * @return
     */
    public abstract Process startFFMPEGProcess();

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
            }

            AVDevice device = getDeviceFromOutputLine(line);

            //Add the device to the list
            avDevices.get(type).add(device);
        }
        return avDevices;
    }

    /**
     * Will start the FFMPEG process and return the buffered reader of the process' stdout
     *
     * @return
     */
    protected BufferedReader getRunAndGetFFMPEGBufferedReader() {
        //create new FFMPEG process to get the AV devices
        Process process = startFFMPEGProcess();

        InputStream stdout = process.getInputStream();
        return new BufferedReader(new InputStreamReader(stdout));
    }

    /**
     * Returns Devices.FFMPEGAVDevice from the supplied output line
     *
     * @param line
     * @return
     */
    public abstract FFMPEGAVDevice getDeviceFromOutputLine(String line) throws IOException;

    /**
     * Returns FFmpeg input command
     *
     * @return
     */
    public abstract String getFFMPEGInputCommand();

    /**
     * Returns FFmpeg video input command
     *
     * @return
     */
    public abstract String getFFMPEGVideoInputCommand();

    /**
     * Returns FFmpeg audio input command
     *
     * @return
     */
    public abstract String getFFMPEGAudioInputCommand();

}
