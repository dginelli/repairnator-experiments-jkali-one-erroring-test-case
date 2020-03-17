package netcracker.study.monopoly.db.repository;

import netcracker.study.monopoly.db.model.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends CrudRepository<Player, UUID> {
    Optional<Player> findByNickname(String nickname);
}
