package likelion.festival.repository;

import likelion.festival.domain.Waiting;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.MyWaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    void deleteByWaitingNum(Integer waitingNum);

    @Query("SELECT new likelion.festival.dto.MyWaitingList(" +
            "p.enterNum - p.maxWaitingNum, " +
            "p.enterNum - w.waitingNum, " +
            "p.id, " +
            "w.visitorCount) " +
            "FROM Waiting w " +
            "JOIN w.pub p " +
            "WHERE w.user.id = :userId")
    List<MyWaitingList> findWaitingListByUserId(Long userId);

    @Query("SELECT new likelion.festival.dto.AdminWaitingList(" +
            "w.createdAt," +
            "w.waitingNum, " +
            "w.visitorCount, " +
            "w.phoneNumber) " +
            "FROM Waiting w " +
            "JOIN w.pub p " +
            "WHERE w.pub.id = :pubId")
    List<AdminWaitingList> findWaitingsByPubId(Long pubId);
}
