package itomy.sigterra.service;


import itomy.sigterra.config.JHipsterProperties;
import itomy.sigterra.domain.*;
import itomy.sigterra.repository.*;
import itomy.sigterra.service.mapper.CardletBackgroundMapper;
import itomy.sigterra.service.mapper.CardletFooterMapper;
import itomy.sigterra.service.mapper.CardletHeaderMapper;
import itomy.sigterra.service.mapper.CardletLinksMapper;
import itomy.sigterra.web.rest.errors.CardletNotFound;
import itomy.sigterra.web.rest.errors.CustomParameterizedException;
import itomy.sigterra.web.rest.vm.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
  work with Cartdlet views: header,background, footers
 */

@Service
@Transactional
public class CardletViewService {
    private final Logger log = LoggerFactory.getLogger(CardletViewService.class);

    private static final String FILE_NAME_LOGO_TMP = "logo.tmp";
    private static final int MAX_SIZE_LOGO = 1 * 1024 * 1024;
    private static final String FILE_NAME_LOGO_PERSIST = "logoview";
    private static final String FILE_NAME_PHOTO_TMP = "photo.tmp";
    private static final int MAX_SIZE_PHOTO = 2 * 1024 * 1024;
    private static final String FILE_NAME_PHOTO_PERSIST = "photoview";

    @Inject
    private CardletRepository cardletRepository;

    @Inject
    private CardletHeaderRepository cardletHeaderRepository;

    @Inject
    private CardletBackgroundRepository cardletBackgroundRepository;

    @Inject
    private CardletLinksRepository cardletLinksRepository;

    @Inject
    private CardletFooterRepository cardletFooterRepository;

    @Inject
    private AWSS3BucketService awss3BucketService;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private JHipsterProperties jHipsterProperties;

    /**
     * read cardlet data and create view object by cardlet id
     *
     * @param cardletId
     * @return
     */
    public CardletViewRequestResponseVM getCardletView(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(cardletId);
        if (cardlet == null) throw new CardletNotFound(cardletId);
        CardletViewRequestResponseVM cardletView = createCardletViewVM(cardlet);
        return cardletView;
    }

    /**
     * read cardlet data and create view object
     *
     * @param cardlet
     * @return filled object
     */
    private CardletViewRequestResponseVM createCardletViewVM(Cardlet cardlet) {
        CardletViewRequestResponseVM cardletView = new CardletViewRequestResponseVM();
        cardletView.setCardletId(cardlet.getId());
        CardletHeaderVM cardletHeaderVM = null;
        CardletHeader cardletHeader = cardlet.getCardletHeader();
        if (cardletHeader != null) {
            cardletHeaderVM = new CardletHeaderVM(cardletHeader);
        }
        cardletView.setHeader(cardletHeaderVM);

        CardletBackgroundVM cardletBackgroundVM = null;
        CardletBackground cardletBackground = cardlet.getCardletBackground();

        if (cardletBackground != null) {
            cardletBackgroundVM = new CardletBackgroundVM(cardletBackground);
        }
        cardletView.setBackground(cardletBackgroundVM);

        CardletLinksVM cardletLinksVM = null;
        CardletLinks cardletLinks = cardlet.getCardletLinks();

        if (cardletLinks != null) {
            cardletLinksVM = new CardletLinksVM(cardletLinks);
        }
        cardletView.setLinks(cardletLinksVM);

        CardletFooterVM cardletFooterVM = null;
        CardletFooter cardletFooter = cardlet.getCardletFooter();

        if (cardletFooter != null) {
            cardletFooterVM = new CardletFooterVM(cardletFooter);
        }
        cardletView.setFooter(cardletFooterVM);

        return cardletView;
    }

