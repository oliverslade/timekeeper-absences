package timekeeper.absences.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.absences.exceptions.AlreadyApprovedException;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceEvent;
import timekeeper.absences.models.EventType;
import timekeeper.absences.services.contracts.AbsenceService;
import timekeeper.absences.services.impls.AbsenceServiceImpl;
import timekeeper.absences.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbsenceControllerTests {

  @Autowired private AbsenceController controller;

  private AbsenceService mockService;

  @Before
  public void setUp() {
    controller = new AbsenceController();
    mockService = mock(AbsenceServiceImpl.class);
    controller.setService(mockService);
  }

  @Test
  public void getAbsenceDetails_successful() {
    Absence expectedAbsence = TestUtils.getDefaultAbsence(123L, DateTime.now());
    ResponseEntity<Absence> expectedResponse = new ResponseEntity<>(expectedAbsence, HttpStatus.OK);

    when(mockService.getAbsenceDetails(expectedAbsence.getAbsenceId()))
        .thenReturn(Optional.of(expectedAbsence));

    ResponseEntity actualResponse = controller.getAbsencesDetails(expectedAbsence.getAbsenceId());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void getAbsenceDetails_notFound() {
    long absenceId = 12345;

    when(mockService.getAbsenceDetails(absenceId)).thenReturn(Optional.empty());

    ResponseEntity actual = controller.getAbsencesDetails(absenceId);

    assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
  }

  @Test(expected = ResponseStatusException.class)
  public void getAbsenceDetails_internalServerError() {
    long absenceId = 12345;

    when(mockService.getAbsenceDetails(absenceId))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getAbsencesDetails(absenceId);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void getAbsencesForPeriod_successful() {
    LocalDate startDate = new LocalDate(2019, 8, 1);
    LocalDate endDate = new LocalDate(2019, 8, 5);
    long userId = 1234;
    List<Absence> expectedAbsences = TestUtils.getDefaultAbsences();
    ResponseEntity<List<Absence>> expectedResponse =
        new ResponseEntity<>(expectedAbsences, HttpStatus.OK);

    when(mockService.getAbsencesForPeriod(startDate, endDate, userId)).thenReturn(expectedAbsences);

    ResponseEntity actualResponse = controller.getAbsencesForPeriod(startDate, endDate, userId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void getAbsencesForPeriod_noAbsences() {
    LocalDate startDate = new LocalDate(2019, 8, 1);
    LocalDate endDate = new LocalDate(2019, 8, 5);
    long userId = 1234;
    ResponseEntity<List<Absence>> expectedResponse =
        new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

    when(mockService.getAbsencesForPeriod(startDate, endDate, userId))
        .thenReturn(Collections.emptyList());

    ResponseEntity actualResponse = controller.getAbsencesForPeriod(startDate, endDate, userId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void getAbsencesForPeriod_internalServerError() {
    LocalDate startDate = new LocalDate(2019, 8, 1);
    LocalDate endDate = new LocalDate(2019, 8, 5);
    long userId = 1234;

    when(mockService.getAbsencesForPeriod(startDate, endDate, userId))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getAbsencesForPeriod(startDate, endDate, userId);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void getAllAbsencesByUser_successful() {
    List<Absence> expectedAbsences = TestUtils.getDefaultAbsences();
    long userId = 1234;
    ResponseEntity<List<Absence>> expectedResponse =
        new ResponseEntity<>(expectedAbsences, HttpStatus.OK);

    when(mockService.getAllAbsencesByUser(userId)).thenReturn(expectedAbsences);

    ResponseEntity actualResponse = controller.getAllAbsencesByUser(userId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void getAllAbsencesByUser_noAbsences() {
    long userId = 12345;
    ResponseEntity<List<Absence>> expectedResponse =
        new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

    when(mockService.getAllAbsencesByUser(userId)).thenReturn(Collections.emptyList());

    ResponseEntity actualResponse = controller.getAllAbsencesByUser(userId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void getAllAbsencesByUser_internalServerError() {
    long userId = 12345;

    when(mockService.getAllAbsencesByUser(userId))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actual = controller.getAllAbsencesByUser(userId);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    assertEquals("something broke", Objects.requireNonNull(actual.getBody()).toString());
  }

  @Test
  public void createAbsence_successful() {
    Absence expectedAbsence = TestUtils.getDefaultAbsence(123L, DateTime.now());
    ResponseEntity<Absence> expectedResponse = new ResponseEntity<>(expectedAbsence, HttpStatus.OK);

    when(mockService.getAbsencesForPeriod(
            expectedAbsence.getStartDate(),
            expectedAbsence.getEndDate(),
            expectedAbsence.getUserId()))
        .thenReturn(Collections.emptyList());

    when(mockService.createAbsence(
            expectedAbsence.getUserId(),
            expectedAbsence.getAbsenceType(),
            expectedAbsence.getStartDate(),
            expectedAbsence.getEndDate(),
            expectedAbsence.getDescription()))
        .thenReturn(expectedAbsence);

    ResponseEntity<Absence> actualResponse =
        controller.createAbsence(
            expectedAbsence.getUserId(),
            expectedAbsence.getAbsenceType(),
            expectedAbsence.getStartDate(),
            expectedAbsence.getEndDate(),
            expectedAbsence.getDescription());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void createAbsence_alreadyExists() {
    Absence existingAbsence = TestUtils.getDefaultAbsence(123L, DateTime.now());
    ResponseEntity expectedResponse = new ResponseEntity<>(HttpStatus.CONFLICT);

    when(mockService.getAbsencesForPeriod(
            existingAbsence.getStartDate(),
            existingAbsence.getEndDate(),
            existingAbsence.getUserId()))
        .thenReturn(Collections.singletonList(existingAbsence));

    ResponseEntity actualResponse =
        controller.createAbsence(
            existingAbsence.getUserId(),
            existingAbsence.getAbsenceType(),
            existingAbsence.getStartDate(),
            existingAbsence.getEndDate(),
            existingAbsence.getDescription());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void createAbsence_internalServerError() {
    Absence existingAbsence = TestUtils.getDefaultAbsence(123L, DateTime.now());
    ResponseEntity expectedResponse = new ResponseEntity<>("something broke", HttpStatus.INTERNAL_SERVER_ERROR);

    when(mockService.getAbsencesForPeriod(
            existingAbsence.getStartDate(),
            existingAbsence.getEndDate(),
            existingAbsence.getUserId()))
        .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actualResponse =
        controller.createAbsence(
            existingAbsence.getUserId(),
            existingAbsence.getAbsenceType(),
            existingAbsence.getStartDate(),
            existingAbsence.getEndDate(),
            existingAbsence.getDescription());

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void approveAbsence_successful() {
    long absenceId = 123;
    long approverId = 321;
    Absence expectedAbsence = TestUtils.getDefaultAbsence(123L, DateTime.now().minusMonths(1));
    expectedAbsence
        .getAbsenceEvents()
        .add(new AbsenceEvent(0L, DateTime.now(), approverId, EventType.APPROVE));

    ResponseEntity expectedResponse = new ResponseEntity<>(expectedAbsence, HttpStatus.OK);

    when(mockService.approveAbsence(absenceId, approverId))
        .thenReturn(Optional.of(expectedAbsence));

    ResponseEntity actualResponse = controller.approveAbsence(absenceId, approverId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void approveAbsence_notFound() {
    long absenceId = 123;
    long approverId = 321;
    ResponseEntity expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    ResponseEntity actualResponse = controller.approveAbsence(absenceId, approverId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void approveAbsence_alreadyApproved() {
    long absenceId = 123;
    long approverId = 321;
    ResponseEntity expectedResponse = new ResponseEntity<>("already approved", HttpStatus.CONFLICT);

    when(mockService.approveAbsence(absenceId, approverId))
        .thenThrow(new AlreadyApprovedException("already approved"));

    ResponseEntity actualResponse = controller.approveAbsence(absenceId, approverId);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ResponseStatusException.class)
  public void approveAbsence_internalServerError() {
    long absenceId = 123;
    long approverId = 321;
    ResponseEntity expectedResponse = new ResponseEntity<>("something broke", HttpStatus.INTERNAL_SERVER_ERROR);

    when(mockService.approveAbsence(absenceId, approverId))
            .thenThrow(new RuntimeException("something broke"));

    ResponseEntity actualResponse = controller.approveAbsence(absenceId, approverId);

    assertEquals(expectedResponse, actualResponse);
  }
}
