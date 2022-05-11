package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.exception.ExceptionMessage;

class SectionTest {

    @ParameterizedTest
    @DisplayName("구간을 다른 구간으로 쪼개기")
    @MethodSource("provideSection_divide")
    void insert_divide(Section from, Section to, Section expect) {
        // when
        Section actual = from.divideFrom(to);

        // then
        assertAll(() -> {
            assertThat(actual.getUpStationId()).isEqualTo(expect.getUpStationId());
            assertThat(actual.getDownStationId()).isEqualTo(expect.getDownStationId());
            assertThat(actual.getDistance()).isEqualTo(expect.getDistance());
        });
    }

    private static Stream<Arguments> provideSection_divide() {
        return Stream.of(
                Arguments.of(new Section(1L, 1L, 2L, 4),
                        new Section(1L, 3L, 2L, 1),
                        new Section(1L, 1L, 3L, 3)),
                Arguments.of(new Section(1L, 1L, 2L, 4),
                        new Section(1L, 1L, 3L, 1),
                        new Section(1L, 3L, 2L, 3))
        );
    }

    @Test
    @DisplayName("연결할 역이 없는 경우 예외 생성하기")
    void insert_invalidNotMatch() {
        // given
        Section section = new Section(1L, 1L, 2L, 4);
        Section sectionForAdd = new Section(1L, 3L, 4L, 1);

        // when
        assertThatThrownBy(() -> section.divideFrom(sectionForAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.INVALID_DIVIDE_SECTION.getContent());
    }

    @Test
    @DisplayName("삽입되는 구간이 쪼개지는 구간 길이보다 길거나 같은 경우 예외 생성하기")
    void insert_invalidDistance() {
        // given
        Section section = new Section(1L, 1L, 2L, 4);
        Section sectionForAdd = new Section(1L, 1L, 3L, 4);

        // when
        assertThatThrownBy(() -> section.divideFrom(sectionForAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.INVALID_INSERT_SECTION_DISTANCE.getContent());
    }

    @Test
    @DisplayName("같은 출발점과 도착점을 쪼개려는 경우 예외 생성하기")
    void insert_invalidSection() {
        // given
        Section section = new Section(1L, 1L, 2L, 4);
        Section sectionForAdd = new Section(1L, 1L, 2L, 3);

        // when
        assertThatThrownBy(() -> section.divideFrom(sectionForAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.SAME_STATIONS_SECTION.getContent());
    }

    @ParameterizedTest
    @DisplayName("하나의 역으로 연결된 구간을 하나의 구간으로 합치기")
    @MethodSource("provideSectionsForMergeTest")
    void merge(Section section, Section other, Section expected) {
        // when
        Section merged = section.merge(other);

        // then
        assertThat(merged).isEqualTo(expected);
    }

    private static Stream<Arguments> provideSectionsForMergeTest() {
        return Stream.of(
                Arguments.of(
                        new Section(1L, 1L, 2L, 4),
                        new Section(1L, 2L, 3L, 5),
                        new Section(1L, 1L, 3L, 9)
                ),
                Arguments.of(
                        new Section(1L, 2L, 3L, 5),
                        new Section(1L, 1L, 2L, 4),
                        new Section(1L, 1L, 3L, 9)
                )
        );
    }

    @Test
    @DisplayName("같은 출발점과 도착점을 합치려는 경우 예외 생성하기")
    void merge_invalidSameStations() {
        // given
        Section section = new Section(1L, 1L, 2L, 4);
        Section sectionForAdd = new Section(1L, 1L, 2L, 5);

        // then
        assertThatThrownBy(() -> section.merge(sectionForAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.SAME_STATIONS_SECTION.getContent());
    }

    @Test
    @DisplayName("연결되지 않는 두 구간을 합치려는 경우 예외 생성하기")
    void merge_invalidNotConnectedSections() {
        // given
        Section section = new Section(1L, 1L, 2L, 4);
        Section sectionForAdd = new Section(1L, 3L, 4L, 5);

        // then
        assertThatThrownBy(() -> section.merge(sectionForAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.NOT_CONNECTED_SECTIONS.getContent());
    }
}
