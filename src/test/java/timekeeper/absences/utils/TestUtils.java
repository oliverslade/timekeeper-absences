package timekeeper.absences.utils;

import static timekeeper.absences.models.EventType.CREATE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import timekeeper.absences.models.Absence;
import timekeeper.absences.models.AbsenceEvent;
import timekeeper.absences.models.EventType;

public class TestUtils {

  public static AbsenceEvent getDefaultAbsenceEvent(long id, EventType type) {
    return new AbsenceEvent(id, new Date(1564617600), (long) 1234, type);
  }

  public static Absence getDefaultAbsence(long id) {
    List<AbsenceEvent> absenceEventList = new ArrayList<>();
    absenceEventList.add(getDefaultAbsenceEvent((long) 1234, CREATE));
    return new Absence(
        id, (long) 1234, new Date(1564876800), new Date(1564876800), "Holiday", absenceEventList);
  }

  public static List<Absence> getDefaultAbsences() {
    List<Absence> absenceList = new ArrayList<Absence>();
    absenceList.add(getDefaultAbsence(1));
    absenceList.add(getDefaultAbsence(2));
    return absenceList;
  }
}
