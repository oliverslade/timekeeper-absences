package timekeeper.absences.models;

import java.util.Date;
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

  @Column(name = "start_date")
  private Date startDate;

  @Column(name = "end_date")
  private Date endDate;

  @Column(name = "description")
  private String description;
}
