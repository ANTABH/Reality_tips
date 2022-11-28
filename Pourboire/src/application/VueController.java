package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.*;

public class VueController {
	
	@FXML
	TextField saisieBill;
	
	@FXML
	TextField saisieTip;
	
	@FXML
	TextField saisieNBpeople;
	
	@FXML
	Button calculate;
	
	@FXML
	TextField tipResult;
	
	@FXML
	TextField totalResult;
	
	@FXML
	TextField erreur;
	
	@FXML
	DatePicker dateCalcul;
	
	public void calcul(){
		try {
			//Son pour l'accessibilité utilisateur
			String uriString = new File("sound.mp3").toURI().toString();
			MediaPlayer player = new MediaPlayer( new Media(uriString));
			player.play();
				
			int saisieBill = this.typeChiffre(this.saisieBill.getText(), "Bill");
			int saisieTip = this.typeChiffre(this.saisieTip.getText(), "Tip %");
			int saisieNBpeople = this.typeChiffre(this.saisieNBpeople.getText(), "NB people");
			String dateCalcul = this.typeDate(this.dateCalcul.getValue(), "Date");
			
			negatif(saisieBill , "Bill");
			negatif(saisieTip , "Tip %");
			negatif(saisieNBpeople , "NB people");
			
			float tipParPersonnes = (saisieBill * saisieTip /100) / saisieNBpeople;
			float totalParPersonnes = (saisieBill / saisieNBpeople) + tipParPersonnes;
			
			this.tipResult.setText(Float.toString(tipParPersonnes));
			this.totalResult.setText(Float.toString(totalParPersonnes)); 
			
			String texte = dateCalcul + ";" + saisieBill + ";" + saisieTip + ";" + saisieNBpeople + "\n";
			FileWriter fw = new FileWriter("Calculs.txt", true);
			
			if(this.Datefichier(dateCalcul)) dateCalcul = "\n" + dateCalcul + "\n";
				
			for (int i = 0; i < texte.length(); i++) 
			{
				fw.write(texte.charAt(i));
			}
			fw.close();
					
			
			
		} catch(NumberFormatException e) {
			this.erreur.setText(e.getMessage());
		
		} catch(IndexOutOfBoundsException e) {
			this.erreur.setText(e.getMessage());
		
		} catch(Exception e) {
			this.erreur.setText(e.getMessage());
		
		} 

	}
	
	private void negatif(int saisie , String element) throws IndexOutOfBoundsException {
		if (saisie < 0) {
			throw new IndexOutOfBoundsException("La valeur " + element + " ne peut pas être négative");
		}else if(saisie == 0) {
			throw new IndexOutOfBoundsException("La valeur " + element + " ne peut pas être égale à 0");
		}
	}
	
	private int typeChiffre (String saisie, String element) throws NumberFormatException {
		try {
			
			int saisieInt = Integer.parseInt(saisie);
			return saisieInt;
			
		} catch(NumberFormatException e) {
			throw new NumberFormatException("La valeur " + element + " n'est pas numérique.");
		}
	}
	
	private String typeDate (LocalDate saisie, String element) {
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(saisie.toString());
			sdf.applyPattern("dd/MM/yyyy");
			return sdf.format(d);
			
		} catch(Exception e) {
			throw new NumberFormatException("La valeur " + element + " n'est pas une date valide.");
		}
	}
	
	private boolean Datefichier (String Date) throws IOException {
		Scanner fileScanner = new Scanner(new File("Calculs.txt"));
		String touteLignes = "";
		boolean trouve = false;
		while (fileScanner.hasNextLine()) {
			 String ligne = fileScanner.nextLine();
			 
			 if(ligne.contains(Date)){
				 trouve = true;
			 }else {
				 touteLignes += ligne ; 
			 }
		 }
		if(trouve) {
			try(FileWriter writer = new FileWriter("Calculs.txt")){
				writer.write(touteLignes);
			}
		}
		return trouve;
	}
	
	public void Remplissage() throws IOException {
		Scanner fileScanner = new Scanner(new File("Calculs.txt"));
		String Date = this.typeDate(this.dateCalcul.getValue(), "Date");	
		
		while(fileScanner.hasNextLine()) {
			String ligne = fileScanner.nextLine();
			if(ligne.contains(Date)) {
				String[]value = ligne.split(";");
				this.saisieBill.setText(value[1]);
				this.saisieTip.setText(value[2]);
				this.saisieNBpeople.setText(value[3]);
			}
		}
	
	}
}
