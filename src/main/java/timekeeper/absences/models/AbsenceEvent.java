package timekeeper.absences.models;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "absence_events")
@AllArgsConstructor
@EqualsAndHashCode
public class AbsenceEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eventId;

  @Column(name = "event_timestamp")
  private Date eventTimeStamp;

  @Column(name = "user_created_by")
  private Long userCreatedBy;

  @ManyToOne
  @JoinColumn(name = "absenceId")
  private Absence absence;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "event_type")
  private EventType eventType;
}
