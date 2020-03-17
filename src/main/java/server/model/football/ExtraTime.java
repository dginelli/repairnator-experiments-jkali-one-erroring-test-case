package server.model.football;


import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class ExtraTime {
    private Long homeTeam;
    private Long awayTeam;
}
