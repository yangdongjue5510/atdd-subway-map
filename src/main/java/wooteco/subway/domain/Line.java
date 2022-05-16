package wooteco.subway.domain;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.LineException;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(Long id, String name, String color, List<Section> sections) {
        if (name.isBlank()) {
            throw new LineException(ExceptionMessage.BLANK_LINE_NAME.getContent());
        }
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new Sections(sections);
    }

    public Line(String name, String color, List<Section> sections) {
        this(null, name, color, sections);
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Long> getSortedStationId() {
        return sections.getSortedStationId();
    }

    public void deleteSectionsByStationId(Long stationId) {
        sections.deleteSectionsByStationId(stationId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getValue();
    }
}
