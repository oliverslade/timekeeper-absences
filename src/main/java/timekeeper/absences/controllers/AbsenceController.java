package timekeeper.absences.controllers;

import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.absences.services.contracts.AbsenceService;

@RestController
public class AbsenceController {

  @Autowired AbsenceService absenceService;

  @GetMapping("/get-absences-by-user")
  public ResponseEntity getAllAbsencesByUser(long userId) {
    try {
      return new ResponseEntity<>(absenceService.getAllAbsencesByUser(userId), OK);
    } catch (Exception e) {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }

  @GetMapping("/get-absences-details")
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

  /**
   * This setter method should be used only by unit tests.
   *
   * @param absenceService
   */
  public void setService(AbsenceService absenceService) {
    this.absenceService = absenceService;
  }
}
