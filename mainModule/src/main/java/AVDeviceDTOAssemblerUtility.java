import java.util.LinkedList;

/**
 * Created by olivier on 16-12-27.
 */
public class AVDeviceDTOAssemblerUtility {

    /**
     * Returns AVDeviceDTO from supplied Driver and Device
     *
     * @param driver
     * @param device
     * @return
     */
    public static AVDeviceDTO assemble(AVDriver driver, AVDevice device) {
        return new AVDeviceDTO(driver, device);
    }

    /**
     * Returns list of AVDeviceDTOs from supplied Driver and list of Devices
     *
     * @param driver
     * @param devices
     * @return
     */
    public static LinkedList<AVDeviceDTO> assembleFromList(AVDriver driver, LinkedList<AVDevice> devices) {
        LinkedList<AVDeviceDTO> dtos = new LinkedList<>();
        for (AVDevice device :
                devices) {
            dtos.add(assemble(driver, device));
        }

        return dtos;
    }
}
