package hello.b01.service;

import hello.b01.dto.BoardDTO;
import hello.b01.dto.BoardListReplyCountDTO;
import hello.b01.dto.PageRequestDTO;
import hello.b01.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
}
