package me.ljseokd.studyrestapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
    @CsvSource({
            "0, 0, true",
            "100, 0, false",
            "0, 1000, false"
    })
    public void various_tests_isFree_NonSafety(int basePrice, int maxPrice, boolean isFree){
        System.out.println(basePrice);
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
    @MethodSource("paramsForTestFree")
    public void various_tests_isFree_Safety(int basePrice, int maxPrice, boolean isFree){
        System.out.println(basePrice);
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Object[] paramsForTestFree(){
        return new Object[]{
                new Object[]{0,0, true},
                new Object[]{100,0, false},
                new Object[]{0,1000, false},
        };
    }

    @ParameterizedTest(name = "{index} => location={0}, offline={1}")
    @MethodSource("paramsForTestOffline")
    void testOffline(String location, boolean offline){
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(offline);
    }

    private static Object[] paramsForTestOffline(){
        return new Object[]{
                new Object[]{"강남역 네이버 D2 스타텁 팩토리", true},
                new Object[]{null, false},
        };
    }

}