package ucbl.ptm.projet.modele;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pdr_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true, nullable = false, length = 256)
    private String email;
    @Column(nullable = false, length = 64)
    private String nom;
    @Column(nullable = false, length = 64)
    private String prenom;
    @Column(unique = true, nullable = false, length = 64)
    private String pseudo;
    @Column(nullable = false, length = 256)
    private String hashMdp;
    @Column(unique = true, nullable = false, length = 8)
    private String numEtu;
    @Column(nullable = false, length = 64)
    private String titre;
    @Column(nullable = false)
    private boolean isAdmin;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pdr_est_modo",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_rubrique"))
    private List<Rubrique> moderatedRubrique = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pdr_est_abonne",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_rubrique"))
    private List<Rubrique> abonneRubrique = new ArrayList<>();

    public User() {
    }

    public User(String email, String nom, String prenom, String pseudo, String hashMdp, String numEtu, String titre, boolean isAdmin) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.hashMdp = hashMdp;
        this.numEtu = numEtu;
        this.titre = titre;
        this.isAdmin = isAdmin;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getHashMdp() {
        return hashMdp;
    }

    public void setHashMdp(String hashMdp) {
        this.hashMdp = hashMdp;
    }

    public String getNumEtu() {
        return numEtu;
    }

    public void setNumEtu(String numEtu) {
        this.numEtu = numEtu;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<Rubrique> getAllModeratedRubrique() {
        return moderatedRubrique;
    }

    public void addModeratedRubrique(Rubrique rubrique) {
        moderatedRubrique.add(rubrique);
    }

    public List<Rubrique> getAbonneRubrique() {
        return abonneRubrique;
    }

    public void setAbonneRubrique(List<Rubrique> abonneRubrique) {
        this.abonneRubrique = abonneRubrique;
    }

    public void addAbonneRubrique(Rubrique rubrique) {
        if (!abonneRubrique.contains(rubrique)) {
            abonneRubrique.add(rubrique);
        }
    }

    public void removeAbonneRubrique(Rubrique rubrique) {
        abonneRubrique.remove(rubrique);
    }

    public void removeModeratedRubrique(Rubrique rubrique) {
        moderatedRubrique.remove(rubrique);
    }

}
