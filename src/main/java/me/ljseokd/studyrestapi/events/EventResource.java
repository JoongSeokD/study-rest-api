package me.ljseokd.studyrestapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class EventResource extends EntityModel<Event> {


    public EventResource(Event event, Link... links) {
        super(event, links);
        add(WebMvcLinkBuilder.linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
