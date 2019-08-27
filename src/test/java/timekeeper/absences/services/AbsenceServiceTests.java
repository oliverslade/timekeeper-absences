package timekeeper.absences.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceEvent;
import timekeeper.absences.models.EventType;
import timekeeper.absences.repositories.AbsenceRepository;
import timekeeper.absences.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbsenceServiceTests {

  private AbsenceService absenceService;

  private AbsenceRepository mockAbsenceRepository;

  @Before
  public void setUp() {
    absenceService = new AbsenceService();

    mockAbsenceRepository = mock(AbsenceRepository.class);
    absenceService.setAbsenceRepository(mockAbsenceRepository);
  }

  @Test
  public void getAllAbsencesByUser_successful() {
    long userId = 1234;
    List<Absence> expectedAbsenceList = TestUtils.getDefaultAbsences();
    when(mockAbsenceRepository.getAllByUserId(userId)).thenReturn(Optional.of(expectedAbsenceList));

    List actualAbsenceList = absenceService.getAllAbsencesByUser(userId);
    assertEquals(expectedAbsenceList, actualAbsenceList);
  }

  @Test
  public void getAllAbsencesByUser_empty() {
    long userId = 1234;
    List expectedAbsenceList = Collections.EMPTY_LIST;
    when(mockAbsenceRepository.getAllByUserId(userId)).thenReturn(Optional.empty());

    List actualAbsenceList = absenceService.getAllAbsencesByUser(userId);
    assertEquals(expectedAbsenceList, actualAbsenceList);
  }

  @Test
  public void getAbsenceDetails_successful() {
    long absenceId = 1234;
    Absence expectedAbsence = TestUtils.getDefaultAbsence(absenceId, DateTime.now());
    when(mockAbsenceRepository.findById(absenceId)).thenReturn(Optional.of(expectedAbsence));

    Optional<Absence> actualAbsence = absenceService.getAbsenceDetails(absenceId);

    assertEquals(expectedAbsence, actualAbsence.get());
  }

  @Test
  public void getAbsenceDetails_notFound() {
    long absenceId = 1234;
    when(mockAbsenceRepository.findById(absenceId)).thenReturn(Optional.empty());

    Optional<Absence> actualAbsence = absenceService.getAbsenceDetails(absenceId);

    assertEquals(Optional.empty(), actualAbsence);
  }

  @Test
  public void getAbsencesForPeriod_successful() {
    long userId = 1234;
    LocalDate startOfPeriod = new LocalDate(1564617600);
    List<Absence> expectedAbsenceList = TestUtils.getDefaultAbsences();
    when(mockAbsenceRepository.getAbsencesByStartDateBetweenAndUserId(
            startOfPeriod, startOfPeriod, userId))
        .thenReturn(Optional.of(expectedAbsenceList));

    List actualAbsenceList =
        absenceService.getAbsencesForPeriod(startOfPeriod, startOfPeriod, userId);
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
        absenceService.getAbsencesForPeriod(startOfPeriod, startOfPeriod, userId);
    assertEquals(expectedAbsenceList, actualAbsenceList);
  }

  @Test
  public void createAbsence_successful() {
    Absence expectedAbsence =
        TestUtils.getDefaultAbsence(0, LocalDate.now().toDateTimeAtStartOfDay());
    when(mockAbsenceRepository.save(any())).thenReturn(expectedAbsence);

    Absence actualAbsence =
        absenceService.createAbsence(
            expectedAbsence.getUserId(),
            expectedAbsence.getStartDate(),
            expectedAbsence.getEndDate(),
            expectedAbsence.getDescription());

    assertEquals(expectedAbsence, actualAbsence);
  }

  @Test
  public void approveAbsence_successful() {
    Absence expectedAbsence =
        TestUtils.getDefaultAbsence(0, LocalDate.now().toDateTimeAtStartOfDay());

    Absence updatedAbsence =
        new Absence(
            expectedAbsence.getAbsenceId(),
            expectedAbsence.getUserId(),
            expectedAbsence.getStartDate(),
            expectedAbsence.getEndDate(),
            expectedAbsence.getDescription(),
            expectedAbsence.getAbsenceEvents());
    updatedAbsence
        .getAbsenceEvents()
        .add(new AbsenceEvent((long) 0, DateTime.now(), (long) 1234, EventType.APPROVE));

    when(mockAbsenceRepository.findById(expectedAbsence.getAbsenceId()))
        .thenReturn(Optional.of(expectedAbsence));
    when(mockAbsenceRepository.save(any())).thenReturn(updatedAbsence);

    Absence actualAbsence = absenceService.approveAbsence(expectedAbsence.getAbsenceId(), (long) 1234).get();

    assertEquals(expectedAbsence, actualAbsence);
  }

  @Test
  public void approveAbsence_notFound() {
    long absenceId = 1234;
    long approverId = 123;
    when(mockAbsenceRepository.findById(absenceId)).thenReturn(Optional.empty());

    Optional<Absence> actualAbsence = absenceService.approveAbsence(absenceId, approverId);

    assertEquals(Optional.empty(), actualAbsence);
  }
}
