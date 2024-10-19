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
    public final static String MULTI_SHEETS_SCREEN_FXML_RESOURCE_LOCATION = "/client/components/multiSheetsScreen/multiSheetsScreen.fxml";
    public final static String CELL_FXML_RESOURCE_LOCATION = "/client/components/sheetManager/parts/center/cell/cell.fxml";
    public final static String TABLE_FXML_RESOURCE_LOCATION = "/client/components/sheetManager/parts/center/cellsTable/table.fxml";
    public final static String FILTER_POPUP_FXML_RESOURCE_LOCATION = "/client/components/sheetManager/parts/left/commands/filter/filterPopup.fxml";


    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/shticell";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    /*URLs*/
    //MainAppController
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String  GET_CHANGED_PERMISSION_FOR_USER = FULL_SERVER_PATH +"/getPermissionUpdatesForUser";

    //MultiSheetsScreenController
    public static final String UPLOAD_FILE = FULL_SERVER_PATH + "/uploadFile";
    public final static String GET_SHEETS_LIST = FULL_SERVER_PATH + "/getSheetsDetailsList";


    //SheetManagerController
    public static final String GET_SHEET_DTO = FULL_SERVER_PATH + "/getSheetDTO";
    public static final String GET_CLONED_SHEET = FULL_SERVER_PATH + "/getClonedSheet";
    public static final String GET_SHEET_DTO_BY_VERSION = FULL_SERVER_PATH + "/getSheetDTOByVersion";

    //cells
    public static final String UPDATE_CELL = FULL_SERVER_PATH + "/updateCell";
    public static final String GET_CELL_DTO_URL = FULL_SERVER_PATH + "/getCellDTO";

    //ranges
    public static final String ADD_RANGE_URL = FULL_SERVER_PATH + "/addRange";
    public static final String DELETE_RANGE_URL = FULL_SERVER_PATH + "/deleteRange";
    public static final String GET_RANGE_COORDINATES = FULL_SERVER_PATH + "/getRangeCoordinates";
    public static final String GET_COLUMN_DATA_IN_RANGE = FULL_SERVER_PATH + "/getColumnDataInRange";

    //sorting
    public static final String GET_SORTED_SHEET_DTO = FULL_SERVER_PATH + "/getSortedSheetDTO";

    //filtering
    public static final String GET_DISTINCT_VALUES_OF_MULTIPLE_COLS_PATH = FULL_SERVER_PATH + "/getDistinctValuesOfMultipleColsInRange";
    //public static final String FILTER_DATA = "/filterData";
    public static final String GET_FILTERED_SHEET_DTO = FULL_SERVER_PATH + "/getFilteredSheetDTOFromMultipleCols";

    //what if
    public static final String SET_SHEET_IN_WHAT_IF_MODE = FULL_SERVER_PATH + "/setSheetInWhatIfMode";
    public static final String CALCULATE_WHAT_IF_VALUE_FOR_CELL = FULL_SERVER_PATH + "/calculateWhatIfValueForCell";

    //permissions
    public static final String REQUEST_PERMISSION_URL = FULL_SERVER_PATH + "/requestPermission";
    public static final String HANDLE_REQUEST_PERMISSION_URL = FULL_SERVER_PATH + "/handleRequestPermission";
    public static final String GET_REQUESTS_LIST_URL = FULL_SERVER_PATH + "/getRequestsList";


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
