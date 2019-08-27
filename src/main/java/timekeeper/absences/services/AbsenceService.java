package timekeeper.absences.services;

import static java.util.Collections.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import timekeeper.absences.repositories.AbsenceEventRepository;
import timekeeper.absences.repositories.AbsenceRepository;

public class AbsenceService {

  @Autowired AbsenceRepository absenceRepository;

  @Autowired AbsenceEventRepository eventRepository;

  public List getAllAbsencesByUser(long userId) {
    return absenceRepository.getAllByUserId(userId).orElse(EMPTY_LIST);
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
