package likelion.festival.repository;

import likelion.festival.domain.Waiting;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.MyWaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT w FROM Waiting w WHERE w.user.id = :userId")
    List<Waiting> findWaitingListByUserId(Long userId);

    @Query("SELECT new likelion.festival.dto.AdminWaitingList(" +
            "w. id," +
            "w.createdAt," +
            "w.waitingNum, " +
            "w.visitorCount, " +
            "w.phoneNumber, " +
            "'Online')" +
            "FROM Waiting w " +
            "JOIN w.pub p " +
            "WHERE w.pub.id = :pubId")
    List<AdminWaitingList> findWaitingsByPubId(Long pubId);
}
