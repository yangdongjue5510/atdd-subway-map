package wooteco.subway.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import wooteco.subway.domain.Station;

@Repository
public class JdbcStationDao implements StationDao {

    private static final String TABLE_NAME = "station";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleInserter;

    public JdbcStationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Station save(Station station) {
        final Map<String, String> params = Map.of("name", station.getName());

        long savedId = simpleInserter.executeAndReturnKey(params).longValue();
        return setId(station, savedId);
    }

    private Station setId(Station station, long id) {
        Field field = ReflectionUtils.findField(Station.class, "id");
        Objects.requireNonNull(field).setAccessible(true);
        ReflectionUtils.setField(field, station, id);
        return station;
    }

    @Override
    public List<Station> findAll() {
        String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    private RowMapper<Station> getRowMapper() {
        return (resultSet, rowNumber) -> {
            String name = resultSet.getString("name");
            long id = resultSet.getLong("id");
            return setId(new Station(name), id);
        };
    }

    @Override
    public Integer deleteById(Long id) {
        String sql = "DELETE FROM station WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
