package itomy.sigterra.web.rest.util;


import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ConverterUtil {

    public ConverterUtil() {
    }

    public static File convertMultipartFileToJavaFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertFile;
    }

    public static MultipartFile convertJavaFileToMultipartFile(File file) throws IOException {
        MultipartFile multipartFile = null;
        FileInputStream input = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
        return multipartFile;
    }
}
