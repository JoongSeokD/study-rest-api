package me.ljseokd.studyrestapi.events;

import me.ljseokd.studyrestapi.commoon.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    void createEvent() throws Exception{
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 06, 23, 10, 9))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 06, 24, 10, 9))
                .beginEventDateTime(LocalDateTime.of(2020, 06, 25, 10, 9))
                .endEventDateTime(LocalDateTime.of(2020, 06, 26, 10, 9))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(LOCATION))
                .andExpect(header().string(CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                            links(
                                    linkWithRel("self").description("link to self"),
                                    linkWithRel("query-events").description("link to query events"),
                                    linkWithRel("update-event").description("link to update and existing events")
                            ),
                        HeaderDocumentation.requestHeaders(
                                headerWithName(ACCEPT).description("accept header"),
                                headerWithName(CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("Description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time to begin of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time to close of new Event"),
                                fieldWithPath("beginEventDateTime").description("date time to begin of new Event"),
                                fieldWithPath("endEventDateTime").description("date time to end of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("base price of new Event"),
                                fieldWithPath("maxPrice").description("max price of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of new Event")
                        ),
                        responseHeaders(
                                headerWithName(LOCATION).description("Location header"),
                                headerWithName(CONTENT_TYPE).description("content-type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("identifier of new Event"),
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("Description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time to begin of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time to close of new Event"),
                                fieldWithPath("beginEventDateTime").description("date time to begin of new Event"),
                                fieldWithPath("endEventDateTime").description("date time to end of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("base price of new Event"),
                                fieldWithPath("maxPrice").description("max price of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of new Event"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("eventStatus").description("event status")
                        )

                ))
        ;
    }

    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    void createEvent_Bad_Request() throws Exception{
        Event event = Event.builder()
                .id(10L)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 06, 23, 10, 9))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 06, 24, 10, 9))
                .beginEventDateTime(LocalDateTime.of(2020, 06, 25, 10, 9))
                .endEventDateTime(LocalDateTime.of(2020, 06, 26, 10, 9))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 06, 26, 10, 9))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 06, 25, 10, 9))
                .beginEventDateTime(LocalDateTime.of(2020, 06, 24, 10, 9))
                .endEventDateTime(LocalDateTime.of(2020, 06, 23, 10, 9))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    void queryEvents() throws Exception{
        // Given
        IntStream.range(0,30).forEach(this::generateEvent);
        // WHen
        mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("page").exists())
        ;
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event" + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 06, 23, 10, 9))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 06, 24, 10, 9))
                .beginEventDateTime(LocalDateTime.of(2020, 06, 25, 10, 9))
                .endEventDateTime(LocalDateTime.of(2020, 06, 26, 10, 9))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return eventRepository.save(event);
    }

    @Test
    @DisplayName("기존의 이벤트를 하나 조회하기")
    void getEvent() throws Exception {
        //given
        Event event = generateEvent(100);

        //when
         mockMvc.perform(get("/api/events/{id}", event.getId()))
                 .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
         ;

    }

    @Test
    @DisplayName("없는 이벤트를 조회했을 때 404 응답받기")
    void getEvent404() throws Exception {
        mockMvc.perform(get("/api/events/123425"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("이벤트를 정상적으로 수정하기")
    void updateEvent() throws Exception {
        Event event = generateEvent(100);

        EventDto eventDto = modelMapper.map(event, EventDto.class);
        String eventName = "Update Event";
        eventDto.setName(eventName);

        mockMvc.perform(put("/api/events/{id}",event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("_links.self").exists())

            ;
    }

    @Test
    @DisplayName("입력값이 비어있는 경우에 이벤트 수정 실패")
    void updateEvent400_Empty() throws Exception {
        Event event = generateEvent(100);

        EventDto eventDto = new EventDto();

        mockMvc.perform(put("/api/events/{id}",event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())

            ;
    }

    @Test
    @DisplayName("입력값이 잘못된 경우에 이벤트 수정 실패")
    void updateEvent400_Wrong() throws Exception {
        Event event = generateEvent(100);

        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);
        mockMvc.perform(put("/api/events/{id}",event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())

            ;
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 수정 실패")
    void updateEvent404() throws Exception {
        Event event = generateEvent(100);

        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        mockMvc.perform(put("/api/events/12342")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())

            ;
    }

}
