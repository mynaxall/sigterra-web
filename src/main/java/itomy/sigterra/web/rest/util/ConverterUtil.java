package itomy.sigterra.web.rest.util;

import itomy.sigterra.service.AWSS3BucketService;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConverterUtil {
    private MultipartFile file;
    private List<PDPage> listOfPages;

    public ConverterUtil() {
    }

    public ConverterUtil(MultipartFile file) {
        this.file = file;
    }

    public ConverterUtil(List<PDPage> listOfPages) {
        this.listOfPages = listOfPages;
    }

    public File multipartFileToJavaFile() throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertFile;
    }

//    public List<URI> convertPDFToImages() throws IOException {
//        List<URI> listOfURIImages = new ArrayList<>();
//        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-S").format(new Date());
//        for (PDPage page : listOfPages) {
//            BufferedImage image = page.convertToImage();
//            File outPutFile = new File(".jpeg");
//            ImageIO.write(image, "jpeg", outPutFile);
//            MultipartFile multipartFile = javaFileToMultipartFile(outPutFile);
//            URI url = awss3BucketService.uploadSignatureImage(multipartFile, null, name);
//            listOfURIImages.add(url);
//        }

//        return listOfURIImages;
////    }

    public  MultipartFile javaFileToMultipartFile(File file) {
        MultipartFile multipartFile = null;
        try {
            FileInputStream input = new FileInputStream(file);
            multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }
}