    /**
     * Save cardlet view: footers, header, background
     *
     * @param cardletView CardletViewRequestResponseVM to save
     * @return
     */
    @Transactional
    public CardletViewRequestResponseVM saveCardletView(CardletViewRequestResponseVM cardletView) {
        Cardlet cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(cardletView.getCardletId());
        if (cardlet == null) {
            String errorMessage = "Cardlet ID not found for user";
            log.warn(errorMessage);
            throw new CustomParameterizedException(errorMessage);
        }

        CardletHeaderVM cardletHeaderVM = cardletView.getHeader();
        if (cardletHeaderVM != null) {
            CardletHeader cardletHeader = checkAndGetCardletVewPageTab(cardletHeaderRepository, cardlet, cardlet.getCardletHeader(), cardletHeaderVM, CardletHeader.class);
            cardletHeaderVM.setLogoUrl(
                renameIfTmp(cardlet, cardletHeaderVM.getLogoUrl(), FILE_NAME_LOGO_TMP, FILE_NAME_LOGO_PERSIST));
            cardletHeaderVM.setPhotoUrl(
                renameIfTmp(cardlet, cardletHeaderVM.getPhotoUrl(), FILE_NAME_PHOTO_TMP, FILE_NAME_PHOTO_PERSIST));
            CardletHeaderMapper.map(cardletHeaderVM, cardletHeader);
            cardletHeader = cardletHeaderRepository.save(cardletHeader);
            cardlet.setCardletHeader(cardletHeader);
        }

        CardletBackgroundVM cardletBackgroundVM = cardletView.getBackground();
        if (cardletBackgroundVM != null) {
            CardletBackground cardletBackground = checkAndGetCardletVewPageTab(cardletBackgroundRepository, cardlet, cardlet.getCardletBackground(), cardletBackgroundVM, CardletBackground.class);
            CardletBackgroundMapper.map(cardletBackgroundVM, cardletBackground);
            cardletBackground = cardletBackgroundRepository.save(cardletBackground);
            cardlet.setCardletBackground(cardletBackground);
        }

        CardletLinksVM cardletLinksVM = cardletView.getLinks();
        if (cardletLinksVM != null) {
            CardletLinks cardletLinks = checkAndGetCardletVewPageTab(cardletLinksRepository, cardlet, cardlet.getCardletLinks(), cardletLinksVM, CardletLinks.class);
            CardletLinksMapper.map(cardletLinksVM, cardletLinks);
            cardletLinks = cardletLinksRepository.save(cardletLinks);
            cardlet.setCardletLinks(cardletLinks);
        }

        CardletFooterVM cardletFooterVM = cardletView.getFooter();
        if (cardletFooterVM != null) {
            CardletFooter cardletFooter = checkAndGetCardletVewPageTab(cardletFooterRepository, cardlet, cardlet.getCardletFooter(), cardletFooterVM, CardletFooter.class);
            CardletFooterMapper.map(cardletFooterVM, cardletFooter);
            cardletFooter = cardletFooterRepository.save(cardletFooter);
            cardlet.setCardletFooter(cardletFooter);
        }

        return createCardletViewVM(cardlet);
    }

    /**
     * id we have tmp file we must rename it in AWS bucket  before save
     *
     * @param cardlet cardlet for generate path
     * @param url     source url of file
     * @param from    tempalate for source file
     * @param to      name for destination file
     * @return renamed file if did, or equivalent to input
     */
    private String renameIfTmp(Cardlet cardlet, String url, String from, String to) {
        String result = url;
        if (url != null && url.contains(from)) {
            String source;
            try {
                source = new URL(url).getPath();
                source = source.substring(1); //remove first "/"
            } catch (MalformedURLException e) {
                throw new CustomParameterizedException(e.getMessage());
            }
            String dest = getFileNameInBucket(to, null, cardlet)
                + source.substring(source.lastIndexOf("."));//save extension
            result = awss3BucketService.renameFile(source, dest).toString();
            //remove parameters
            if (result.contains("?")) result = result.substring(0, result.indexOf("?"));
        }
        return result;
    }

    private <T> T checkAndGetCardletVewPageTab(JpaRepository repository, Cardlet cardlet
        , EntityWithLongId entity, VmWithLongId vm
        , Class<T> classObject) {
        EntityWithLongId result = null;
        if (vm.getId() == null) {
            if (entity != null) {
                String errorMessage = "Unable to create " + classObject.getSimpleName() + " if it's already exist";
                log.warn(errorMessage);
                throw new CustomParameterizedException(errorMessage);
            }
            try {
                result = (EntityWithLongId) classObject.newInstance();
            } catch (Exception e) { //ignore
            }
        } else {
            result = (EntityWithLongId) repository.findOne(vm.getId());
            if (result == null || (entity != null && !entity.equals(result))) {
                String errorMessage = "Unable to create " + classObject.getSimpleName() + " if it's already exist";
                log.warn(errorMessage);
                throw new CustomParameterizedException(errorMessage);
            }
        }
        return (T) result;
    }

