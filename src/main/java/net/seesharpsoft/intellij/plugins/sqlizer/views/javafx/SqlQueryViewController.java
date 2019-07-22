package net.seesharpsoft.intellij.plugins.sqlizer.views.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SqlQueryViewController {

    @FXML private TextArea txtInput;
    @FXML private TableView tblResult;

    private Connection connection;

    public SqlQueryViewController() {

    }

    public SqlQueryViewController(Connection connection) {
        this.connection = connection;
    }

    protected ResultSet performQuery(String sql) {
        try {
            return connection.prepareStatement(sql).executeQuery();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    protected int prepareTable(ResultSetMetaData resultSetMetaData) throws SQLException {
        Object[] tableColumns = new Object[resultSetMetaData.getColumnCount()];

        tblResult.getColumns().clear();
        for (int i = 0; i < tableColumns.length; ++i) {
            final int j = i;
            TableColumn tableColumn = new TableColumn(resultSetMetaData.getColumnName(i + 1));
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
            throw new RuntimeException(exc);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException exc) {
                throw new RuntimeException(exc);
            }
        }
    }

    @FXML protected void onTxtInputKeyRelease(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case ENTER:
                populateTable(performQuery(txtInput.getText()));
                break;
        }
    }
}
