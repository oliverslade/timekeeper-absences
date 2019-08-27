package timekeeper.absences.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import timekeeper.absences.models.Absence;
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
}
