/**
 * Created by olivier on 16-10-10.
 */

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import Exceptions.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.NumberFormat;

public class MainUIController {

    private Logger logger = LoggerFactory.getLogger(Main.class);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox Layout;

    @FXML
    private ComboBox<AVDeviceDTO> inputVideoPath;

    @FXML
    private ComboBox<AVDeviceDTO> inputAudioPath;

    @FXML
    private ToggleButton streamToggle;

    @FXML
    private TextField outputPath;

    @FXML
    private ComboBox<String> resolutionsList;

    @FXML
    private Slider slider;

    @FXML
    private TextField audioOffsetValue;

    private StreamManager sm;

    private Computer computer;

    private boolean streaming = false;

    public MainUIController() throws OSNotSupportedError, IOException {
        computer = Computer.getInstance();
        logger.info("app started");
    }

    /**
     * Update audio inputs
     *
     * @param event
     */
    @FXML
    void updateAudioInputs(MouseEvent event) {
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                computer.updateDriverDevices();

                //update the UI with the new list
                Platform.runLater(
                        () -> {
                            updateAudioInputsUIList();
                        }
                );

                return 0;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Update Audio Inputs UI list
     *
     */
    private void updateAudioInputsUIList() {
        inputAudioPath.getItems().clear();

        populateAudioInputUIList();
    }

    /**
     * Populate the audio input UI list
     */
    private void populateAudioInputUIList() {
        for (AVDriver driver : computer.getAvDrivers()) {

            //Check if the driver has Audio devices, if not move to the next driver
            if(driver.getAudioDevices() == null)
                continue;

            LinkedList<AVDeviceDTO> avDTOs = AVDeviceDTOAssemblerUtility.assembleFromList(driver, driver.getAudioDevices());
            inputAudioPath.getItems().addAll(avDTOs);
        }
    }

    /**
     * Reads the value of the offset slider on the UI and sets the value to the audioOffsetValue variable
     * @param event
     */
    @FXML
    void updateAudioOffset(MouseEvent event) {
        audioOffsetValue.textProperty().bindBidirectional(slider.valueProperty(),NumberFormat.getNumberInstance());
    }

    /**
     * Update video inputs
     *
     * @param event
     */
    @FXML
    void updateVideoInputs(MouseEvent event) {
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                computer.updateDriverDevices();

                //update the UI with the new list
                Platform.runLater(
                        () -> {
                            updateVideoInputsUIList();
                        }
                );
                return 0;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Update the video input UI list
     *
     * @throws IOException
     */
    private void updateVideoInputsUIList() {
        inputVideoPath.getItems().clear();
        populateVideoInputUIList();
    }

    /**
     * Populate the video input UI list
     */
    private void populateVideoInputUIList() {
        for (AVDriver driver : computer.getAvDrivers()) {

            //Check if the driver has Audio devices, if not move to the next driver
            if(driver.getVideoDevices() == null)
                continue;

            LinkedList<AVDeviceDTO> avDTOs = AVDeviceDTOAssemblerUtility.assembleFromList(driver, driver.getVideoDevices());
            inputVideoPath.getItems().addAll(avDTOs);
        }
    }

    /**
     * Will start/stop the stream
     * @param event
     */
    @FXML
    void onAction(ActionEvent event) {

        if (streamToggle.isArmed() && streaming == false) {
            startStreaming();

            streamToggle.setText("Stop Stream");
        } else if (streamToggle.isArmed() && streaming == true) {
            stopStreaming();

            streamToggle.setText("Start Stream");
        }
    }

    /**
     * Stops the stream
     */
    private void stopStreaming() {
        System.out.println("Stopping...");
        try {
            sm.stopStreaming();
        } catch (IOException e) {
            logger.error("IO Exception thrown", e);
            e.printStackTrace();
        }
        System.out.println("Stopped.");
        streaming = false;
    }

    /**
     * Start Streaming
     *
     */
    private void startStreaming() {
        try {
            sm = new StreamManager();
        } catch (OSNotSupportedError e) {
            logger.error("The Operating system is not supported", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IO Exception thrown", e);
            e.printStackTrace();
        }

        //todo refactor into builder pattern
        AVDevice selectedVideoDevice = inputVideoPath.getValue().getDevice();
        AVDriver selectedVideoDriver = inputVideoPath.getValue().getDriver();


        try {
            if(resolutionsList.getValue() != null)
                selectedVideoDevice.setSelectedResolution(resolutionsList.getValue());
        } catch (ResolutionNotSupportedException e) {
            logger.error("The selected resolution is not supported", e);
            e.printStackTrace();
        }

        try {
            selectedVideoDriver.selectVideoDevice(selectedVideoDevice);
        } catch (AVDeviceDoesNotExistException e) {
            logger.error("The selected AV device does not exists", e);
            e.printStackTrace();
        }

        AVDevice selectedAudioDevice = inputAudioPath.getValue().getDevice();
        AVDriver selectedAudioDriver = inputAudioPath.getValue().getDriver();

        try {
            selectedAudioDriver.selectAudioDevice(selectedAudioDevice);
        } catch (AVDeviceDoesNotExistException e) {
            logger.error("The selected AV device does not exists", e);
            e.printStackTrace();
        }

        sm.setInputVideoDriver(selectedVideoDriver);
        sm.setInputAudioDriver(selectedAudioDriver);
        sm.setOutputPath(outputPath.getText());
		sm.setAudioOffset(slider.getValue());

        try {
            sm.prepareStream();
        } catch (IOException e) {
            logger.error("IO Exception thrown", e);
            e.printStackTrace();
        }

        try {
            sm.setOutputPreset(new YoutubePreset());
        } catch (StreamIONotSetException e) {
            logger.error("You need to set the stream IO before the setting the preset", e);
            e.printStackTrace();
        }

        try {
            sm.startStreaming();
        } catch (CantStartStreamException e) {
            logger.error("The stream failed to start", e);
            e.printStackTrace();
        }

        streaming = true;
    }

    /**
     * On video input selected action
     *
     * @param event
     */
    @FXML
    void videoInputSelected(ActionEvent event) {

        AVDeviceDTO selectedValue = inputVideoPath.getValue();
        if (selectedValue == null)
            return;

        LinkedList<String> resolutions = selectedValue.getDevice().getResolutions();

        if (resolutions != null) {
            resolutionsList.getItems().clear();
            //put the resolutions in the resolutions' combobox
            resolutionsList.getItems().addAll(resolutions);
            //active the resolutions' combobox
            resolutionsList.setDisable(false);
        } else {
            resolutionsList.setDisable(true);
            resolutionsList.getItems().clear();
        }

    }

    /**
     * Called when the UI is initialized
     */
    @FXML
    void initialize() {
        assert Layout != null : "fx:id=\"Layout\" was not injected: check your FXML file 'UI.fxml'.";
        assert inputVideoPath != null : "fx:id=\"inputVideoPath\" was not injected: check your FXML file 'UI.fxml'.";
        assert inputAudioPath != null : "fx:id=\"inputAudioPath\" was not injected: check your FXML file 'UI.fxml'.";
        assert streamToggle != null : "fx:id=\"streamToggle\" was not injected: check your FXML file 'UI.fxml'.";
        assert outputPath != null : "fx:id=\"outputPath\" was not injected: check your FXML file 'UI.fxml'.";
		assert slider != null : "fx:id=\"slider\" was not injected: check your FXML file 'UI.fxml'.";
        assert audioOffsetValue != null : "fx:id=\"field\" was not injected: check your FXML file 'UI.fxml'.";

        populateVideoInputUIList();
        populateAudioInputUIList();
		audioOffsetValue.textProperty().bindBidirectional(slider.valueProperty(),NumberFormat.getNumberInstance());
    }
}
