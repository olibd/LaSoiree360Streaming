package Devices;
import Preprocessors.FFMPEGAudioInputUDPStreamer;
import Preprocessors.IInputPreprocessor;
import Utilities.IActivable;

/**
 * Created by olivier on 2017-03-12.
 *
 * This class represents the audio portion of the AVFoundation Driver
 */
public class AVFoundationAudioDevice extends FFMPEGAVDevice implements IActivable {
    private IInputPreprocessor preprocessor;
    private String destination;

    public AVFoundationAudioDevice(String name) {
        super(name);
        preprocessor = new FFMPEGAudioInputUDPStreamer("AVFoundation", this);
    }

    /**
     * Allows activation
     */
    @Override
    public void activate() {
        preprocessor.activate();
        destination = preprocessor.getDestination();
    }

    /**
     * Allows deactivation
     */
    @Override
    public void deactivate() {
        preprocessor.deactivate();
        destination = null;
    }

    /**
     * Returns the activation state
     * @return
     */
    @Override
    public boolean isActive() {
        return preprocessor.isActive();
    }

    /**
     * Returns the destination
     * @return
     */
    @Override
    public String toString() {
        return destination;
    }
}
