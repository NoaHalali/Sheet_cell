package client.components.Utils;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/client/components/mainAppController/app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/client/components/login/login.fxml";
    public final static String SHEET_MANAGER_FXML_RESOURCE_LOCATION = "/client/components/sheetManager/sheetManager.fxml";
    public final static String SHEETS_AND_PERMISSIONS_FXML_RESOURCE_LOCATION = "/client/components/multiSheetsScreen/multiSheetsScreen.fxml";
    public final static String CELL_FXML_RESOURCE_LOCATION = "/client/components/sheetManager/parts/center/cell/cell.fxml";
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/shticell";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;


    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public static final String UPLOAD_FILE = FULL_SERVER_PATH + "/uploadFile";
    public static final String GET_SHEET_DTO = FULL_SERVER_PATH + "/getSheetDTO";
    public static final String GET_SHEET_DTO_BY_VERSION = FULL_SERVER_PATH + "/getSheetDTOByVersion";
    public static final String UPDATE_CELL = FULL_SERVER_PATH + "/updateCell";
    public static final String GET_CELL_DTO_URL = FULL_SERVER_PATH + "/getCellDTO";
    public static final String ADD_RANGE_URL = FULL_SERVER_PATH + "/addRange";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
