package com.opaleye.snackvar.settings;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.opaleye.snackvar.GanseqTrace;
import com.opaleye.snackvar.RootController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsController implements Initializable  {
	@FXML private TextField tf_secondPeakCutoff;
	@FXML private TextField tf_gapOpenPenalty;
	@FXML private TextField tf_trainStartFileNo;
	@FXML private TextField tf_testStartFileNo;
	@FXML private HBox refHBox;


	public static final String noFiltering = "No filtering";
	public static final String ruleBasedFiltering = "Rule based";
	//public static final String AIBasedFiltering = "AI based";

	//for test data generation
	private static int trainCsvCounter = 0;
	private static int testCsvCounter = 0;
	private static File baseDir = new File("../../chromatogram");
	private static File baseTrainDir = new File(baseDir, "train");
	private static File featuresDirTrain = new File(baseTrainDir, "features");
	private static File labelsDirTrain = new File(baseTrainDir, "labels");
	private static File baseTestDir = new File(baseDir, "test");
	private static File featuresDirTest = new File(baseTestDir, "features");
	private static File labelsDirTest = new File(baseTestDir, "labels");

	private RootController rootController = null;
	private Stage primaryStage;



	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setRootController(RootController rootController) {
		this.rootController = rootController;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void initValues(double secondPeakCutoff, int gapOpenPenalty, int filterQualityCutoff) {
		refHBox.getChildren().add(rootController.getOpenRefButton());


		/*
		tf_trainStartFileNo.setText(String.format("%d",  trainCsvCounter));
		tf_testStartFileNo.setText(String.format("%d",  testCsvCounter));
		 */
		tf_secondPeakCutoff.setText(String.format("%.2f",  secondPeakCutoff));
		tf_gapOpenPenalty.setText(String.format("%d",  gapOpenPenalty));

	}


	private boolean setValues() {
		double secondPeakCutoff;
		int gapOpenPenalty;

		try {
			secondPeakCutoff = Double.parseDouble(tf_secondPeakCutoff.getText());
			gapOpenPenalty = Integer.parseInt(tf_gapOpenPenalty.getText());

			if(secondPeakCutoff>1 || secondPeakCutoff <0)
				throw new NumberFormatException("Cutoff value should be number between 0 and 1");
			if(gapOpenPenalty <= 0)
				throw new NumberFormatException("Gap open penalty should be positive integer");

			rootController.setProperties(secondPeakCutoff, gapOpenPenalty);
		}
		catch (Exception ex) {
			rootController.popUp(ex.getMessage());
			return false;
		}
		return true;
	}

	public void handleConfirm() {
		if(setValues()) {
			if(rootController.fwdLoaded)
				rootController.trimmedFwdTrace.applyAmbiguousSymbol();
			if(rootController.revLoaded)
				rootController.trimmedRevTrace.applyAmbiguousSymbol();
			if(rootController.alignmentPerformed) {
				rootController.handleRun();
			}
			primaryStage.close();
		}
	}

	public void handleDefault() {
		tf_secondPeakCutoff.setText(String.format("%.2f",  RootController.defaultSecondPeakCutoff));
		tf_gapOpenPenalty.setText(String.format("%d",  RootController.defaultGOP));
	}



	public void handleCustomRef() {
		rootController.handleOpenRef();
	}



}
