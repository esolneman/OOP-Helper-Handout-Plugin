package environment;

public class Messages {

    private Messages(){}

    public static String TOOL_WINDOW_NAME = "Aufgabe (OOP)";

    public static final String FILES_SELECTING_TEXT = "Choose location for PDF";
    public static final String FILES_SELECTING_DESCRIPTION = "Description for Downloading PDF";

    public static final String QUESTIONNAIRE_URL_BASE = "https://docs.google.com/forms/d/e/1FAIpQLSfKACxx2U5O0ZhdUkfKyIs-7oiUxH81KLIfB44mn2s7e0q7nw/viewform?entry.424473088=";
    public static final String QUESTIONNAIRE_DESCRIPTION_START = " <html><body>" //
            + "Bitte f&uuml;lle den Zwischenfragebogen aus: <br> <br>" +
            "<a href=" ;
    public static final String QUESTIONNAIRE_DESCRIPTION_END = "> Link zum Zwischenfragebogen </a>" //
            + "<br> <br> Vielen Dank f&uuml;r deine Teilnahme an der Studie und weiterhin \n viel Erfolg bei der Bearbeitung der Studienleistung" +//
            "</body> </html>";

    public static final String QUESTIONNAIRE_TITLE = "Teilnahme am Zwischenfragebogen";

    public static final String CONTENT_CHANGED_TITLE = "Eine Aktuelle Version des Handouts wurde heruntergeladen";

    public static final String FILE_CHOOSER_TITLE = "Download des Handouts";
    public static final String DOWNLOAD_ERROR_MESSAGE = "Error while downloading the handout. Please try again.";
    public static final String DOWNLOAD_SUCCESS_MESSAGE = "Downloading was successfully";

    public static final String ERROR_MESSAGE_UPDATING_NO_INTERNET = "Keine Internetverbindung vorhanden. Handout Daten können momentan nicht aktualisiert werden.";
    public static final String ERROR_MESSAGE_DOWNLOADING_NO_INTERNET = "Keine Internetverbindung vorhanden. Handout Daten können momentan nicht heruntergeladen werden.";

    public static final String ERROR_MESSAGE_DOWNLOADING = "Fehler beim Herunterladen. Bitte versuche es erneut.";

    public static final String DOWNLOADING_DATA = "Handout Daten wurden runtergeladen.";
    public static final String DATA_IS_ACTUAL ="Handoutdaten sind bereits auf dem aktuellsten Stand";

}
