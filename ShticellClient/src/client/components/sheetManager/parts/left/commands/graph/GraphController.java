package client.components.sheetManager.parts.left.commands.graph;
import client.components.Utils.StageUtils;
import client.components.sheetManager.SheetManagerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import parts.cell.CellDTO;

import java.util.List;

public class GraphController {

//    @FXML
//    private ComboBox<String> xAxisComboBox;
//
//    @FXML
//    private ComboBox<String> yAxisComboBox;

    @FXML
    private TextField xRangeTextField;

    @FXML
    private TextField yRangeTextField;

    private SheetManagerController mainController;

    public void setMainController(SheetManagerController mainController) {
        this.mainController = mainController;
    }

//    public void createGraph(String xRange, String yRange) throws Exception {
//        // שליפת הנתונים מהגיליון דרך המנוע
//        if (xRange.isEmpty() || yRange.isEmpty()) {
//            throw new IllegalArgumentException("Please enter valid ranges.");
//        }
//
//        List<CellDTO> xData = mainController.getColumnDataInRange(xRange);
//        List<CellDTO> yData = mainController.getColumnDataInRange(yRange);
//
//        if (xData.size() == yData.size()) {
//            NumberAxis xAxis = new NumberAxis();
//            NumberAxis yAxis = new NumberAxis();
//            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//
//            XYChart.Series<Number, Number> series = new XYChart.Series<>();
//            for (int i = 0; i < xData.size(); i++) {
//                double xValue = xData.get(i).getEffectiveValue().extractValueWithExpectation(Double.class);
//                double yValue = yData.get(i).getEffectiveValue().extractValueWithExpectation(Double.class);
//                series.getData().add(new XYChart.Data<>(xValue, yValue));
//            }
//
//            lineChart.getData().add(series);
//
//            // הצגת הגרף בחלון חדש
//            Stage stage = new Stage();
//            Scene scene = new Scene(lineChart, 800, 600);
//            stage.setScene(scene);
//            stage.setTitle("Generated Graph");
//            stage.show();
//        } else {
//            throw new IllegalArgumentException("Mismatch in data sizes between X and Y columns.");
//        }
//    }

    public void createGraph(String xRange, String yRange) throws Exception {
        // בדיקה אם הטווחים ריקים
        if (xRange.isEmpty() || yRange.isEmpty()) {
            throw new IllegalArgumentException("Please enter valid ranges.");
        }
        // קריאה אסינכרונית לנתוני X
        mainController.getColumnDataInRange(xRange, xData -> {
            // קריאה אסינכרונית לנתוני Y אחרי קבלת הנתונים של X
            mainController.getColumnDataInRange(yRange, yData -> {
                // בדיקה אם גודל הנתונים תואם
                if (xData.size() == yData.size()) {
                    Platform.runLater(() -> {
                        try {
                            // יצירת הגרף
                            NumberAxis xAxis = new NumberAxis();
                            NumberAxis yAxis = new NumberAxis();
                            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

                            XYChart.Series<Number, Number> series = new XYChart.Series<>();
                            for (int i = 0; i < xData.size(); i++) {
                                double xValue = xData.get(i).getEffectiveValue().extractValueWithExpectation(Double.class);
                                double yValue = yData.get(i).getEffectiveValue().extractValueWithExpectation(Double.class);
                                series.getData().add(new XYChart.Data<>(xValue, yValue));
                            }

                            lineChart.getData().add(series);

                            // הצגת הגרף בחלון חדש
                            Stage stage = new Stage();
                            Scene scene = new Scene(lineChart, 800, 600);
                            stage.setScene(scene);
                            stage.setTitle("Generated Graph");
                            stage.show();
                        } catch (Exception e) {
                            StageUtils.showAlert("Error", "Failed to create graph: " + e.getMessage());
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        StageUtils.showAlert("Error", "Mismatch in data sizes between X and Y columns.");
                    });
                }
            });
        });
    }


}
