package likelion.festival.repository;

import likelion.festival.domain.GuestWaiting;
import likelion.festival.dto.AdminWaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GuestWaitingRepository extends JpaRepository<GuestWaiting, Long> {
    @Query("SELECT new likelion.festival.dto.AdminWaitingList(" +
            "gw.createdAt," +
            "gw.waitingNum, " +
            "gw.visitorCount, " +
            "gw.phoneNumber, " +
            "'WalkIn')" +
            "FROM GuestWaiting gw " +
            "JOIN gw.pub p " +
            "WHERE gw.pub.id = :pubId")
    List<AdminWaitingList> findGuestWaitingListByPubId(Long pubId);
}
