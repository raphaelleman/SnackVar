package com.opaleye.snackvar;

import java.net.URL;
import java.util.ResourceBundle;

import com.opaleye.snackvar.tools.TooltipDelay;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Title : TrimController
 * FXML Controller class for Trim.fxml
 * @author Young-gon Kim
 * 2018.7.
 */
public class TrimController implements Initializable {
	@FXML private HBox traceHBox;
	@FXML private VBox sliderVBox;
	@FXML private ScrollPane tracePane;
	@FXML private Button confirmBtn;
	@FXML private Button zoomInButton;
	@FXML private Button zoomOutButton;


	private GanseqTrace targetTrace;

	private int direction = 1;
	private int startTrimPosition = 0;
	private int endTrimPosition = 0;
	boolean complement = false;

	private RootController rootController = null;
	private Stage primaryStage;
	private ImageView imageView = null;

	/**
	 *Handler for confirm button
	 */
	public void handleConfirm() {
		try {
			targetTrace.makeTrimmedTrace(startTrimPosition, endTrimPosition, complement);
		}
		catch(Exception ex) {
			rootController.popUp("An error has occured during trimming the trace, " + ex.getMessage());
			ex.printStackTrace();
			rootController.popUp("An error occured in trimming\n"+ex.getMessage());
			return;
		}

		if(targetTrace.getDirection() == GanseqTrace.FORWARD ) 
			rootController.confirmFwdTrace(targetTrace);
		else 
			rootController.confirmRevTrace(targetTrace);
		primaryStage.close();
	}

	/**
	 * Handler for Reset button
	 */
	public void handleReset() {
		startTrimPosition = targetTrace.getFrontTrimPosition();
		endTrimPosition = targetTrace.getTailTrimPosition();
		
		Image image = targetTrace.getTrimmingImage(startTrimPosition, endTrimPosition);
		imageView.setImage(image);
		traceHBox.getChildren().clear();
		traceHBox.getChildren().add(imageView);
	}


	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Initialize
	 */
	public void init() {
		Tooltip zoomInTooltip = new Tooltip("Zoom In");
		Tooltip zoomOutTooltip = new Tooltip("Zoom Out");
		TooltipDelay.activateTooltipInstantly(zoomInTooltip);
		TooltipDelay.activateTooltipInstantly(zoomOutTooltip);


		zoomInButton.setTooltip(zoomInTooltip);
		zoomOutButton.setTooltip(zoomOutTooltip);
		
		startTrimPosition = targetTrace.getFrontTrimPosition();
		endTrimPosition = targetTrace.getTailTrimPosition();
		
		if(startTrimPosition >= endTrimPosition) {
			startTrimPosition = 0;
			endTrimPosition = targetTrace.traceLength * GanseqTrace.traceWidth-1;
		}

		Image ret =targetTrace.getTrimmingImage(startTrimPosition, endTrimPosition);
		imageView = new ImageView(ret);
		traceHBox.getChildren().clear();
		traceHBox.getChildren().add(imageView);

		setMouseClick();
	}

	private void setMouseClick() {
		imageView.setOnMouseClicked(t-> {
			int tempStart = startTrimPosition, tempEnd = endTrimPosition;
			if(t.getButton() == MouseButton.PRIMARY) {
				tempStart = (int)t.getX();
			}
			else if (t.getButton()==MouseButton.SECONDARY) {
				tempEnd = (int)t.getX();
			}
			if(tempStart >= tempEnd) {
				rootController.popUp("Overlapping trimming area");
				return;
			}
			startTrimPosition = tempStart;
			endTrimPosition = tempEnd;
			imageView.setImage(targetTrace.getTrimmingImage(startTrimPosition, endTrimPosition));
			tracePane.setContent(imageView);
		}
				);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setRootController(RootController rootController) {
		this.rootController = rootController;
	}



	/**
	 * Loads ABI trace
	 * @param trace
	 * @param direction
	 */

	//to replace
	public void setTargetTrace(GanseqTrace trace, boolean complement) {
		this.targetTrace = trace;
		this.complement = complement;
	}


	public void handleZoomIn() {
		targetTrace.zoomIn();
		imageView.setImage(targetTrace.getTrimmingImage(startTrimPosition, endTrimPosition));
		traceHBox.getChildren().clear();
		traceHBox.getChildren().add(imageView);
		tracePane.setVvalue(1.0);
	}

	public void handleZoomOut() {
		targetTrace.zoomOut();
		imageView.setImage(targetTrace.getTrimmingImage(startTrimPosition, endTrimPosition));
		traceHBox.getChildren().clear();
		traceHBox.getChildren().add(imageView);
		tracePane.setVvalue(1.0);
	}

}