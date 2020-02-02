package ucbl.ptm.projet.modele;

import javax.persistence.*;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Table(name = "pdr_vote",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"voteur", "message"})
        })
public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne
    @JoinColumn(name = "voteur", nullable = false)
    private User voteur;

    @ManyToOne
    @JoinColumn(name = "message", nullable = false, unique = true)
    private Message message;

    @Column(nullable = false)
    private int quanta;

    public Vote() {

    }

    public Vote(User voteur, Message message, int quanta) {
        this.voteur = voteur;
        this.message = message;
        this.quanta = quanta;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getVoteur() {
        return voteur;
    }

    public void setVoteur(User voteur) {
        this.voteur = voteur;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getQuanta() {
        return quanta;
    }

    public void setQuanta(int quanta) {
        this.quanta = quanta;
    }
}
