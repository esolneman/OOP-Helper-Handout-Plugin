package controller;

//is singleton
public class ToolWindowController {
    private static ToolWindowController single_instance = null;

    public static ToolWindowController getInstance() {
        if (single_instance == null) {
            single_instance = new ToolWindowController();
        }
        return single_instance;
    }

    private ToolWindowController(){

    }

}
