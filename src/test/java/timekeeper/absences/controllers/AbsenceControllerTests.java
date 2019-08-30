package timekeeper.absences.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import timekeeper.absences.models.Absence;
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
}
