package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.exception.ExceptionMessage;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse create(final LineRequest request) {
        Line line = new Line(request.getName(), request.getColor());
        try {
            final Line savedLine = lineDao.save(line);
            return LineResponse.of(savedLine);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException(ExceptionMessage.DUPLICATED_LINE_NAME.getContent());
        }
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Line line = lineDao.findById(id);
        return LineResponse.of(line);
    }

    public void updateById(final Long id, final LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        lineDao.updateById(id, line);
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
