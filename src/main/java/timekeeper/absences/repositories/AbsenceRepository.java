package timekeeper.absences.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timekeeper.absences.models.Absence;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
  public Optional<List<Absence>> getAllByUserId(long userId);
}
