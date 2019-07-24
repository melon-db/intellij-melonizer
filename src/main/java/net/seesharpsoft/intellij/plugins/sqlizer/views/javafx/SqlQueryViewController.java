package net.seesharpsoft.intellij.plugins.sqlizer.views.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SqlQueryViewController {

    public static final int MAX_COLUMN_WIDTH = 300;
    public static final String NEW_LINE = "\n";

    @FXML private TextArea txtInput;
    @FXML private TableView tblResult;
    @FXML private TextFlow tfConsole;
    @FXML private ScrollPane spConsole;
    @FXML private AnchorPane apRoot;

    private Connection connection;

    public SqlQueryViewController() {

    }

    public SqlQueryViewController(Connection connection) {
        this.connection = connection;
    }

    @FXML
    public void initialize() {
        spConsole.vvalueProperty().bind(tfConsole.heightProperty());
    }

    protected ResultSet performQuery(String sql) {
        try {
            log(">> " + sql);
            return connection.prepareStatement(sql).executeQuery();
        } catch (SQLException exc) {
            logError(exc.getLocalizedMessage());
            throw new RuntimeException(exc);
        }
    }

    protected int prepareTable(ResultSetMetaData resultSetMetaData) throws SQLException {
        Object[] tableColumns = new Object[resultSetMetaData.getColumnCount()];

        tblResult.getColumns().clear();
        for (int i = 0; i < tableColumns.length; ++i) {
            final int j = i;
            TableColumn tableColumn = new TableColumn(resultSetMetaData.getColumnName(i + 1));
            tableColumn.setMaxWidth(MAX_COLUMN_WIDTH);
            tableColumn.setCellValueFactory(
                    (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())
            );
            tableColumns[i] = tableColumn;
        }
        tblResult.getColumns().addAll(tableColumns);

        return tableColumns.length;
    }

    protected void populateTable(ResultSet resultSet) {
        try {
            int noOfColumns = prepareTable(resultSet.getMetaData());
            ObservableList data = FXCollections.observableArrayList();

            while (resultSet.next()) {
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 0; i < noOfColumns; ++i) {
                    row.add(resultSet.getString(i + 1));
                }
                data.add(row);
            }

            tblResult.setItems(data);
        } catch (SQLException exc) {
            logError(exc.getLocalizedMessage());
            throw new RuntimeException(exc);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException exc) {
                logError(exc.getLocalizedMessage());
                throw new RuntimeException(exc);
            }
        }
    }

    @FXML protected void onTxtInputKeyRelease(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case ENTER:
                if (keyEvent.isControlDown()) {
                    populateTable(performQuery(txtInput.getText()));
                }
                break;
        }
    }

    protected void log(String message) {
        Text text = new Text();
        text.wrappingWidthProperty().bind(apRoot.widthProperty());
        text.setStyle("-fx-fill: #4F8A10;-fx-font-weight: bold;");
        text.setText(message + NEW_LINE);
        tfConsole.getChildren().add(text);
    }

    protected void logError(String error) {
        Text text = new Text();
        text.wrappingWidthProperty().bind(apRoot.widthProperty());
        text.setStyle("-fx-fill: #FF0000;-fx-font-weight: normal;");
        text.setText(error + NEW_LINE);
        tfConsole.getChildren().add(text);
    }
}
