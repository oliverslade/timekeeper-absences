package timekeeper.absences.services.contracts;

import java.util.List;
import java.util.Optional;
import org.joda.time.LocalDate;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceType;

public interface AbsenceService {
  List getAllAbsencesByUser(long userId);

  Optional<Absence> getAbsenceDetails(long absenceId);

  List getAbsencesForPeriod(LocalDate startOfPeriod, LocalDate endOfPeriod, long userId);

  Absence createAbsence(
      long userId, AbsenceType type, LocalDate startDate, LocalDate endDate, String description);

  Optional<Absence> approveAbsence(long absenceId, long approverId);

  Optional<Absence> updateAbsence(
      long absenceId, LocalDate startDate, LocalDate endDate, String description);
}
