package hello.b01.repository.search;

import com.querydsl.jpa.JPQLQuery;
import hello.b01.domain.Board;
import hello.b01.domain.QBoard;
import hello.b01.domain.QReply;
import hello.b01.dto.BoardListAllDTO;
import hello.b01.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);


}



