package components.left.commands.graph;
import components.MainComponent.AppController;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import parts.cell.CellDTO;

import java.util.List;

public class GraphController {

    private AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void createGraph(String xColumn, String yColumn) {
        // שליפת הנתונים מהגיליון דרך המנוע
        //SheetDTO sheet = mainController.getCurrentSheet();
        List<CellDTO> xData = mainController.getColumnData(xColumn);
        List<CellDTO> yData = mainController.getColumnData(yColumn);

        // וידוא שהעמודות מכילות את אותו מספר נתונים
        if (xData.size() == yData.size()) {
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
        } else {
            System.out.println("Mismatch in data sizes between X and Y columns.");
        }
    }
}
