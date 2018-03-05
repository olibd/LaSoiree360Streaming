package Drivers;

import java.io.IOException;

/**
 * Created by olivier on 16-12-26.
 */
public class MacAVDriverFactory extends AVDriverFactory {
    private AVDriver[] aVDrivers;

    public MacAVDriverFactory() throws IOException {
        aVDrivers = new AVDriver[3];
        aVDrivers[0] = new AVFoundationAudioDriver();
        aVDrivers[1] = new AVFoundationVideoDriver();
        aVDrivers[2] = new DeckLinkDriver();
    }

    /**
     * @return array of AVDrivers
     */
    @Override
    public AVDriver[] getAVDrivers() {
        return aVDrivers;
    }
}
