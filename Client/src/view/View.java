package view;

public enum View {
    START("fxml/StartView.fxml"),
    DASHBOARD("fxml/DashboardView.fxml"),
    SETTINGS("fxml/SettingsView.fxml");

    private final String fxmlFile;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
