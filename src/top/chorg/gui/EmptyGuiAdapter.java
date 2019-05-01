package top.chorg.gui;

public class EmptyGuiAdapter implements GuiAdapter {
    @Override
    public int makeEvent(String... args) {
        return 0;
    }

    @Override
    public void executeCmd(String... args) {

    }

    @Override
    public boolean containsEvent(String key) {
        return false;
    }

    @Override
    public void register(String name, Class<?> event, String methodName) {

    }
}
