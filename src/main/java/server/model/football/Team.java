package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Area area;

    @NotNull
    @Column(unique = true)
    private String name;

    @Column
    private String shortName;

    @Column
    private String tla;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String website;

    @Column
    private String email;

    @Column
    private String logo;

    @Column
    private Long founded;

    @Column
    private String clubColors;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "teams")
    @JsonIgnore
    private Set<Competition> competition = new HashSet<>();



}
