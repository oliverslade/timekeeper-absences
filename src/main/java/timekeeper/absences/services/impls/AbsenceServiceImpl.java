package timekeeper.absences.services.impls;

import static java.util.Collections.EMPTY_LIST;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timekeeper.absences.exceptions.AlreadyApprovedException;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceEvent;
import timekeeper.absences.models.AbsenceType;
import timekeeper.absences.models.EventType;
import timekeeper.absences.repositories.AbsenceRepository;
import timekeeper.absences.services.contracts.AbsenceService;

@Service
public class AbsenceServiceImpl implements AbsenceService {

  private final AbsenceRepository absenceRepository;

  @Autowired
  public AbsenceServiceImpl(AbsenceRepository absenceRepository) {
    this.absenceRepository = absenceRepository;
  }

  @Override
  public List getAllAbsencesByUser(long userId) {
    return absenceRepository.getAllByUserId(userId).orElse(EMPTY_LIST);
  }

  @Override
  public Optional<Absence> getAbsenceDetails(long absenceId) {
    return absenceRepository.findById(absenceId);
  }

  @Override
  public List getAbsencesForPeriod(LocalDate startOfPeriod, LocalDate endOfPeriod, long userId) {
    return absenceRepository
        .getAbsencesByStartDateBetweenAndUserId(startOfPeriod, endOfPeriod, userId)
        .orElse(EMPTY_LIST);
  }

  @Override
  public Absence createAbsence(
      long userId, AbsenceType type, LocalDate startDate, LocalDate endDate, String description) {
    ArrayList<AbsenceEvent> createEvent = new ArrayList<>();
    createEvent.add(new AbsenceEvent((long) 0, DateTime.now(), userId, EventType.CREATE));
    Absence absenceToSave =
        new Absence((long) 0, userId, type, startDate, endDate, description, createEvent);
    return absenceRepository.save(absenceToSave);
  }

  @Override
  public Optional<Absence> approveAbsence(long absenceId, long approverId) {
    Optional<Absence> absenceToApprove = absenceRepository.findById(absenceId);
    if (absenceToApprove.isEmpty()) return absenceToApprove;
    if (absenceToApprove
        .get()
        .getAbsenceEvents()
        .stream()
        .anyMatch(o -> o.getEventType().equals(EventType.APPROVE)))
      throw new AlreadyApprovedException("Absence with id " + absenceId + " is already approved");
    absenceToApprove
        .get()
        .getAbsenceEvents()
        .add(new AbsenceEvent((long) 0, DateTime.now(), approverId, EventType.APPROVE));
    return Optional.of(absenceRepository.save(absenceToApprove.get()));
  }

  @Override
  public Optional<Absence> updateAbsence(
      long absenceId, LocalDate startDate, LocalDate endDate, String description) {
    Optional<Absence> absenceToUpdate = absenceRepository.findById(absenceId);

    if (absenceToUpdate.isEmpty()) return absenceToUpdate;
    Absence confirmedAbsence = absenceToUpdate.get();

    confirmedAbsence.setStartDate(startDate);
    confirmedAbsence.setEndDate(endDate);
    confirmedAbsence.setDescription(description);

    return Optional.of(absenceRepository.save(confirmedAbsence));
  }
}
