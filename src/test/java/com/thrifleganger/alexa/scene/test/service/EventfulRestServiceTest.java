/*
package com.thrifleganger.alexa.scene.test.service;

import com.thrifleganger.alexa.scene.exception.handler.RestResult;
import com.thrifleganger.alexa.scene.model.eventful.EventfulRequest;
import com.thrifleganger.alexa.scene.model.bandsintown.EventDataModel;
import com.thrifleganger.alexa.scene.service.EventfulRestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EventfulRestServiceTest {

    @Autowired
    private EventfulRestService restService;

    @Test
    public void callRestServiceTest() {
        EventfulRequest request = EventfulRequest.builder()
                .keywords(Optional.of("lord huron"))
                .location(Optional.of("Dublin"))
                .category(Optional.empty())
                .date(Optional.empty())
                .pageSize(Optional.empty())
                .sortBy(Optional.empty())
                .build();
        RestResult response = restService.callEventSearchEndpoint(request);
        log.info(response.getResultObject().get().toString());
    }
}
*/
