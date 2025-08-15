package likelion.festival.lostitem.repository;

import likelion.festival.lostitem.domain.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByFoundDate(String lostDate);

    List<LostItem> findByNameContaining(String name);

    List<LostItem> findByFoundDateAndNameContaining(String lostDate, String name);
}
