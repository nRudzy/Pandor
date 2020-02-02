package ucbl.ptm.projet.modele;


import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;


@Entity
@Table(name = "pdr_rubrique")
public class Rubrique implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false, unique = true, length = 64)
    private String nom;

    @Column(nullable = false, length = 2048)
    private String presentation;

    @ManyToMany(mappedBy = "abonneRubrique")
    private List<User> abonneRubrique = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "rubrique")
    private List<Topic> topics = new ArrayList<>();

    public Rubrique() {
    }

    public Rubrique(String nom, String presentation) {
        this.nom = nom;
        this.presentation = presentation;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public List<User> getAbonneRubrique() {
        return abonneRubrique;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void addTopics(Topic t) {
        if (!topics.contains(t))
            topics.add(t);
    }
    
    public void addAbo(User u) {
        if (!abonneRubrique.contains(u))
            abonneRubrique.add(u);
    }
    
    public void removeAbo(User u) {
        if (abonneRubrique.contains(u))
            abonneRubrique.remove(u);
    }
}



