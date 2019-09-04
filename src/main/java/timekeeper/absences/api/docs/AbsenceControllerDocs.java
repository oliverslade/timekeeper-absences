package timekeeper.absences.api.docs;

import io.swagger.annotations.*;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceType;

@Api(value = "Absence API", description = "Endpoints allowing operations on the absence database")
public interface AbsenceControllerDocs {

  @ApiOperation(value = "Get all absences for a user", response = List.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GetMapping("/get-absences-by-user")
  ResponseEntity getAllAbsencesByUser(
      @ApiParam(value = "The user id of the user to get all absences for", required = true)
          long userId);

  @ApiOperation(value = "Get the details of a specific absence", response = Absence.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GetMapping("/get-absences-details")
  ResponseEntity getAbsencesDetails(
      @ApiParam(value = "The id of the absence to get details for", required = true)
          long absenceId);

  @ApiOperation(value = "Get all absences between two dates", response = List.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  @GetMapping("/get-absences-for-period")
  ResponseEntity getAbsencesForPeriod(
      @ApiParam(value = "The start date of the period", required = true) LocalDate startOfPeriod,
      @ApiParam(value = "The end of the period", required = true) LocalDate endOfPeriod,
      @ApiParam(value = "The user id to get absences for", required = true) long userId);

  @PostMapping("/create-absence")
  ResponseEntity<Absence> createAbsence(
      long userId, AbsenceType type, LocalDate startDate, LocalDate endDate, String description);

  @PutMapping("/approve-absence")
  ResponseEntity approveAbsence(long absenceId, long approverId);

  @PutMapping("/update-absence")
  ResponseEntity updateAbsence(
      long absenceId, LocalDate startDate, LocalDate endDate, String description);
}
