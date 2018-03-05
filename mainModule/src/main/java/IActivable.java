/**
 * Created by olivier on 2017-03-12.
 */
public interface IActivable {
    /**
     * Allows activation
     */
    void activate();

    /**
     * Allows deactivation
     */
    void deactivate();

    /**
     * @return the activation state
     */
    boolean isActive();
}
