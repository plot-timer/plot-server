package com.plot.plotserver.controller;

import com.plot.plotserver.dto.request.user.UserReqDto;
import com.plot.plotserver.dto.response.user.UserResponseDto;
import com.plot.plotserver.service.UserService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 생성 api", description = "유저 하나를 생성한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 생성 완료"),
                    @ApiResponse(responseCode = "401", description = "토큰 만료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @PostMapping("/one")
    public ResponseEntity<UserResponseDto> createOne(@RequestBody UserReqDto.CreateOne req) throws Exception {
        return ResponseEntity.ok(userService.createOne(req));
    }


    @Operation(summary = "유저 삭제 api", description = "유저 하나를 삭제한다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 삭제 완료"),
                    @ApiResponse(responseCode = "401", description = "토큰 만료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "해당 페이지 존재하지 않음"),
            }
    )
    @DeleteMapping("/one")
    public ResponseEntity<Boolean> deleteOne(@RequestBody UserReqDto.DeleteUser req) throws Exception {
        return ResponseEntity.ok(userService.deleteOne(req));
    }

}
