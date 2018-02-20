package itomy.sigterra.web.rest.util;

import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConverterUtil {
    private MultipartFile file;
    private List<PDPage> listOfPages;

    public ConverterUtil(MultipartFile file) {
        this.file = file;
    }

    public ConverterUtil(List<PDPage> listOfPages) {
        this.listOfPages = listOfPages;
    }

    public File MultipartFileToJavaFile() throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertFile;
    }

    public List<URI> convertPDFToImages() throws IOException {
        List<URI> listOfURIImages= new ArrayList<>();

        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-S").format(new Date());
        for (PDPage page : listOfPages) {
            BufferedImage image = page.convertToImage();
            File outPutFile = new File(name + ".jpg");
            ImageIO.write(image, "jpg", outPutFile);
            listOfURIImages.add(null);
        }

return null;
    }
}
