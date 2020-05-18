package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.LineStation;

public class LineStationCreateRequest {
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    public LineStationCreateRequest() {
    }

    public LineStationCreateRequest(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public static LineStation toLineStation(LineStationCreateRequest request) {
        return new LineStation(request.preStationId, request.stationId, request.distance,
            request.duration);
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

}
