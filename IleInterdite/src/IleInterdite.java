import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


interface Observer {
    public void update();
}

abstract class Observable {
    private ArrayList<Observer> observers;
    public Observable() {
	this.observers = new ArrayList<Observer>();
    }
    public void addObserver(Observer o) {
	observers.add(o);
    }
    public void notifyObservers() {
	for(Observer o : observers) {
	    o.update();
	}
    }
}


public class IleInterdite {
	public static void main(String[] args) {
	EventQueue.invokeLater(() -> {
	                CModele modele = new CModele();
	                CVue vue = new CVue(modele);
	 	});
	}
}

class CModele extends Observable {
    public static final int HAUTEUR=5, LARGEUR=6;
    private Zone[][] zones;
    public int nbinondees;
    private ArrayList<Joueur> joueurs;
    private ArrayList<Artefact> artefacts;
    private ArrayList<CleArtefact> cles;
    
    public CModele() {
	zones = new Zone[LARGEUR][HAUTEUR];
	for(int i=0; i<LARGEUR; i++) {
	    for(int j=0; j<HAUTEUR; j++) {
		zones[i][j] = new Zone(this,i, j);
	    }
	}
	
	
	
	init();
    }	
    
    public void init() {
    	this.nbinondees = 0;
    	Random rand = new Random();
    	for(int i=0; i<5; i++) {
	    	
	    	int x,y;
	    	do {
	    		x = rand.nextInt(LARGEUR);
	    		y = rand.nextInt(HAUTEUR);
	    	}
	    	while (getZone(x,y).getArtefact()!=Artefact.aucun);
	    	switch(i) {
	    	case 0 : getZone(x,y).addArtefact(Artefact.feu); System.out.println("feu : ("+x+","+y+")") ;break;
	    	case 1 : getZone(x,y).addArtefact(Artefact.air); System.out.println("air : ("+x+","+y+")"); break;
	    	case 2 : getZone(x,y).addArtefact(Artefact.eau); System.out.println("eau : ("+x+","+y+")"); break;
	    	case 3 : getZone(x,y).addArtefact(Artefact.terre); System.out.println("terre : ("+x+","+y+")"); break;
	    	case 4 : getZone(x,y).addArtefact(Artefact.heliport); System.out.println("heliport : ("+x+","+y+")"); break;
	    	}
	}
    }
    
    
    public void inondation() {
    	Random rand = new Random();
    	for(int i=0; i<3; i++) {
    	if (nbinondees<HAUTEUR*LARGEUR) {
    	int x,y;
    	do {
    		x = rand.nextInt(LARGEUR);
    		y = rand.nextInt(HAUTEUR);
    	}
    	while (getZone(x,y).getEtat()==EtatZone.submergee);	// Verifie que l'etat n'est pas submergee
    	getZone(x,y).assecher();
    	System.out.println("zone : ("+x+","+y+") assechee");
    	if(getZone(x,y).getEtat()==EtatZone.submergee) {
    		nbinondees++;
    	}
    	}
    	}
    }

    protected int compteVoisines(int x, int y) {
	int res=0;
	
	for(int i=x-1; i<=x+1; i++) {
	    for(int j=y-1; j<=y+1; j++) {
		if (zones[i][j].etat!=EtatZone.submergee) { res++; }
	    }
	}
	if (zones[x][y].etat!=EtatZone.submergee) { return res; }
	else { return res - 1;}
    }

    public Zone getZone(int x, int y) {
	return zones[x][y];
    }
}


class Joueur {
	private CModele modele;
	private ArrayList<Artefact> artefactsJ;
	private ArrayList<CleArtefact> clesJ;
	private String nom;
	private Color couleur;
	private Zone position;
	
	public Joueur(CModele modele, Color c, String name, Zone z) {
		this.modele = modele;
		this.artefactsJ = new ArrayList<Artefact>();
		this.clesJ = new ArrayList<CleArtefact>();
		this.nom = name;
		this.couleur = c;
		this.position = z;
	}
	
	public void chercheCle() {
		
	}
}



class Zone {
    private CModele modele;

    protected EtatZone etat;
    
    protected Artefact artefact;

    private final int x, y;
    
    private boolean occupee;
    
    public Zone(CModele modele, int x, int y) {
        this.modele = modele;
        this.etat = EtatZone.normale;
        this.x = x; this.y = y;
        this.artefact = Artefact.aucun;
        this.occupee = false;
    }	
    
