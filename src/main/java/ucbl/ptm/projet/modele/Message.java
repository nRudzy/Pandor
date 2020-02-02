package ucbl.ptm.projet.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "pdr_message")
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @ManyToOne
    @JoinColumn(name = "auteur")
    private User auteur;
    @Column(length = 64)
    private String sousPseudo;
    @Column(nullable = false, length = 2048)
    private String contenu;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePost;

    @OneToMany
    @JoinTable(name = "pdr_est_commentaire",
            joinColumns = @JoinColumn(name = "id_message"),
            inverseJoinColumns = @JoinColumn(name = "id_reponse"))
    private List<Message> reponses = new ArrayList<>();

    @OneToMany(mappedBy = "message")
    private List<Vote> votes = new ArrayList<>();

    public Message() {
    }

    public Message(String sousPseudo, String contenu) {
        this.sousPseudo = sousPseudo;
        this.contenu = contenu;
        datePost = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuteur() {
        return auteur;
    }

    public void setAuteur(User auteur) {
        this.auteur = auteur;
    }

    public String getSousPseudo() {
        return sousPseudo;
    }

    public void setSousPseudo(String sousPseudo) {
        this.sousPseudo = sousPseudo;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }


    public List<Message> getReponses() {
        this.reponses.sort((o1, o2) -> (o2.getNbVotes() - o1.getNbVotes()));
        return this.reponses;
    }

    public void addReponse(Message reponse) {
        this.reponses.add(reponse);
    }

    public void removeReponse(Message reponse) {
        this.reponses.remove(reponse);
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public int getNbVotes() {
        int sum = 0;
        for (Vote v : getVotes()) {
            sum += v.getQuanta();
        }
        return sum;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public void removeVote(Vote vote) {
        votes.remove(vote);
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setFin(Date datePost) {
        this.datePost = datePost;
    }
}