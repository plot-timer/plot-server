package com.plot.plotserver.controller;

import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.user.UserReqDto;
import com.plot.plotserver.dto.response.user.UserResponseDto;
import com.plot.plotserver.service.UserService;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 생성 api", description = "유저 하나를 생성한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 생성 완료"),
                    @ApiResponse(responseCode = "400", description = "유저 이메일이 이미 존재할 때"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody UserReqDto.CreateOne req) throws Exception {
        userService.createOne(req);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }


    @Operation(summary = "유저 삭제 api", description = "유저 하나를 삭제한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 삭제 완료"),
                    @ApiResponse(responseCode = "401", description = "토큰 만료,"),
                    @ApiResponse(responseCode = "401", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @DeleteMapping("/one")
    public ResponseEntity<?> deleteOne() throws Exception {
        userService.deleteOne();

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }



    /**
     * TEST URL : 테스트가 끝나면 필수적으로 지워야 함.
     * */
    @Operation(summary = "로그인 테스트 api", description = "로그인 상태에서, 해당 유저의 정보를 출력.")
    @GetMapping("/test1")
    public ResponseEntity<?> TestApi() throws Exception {
        Long userId = SecurityContextHolderUtil.getUserId();
        try {
            UserResponseDto userResDto = UserResponseDto.of(userService.findOne(userId));
            Message message = Message.builder()
                    .data(userResDto)
                    .status(HttpStatus.OK)
                    .message("success")
                    .build();
            return new ResponseEntity<>(message, message.getStatus());
        }catch (Exception e) {
            Message message = Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    @GetMapping("/test2")
    public ResponseEntity<?> searchAll(){
        List<UserResponseDto> result = userService.findAll();

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }

}