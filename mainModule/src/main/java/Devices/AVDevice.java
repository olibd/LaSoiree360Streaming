package Devices;

import Exceptions.ResolutionNotSupportedException;

import java.util.LinkedList;

/**
 * Created by olivier on 2017-03-08.
 */
public class AVDevice {
    protected String name;
    private LinkedList<String> resolutions;
    private String selectedResolution;

    public AVDevice(String name) {
        this.name = name;
    }

    /**
     * Returns list of available resolutions
     *
     * @return
     */
    public LinkedList<String> getResolutions() {
        return resolutions;
    }

    public void setResolutions(LinkedList<String> resolutions) {
        this.resolutions = resolutions;
    }

    /**
     * sets the supplied resolution provided the resolution exists for this device
     *
     * @param res
     * @throws ResolutionNotSupportedException
     */
    public void setSelectedResolution(String res) throws ResolutionNotSupportedException {
        if(resolutions != null && resolutions.contains(res))
            selectedResolution = res;
        else
            throw new ResolutionNotSupportedException("The supplied resolution is not supported by this device");
    }

    /**
     * Returns Selected Resolution
     *
     * @return
     */
    public String getSelectedResolution() {
        return selectedResolution;
    }

    /**
     * Returns Name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        AVDevice dev = (AVDevice) obj;
        return name.equals(dev.getName()) && (resolutions == null && dev.getResolutions() == null || resolutions.equals(dev.getResolutions()));
    }
}
