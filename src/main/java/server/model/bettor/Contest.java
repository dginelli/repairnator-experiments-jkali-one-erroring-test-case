package server.model.bettor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import server.model.football.Competition;
import server.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "CONTEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CAPTION")
    @NotNull
    private String caption;

    @OneToOne
    private User user;

    @Column(name = "TYPE", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ContestType type;

    @OneToOne(fetch = FetchType.LAZY)
    private Competition competition;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "CONTEST_PLAYER",
            joinColumns = {@JoinColumn(name = "CONTEST_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "PLAYER_ID", referencedColumnName = "ID")})
    @JsonIgnore
    private List<Player> players;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "CONTEST_MESSAGE",
            joinColumns = {@JoinColumn(name = "CONTEST_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "MESSAGE_ID", referencedColumnName = "ID")})
    private List<Message> messages;



}
