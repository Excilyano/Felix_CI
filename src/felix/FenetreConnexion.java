package felix;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * Classe de la fenetre de connexion de Felix
 * L'utilisateur peut saisir une ip et un port sur cette fenetre
 * Au clic sur le bouton de connexion, si ces deux champs sont correctement renseignes, la fenetre de chat s'ouvre
 * Dans le cas contraire, un message d'information le signale a l'utilisateur.
 * @author Etienne
 *
 */
public class FenetreConnexion extends JFrame {

	/**
	 * @author Etienne
	 * Cette classe permet d'afficher les paremetres de connexion au chat.
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	
	private JLabel ipLabel;
	private JLabel portLabel;
	
	private JTextPane information;
	
	private JTextField ipText;
	private JTextField portText;
	
	private JButton connexionButton;
	
	/**
	 * Constructeur de la fenetre de connexion
	 * @param ipInitiale ip par defaut de la fenetre
	 * @param portInitial port par defaut de la fenetre
	 */
	public FenetreConnexion(String ipInitiale, String portInitial){
		// Construction de la fenêtre.
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350, 150);

		setTitle("Connexion a Camix");
		setResizable(true);

		// Construction du contenu de la fenêtre.
		this.construireFenetre(ipInitiale, portInitial);
	}

	/**
	 * ajout des differents elements sur la fenetre de connexion
	 * @param ipInitiale ip par defaut de la fenetre
	 * @param portInitial port par defaut de la fenetre
	 */
	private void construireFenetre(String ipInitiale, String portInitial) {
		panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		
		initializeFields(ipInitiale, portInitial);
		
		panel.add(generateInfo());
		panel.add(generateIpInput());
		panel.add(generatePortInput());
		panel.add(generateButton());
		
		add(panel);
	}

	/**
	 * Methode de generation du ruban de connexion
	 * @return le bouton de connexion
	 */
	private Component generateButton() {
		return connexionButton;
	}

	/**
	 * Methode de generation de la saisie de l'ip
	 * @return le panneau de saisie de l'ip
	 */
	private Component generateIpInput() {
		JPanel ip = new JPanel();
		ip.setLayout(new FlowLayout());
		
		ip.add(ipLabel);
		ip.add(ipText);
		return ip;
	}
	
	/**
	 * Methode de generation de la saisie du port
	 * @return le panneau de saisie du port
	 */
	private Component generatePortInput() {
		JPanel port = new JPanel();
		port.setLayout(new FlowLayout());
		
		port.add(portLabel);
		port.add(portText);
		return port;
	}

	/**
	 * Methode de generation du ruban d'information
	 * @return le message d'information de la fenetre
	 */
	private Component generateInfo() {
		return information;
	}

	/**
	 * Methode d'initialisation des champs de le fenetre de connexion
	 * @param ipInitiale l'ip par defaut de la fenetre
	 * @param portInitial le port par defaut de la fenetre
	 */
	private void initializeFields(String ipInitiale, String portInitial) {
		ipLabel = new JLabel("IP");
		portLabel = new JLabel("Port");
		
		information = new JTextPane();
		information.setText("Saisir l'adresse et le port du serveur chat.");
		information.setEditable(false);
		information.setBackground(new Color(160, 160, 160));
		 /**
		  * On peut (et il faudrait) creer des class heritant de Document, permettant d'imposer un formattage aux textes
		  */
		ipText = new JTextField(ipInitiale);
		ipText.setColumns(15);
		portText = new JTextField(portInitial);
		portText.setColumns(5);
		
		connexionButton = new JButton("Connexion au chat");
		connexionButton.addActionListener(new ActionListener() {
			
			/**
			 * ActionListener implemente en classe interne
			 * L'appui sur le bouton de connexion lance les commandes suivantes
			 * Si l'ip et le port entres sont corrects, on se connecte au serveur de chat
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				information.setText("Connexion au chat @" + ipText.getText() + ":" + portText.getText());
				
				try {
					final Connexion connexion = new Connexion(
						ipText.getText(),
						Integer.parseInt(portText.getText())
					);

					final Fenetre fenetre = new Fenetre(
						Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_LARGEUR")),
						Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_HAUTEUR")),
						Felix.CONFIGURATION.getString("FENETRE_TITRE"),
						connexion
					);

					fenetre.setVisible(true);
					ferme();
				} 
				catch (IOException ex) {
					System.err.println(ex.getMessage());
					information.setText("Connexion au chat @" + ipText.getText() + ":" + portText.getText() + " impossible.");
				}
				catch(NumberFormatException nfe){
					System.err.println(nfe.getMessage());
					information.setText("Connexion au chat @" + ipText.getText() + ":" + portText.getText() + " impossible.");
				}
			}
		});
	}

	/**
	 * methode d'affichage de le fenetre
	 */
	public void affiche() {
		this.setVisible(true);
	}

	/**
	 * methode de fermeture de la fenetre
	 */
	public void ferme() {
		this.dispose();
	}
}
