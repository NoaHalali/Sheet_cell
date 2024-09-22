package components.left.commands.filter;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;

import java.util.Set;

public class FilterPopupController {
    @FXML
    private ListView<String> filterListView;
    private boolean isSelected = false;
    private ObservableList<String> selectedItems = FXCollections.observableArrayList();
    private ObservableList<String> items ;
//    public void initialize() {
//        // אתחול ListView לסינון
//        initializeFilterListView();
//    }

    public void initializeFilterListView(Set<String> values) {
        // יצירת רשימה של פריטים מהערכים שהועברו ל-Set
         items = FXCollections.observableArrayList(values);

        // הגדרת רשימת הפריטים ל-ListView
        filterListView.setItems(items);

        // יצירת CheckBox עבור כל פריט ברשימה
        filterListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

            // הוספת מאזין כדי לעדכן את רשימת הפריטים הנבחרים
            selected.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
            });

            return selected;
        }));
    }


    @FXML
    private void applyFilterAction() {
        // כאן תוכל לבצע את הלוגיקה לסינון
        System.out.println("Selected items: " + selectedItems);
        isSelected=true;
        // סגור את הפופאפ
        Stage stage = (Stage) filterListView.getScene().getWindow();
        stage.close();
    }

    public ObservableList<String> getSelectedItems() {
        if(isSelected) {
            return selectedItems;
        }
        return items;
    }
}
