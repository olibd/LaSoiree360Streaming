import Exceptions.AVDeviceDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by olivier on 2017-03-08.
 */
public abstract class AVDriver implements IFFMPEGCompatibleDriver {
    protected HashMap<String, LinkedList<AVDevice>> avDevices;
    protected String driverInputFormat;
    protected AVDevice selectedAudioDevice;
    protected AVDevice selectedVideoDevice;
    private Logger logger = LoggerFactory.getLogger(AVDriver.class);

    public AVDriver() {
        avDevices = new HashMap<>();
    }

    /**
     * Returns audio devices
     *
     * @return
     */
    public LinkedList<AVDevice> getAudioDevices() {
        return avDevices.get("audio");
    }

    /**
     * Returns video devices
     *
     * @return
     */
    public LinkedList<AVDevice> getVideoDevices() {
        return avDevices.get("video");
    }

    /**
     * Select supplied audio device
     *
     * @param device
     * @throws AVDeviceDoesNotExistException
     */
    public void selectAudioDevice(AVDevice device) throws AVDeviceDoesNotExistException {

        if (avDevices.get("audio").contains(device))
            selectedAudioDevice = device;
        else
            throw new AVDeviceDoesNotExistException("The AVDevice you are trying to set does not exist in this driver");
    }

    /**
     * Return selected audio device
     *
     * @return
     */
    public AVDevice getSelectedAudioDevice() {
        return selectedAudioDevice;
    }

    /**
     * Select supplied video device
     *
     * @param device
     * @throws AVDeviceDoesNotExistException
     */
    public void selectVideoDevice(AVDevice device) throws AVDeviceDoesNotExistException {

        if(avDevices.get("video").contains(device))
            selectedVideoDevice = device;
        else
            throw new AVDeviceDoesNotExistException("The AVDevice you are trying to set does not exist in this driver");
    }

    /**
     * Return selected video device
     *
     * @return
     */
    public AVDevice getSelectedVideoDevice() {
        return selectedVideoDevice;
    }

    /**
     * Resets currently selected video device
     */
    public void resetSelectedVideoDevice() {
        selectedVideoDevice = null;
    }

    /**
     * Resets currently selected audio device
     */
    public void resetSelectedAudioDevice() {
        selectedAudioDevice = null;
    }

    /**
     * Return driver input format for ffmpeg
     *
     * @return
     */
    public String getInputFormat() {
        return driverInputFormat;
    }

    public abstract HashMap<String, LinkedList<AVDevice>> updateAVdevices() throws IOException;

    /**
     * activates the supplied device provided it is an instance of IActivable
     * @param device
     */
    protected void activateDevice(AVDevice device) {
        if(device != null && device instanceof IActivable)
            ((IActivable)device).activate();
    }

    /**
     * deactivates the supplied device provided it is an instance of IActivable
     * @param device
     */
    protected void deactivateDevice(AVDevice device) {
        if (device != null && device instanceof IActivable) {
            ((IActivable)device).deactivate();
        }
    }
}
