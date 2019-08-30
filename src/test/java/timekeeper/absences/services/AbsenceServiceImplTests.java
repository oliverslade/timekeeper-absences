package timekeeper.absences.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static timekeeper.absences.utils.TestUtils.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import timekeeper.absences.exceptions.AlreadyApprovedException;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceEvent;
import timekeeper.absences.models.EventType;
import timekeeper.absences.repositories.AbsenceRepository;
import timekeeper.absences.services.impls.AbsenceServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbsenceServiceImplTests {

  private AbsenceServiceImpl absenceServiceImpl;

  private AbsenceRepository mockAbsenceRepository;

  @Before
  public void setUp() {
    absenceServiceImpl = new AbsenceServiceImpl();

    mockAbsenceRepository = mock(AbsenceRepository.class);
    absenceServiceImpl.setAbsenceRepository(mockAbsenceRepository);
  }

  @Test
  public void getAllAbsencesByUser_successful() {
    long userId = 1234;
    List<Absence> expectedAbsenceList = getDefaultAbsences();
    when(mockAbsenceRepository.getAllByUserId(userId)).thenReturn(Optional.of(expectedAbsenceList));

    List actualAbsenceList = absenceServiceImpl.getAllAbsencesByUser(userId);
    assertEquals(expectedAbsenceList, actualAbsenceList);
  }

  @Test
  public void getAllAbsencesByUser_empty() {
    long userId = 1234;
    List expectedAbsenceList = Collections.EMPTY_LIST;
    when(mockAbsenceRepository.getAllByUserId(userId)).thenReturn(Optional.empty());

    List actualAbsenceList = absenceServiceImpl.getAllAbsencesByUser(userId);
    assertEquals(expectedAbsenceList, actualAbsenceList);
  }

  @Test
  public void getAbsenceDetails_successful() {
    long absenceId = 1234;
    Absence expectedAbsence = getDefaultAbsence(absenceId, DateTime.now());
    when(mockAbsenceRepository.findById(absenceId)).thenReturn(Optional.of(expectedAbsence));

    Optional<Absence> actualAbsence = absenceServiceImpl.getAbsenceDetails(absenceId);

    assertEquals(expectedAbsence, actualAbsence.get());
  }

  @Test
  public void getAbsenceDetails_notFound() {
    long absenceId = 1234;
    when(mockAbsenceRepository.findById(absenceId)).thenReturn(Optional.empty());

    Optional<Absence> actualAbsence = absenceServiceImpl.getAbsenceDetails(absenceId);

    assertEquals(Optional.empty(), actualAbsence);
  }

  @Test
  public void getAbsencesForPeriod_successful() {
    long userId = 1234;
    LocalDate startOfPeriod = new LocalDate(1564617600);
    List<Absence> expectedAbsenceList = getDefaultAbsences();
    when(mockAbsenceRepository.getAbsencesByStartDateBetweenAndUserId(
            startOfPeriod, startOfPeriod, userId))
        .thenReturn(Optional.of(expectedAbsenceList));

    List actualAbsenceList =
        absenceServiceImpl.getAbsencesForPeriod(startOfPeriod, startOfPeriod, userId);
    assertEquals(expectedAbsenceList, actualAbsenceList);
  }

  @Test
  public void getAbsencesForPeriod_empty() {
    long userId = 1234;
    LocalDate startOfPeriod = new LocalDate(1564617600);
    List expectedAbsenceList = Collections.EMPTY_LIST;
    when(mockAbsenceRepository.getAbsencesByStartDateBetweenAndUserId(
            startOfPeriod, startOfPeriod, userId))
        .thenReturn(Optional.empty());

    List actualAbsenceList =
        absenceServiceImpl.getAbsencesForPeriod(startOfPeriod, startOfPeriod, userId);
    assertEquals(expectedAbsenceList, actualAbsenceList);
  }

  @Test
  public void createAbsence_successful() {
    Absence expectedAbsence = getDefaultAbsence(0, LocalDate.now().toDateTimeAtStartOfDay());
    when(mockAbsenceRepository.save(any())).thenReturn(expectedAbsence);

    Absence actualAbsence =
        absenceServiceImpl.createAbsence(
            expectedAbsence.getUserId(),
            expectedAbsence.getAbsenceType(),
            expectedAbsence.getStartDate(),
            expectedAbsence.getEndDate(),
            expectedAbsence.getDescription());

    assertEquals(expectedAbsence, actualAbsence);
  }

  @Test
  public void approveAbsence_successful() {
    Absence expectedAbsence = getDefaultAbsence(0, LocalDate.now().toDateTimeAtStartOfDay());

    when(mockAbsenceRepository.findById(expectedAbsence.getAbsenceId()))
        .thenReturn(Optional.of(expectedAbsence));
    when(mockAbsenceRepository.save(any())).thenReturn(expectedAbsence);

    Absence actualAbsence =
        absenceServiceImpl.approveAbsence(expectedAbsence.getAbsenceId(), 1234).get();

    assertEquals(expectedAbsence, actualAbsence);
  }

  @Test
  public void approveAbsence_notFound() {
    long absenceId = 1234;
    long approverId = 123;
    when(mockAbsenceRepository.findById(absenceId)).thenReturn(Optional.empty());

    Optional<Absence> actualAbsence = absenceServiceImpl.approveAbsence(absenceId, approverId);

    assertEquals(Optional.empty(), actualAbsence);
  }

  @Test(expected = AlreadyApprovedException.class)
  public void approveAbsence_alreadyApproved() {
    long absenceId = 123;
    long approverId = 321;
    Absence absenceToApprove = getDefaultAbsence(123L, DateTime.now().minusMonths(1));
    absenceToApprove
        .getAbsenceEvents()
        .add(new AbsenceEvent(0L, DateTime.now().minusDays(10), approverId, EventType.APPROVE));

    when(mockAbsenceRepository.findById(absenceId)).thenReturn(Optional.of(absenceToApprove));

    absenceServiceImpl.approveAbsence(absenceId, approverId);
  }

  @Test
  public void updateAbsence_successful() {
    Absence expectedAbsence = getDefaultAbsence(0, LocalDate.now().toDateTimeAtStartOfDay());

    Absence updatedAbsence =
        new Absence(
            expectedAbsence.getAbsenceId(),
            expectedAbsence.getUserId(),
            expectedAbsence.getAbsenceType(),
            expectedAbsence.getStartDate(),
            expectedAbsence.getEndDate(),
            expectedAbsence.getDescription(),
            expectedAbsence.getAbsenceEvents());
    updatedAbsence.setDescription("Holiday in toy town");
    updatedAbsence
        .getAbsenceEvents()
        .add(new AbsenceEvent((long) 0, DateTime.now(), (long) 1234, EventType.UPDATE));

    when(mockAbsenceRepository.findById(expectedAbsence.getAbsenceId()))
        .thenReturn(Optional.of(expectedAbsence));
    when(mockAbsenceRepository.save(any())).thenReturn(updatedAbsence);

    Absence actualAbsence =
        absenceServiceImpl
            .updateAbsence(
                updatedAbsence.getAbsenceId(),
                updatedAbsence.getStartDate(),
                updatedAbsence.getEndDate(),
                updatedAbsence.getDescription())
            .get();

    assertEquals(expectedAbsence, actualAbsence);
  }

  @Test
  public void updateAbsence_notFound() {
    Absence absence = getDefaultAbsence((long) 1234, DateTime.now());
    when(mockAbsenceRepository.findById(absence.getAbsenceId())).thenReturn(Optional.empty());

    Optional<Absence> actualAbsence =
        absenceServiceImpl.updateAbsence(
            absence.getAbsenceId(),
            absence.getStartDate(),
            absence.getEndDate(),
            absence.getDescription());

    assertEquals(Optional.empty(), actualAbsence);
  }
}
