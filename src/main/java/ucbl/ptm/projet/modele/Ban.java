package ucbl.ptm.projet.modele;

import java.util.Date;
import javax.persistence.*;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "pdr_ban")
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne
    @JoinColumn(name = "banner", nullable = false)
    private User banner;

    @OneToOne
    @JoinColumn(name = "banni", nullable = false, unique = true)
    private User banni;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fin;

    @Column(nullable = false, length = 2048)
    private String motif;

    public Ban() {
    }

    public Ban(User banner, User banni, Date fin, String motif) {
        this.banner = banner;
        this.banni = banni;
        this.fin = fin;
        this.motif = motif;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getBanner() {
        return banner;
    }

    public void setBanner(User banner) {
        this.banner = banner;
    }

    public User getBanni() {
        return banni;
    }

    public void setBanni(User banni) {
        this.banni = banni;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

}
