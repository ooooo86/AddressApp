package ch.makery.address;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.makery.address.model.Person;
import ch.makery.address.model.PersonListWrapper;
import ch.makery.address.view.PersonEditDialogController;
import ch.makery.address.view.PersonOverviewController;
import ch.makery.address.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	/**
	 * ����ó�� ���� observable ����Ʈ
	 *
	 */
	private ObservableList<Person> personData = FXCollections.observableArrayList();
	
	
	
	 /**
     * ������
     */
    public MainApp() {
        // ���� �����͸� �߰��Ѵ�
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }
	/**
	 * ����ó�� ���� observable ����Ʈ�� ��ȯ�Ѵ�.
	 * @return
	 */
    
    public ObservableList<Person> getPersonData(){
    	return personData;
    }
    
	
	@Override
	//main ����� �����
	public void start(Stage primaryStage) {
		this.primaryStage=primaryStage;
		this.primaryStage.setTitle("AddressApp");
		
		initRootLayout();
		showPersonOverview();
	}
	
	/**
	 * ���� ���̾ƿ��� �ʱ�ȭ�ϰ� ���������� ������ ����ó ������ �����´�.
	 */
	public void initRootLayout() {
		try {
			//Fxml ���Ͽ��� ���� ���̾ƿ��� �����´�.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane)loader.load();
			
			//���� ���̾ƿ��� �����ϴ� scene�� �����ش�.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			//��Ʈ�ѷ����� MainApp���� ������ �ش�.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			
			primaryStage.show();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
		//���������� ������ ����ó ������ �����´�.
		File file = getPersonFilePath();
		if(file!=null) {
			loadPersonDataFromFile(file);
		}
	
	}
	
	//���� ���̾ƿ� �ȿ� ����ó ����� �����ش�.
	public void showPersonOverview() {
		try {
			//����ó ����� �����´�.
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/PersonOverview.fxml"));
			AnchorPane personOverview = loader.load();
			
			//����ó ����� ���� ���̾ƿ� ����� �����Ѵ�.
			rootLayout.setCenter(personOverview);
			
			//���� ���ø����̼��� ��Ʈ�ѷ��� �̿��� �� �ְ� �Ѵ�.
			PersonOverviewController controller = loader.getController();
			controller.setMainApp(this);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * ���� ���������� ��ȯ�Ѵ�.
	 * @return
	*/
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/**
	 * person�� �ڼ��� ������ �����ϱ� ���� ���̾�α׸� ����.
	 * ���� ����ڰ� ok�� Ŭ���ϸ� �־��� person�� ������ ������ �� true�� ��ȯ�Ѵ�.
	 * 
	 * @param person the person object to be edited
	 * @return true if the user clicked ok, false otherwise.
	 */
	//�޸��� �� ���� ��� �߰��Ҷ� 
	public boolean showPersonEditDialog(Person person) {
		try {
			//fxml ������ �ε��ϰ� ���� ���ο� ���������� �����.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			//���̾�α� ���������� �����.
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			//person �� ��Ʈ�ѷ��� �����Ѵ�.
			
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);
			
			dialogStage.showAndWait();
			
			return controller.isOKClicked();
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ����ó ���� ȯ�漳���� ��ȯ�Ѵ�.
	 * �� ������ ���������� ���� ���̰�, ȯ�漳���� OS Ư�� ������Ʈ���κ��� �д´�.
	 * ���� preference�� ã�� ���ϸ� null�� ��ȯ�Ѵ�.
 	 * @param args
	 */
	
	public File getPersonFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if(filePath != null) {
			return new File(filePath);
		}
		else {
			return null;
		}
	}
	

	/**
	 * �� �Լ��� Person Data�� XML�������� �����ϴ� �޼ҵ��Դϴ�.
	 * @param file ���� ��ü
	 * @author ȫ����
	 * @since 2018-08-01
	 */
	public void setPersonFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if(file != null) {
			prefs.put("filePath",file.getPath());
			//Stage Ÿ��Ʋ�� ������Ʈ�Ѵ�.
			primaryStage.setTitle("AddressApp-"+file.getName());
		}
		else {
			prefs.remove("filePath");
			
			//Stage Ÿ��Ʋ�� ������Ʈ�Ѵ�.
			primaryStage.setTitle("AddressApp");
		}
	}
	
	/**
	 * ������  ���Ϸκ��� ����ó �����͸� �����´�. ���� ����ó �����ͷ� ��ü�ȴ�.
	 * @param file
	 */
	
	public void loadPersonDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			//���Ϸκ��� XML�� ���� ���� �� �������Ѵ�.
			PersonListWrapper wrapper = (PersonListWrapper)um.unmarshal(file);
			
			personData.clear();
			personData.addAll(wrapper.getPersons());
			
			//���� ��θ� ������Ʈ���� �����Ѵ�.
			setPersonFilePath(file);
		}
		catch(Exception e) {	//���ܸ� ��´�.
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n"+file.getPath());
			
			alert.showAndWait();
		}
	}
	
	/**
	 * ���� ����ó �����͸� ������ ���Ͽ� �����Ѵ�.
	 * 
	 * @param file
	 */
	public void savePersonDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//����ó �����͸� ���Ѵ�.
			PersonListWrapper wrapper = new PersonListWrapper();
			wrapper.setPersons(personData);
			
			//������ �� XML�� ���Ͽ� �����Ѵ�.
			m.marshal(wrapper, file);
			
			//���� ��θ� ������Ʈ���� �����Ѵ�.
			setPersonFilePath(file);
		}
		catch(Exception e) { //���ܸ� ��´�.
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n"+file.getPath());
			
			
			alert.showAndWait();
		}
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
