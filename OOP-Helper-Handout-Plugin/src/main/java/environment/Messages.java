package environment;

public class Messages {

    private Messages(){}

    public static final String FILES_SELECTING_TEXT = "Choose location for PDF";
    public static final String FILES_SELECTING_DESCRIPTION = "Description for Downloading PDF";

    public static final String CHECKLIST_PREDEFINED_HEADER = "Vorgegebene Aufgaben";
    public static final String CHECKLIST_USER_HEADER = "Deine Aufgaben";
    public static final String NOTES_HEADER = "Deine Notizen";
    public static final String ASSESSMENT_CRITERIA_HEADER = "Bewertungskriterien für die Studienleistung";

    public static final String QUESTIONNAIRE_URL_BASE = "https://docs.google.com/forms/d/e/1FAIpQLSfKACxx2U5O0ZhdUkfKyIs-7oiUxH81KLIfB44mn2s7e0q7nw/viewform?entry.424473088=";
    public static final String QUESTIONNAIRE_DESCRIPTION_START = " <html><body>" //
            + "Bitte f&uuml;lle den Zwischenfragebogen aus: <br> <br>" +
            "<a href=" ;
    public static final String QUESTIONNAIRE_DESCRIPTION_END = "> Link zum Zwischenfragebogen </a>" //
            + "<br> <br> Vielen Dank f&uuml;r deine Teilnahme an der Studie und weiterhin \n viel Erfolg bei der Bearbeitung der Studienleistung" +//
            "</body> </html>";

    public static final String QUESTIONNAIRE_TITLE = "Teilnahme am Zwischenfragebogen";

    public static final String INITIAL_NOTES_TEXT = "<p id=\"date\" style=\"text-align: right;\">23.11.2019 14:15</p>\n" + "<p><strong>Die erste Notiz</strong></p>\n" +
            "<p>Hier kannst du Notizen verfassen, zum Beispiel um zu notieren</p>\n" +
            "<ul>\n" +
            "<li>woran du als letztes gearbeitet hast, wenn du eine Pause machst,</li>\n" +
            "<li>was du vorhast als n&auml;chstes zu machen oder</li>\n" +
            "<li>was du dir sonst noch merken willst</li>\n" +
            "</ul>\n" +
            "<p>Du kannst deine Notizen bearbeiten, indem du auf den \"Edit-Button\" neben der Seiten&uuml;berschrift klickst.</p>";
}
