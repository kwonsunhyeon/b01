package hello.b01.controller;

import hello.b01.dto.upload.UploadFileDTO;
import hello.b01.dto.upload.UploadResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Tag(name="upload-controller" , description = "업로드 관련 API")
@Log4j2
public class UpDownController {

   @Value("${hello.upload.path}")
   private String uploadPath;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "POST 방식 으로 파일을 업로드 합니다.")

    public List<UploadResultDTO> upload(@ModelAttribute UploadFileDTO uploadFileDTO) {

        log.info(uploadFileDTO);

        if (uploadFileDTO.getFiles() != null) {
            final List<UploadResultDTO> list = new ArrayList<>();
            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid+"_"+ originalName);

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath);
                    //이미지 파일의 종류라면
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid+"_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200,200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .img(image).build()
                );

            });  //end each

            return list;
      }//end if
        return null;
    }
    @GetMapping("/view/{fileName}")
    @Operation(summary = "GET방식으로 첨부파일을 조회합니다.")
    public ResponseEntity<Resource>viewFileGet(@PathVariable String fileName) {
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }
    @DeleteMapping("/remove/{fileName}")
    @Operation(summary = "DELETE 방식으로 파일을 삭제합니다.")
    public Map<String,Boolean> removeFile(@PathVariable String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            //썸네일이 존재한다면
            if (contentType.startsWith("image")) {
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                thumbnailFile.delete();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        resultMap.put("result", removed);

        return resultMap;
    }
}
