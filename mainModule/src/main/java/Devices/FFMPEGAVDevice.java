package Devices;

import Platform.PathFactory;

/**
 * Created by olivier on 16-12-26.
 *
 * This class represents the device of an Drivers.FFMPEGAVDriver
 */
public class FFMPEGAVDevice extends AVDevice {
    protected String ffmpegPath;

    public FFMPEGAVDevice(String name) {
        super(name);
        ffmpegPath = PathFactory.getInstance().getFFMPEGPath(this.getClass());
    }


}