    public int getX() {
    	return this.x;
    }
    public int getY() {
    	return this.y;
    }
    public EtatZone getEtat() {
    	return etat;
    }
    public void changeEtat(EtatZone e) {
    	this.etat = e;
    }
    public Artefact getArtefact() {
    	return this.artefact;
    }
    public void addArtefact(Artefact a) {
    	this.artefact = a;
    }
    public void removeArtefact() {
    	this.artefact = Artefact.aucun;
    }
    public void assecher() {
    	if (this.etat!=EtatZone.normale) {
    		this.changeEtat(EtatZone.submergee);
    	} else { this.changeEtat(EtatZone.inondee);}
    }
    public boolean estTraversable() {
        return (etat!=EtatZone.submergee);
    }
    public void occuper() {
    	this.occupee = true;
    }
    public void liberer() {
    	this.occupee = false;
    }
}

class CVue {
    private JFrame frame;
    private VueGrille grille;
    private VueCommandes commandes;

    public CVue(CModele modele) {
	frame = new JFrame();
	frame.setTitle("IleInterdite de Leacock");
	frame.setLayout(new FlowLayout());

	grille = new VueGrille(modele);
	frame.add(grille);
	commandes = new VueCommandes(modele);
	frame.add(commandes);
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
}

class VueGrille extends JPanel implements Observer {
    private CModele modele;
    private final static int TAILLE = 30;

    public VueGrille(CModele modele) {
	this.modele = modele;
	modele.addObserver(this);
	Dimension dim = new Dimension(TAILLE*CModele.LARGEUR*3,
				      TAILLE*CModele.HAUTEUR*3);
	this.setPreferredSize(dim);
    }

    public void update() { repaint(); }

    public void paintComponent(Graphics g) {
	super.repaint();
	for(int i=0; i<CModele.LARGEUR; i++) {
	    for(int j=0; j<CModele.HAUTEUR; j++) {
		paint(g, modele.getZone(i, j), i*TAILLE*3, j*TAILLE*3);
		paint2(g, modele.getZone(i, j), i*TAILLE*3 + TAILLE, j*TAILLE*3 + TAILLE);
		}
	    }
	
	}
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où le type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] était déclarée privée dans [CModele].
     */
    private void paint(Graphics g, Zone zone, int x, int y) {
	if (zone.etat==EtatZone.normale) {
	    g.setColor(Color.WHITE);
	}
	else if (zone.etat==EtatZone.inondee) {
        g.setColor(Color.GRAY);
        }
	else { g.setColor(Color.BLACK); }
    	g.fillRect(x, y, TAILLE*3, TAILLE*3);
}
    
    private void paint2(Graphics g, Zone zone, int x, int y) {
    	if(zone.getEtat()!=EtatZone.submergee) {
    	switch(zone.getArtefact()) {
    	case aucun : break;
    	case eau : g.setColor(Color.BLUE); g.fillRect(x, y, TAILLE, TAILLE); break;
    	case feu : g.setColor(Color.RED); g.fillRect(x, y, TAILLE, TAILLE); break;
    	case terre : g.setColor(Color.ORANGE); g.fillRect(x, y, TAILLE, TAILLE); break;
    	case air : g.setColor(Color.CYAN); g.fillRect(x, y, TAILLE, TAILLE); break;
    	case heliport : g.setColor(Color.GREEN); g.fillRect(x, y, TAILLE, TAILLE); break;
        }
    }
}
}

class VueCommandes extends JPanel {
    private CModele modele;

    public VueCommandes(CModele modele) {
	this.modele = modele;
	JButton boutonHaut = new JButton("^");
	this.add(boutonHaut);
	Controleur ctrlhaut = new Controleur(modele);
	boutonHaut.addActionListener(ctrlhaut);
	JButton boutonGauche = new JButton("<");
	this.add(boutonGauche);
	Controleur ctrlgauche = new Controleur(modele);
	boutonGauche.addActionListener(ctrlgauche);
	JButton boutonDroite = new JButton(">");
	this.add(boutonDroite);
	Controleur ctrldroite = new Controleur(modele);
	boutonDroite.addActionListener(ctrldroite);
	JButton boutonBas = new JButton("v");
	this.add(boutonBas);
	Controleur ctrlbas = new Controleur(modele);
	boutonBas.addActionListener(ctrlbas);
	JButton finDeTour = new JButton("Fin");
	this.add(finDeTour);
	Controleur ctrltour = new Controleur(modele);
	finDeTour.addActionListener(ctrltour);
    }
}

class Controleur implements ActionListener {
    CModele modele;
    public Controleur(CModele modele) { this.modele = modele; }
    
    public void actionPerformed(ActionEvent e) {
    	modele.inondation();
    }
}