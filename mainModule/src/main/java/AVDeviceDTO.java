/**
 * Created by olivier on 16-12-26.
 */
public class AVDeviceDTO {
    private AVDriver driver;
    private AVDevice device;

    public AVDeviceDTO(AVDriver driver, AVDevice device) {
        this.driver = driver;
        this.device = device;
    }

    /**
     * Returns driver
     *
     * @return
     */
    public AVDriver getDriver() {
        return driver;
    }

    /**
     * Returns Device
     *
     * @return
     */
    public AVDevice getDevice() {
        return device;
    }

    /**
     * returns the device name
     * @return
     */
    @Override
    public String toString() {
        return device.getName();
    }
}
