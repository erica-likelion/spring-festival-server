package likelion.festival.repository;

import likelion.festival.domain.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByLostDate(String lostDate);

    List<LostItem> findByLostDateAndNameContaining(String lostDate, String name);
}
