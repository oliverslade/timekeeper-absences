package timekeeper.absences.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

@Data
@Entity
@Table(name = "absence_events")
@AllArgsConstructor
@EqualsAndHashCode
public class AbsenceEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eventId;

  @Column(name = "event_timestamp", updatable = false, nullable = false)
  private DateTime eventTimeStamp;

  @Column(name = "user_created_by", updatable = false, nullable = false)
  private Long userCreatedBy;

  @Column(name = "event_type", updatable = false, nullable = false)
  private EventType eventType;
}
