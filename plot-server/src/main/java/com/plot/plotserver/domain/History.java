import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime endDate;

    @Column(name = "time", nullable = true, columnDefinition = "time")
    private Time time;

    @Column(name = "category_name", nullable = false, columnDefinition = "varchar (36)")
    private String categoryName;

    @Column(name = "todo_id", nullable = false, columnDefinition = "bigint")
    private Long todoId;

}