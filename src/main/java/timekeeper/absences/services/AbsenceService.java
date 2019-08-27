package timekeeper.absences.services;

import static java.util.Collections.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import timekeeper.absences.models.Absence;
import timekeeper.absences.repositories.AbsenceEventRepository;
import timekeeper.absences.repositories.AbsenceRepository;

public class AbsenceService {

  @Autowired AbsenceRepository absenceRepository;

  @Autowired AbsenceEventRepository eventRepository;

  public List getAllAbsencesByUser(long userId) {
    return absenceRepository.getAllByUserId(userId).orElse(EMPTY_LIST);
  }

  public Optional<Absence> getAbsenceDetails(long absenceId) {
    return absenceRepository.findById(absenceId);
  }

  public List getAbsencesForPeriod(Date startOfPeriod, Date endOfPeriod, long userId) {
    return absenceRepository
        .getAbsencesByStartDateBetweenAndUserId(startOfPeriod, endOfPeriod, userId)
        .orElse(EMPTY_LIST);
  }

  /**
   * This setter method should be used only by unit tests.
   *
   * @param absenceRepository
   */
  public void setAbsenceRepository(AbsenceRepository absenceRepository) {
    this.absenceRepository = absenceRepository;
  }
}
