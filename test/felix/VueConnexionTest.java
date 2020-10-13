package felix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JTextPaneOperator;

import junit.framework.TestCase;

public class VueConnexionTest extends TestCase{

	/**
	 * Vue Caisse nécessaire aux tests.
	 */
	private FenetreConnexion vueConnexion;
	
	/**
	 * Mock d'un contrôleur nécessaire aux tests.
	 */
	//private JButton boutonMock;

	/**
	 * La fenêtre de la vue.
	 */
	private JFrameOperator fenetre;
	
	/**
	 * Le bouton de la vue.
	 */
	private JButtonOperator boutonConnexion;

	/**
	 * Les champs texte de la vue.
	 */
	private JTextPaneOperator texteInfo;
	
	/**
	 * Les champs ip et port
	 */
	private JTextFieldOperator portField, ipField;

	/**
	 * Fixe les propriétés de Jemmy pour les tests.
	 * Crée et affiche la vue nécessaire aux tests.
	 *
	 * <p>Code exécuté avant les tests.</p>
	 *
	 * @throws Exception toute exception.
	 * 
	 * @see org.netbeans.jemmy.JemmyProperties
	 *
	 */
	@Before
	public void setUp() throws Exception 
	{
		// Fixe les timeouts de Jemmy (http://jemmy.java.net/OperatorsEnvironment.html#timeouts),
		// ici : 3s pour l'affichage d'une frame.
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
		
		// Création d'un mock de contrôleur.
		//this.boutonMock = EasyMock.createMock(ControleurVente.class);
		//assertNotNull(this.boutonMock);
		
		// Création d'un mock de stock.
		//this.stockMock = EasyMock.createMock(Stock.class);
		//assertNotNull(this.stockMock);
		
		// Création de la vue nécessaire aux tests.
		// La vue s'appuie sur le mock de contrôleur.
		this.vueConnexion = new FenetreConnexion(
				Felix.CONFIGURATION.getString("ADRESSE_CHAT"),
				Felix.CONFIGURATION.getString("PORT_CHAT"));
		assertNotNull(this.vueConnexion);
		
		// Affichage de la vue et récupération de cette vue.
		this.vueConnexion.affiche();
		this.recuperationVue();
	}

	/**
	 * Fermeture de la vue caisse.
	 *
	 * <p>Code exécuté après les tests.</p>
	 *
	 * @throws Exception toute exception.
	 *
	 */
	@After
	public void tearDown() throws Exception 
	{
		// Pour avoir le temps d'observer les manipulations sur la vue (objectif pédagogique),
		// ici : 2 secondes.
		final Long timeout = Long.valueOf(2000); 
		Thread.sleep(timeout);
		
		if (this.vueConnexion != null) {
			this.vueConnexion.ferme();
		}
	}

	/**
	 * Récupération de la vue caisse.
	 * 
	 * <p>Cette méthode concerne la récupération de la fenêtre, avec titre adéquat, 
	 * et des widgets attendus pour cette vue.</p>
	 */
	private void recuperationVue()
	{
		
		// Récupération de la fenêtre de la vue de la caisse.
		this.fenetre = new JFrameOperator("Connexion a Camix");
		assertNotNull("La fenetre de la vue connexion n'est pas accessible.", this.fenetre);
	
		// Récupération du champs de saisie d'un identifiant produit.
		this.texteInfo = new JTextPaneOperator(this.fenetre, 0);
		assertNotNull("Le champ d'affichage des informations n'est pas accessible.", this.texteInfo);
		
		// Récupération du champ de saisie de la quantité de produit.
		this.ipField = new JTextFieldOperator(this.fenetre, 0);
		assertNotNull("Le champ de saisie de l'adresse IP n'est pas accessible.", this.ipField);
		
		// Récupération du champ de libellé d'un produit.
		this.portField = new JTextFieldOperator(this.fenetre, 1);
		assertNotNull("Le champ de saisie du port n'est pas accessible.", this.portField);
		
		// Récupération du bouton d'ajout d'un produit à la vente.
		this.boutonConnexion = new JButtonOperator(this.fenetre, 0);
		assertNotNull("Le bouton de connexion n'est pas accessible.", this.boutonConnexion);
	}
	
