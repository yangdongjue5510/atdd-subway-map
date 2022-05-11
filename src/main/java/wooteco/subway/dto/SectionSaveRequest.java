package wooteco.subway.dto;

public class SectionSaveRequest {

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionSaveRequest() {
    }

    public SectionSaveRequest(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
