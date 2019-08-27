package timekeeper.absences.repositories;

import java.util.List;
import java.util.Optional;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timekeeper.absences.models.Absence;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
  Optional<List<Absence>> getAllByUserId(long userId);

  Optional<List<Absence>> getAbsencesByStartDateBetweenAndUserId(
      LocalDate startOfPeriod, LocalDate endOfPeriod, Long userId);
}