	/**
	 * Test l'initialisation des différents champs de la vue.
	 */
	@Test
	public void testInitialiseVue()
	{
		/*
		 * Données de test.
		 */
		final String infoAttendue = "Saisir l'adresse et le port du serveur chat.";
		final String ipAttendue = Felix.CONFIGURATION.getString("ADRESSE_CHAT");
		final String portAttendu = Felix.CONFIGURATION.getString("PORT_CHAT");
		/*
		 * Exécution du test.
		 */
		try {
			// Récupération des valeurs des champs de la vue.
			final String ipActuel = this.ipField.getText();
			final String portActuel = this.portField.getText();
			final String infoActuelle = this.texteInfo.getText();
			
			// Récupération les libellés des boutons de la vue.
			final String boutonActuel = this.boutonConnexion.getText();

			// Assertions.
			assertEquals("IP produite par defaut invalide.", ipAttendue, ipActuel);
			assertEquals("Port produit par defaut invalide.", portAttendu, portActuel);
			assertEquals("Information produite par defaut invalide.", infoAttendue, infoActuelle);
			assertEquals("Valeur du bouton de connexion invalide.", "Connexion au chat", boutonActuel);
		}
		catch (Exception e) {
			fail("Manipulation de la vue connexion invalide." + e.getMessage());
		}
	}
	
	
	/**
	 * Test de la reaction suite a une tentative de connexion avec une ip incorrecte.
	 */
	@Test
	public void testConnexion_ipNonValide()
	{
		/*
		 * Données de test.
		 */
		final String ipErronee = "256";
		final String infoAttendue = "Connexion au chat @256:12345 impossible.";
		
		/*
		 * Exécution du test.
		 */
		try {
			// Saisie de l'ip du chat.
			this.ipField.clearText();
			this.ipField.typeText(ipErronee);
			
			// On declenche l'action de connexion
			this.boutonConnexion.clickMouse();
			
			// Récupération de la valeur du champ d'information.
			final String infoActuelle = this.texteInfo.getText();

			// Assertions.
			assertEquals("Information pour une ip incorrecte invalide.", infoAttendue, infoActuelle);
		}
		catch (Exception e) {
			fail("Manipulation de la vue connexion invalide." + e.getMessage());
		}
	}
	
	/**
	 * Test de la reaction suite a une tentative de connexion avec un port incorrect.
	 */
	@Test
	public void testConnexion_portNonValide()
	{
		/*
		 * Données de test.
		 */
		final String portErrone = "abc";
		final String infoAttendue = "Connexion au chat @127.0.0.1:abc impossible.";
		
		/*
		 * Exécution du test.
		 */
		try {
			// Saisie du port du chat.
			this.portField.clearText();
			this.portField.typeText(portErrone);
			
			// On declenche l'action de connexion
			this.boutonConnexion.clickMouse();
			
			// Récupération de la valeur du champ d'information.
			final String infoActuelle = this.texteInfo.getText();

			// Assertions.
			assertEquals("Information pour un port incorrect invalide.", infoAttendue, infoActuelle);
		}
		catch (Exception e) {
			fail("Manipulation de la vue connexion invalide." + e.getMessage());
		}
	}
	
	/**
	 * Test de la connexion avec une ip et un port valides.
	 */
	@Test
	public void testFocusLost_produitValide()
	{
		/*
		 * Données de test.
		 */
		final String infoAttendue = "Connexion au chat @" + ipField.getText() + ":" + portField.getText();
		
		/*
		 * Exécution du test.
		 */
		try {
			// On declenche l'action de connexion
			this.boutonConnexion.clickMouse();
			
			// Récupération de la valeur du champ d'information.
			final String infoActuelle = this.texteInfo.getText();

			// Assertions.
			// Devrait utiliser un mock ici normalement, pour ne pas dependre du lancement de Camix
			assertEquals("Message de connexion invalide. - Verifiez que Camix est bien lance", infoAttendue, infoActuelle);
		}
		catch (Exception e) {
			fail("Manipulation de la vue caisse invalide." + e.getMessage());
		}
	}

}
