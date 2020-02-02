package ucbl.ptm.projet.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "pdr_topic",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"msg", "rubrique"}),
                @UniqueConstraint(columnNames = {"titre", "rubrique"})
        })
public class Topic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @OneToOne
    @JoinColumn(name = "msg")
    private Message msg;
    @ManyToOne
    @JoinColumn(name = "rubrique")
    private Rubrique rubrique;
    @Column(name = "titre", nullable = false, length = 256)
    private String titre;

    @OneToMany
    @JoinTable(name = "pdr_est_tag",
            joinColumns = @JoinColumn(name = "id_topic"),
            inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private List<Tag> tags = new ArrayList<>();

    public Topic() {
    }

    public Topic(String titre) {
        this.titre = titre;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public Rubrique getRubrique() {
        return rubrique;
    }

    public void setRubrique(Rubrique rubrique) {
        this.rubrique = rubrique;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

}
