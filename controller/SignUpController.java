package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Citizen;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class SignUpController implements Initializable {
	@FXML	private TextField sighTextAddr;
	@FXML	private TextField sighTextAddr1;
	@FXML	private PasswordField sighTextPw;
	@FXML	private PasswordField sighTextPw2;
	@FXML	private TextField sighTextName;
	@FXML	private ComboBox<String> sighCmbGender;
	@FXML	private TextField sighTextBirth;
	@FXML	private TextField sighTextEmail;
	@FXML	private TextField sighTextPhone;
	@FXML	private Button signBtnSave;
	@FXML	private Button signBtnClose;
	@FXML	private Button pwcheck;
	@FXML	private Button idcheck;
	ArrayList<Citizen> dbArrayList = new ArrayList<>();

	public Stage citizensignstage;
	ObservableList<String> signGenderList = FXCollections.observableArrayList();
	ObservableList<Citizen> citizenData = FXCollections.observableArrayList();

	public static String dongho;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		setComboBoxGender();

		setButtonTextFieldInitiate("ó��");
		// ���� ��ư Ŭ���� ���� �����Ű�� �α��� ȭ������ ��ȯ
		signBtnSave.setOnAction((e) -> {handlerBtnSaveAction();});

		// �ݱ� ��ư Ŭ���� �α��� ȭ������ ��ȯ
		signBtnClose.setOnAction((e) -> {handlerBtnCloseAction();});

		// ���̵� �ߺ�üũ
		idcheck.setOnAction((e) -> {hadlejBtnIDcheckAction();});
		// �н����� Ȯ��
		pwcheck.setOnAction((e) -> {	handlerpwcheckAction();});

		// �ڵ��� ����
		inputDecimalFormatPhone(sighTextPhone);
		// ������� ����
		inputDecimalFormatResidentt(sighTextBirth);
		// ���� �ּ� ����(��)
		inputDecimalFormatAPT(sighTextAddr);
		// �����ּ� ����(ȣ)
		inputDecimalFormatAPT(sighTextAddr1);

		// inputDecimalFormatEmail(sighTextEmail);
		// �̸�(���� �ѱ� ����)
		sighTextName.textProperty().addListener(inputDecimalFormatName);
	}

	private void setButtonTextFieldInitiate(String message) {

		switch (message) {
		case "����":
			sighTextPw.setDisable(true);
			sighTextPw2.setDisable(true);

			break;
		case "�ߺ�":
			sighTextAddr.setDisable(true);
			sighTextAddr1.setDisable(true);

			break;
		}
	}

	private void setComboBoxGender() {
		signGenderList.addAll("��", "��");
		sighCmbGender.setItems(signGenderList);

	}

	// �����ư Ŭ���� ���� ���
	private void handlerBtnSaveAction() {

		if(idcheck.isDisable() && pwcheck.isDisable()) {
			
			try {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/citizenlogin.fxml"));
				Parent root = loader.load();
				
				CitiLoginController CitiLoginController = loader.getController();
				CitiLoginController.primaryStage = primaryStage;
				
				if (sighTextAddr != null && sighTextAddr1 != null && sighTextPw != null && sighTextPw2 != null
						&& sighTextName != null && sighCmbGender != null && sighTextBirth != null && sighTextEmail != null
						&& sighTextPhone != null && pwcheck != null && idcheck != null) {
					dongho = sighTextAddr.getText() + sighTextAddr1.getText();
					Citizen citizen = new Citizen(dongho, 
							sighTextAddr.getText(), 
							sighTextAddr1.getText(),
							sighTextPw.getText(), 
							sighTextPw2.getText(), 
							sighTextName.getText(),
							sighCmbGender.getSelectionModel().getSelectedItem(), 
							sighTextBirth.getText(),
							sighTextEmail.getText(), 
							sighTextPhone.getText());
					citizenData.add(citizen);
					int count = SignUpDAO.insertAdminCostData(citizen);
					if (count != 0) {
						callAlert("�Է¼��� : ����Ÿ���̽� �Է��� �����Ǿ����ϴ�. ");
					}
					
				} else {
					Platform.exit();
					callAlert("��ĭ �߻�!!! : ��ĭ�� ��� ä���ּ���");
				}
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				citizensignstage.close();
		
				
			} catch (IOException e) {
			}
			
		
		
		
		}else {
			callAlert("���Կ���!: �ߺ�Ȯ���� �ϼ̽��ϱ�?");
		}
		
		
		

	}

	// �ݱ� ��ư Ŭ���� �α��� ȭ������ ��ȯ
	private void handlerBtnCloseAction() {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/citizenlogin.fxml"));
			Parent root = loader.load();

			CitiLoginController CitiLoginController = loader.getController();
			CitiLoginController.primaryStage = primaryStage;

			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			citizensignstage.close();
			primaryStage.show();// ���ο� â�� ����.
		} catch (IOException e1) {
		}

	}

	// �н����� Ȯ��
	private void handlerpwcheckAction() {
		if(sighTextPw.getText().isEmpty()&&sighTextPw2.getText().isEmpty()) {
			
			return;	
			}
		if (!(sighTextPw.getText().equals(sighTextPw2.getText()))) {
			callAlert("��й�ȣ�� �������� �ʽ��ϴ�.: ��й�ȣ�� �����ϰ� �־��ּ���");
			sighTextPw.clear();
			sighTextPw2.clear();
			return;
		}
		callAlert("��й�ȣ Ȯ�οϷ� : ������ �������ּ���");
		pwcheck.setDisable(true);
		setButtonTextFieldInitiate("����");

	}

	// ���̵� �ߺ�üũ
	private void hadlejBtnIDcheckAction() {
		if(sighTextAddr.getText().isEmpty()||sighTextAddr1.getText().isEmpty()) {
			callAlert("��ĭ �߻�: ������ ȣ���� ��� �Է����ּ���");
			return;	
		}
		if (id(sighTextAddr.getText() + sighTextAddr1.getText()) == true) {
			callAlert("���̵� �ߺ�: ���Ұ����� ���̵��Դϴ�. �ٽ� �Է¹ٶ��ϴ�.");
			sighTextAddr.clear();
			sighTextAddr1.clear();
			return;
		} else {
			setButtonTextFieldInitiate("�ߺ�");
			idcheck.setDisable(true);
			callAlert("���̵� ��밡��: ��밡���� ���̵��Դϴ�.");
		}
		
	

	}

	// ���̵� �ߺ�Ȯ�ο��� ���̴� �Լ�
	public boolean id(String dongho) {
		boolean result = false;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select dongho from citizen where dongho=? ";

		try {
			con = DBUtility.getConnection();
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, dongho);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = true;
			} else {
				result = false;
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// ��Ÿ �˸�â
	public static void callAlert(String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸�â");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":") + 1));
		alert.showAndWait();
	}

	// ������� ����
	private void inputDecimalFormatResidentt(TextField textField) {

		DecimalFormat format = new DecimalFormat("##");
		textField.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 9 || event.getControlNewText() == "."
					|| event.getControlNewText() == "-") {
				return null;
			} else {
				return event;
			}
		}));

	}

	// �ڵ��� ����
	private void inputDecimalFormatPhone(TextField textField) {

		DecimalFormat format = new DecimalFormat("##");
		textField.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 12 || event.getControlNewText() == "-"
					|| event.getControlNewText() == "��") {
				return null;
			} else {
				return event;
			}
		}));

	}

	// ����Ʈ �����ּ� �Է� ����
	private void inputDecimalFormatAPT(TextField textField) {

		DecimalFormat format = new DecimalFormat("###");
		textField.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);


			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 4 || event.getControlNewText() == "."
					|| event.getControlNewText() == "-") {
				return null;
			} else {
				return event;
			}
		}));

	}

	private void inputDecimalFormatEmail(TextField textField) {
		if (!(sighTextEmail.getText().contains("@"))) {
			callAlert("�̸��� �Է� ����: �̸����� ������ �߸��Ǿ����ϴ�.");
			return;
		}

	}

	// �̸� ����(�ѱ� ����,�����)
	ChangeListener<String> inputDecimalFormatName = (observable, oldValue, newValue) -> {

		if (newValue != null && !newValue.equals("")) {

			if (!newValue.matches("\\D*") || newValue.length() > 5) {

				((StringProperty) observable).setValue(oldValue);
			}
		}
	};

}