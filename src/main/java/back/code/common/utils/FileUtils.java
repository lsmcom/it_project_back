package back.code.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.MultiStepRescaleOp;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@Component
public class FileUtils {

    /**
     * 파일 업로드 기능
     * @param file
     * @param type
     * @return
     * @throws Exception
     */
    public Map<String, Object> uploadFiles(MultipartFile file, String filePath) throws Exception{

        Map<String, Object> resultMap = new HashMap<>();

        if(file == null || file.isEmpty()){
            return null;
        }

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String randName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        String storedFileName = randName + "." + extension;

        String fullPath = filePath + storedFileName;

        File newFile = new File(fullPath);

        //경로없으면 만들기
        if(!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }

        newFile.createNewFile(); //빈파일
        file.transferTo(newFile);

        resultMap.put("fileName", fileName);
        resultMap.put("storedFileName", storedFileName);
        resultMap.put("filePath", filePath);

        return resultMap;
    }

    /**
     * 파일 삭제
     * @param filePath
     * @throws Exception
     */
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
        }
    }

    /**
     * 썸네일 만들기
     */
    public String thumbNailFIle(int width, int height, File originFile, String thumbPath) throws Exception {
        String thumbFileName = "";

        String fileName = originFile.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String randName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        thumbFileName = randName + "." + extension;

        try(
                InputStream in = new FileInputStream(originFile);
                BufferedInputStream bf = new BufferedInputStream(in);
        ){

            //원본 이미지 파일 뜨기
            BufferedImage originImage = ImageIO.read(originFile);
            //이미지 사이즈 줄이기
            MultiStepRescaleOp scaleImage = new MultiStepRescaleOp(width, height);
            //마스킹처리
            scaleImage.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
            //리사이즈 이미지 생성
            BufferedImage resizeImage = scaleImage.filter(originImage, null);

            String thumbFilePath = thumbPath + thumbFileName;

            File resizeFile = new File(thumbFilePath);

            //경로없으면 만들기
            if(!resizeFile.getParentFile().exists()) {
                resizeFile.getParentFile().mkdirs();
            }

            //리사이즈한 파일을 실제 경로에 생성, 결과를 리턴
            boolean isWrite = ImageIO.write(resizeImage, extension, resizeFile);

            if(!isWrite){
                throw new RuntimeException("썸네일 생성 오류");
            }

        }catch(Exception e) {
            thumbFileName = null;
            e.printStackTrace();
            throw new RuntimeException("썸네일 생성 오류");
        }

        return thumbFileName;
    }
}
