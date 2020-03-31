package com.opaleye.snackvar.reference;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import com.opaleye.snackvar.RootController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Title : TVController
 * FXML Controller class for choosing Transcript Variant when multiple transcript variant exist in a genbank file
 * @author Young-gon Kim
 * 2018.7.
 */
public class TVController implements Initializable {
	@FXML private TableView tableView;
	@FXML private Button chooseBtn;

	private RootController rootController = null;
	private Stage primaryStage;
	private int selectedId = 0;
	
	/**
	 *Handler for choose button
	 */
	public void handleChoose() {
		rootController.setTranscriptVariant(selectedId);
		primaryStage.close();
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	public void setRootController(RootController rootController) {
		this.rootController = rootController;
	}

	/**
	 * Initialize
	 */
	public void init(Vector<TranscriptVariant> tvList) {
		
		ObservableList observableTvList = FXCollections.observableArrayList(tvList);
		TableColumn tcDescription = (TableColumn) tableView.getColumns().get(0);
		tcDescription.setCellValueFactory(new PropertyValueFactory("description"));

		TableColumn tcSize = (TableColumn) tableView.getColumns().get(1);
		tcSize.setCellValueFactory(new PropertyValueFactory("size"));
		tcSize.setStyle("-fx-alignment: CENTER;");
		
		tableView.setItems(observableTvList);
		
		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TranscriptVariant>() {
			@Override
			public void changed(ObservableValue<? extends TranscriptVariant> observable, TranscriptVariant oldValue, TranscriptVariant newValue) {
				if(newValue!=null) {
					selectedId = observableTvList.indexOf(newValue);
					//System.out.println("Id : " + selectedId);
				}
			}
		});


	}



}
