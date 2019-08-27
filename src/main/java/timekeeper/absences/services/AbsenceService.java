package timekeeper.absences.services;

import static java.util.Collections.EMPTY_LIST;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceEvent;
import timekeeper.absences.models.EventType;
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

  public List getAbsencesForPeriod(LocalDate startOfPeriod, LocalDate endOfPeriod, long userId) {
    return absenceRepository
        .getAbsencesByStartDateBetweenAndUserId(startOfPeriod, endOfPeriod, userId)
        .orElse(EMPTY_LIST);
  }

  public Absence createAbsence(
      long userId, LocalDate startDate, LocalDate endDate, String description) {
    ArrayList<AbsenceEvent> createEvent = new ArrayList<>();
    createEvent.add(new AbsenceEvent((long) 0, DateTime.now(), userId, EventType.CREATE));
    Absence absenceToSave = new Absence((long) 0, userId, startDate, endDate, description, createEvent);
    return absenceRepository.save(absenceToSave);
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
