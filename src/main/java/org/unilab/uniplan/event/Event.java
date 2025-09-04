package org.unilab.uniplan.event;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.roomcategory.RoomCategory;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "main_event_id")
    private Event mainEvent;

    @OneToMany(mappedBy = "mainEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> occurrences = new ArrayList<>();

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "link", length = 2048)
    private String link;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "participant_id", unique = true)
    private Set<Participant> participants = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "room_category_id")
    private List<RoomCategory> roomCategory = new ArrayList<>();

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recurrence_info_id")
    private RecurrenceInfo recurrenceInfo;

    public void addOccurrences(Event occurrence) {
        occurrences.add(occurrence);
        occurrence.setMainEvent(this);
    }

    public void removeOccurrence(Event occurrence) {
        occurrences.remove(occurrence);
        occurrence.setMainEvent(null);
    }
}
