package ch.makery.address.view;

import java.io.File;

import ch.makery.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * ���� ���̾ƿ��� ���� ��Ʈ�ѷ�. ���� ���̾ƿ��� �޴��ٿ� JAVAFX ������Ʈ�� �� ������ ������ �⺻���� ���̾ƿ�
 * 
 *
 */
public class RootLayoutController {
	//���� ���ø����̼� ����
	private MainApp mainApp;
	
	/**
	 * ������ �ٽ� �����ϱ� ���� ���� ���ø����̼��� ȣ��
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp=mainApp;
	}
	
	/**
	 * ��� �ִ� �ּҷ��� �����.
	 */
	@FXML
	private void handleNew() {
		mainApp.getPersonData().clear();
		mainApp.setPersonFilePath(null);
	}
	
	/**
	 *FileChooser�� ��� ����ڰ� ������ �ּҷ��� �����ϰ� �Ѵ�.
	 */
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();
		
		//Ȯ���� ���͸� �����Ѵ�.
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		//Save File Dialog�� �����ش�.
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		
		if(file !=null) {
			mainApp.loadPersonDataFromFile(file);
		}
	}
	
	/**
	 * ���� ���� �ִ� ���Ͽ� �����Ѵ�.
	 * ���� ���� �ִ� ������ ������ "save as " ���̾�α׸� �����ش�.
	 */
	@FXML
	private void handleSave() {
		File personFile = mainApp.getPersonFilePath();
		if(personFile != null) {
			mainApp.savePersonDataToFile(personFile);
		}
		else {
			handleSaveAs();
		}
	}
	
	/**
	 * 
	 * FileChooser �� ��� ����ڰ� ������ ������ �����ϰ� �Ѵ�.
	 */
	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		
		//Ȯ���� ���͸� �����Ѵ�.
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Save File Dialog�� �����ش�.
      	File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
      	
      	if(file != null) {
      		//��Ȯ�� Ȯ���ڸ� ������ �Ѵ�.
      		if(!file.getPath().endsWith(".xml")) {
      			file = new File(file.getPath()+".xml");
      		}
      		mainApp.savePersonDataToFile(file);
      	}
	}
	/**
	 * About ���̾�α׸� �����ش�.
	 */
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AddressApp");
		alert.setHeaderText("About");
		alert.setContentText("Author:");
		
		alert.showAndWait();
	}
	/**
	 * ���ø����̼��� �ݴ´�.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
