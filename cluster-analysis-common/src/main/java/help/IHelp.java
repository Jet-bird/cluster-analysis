package help;

import java.io.Serializable;


public interface IHelp extends Serializable {
    /**
     * get help information for this class.
     * @return
     */
    String help();

    /**
     * get the function's help
     * @param function
     * @return
     */
    String help(String function);

    /**
     * get the var's help
     * @param type
     * @param var
     * @return
     */
    String help(Object type, String var);
}
