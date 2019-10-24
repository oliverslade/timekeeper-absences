package timekeeper.absences.api.controllers;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.absences.api.docs.AbsenceControllerDocs;
import timekeeper.absences.exceptions.AlreadyApprovedException;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceType;
import timekeeper.absences.services.contracts.AbsenceService;

@RestController
public class AbsenceController implements AbsenceControllerDocs {

  private final AbsenceService absenceService;

  @Autowired
  public AbsenceController(AbsenceService absenceService) {
    this.absenceService = absenceService;
  }

  @Override
  public ResponseEntity getAllAbsencesByUser(long userId) {
    try {
      return new ResponseEntity<>(absenceService.getAllAbsencesByUser(userId), OK);
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity getAbsencesDetails(long absenceId) {
    try {
      return absenceService
          .getAbsenceDetails(absenceId)
          .map(absence -> new ResponseEntity<>(absence, OK))
          .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    } catch (RuntimeException e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity getAbsencesForPeriod(
      LocalDate startOfPeriod, LocalDate endOfPeriod, long userId) {
    try {
      return new ResponseEntity<>(
          absenceService.getAbsencesForPeriod(startOfPeriod, endOfPeriod, userId), OK);
    } catch (RuntimeException e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity<Absence> createAbsence(
      long userId, AbsenceType type, LocalDate startDate, LocalDate endDate, String description) {
    try {
      List existingAbsence = absenceService.getAbsencesForPeriod(startDate, endDate, userId);
      if (!existingAbsence.isEmpty()) return new ResponseEntity<>(CONFLICT);
      return new ResponseEntity<>(
          absenceService.createAbsence(userId, type, startDate, endDate, description), OK);
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity approveAbsence(long absenceId, long approverId) {
    try {
      return absenceService
          .approveAbsence(absenceId, approverId)
          .map(absence -> new ResponseEntity<>(absence, OK))
          .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    } catch (AlreadyApprovedException e) {
      throw new ResponseStatusException(CONFLICT, e.getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @Override
  public ResponseEntity updateAbsence(
      long absenceId, LocalDate startDate, LocalDate endDate, String description) {
    try {
      return absenceService
          .updateAbsence(absenceId, startDate, endDate, description)
          .map(absence -> new ResponseEntity<>(absence, OK))
          .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }
}
