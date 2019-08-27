package timekeeper.absences.models;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "absences")
@AllArgsConstructor
@EqualsAndHashCode
public class Absence {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long absenceId;

  @Column(name = "user_id", updatable = false, nullable = false)
  private Long userId;

  @Column(name = "start_date", nullable = false)
  private Date startDate;

  @Column(name = "end_date", nullable = false)
  private Date endDate;

  @Column(name = "description")
  private String description;

  @OneToMany
  @JoinColumn(name = "eventId")
  private List<AbsenceEvent> absenceEvents;
}
