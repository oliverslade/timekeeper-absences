package timekeeper.absences.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
class Absence {

    private Date startDate;

    private Date endDate;

    private String description;
}
