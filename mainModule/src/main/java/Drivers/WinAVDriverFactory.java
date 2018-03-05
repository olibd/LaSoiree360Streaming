package Drivers;

import java.io.IOException;

/**
 * Created by olivier on 16-12-26.
 */
public class WinAVDriverFactory extends AVDriverFactory {

    private AVDriver[] aVDrivers;

    public WinAVDriverFactory() throws IOException {
        aVDrivers = new AVDriver[1];
        aVDrivers[0] = new DShowDriver();
    }

    /**
     * Return array of AV drivers
     *
     * @return
     */
    @Override
    public AVDriver[] getAVDrivers() {
        return aVDrivers;
    }
}
