# La Soiree 360 - 360 Camera video streaming component
The video streaming software component of the La Soirée 360 project. La Soirée 360 is a software suite (video streaming software, control panel and mobile app for viewing) developed to stream live and in 360 the CBC/Radio-Canada's radio show "La Soirée Est (Encore) Jeune".

Mobile apps:

Android: https://play.google.com/store/apps/details?id=com.s490.lasoiree

iOS: https://itunes.apple.com/ca/app/la-soir%C3%A9e-360/id1230073724?mt=8

# Documentation
Below you can find documentation pertaining to the streaming component

## Class Diagram
![camera subsystem class diagram - page 1 2](https://user-images.githubusercontent.com/6964009/36993843-3cc5e52c-207d-11e8-849e-45a0b0be8728.png)

### Architecture Update Notes
The AVFoundation audio and video were split into two separate drivers because they now handle their inputs differently. The Drivers.AVFoundationVideoDriver handles the video input like the original Drivers.AVFoundationDriver class does. However, the Drivers.AVFoundationAudioDriver now has a special kind of Devices.FFMPEGAVDevice called Devices.AVFoundationAudioDevice. This device also implement the Utilities.IActivable interface which gives it the ability to be "activated" or "deactivated". In the case of this device, this means that when activated the device will start reading from its input even if a stream is not started. This is useful for preprocessing such as adding filters or previewing the input. This is implemented using a new concept called preprocessors. Preprocessors implement the new IPreprocessor interface, this interface also implements the Utilities.IActivable interface. When activated, preprocessors will modified, according to their implementation, the input of the an Devices.AVDevice. The Preprocessors.FFMPEGAudioInputUDPStreamer class is currently the only implementation of the IPreprocessor interface. This class will stream over UDP to the localhost the input of an Devices.FFMPEGAVDevice, in this case the Devices.AVFoundationAudioDevice. This Devices.AVFoundationAudioDevice will instruct the FFMPEG streaming engine that it can read its input from "udp://127.0.0.1:someport".
Since the FFMPEG streaming engine reads a UDP stream and not directly the AVFoundation device it does not stutters since the stuttering is caused by a problem in FFMPEG's implementation of the AVFoundation driver.
An Drivers.IFFMPEGCompatibleDriver interface was also added and is implemented by the Drivers.AVDriver class. It is meant to segregate between drivers that can be read by the FFMPEG streaming engine and those that can't. Currently all drivers can be read by the FFMPEG Streaming engine.
Finally, the Drivers.FFMPEGAVDriver class represents driver's who's input are read directly from FFMPEG.

## Internationalization
In JavaFX, the UI framework used for this application has native support for internationalization. Implementing it is quite simple and requires the developer to create and load a .properties file for the desired language where the actual strings are defined. In our application the file is imported in the Main class as a ResourceBundle object which is passed to the MainUIController class. Finally, in the .fxml file, the UI text is wired to the string's identifier located in the .properties file using "%stringUniqueIdenfier" and the values of the text is loaded at runtime by JavaFX.

## Logging

### SFL4J + LogBack
In order to capture different type of event such as exception and important debug information we integrated the SLF4J logging facade. This allows us to add a logging mechanism in the code and separately define the implementation. Currently we are using the LogBack logging engine which is a native implementation of the SLF4J API but if need be, we can quickly switch the logging engine just by changing a line in Maven without touching the actual code base.
