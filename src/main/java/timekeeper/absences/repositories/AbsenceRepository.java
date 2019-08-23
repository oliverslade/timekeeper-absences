package timekeeper.absences.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timekeeper.absences.models.AbsenceEvent;

@Repository
public interface AbsenceRepository extends JpaRepository<AbsenceEvent, Long> {}
