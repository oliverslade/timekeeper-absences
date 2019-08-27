package timekeeper.absences.utils;

import static timekeeper.absences.models.EventType.CREATE;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceEvent;
import timekeeper.absences.models.EventType;

public class TestUtils {

  private static AbsenceEvent getDefaultAbsenceEvent(long id, EventType type, DateTime timestamp) {
    return new AbsenceEvent(id, timestamp, (long) 1234, type);
  }

  public static Absence getDefaultAbsence(long id, DateTime timestamp) {
    List<AbsenceEvent> absenceEventList = new ArrayList<>();
    absenceEventList.add(getDefaultAbsenceEvent(id, CREATE, timestamp));
    return new Absence(
        id,
        (long) 1234,
        new LocalDate(2019, 8, 1),
        new LocalDate(2019, 8, 5),
        "Holiday",
        absenceEventList);
  }

  public static List<Absence> getDefaultAbsences() {
    List<Absence> absenceList = new ArrayList<Absence>();
    absenceList.add(getDefaultAbsence(1, new LocalDate(2019, 8, 1).toDateTimeAtStartOfDay()));
    absenceList.add(getDefaultAbsence(2, new LocalDate(2019, 8, 1).toDateTimeAtStartOfDay()));
    return absenceList;
  }
}
