package netcracker.study.monopoly.db.repository;

import netcracker.study.monopoly.db.model.Game;
import netcracker.study.monopoly.db.model.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends CrudRepository<Game, UUID> {
    @Query("select g " +
            "from Game g " +
            "join PlayerState ps on g = ps.game " +
            "join Player p on ps.player = p " +
            "where p = ?1 " +
            "order by g.startedAt desc")
    List<Game> findByPlayer(Player player);
}
