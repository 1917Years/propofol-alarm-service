package propofol.alarmservice.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import propofol.alarmservice.domain.entity.Alarm;
import propofol.alarmservice.domain.entity.AlarmStatus;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a from Alarm a where a.toId = :memberId")
    Page<Alarm> findAllAlarmByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select a from Alarm a where a.toId = :memberId and a.status = :status")
    Page<Alarm> findAllAlarmByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") AlarmStatus status,
                                                Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Alarm a where a.id = :alarmId")
    void deleteByAlarmId(@Param("alarmId") Long alarmId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Alarm a where a.toId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
