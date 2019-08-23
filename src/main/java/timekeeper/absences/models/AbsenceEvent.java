package timekeeper.absences.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "absences")
@NoArgsConstructor
@AllArgsConstructor
public class AbsenceEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eventId;

  @Column(name = "event_timestamp")
  private Date eventTimeStamp;

  @Column(name = "absence_id")
  private Long absenceId;

  @Column(name = "user_id")
  private Long userId;
}
