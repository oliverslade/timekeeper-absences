package timekeeper.absences.models;

import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.LocalDate;

@Data
@Entity
@Table(name = "absences")
@AllArgsConstructor
@EqualsAndHashCode
public class Absence {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long absenceId;

  @Column(name = "user_id", updatable = false, nullable = false)
  private Long userId;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "description")
  private String description;

  @OneToMany
  @JoinColumn(name = "eventId")
  private List<AbsenceEvent> absenceEvents;
}
