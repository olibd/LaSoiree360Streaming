import Exceptions.OSNotSupportedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by olivier on 16-11-01.
 */
public class Computer {

    private static Computer instance;
    private Logger logger = LoggerFactory.getLogger(Computer.class);
    public enum OSType {MAC, WIN};
    private static OSType osType;

    static {
        setOStype();
    }

    private HashMap<String, LinkedList<String>> avDevices;
    private AVDriver[] avDrivers;

    private Computer(){
        try {
            avDrivers = AVDriverFactory.getOrInitInstance(osType).getAVDrivers();
        } catch (IOException e) {
            logger.error("IOException while loading the drivers", e);
            e.printStackTrace();
        }
    }

    /**
     * Set the OS
     *
     * @throws OSNotSupportedError
     */
    private static void setOStype() throws OSNotSupportedError {
        if (System.getProperty("os.name").startsWith("Mac"))
            osType = OSType.MAC;
        else if (System.getProperty("os.name").startsWith("Win"))
            osType = OSType.WIN;
        else {
            osType = null;
            throw new OSNotSupportedError();
        }
    }

    /**
     * @return the Computer instance
     * @throws OSNotSupportedError
     * @throws IOException
     */
    public static Computer getInstance() {
        if (instance == null)
            instance = new Computer();

        return instance;
    }

    /**
     * @return the OS osType
     */
    public static OSType getOSType() {
        return osType;
    }

    /**
     * @return true if Computer is MacOS
     */
    public boolean isMac() {
        return OSType.MAC.equals(osType);
    }

    /**
     * @return true if Computer is WinOS
     */
    public boolean isWin() {
        return OSType.WIN.equals(osType);
    }

    /**
     * @return array of AVdrivers of available to this computer and supported by this application
     */
    public AVDriver[] getAvDrivers() {
        return avDrivers;
    }

    /**
     * Update drivers' devices
     *
     * @return The list of Drivers with updated AVDevices
     */
    public AVDriver[] updateDriverDevices() {
        for (AVDriver driver :
                avDrivers) {
            try {
                driver.updateAVdevices();
            } catch (IOException e) {
                logger.error("Unable to update the devices of driver " + driver.getInputFormat(), e);
                e.printStackTrace();
            }
        }

        return avDrivers;
    }

}
