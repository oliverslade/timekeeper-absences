package timekeeper.absences.api.docs;

import org.joda.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceType;

public interface AbsenceControllerDocs {
  @GetMapping("/get-absences-by-user")
  ResponseEntity getAllAbsencesByUser(long userId);

  @GetMapping("/get-absences-details")
  ResponseEntity getAbsencesDetails(long absenceId);

  @GetMapping("/get-absences-for-period")
  ResponseEntity getAbsencesForPeriod(LocalDate startOfPeriod, LocalDate endOfPeriod, long userId);

  @PostMapping("/create-absence")
  ResponseEntity<Absence> createAbsence(
      long userId, AbsenceType type, LocalDate startDate, LocalDate endDate, String description);

  @PutMapping("/approve-absence")
  ResponseEntity approveAbsence(long absenceId, long approverId);

  @PutMapping("/update-absence")
  ResponseEntity updateAbsence(
      long absenceId, LocalDate startDate, LocalDate endDate, String description);
}