    /**
     * upload files to AWS
     *
     * @param file    gotten file
     * @param path    pathKey to save
     * @param maxSize constraint max size
     * @return JSONObjectd with new path
     */
    //todo need to refactor
    public JSONObject uploadFile(MultipartFile file, String path, int maxSize) throws JSONException {
        JSONObject successObject = new JSONObject();
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > maxSize) {
                successObject.put("success", false);
                successObject.put("message", "File is too big. Max allowed file size is " + maxSize);
            }
            String fileName = file.getName();
            String mimeType = file.getContentType();
            if (mimeType.startsWith("image/")) {
                URI url = awss3BucketService.uploadImage(file, path);
                if (url == null) {
                    successObject.put("success", false);
                    successObject.put("message", "Unable to fetch the file from S3 bucket.");
                } else {
                    successObject.put("success", true);
                    successObject.put("url", url);
                }
            } else {
                successObject.put("success", false);
                successObject.put("message", "Invalid file type");
            }


        } else {
            successObject.put("success", false);
            successObject.put("message", "File is empty or NULL");
        }
        return successObject;

    }

    /**
     * upload logo to AWS
     *
     * @param file gotten file
     * @return JSONObjectd with new path
     */
    public JSONObject uploadLogo(MultipartFile file, Long cardletId) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_LOGO_TMP, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_LOGO);
    }

    /**
     * upload photo to AWS
     *
     * @param file gotten file
     * @return JSONObjectd with new path
     */
    public JSONObject uploadPhoto(MultipartFile file, Long cardletId) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_PHOTO_TMP, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_PHOTO);
    }

    /**
     * generate path begin in bucket
     *
     * @param cardlet
     * @return path
     */
    private String getStartFolder(Cardlet cardlet) {
        return "accounts/" + cardlet.getUser().getId() + "/" + cardlet.getId() + "/";
    }

    /**
     * get full name for file
     *
     * @param name    - file name to save
     * @param file    uploaded file
     * @param cardlet
     * @return path
     */
    private String getFileNameInBucket(String name, MultipartFile file, Cardlet cardlet) {
        name = getStartFolder(cardlet) + name;
        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename.contains(".")) {
                name += originalFilename.substring(originalFilename.lastIndexOf("."));
            }
        }
        return name;
    }

    /**
     * validate cardlet
     *
     * @param cardletId
     * @return
     */
    private Cardlet validateAndGetCardlet(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(cardletId);
        if (cardlet == null) throw new CustomParameterizedException("Bad cardlet ID");
        return cardlet;
    }

    /**
     * delete files with images if exist
     *
     * @param cardlet
     */
    public void delete(Cardlet cardlet) {
        if (cardlet.getCardletHeader() != null) {
            deleteFile(cardlet.getCardletHeader().getLogo());
            deleteFile(cardlet.getCardletHeader().getPhoto());
        }
    }

    /**
     * delete file
     *
     * @param path
     */
    private void deleteFile(String path) {
        awss3BucketService.deleteFile(path);
    }

    /**
     * get backgrounds list in resource
     *
     * @return files list
     */
    public List<String> getBackground() {
        return getFilePaths(jHipsterProperties.getSigterraProperties().getPathBackgroundImages());
    }

    /**
     * get images list in resource
     *
     * @return files list
     */
    public List<String> getLinksImages() {
        return getFilePaths(jHipsterProperties.getSigterraProperties().getPathLinkImages());
    }

    /**
     * get files list
     *
     * @param sourcePath resource path
     * @return files list
     */
    private List<String> getFilePaths(String sourcePath) {
        List<String> files = new ArrayList<>();
        Resource resource = applicationContext.getResource(sourcePath);
        if (resource.exists()) {
            try {
                File fileRoot = resource.getFile();
                if (fileRoot.isDirectory()) {
                    //add files only from root
                    for (File f : fileRoot.listFiles()) {
                        if (!f.isDirectory()) {
                            files.add(sourcePath + "/" + f.getName());
                        }
                    }
                } else {
                    files.add(fileRoot.getPath());
                }
            } catch (IOException e) {
                log.error("Cant't read resources path", e);
            }
        }
        return files;
    }

}
