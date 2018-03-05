package Devices;

import Exceptions.FailedToIOWithFFMPEGError;
import Exceptions.ResolutionNotSupportedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by olivier on 16-12-30.
 */
public class DeckLinkDevice extends FFMPEGAVDevice {

    private int resolutionIndex;

    public DeckLinkDevice(String name) throws IOException {
        super(name);
        updateResolutions();
    }

    /**
     * Start the FFMpeg process
     *
     * @return
     * @throws IOException
     */
    protected Process startFFMPEGProcess(){
        ProcessBuilder builder = new ProcessBuilder(ffmpegPath, "-f", "decklink", "-list_formats", "1", "-i", getName());
        builder.redirectErrorStream(true);
        try {
            return builder.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailedToIOWithFFMPEGError();
        }
    }

    /**
     * Updates resolutions and return the updated list of resolutions
     *
     * @return
     * @throws IOException
     */
    private LinkedList<String> updateResolutions() throws IOException {

        //create new FFMPEG process to get the AV devices
        Process process = startFFMPEGProcess();

        InputStream stdout = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));

        LinkedList<String> resolutions = new LinkedList<>();

        //parse the output for the devices
        String line;
        while ((line = reader.readLine()) != null && !line.trim().equals("--EOF--")) {

            if (!line.contains("[decklink @") || line.contains("Supported formats")) {
                //if we are not reading a device line, move to the next line
                continue;
            }

            String res = getResolutionFromOutputLine(line);

            //Add the resolution to the list
            resolutions.add(res);
        }

        setResolutions(resolutions);
        return getResolutions();
    }

    /**
     * Return the resolution from supplied output line
     *
     * @param line
     * @return
     */
    private String getResolutionFromOutputLine(String line) {
        return line.split("\\d\\t")[1];
    }

    /**
     * sets the supplied resolution provided the resolution exists for this device
     *
     * @param res
     * @throws ResolutionNotSupportedException
     */
    public void setSelectedResolution(String res) throws ResolutionNotSupportedException {
        if (getResolutions() != null && getResolutions().contains(res)) {
            resolutionIndex = getResolutions().indexOf(res) + 1;
            super.setSelectedResolution(getResolutions().get(resolutionIndex - 1));
        } else
            throw new ResolutionNotSupportedException("The supplied resolution is not supported by this device");
    }

    /**
     * Returns the position of the selected resolution in the list of the supported resolution of this device
     * The index starts at 1
     * @return
     */
    public int getResolutionIndex() {
        return resolutionIndex;
    }
}
