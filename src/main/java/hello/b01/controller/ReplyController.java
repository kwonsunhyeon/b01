package hello.b01.controller;

import hello.b01.dto.PageRequestDTO;
import hello.b01.dto.PageResponseDTO;
import hello.b01.dto.ReplyDTO;
import hello.b01.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Tag(name="reply-controller" , description = "댓글 관련 API")
@Log4j2
@RequiredArgsConstructor //의존성 주입
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = " POST 방식으로 댓글을 등록합니다.")
    public Map<String,Long> register(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult)throws BindException {

        log.info(replyDTO);

        if (bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String,Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }

    @GetMapping(value = "/list/{bno}")
    @Operation(summary = "GET 방식으로 특정 게시물의 댓글 목록을 구합니다.")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }

    @GetMapping("/{rno}")
    @Operation(summary = "GET 방식으로 특정 댓글을 조회합니다.")
    public ReplyDTO getReplyDTO(@PathVariable("rno")Long rno) {
        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }
    @DeleteMapping("/{rno}")
    @Operation(summary = "DELETE 방식으로 특정 댓글을 삭제합니다.")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {
        replyService.remove(rno);

        Map<String, Long>resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "PUT 방식으로 특정 댓글을 수정합니다.")
    public Map<String, Long>remove(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {

        replyDTO.setRno(rno);    //번호를 일치시킴

        replyService.modify(replyDTO);

        Map<String, Long>resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }

}
