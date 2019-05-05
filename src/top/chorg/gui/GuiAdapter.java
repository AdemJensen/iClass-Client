package top.chorg.gui;

public interface GuiAdapter {

    /**
     * Run the event.
     * This method will be invoked by the AftGuiAdapter.
     *
     * @param args Arguments.
     * @return event execution result
     */
    int makeEvent(String...args);

    /**
     * Send the cmd to the kernel.
     */
    void executeCmd(String...args);

    boolean containsEvent(String key);

    void register(String name, Class<?> event, String methodName);
}
