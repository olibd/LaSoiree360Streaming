package Preprocessors;
import Utilities.IActivable;
/**
 * Created by olivier on 2017-03-11.
 */
public interface IInputPreprocessor extends IActivable {
    /**
     * @return the destination where the output of the preprocessor is sent
     */
    String getDestination();
}
