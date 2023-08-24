package com.plot.plotserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.history.HistoryReqDto;
import com.plot.plotserver.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/histories")
public class HistoryController {

    private  final HistoryService historyService;


    @PostMapping("/{dailyTodoId}")
    public void addHistory(@PathVariable Long dailyTodoId, @RequestBody HistoryReqDto historyReqDto, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        historyService.save(dailyTodoId,historyReqDto);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();

        om.writeValue(response.getOutputStream(), message);

    }

}
