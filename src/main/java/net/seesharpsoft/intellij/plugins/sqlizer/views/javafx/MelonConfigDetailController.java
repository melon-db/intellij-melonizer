package net.seesharpsoft.intellij.plugins.sqlizer.views.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.Getter;
import net.seesharpsoft.commons.collection.Properties;
import net.seesharpsoft.melon.FileAnalyzerManager;
import net.seesharpsoft.melon.config.TableConfig;

public class MelonConfigDetailController {

    @Getter
    private String fileName;
    @Getter
    private TableConfig tableConfig;

    @FXML private TableView tblColumns;
    @FXML private TableView tblProperties;

    @FXML
    public void initialize() {

    }

    public void init(String fileName, Properties properties) {
        this.fileName = fileName;
        this.tableConfig = FileAnalyzerManager.INSTANCE.analyze(getFileName(), properties);
        update();
    }

    protected void updateColumns() {
        tblColumns.getColumns().clear();

        if (tableConfig == null) {
            return;
        }

        Object[] tableColumns = new Object[tableConfig.columns.size() + 1];
        TableColumn tableColumn = new TableColumn("Properties");
        tableColumn.setCellValueFactory(
                (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString())
        );
        tableColumns[0] = tableColumn;

        ObservableList rows = FXCollections.observableArrayList();
        ObservableList row = FXCollections.observableArrayList();
        row.add("Name");
        for (int i = 0; i < tableConfig.columns.size(); ++i) {
            final int j = i + 1;
            tableColumn = new TableColumn("Column " + j);
            tableColumn.setMaxWidth(300);
            tableColumn.setEditable(true);
            tableColumn.setCellValueFactory(
                    (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())
            );
            tableColumns[j] = tableColumn;

            row.add(tableConfig.columns.get(i).name);
        }
        tblColumns.getColumns().addAll(tableColumns);
        rows.add(row);

        tblColumns.setItems(rows);
    }

    protected void updateProperties() {
        assert tableConfig != null;

        tableConfig.getProperties().put("fileName", getFileName());
        tableConfig.getProperties().put("schema", "just a schema");

        ObservableMap<String, String> items = FXCollections.observableMap(tableConfig.getProperties());

        TableColumn<String, String> tableColumn = (TableColumn)tblProperties.getColumns().get(0);
        tableColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue()));

        tableColumn = (TableColumn)tblProperties.getColumns().get(1);
        tableColumn.setCellValueFactory(cd -> Bindings.valueAt(items, cd.getValue()));

        ObservableList<String> keys = FXCollections.observableArrayList(items.keySet());
        tblProperties.setItems(keys);
    }

    protected boolean update() {
        if (tableConfig == null) {
            return false;
        }

        updateProperties();
        updateColumns();

        return true;
    }

    @FXML protected void onItemAdd(MouseEvent mouseEvent) {
    }

    @FXML protected void onItemRemove(MouseEvent mouseEvent) {
    }

}
