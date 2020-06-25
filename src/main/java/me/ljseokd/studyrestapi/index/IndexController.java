package me.ljseokd.studyrestapi.index;

import lombok.RequiredArgsConstructor;
import me.ljseokd.studyrestapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public RepresentationModel index(){
        var index = new RepresentationModel();
        index.add(WebMvcLinkBuilder.linkTo(EventController.class).withRel("events"));
        return index;
    }


}
