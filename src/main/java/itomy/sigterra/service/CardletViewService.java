package itomy.sigterra.service;


import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletBackground;
import itomy.sigterra.domain.CardletFooter;
import itomy.sigterra.domain.CardletHeader;
import itomy.sigterra.domain.enumeration.CardletFooterIndex;
import itomy.sigterra.repository.CardletBackgroundRepository;
import itomy.sigterra.repository.CardletFooterRepository;
import itomy.sigterra.repository.CardletHeaderRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.web.rest.errors.CardletNotFound;
import itomy.sigterra.web.rest.errors.CustomParameterizedException;
import itomy.sigterra.web.rest.vm.CardletBackgroundVM;
import itomy.sigterra.web.rest.vm.CardletFooterVM;
import itomy.sigterra.web.rest.vm.CardletHeaderVM;
import itomy.sigterra.web.rest.vm.CardletViewRequestResponseVM;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/*
  work with Cartdlet views: header,background, footers
 */

@Service
@Transactional
public class CardletViewService {
    private final Logger log               = LoggerFactory.getLogger(CardletViewService.class);

    private static final String FILE_NAME_LOGO_TMP = "logo.tmp";
    private static final int MAX_SIZE_LOGO = 1 * 1024 * 1024;
    private static final String FILE_NAME_LOGO_PERSIST = "logoview";
    private static final String FILE_NAME_PHOTO_TMP = "photo.tmp";
    private static final int MAX_SIZE_PHOTO = 2 * 1024 * 1024;
    private static final String FILE_NAME_PHOTO_PERSIST = "photoview";
    private static final String FILE_NAME_BACKIMAGE_TMP = "backimage.tmp";
    private static final int MAX_SIZE_BACKIMAGE = 10 * 1024 * 1024;
    private static final String FILE_NAME_BACKIMAGE_PERSIST = "backgroundview";
    private static final String FILE_NAME_LINKLOGO_TMP = "tmplinklogo";
    private static final int MAX_SIZE_LINKLOGO = 1 * 1024 * 1024;
    private static final String FILE_NAME_LINKLOGO_PERSIST = "linklogoview";

    @Value("${sigterra-project.paths.banner-images}")
    private String pathToBannerImages;

    @Value("${sigterra-project.paths.links-images}")
    private String pathToLinkImages;


    @Inject
    private CardletRepository cardletRepository;

    @Inject
    private CardletHeaderRepository cardletHeaderRepository;

    @Inject
    private CardletBackgroundRepository cardletBackgroundRepository;

    @Inject
    private CardletFooterRepository cardletFooterRepository;

    @Inject
    private AWSS3BucketService awss3BucketService;

    @Inject
    private ApplicationContext applicationContext;

    /**
     * read cardlet data and create view object by cardlet id
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
        cardletView.setHeaders(cardletHeaderVM);

        CardletBackgroundVM cardletBackgroundVM = null;
        CardletBackground cardletBackground = cardlet.getCardletBackground();

        if (cardletBackground != null) {
            cardletBackgroundVM = new CardletBackgroundVM(cardletBackground);
        }
        cardletView.setBackground(cardletBackgroundVM);

        List<CardletFooterVM> footers = new ArrayList<>();
        for (CardletFooter cardletFooter : cardlet.getCardletFooter()) {
            CardletFooterVM cardletFooterVM = new CardletFooterVM(cardletFooter);
            footers.add(cardletFooterVM);
        }
        cardletView.setFooters(footers);

        return cardletView;
    }

    /**
     *  Save cardlet view: footers, header, background
     * @param cardletView CardletViewRequestResponseVM to save
     * @return
     */
    @Transactional
    public CardletViewRequestResponseVM saveCardletView(CardletViewRequestResponseVM cardletView) {
        Cardlet cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(cardletView.getCardletId());
        if (cardlet == null) {
            throw new CustomParameterizedException("Cardlet ID not found for user");
        }

        CardletHeaderVM cardletHeaderVM = cardletView.getHeaders();
        if (cardletHeaderVM != null) {
            CardletHeader cardletHeader;
            if (cardletHeaderVM.getId() == null) {
                if (cardlet.getCardletHeader() != null)
                    throw new CustomParameterizedException("Bad cardlet header ID");
                cardletHeader = new CardletHeader();
                cardletHeader.setCardlet(cardlet);
            } else {
                cardletHeader = cardletHeaderRepository.findOne(cardletHeaderVM.getId());
                if (cardletHeader == null || !cardlet.equals(cardletHeader.getCardlet()))
                    throw new CustomParameterizedException("Bad cardlet header ID");
            }
            cardletHeaderVM.setLogoUrl(
                renameIfTmp(cardlet,cardletHeaderVM.getLogoUrl(), FILE_NAME_LOGO_TMP, FILE_NAME_LOGO_PERSIST));
            cardletHeaderVM.setPhotoUrl(
                renameIfTmp(cardlet,cardletHeaderVM.getPhotoUrl(), FILE_NAME_PHOTO_TMP, FILE_NAME_PHOTO_PERSIST));
            cardletHeaderVM.mapToCardletHeader(cardletHeader);
            cardletHeader = cardletHeaderRepository.save(cardletHeader);
            cardlet.setCardletHeader(cardletHeader);
        }

        CardletBackgroundVM cardletBackgroundVM = cardletView.getBackground();
        if (cardletBackgroundVM != null) {
            CardletBackground cardletBackground;
            if (cardletBackgroundVM.getId() == null) {
                if (cardlet.getCardletBackground() != null)
                    throw new CustomParameterizedException("Bad cardlet header ID");
                cardletBackground = new CardletBackground();
                cardletBackground.setCardlet(cardlet);
            } else {
                cardletBackground = cardletBackgroundRepository.findOne(cardletBackgroundVM.getId());
                if (cardletBackground == null || !cardlet.equals(cardletBackground.getCardlet()))
                    throw new CustomParameterizedException("Bad cardlet background ID");
            }
            cardletBackgroundVM.mapToCardletBackground(cardletBackground);
            cardletBackground = cardletBackgroundRepository.save(cardletBackground);
            cardlet.setCardletBackground(cardletBackground);
        }

        List<CardletFooterVM> cardletFooterVMList = cardletView.getFooters();
        if (cardletFooterVMList != null) {
            Map<CardletFooterIndex,CardletFooter> cardletFooters = new EnumMap<CardletFooterIndex, CardletFooter>(CardletFooterIndex.class);
            for (CardletFooterVM cardletFoterVM : cardletFooterVMList) {
                CardletFooter cardletFooter;
                CardletFooterIndex position = cardletFoterVM.getPosition();
                if (cardletFoterVM.getId() == null) {

                    if (cardlet.getCardletFooter().stream().anyMatch(a->a.getPosition().equals(position)))
                        throw new CustomParameterizedException("Bad cardlet footer position. It's exist",position.toString());

                    cardletFooter = new CardletFooter();
                    cardletFooter.setCardlet(cardlet);
                } else {
                    cardletFooter = cardletFooterRepository.findOne(cardletFoterVM.getId());
                    if (cardletFooter == null || !cardlet.equals(cardletFooter.getCardlet()))
                        throw new CustomParameterizedException("Bad cardlet footer ID",cardletFoterVM.getId().toString());
                }
                if (cardletFooters.containsKey(position)) {
                    throw new CustomParameterizedException("Bad cardlet footer position",position.toString());
                }
                cardletFoterVM.mapToCardletFooter(cardletFooter);
                cardletFooter = cardletFooterRepository.save(cardletFooter);
                cardletFooters.put(position,cardletFooter);
            }
            cardlet.setCardletFooter(cardletFooters.entrySet().stream().map(en->en.getValue()).collect(Collectors.toSet()));

        }
        return createCardletViewVM(cardlet);
    }

    /**
     *  id we have tmp file we must rename it in AWS bucket  before save
     * @param cardlet cardlet for generate path
     * @param url source url of file
     * @param from tempalate for source file
     * @param to name for destination file
     * @return renamed file if did, or equivalent to input
     */
    private String renameIfTmp(Cardlet cardlet,String url, String from, String to) {
        String result = url;
        if (url!=null && url.contains(from)) {
            String source;
            try {
                source = new URL(url).getPath();
                source = source.substring(1); //remove first "/"
            } catch (MalformedURLException e) {
                throw new CustomParameterizedException(e.getMessage());
            }
            String dest = getFileNameInBucket(to,null,cardlet)
                +source.substring(source.lastIndexOf("."));//save extension
            result = awss3BucketService.renameFile(source, dest).toString();
            //remove parameters
            if (result.contains("?")) result=result.substring(0,result.indexOf("?"));
        }
        return result;
    }

    /**
     * upload files to AWS
     * @param file gotten file
     * @param path pathKey to save
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
     * @param file gotten file
     * @return JSONObjectd with new path
     */
    public JSONObject uploadPhoto(MultipartFile file, Long cardletId) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_PHOTO_TMP, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_PHOTO);
    }

    /**
     * upload background image to AWS
     * @param file gotten file
     * @return JSONObjectd with new path
     */
    public JSONObject uploadBackgroundImage(MultipartFile file, Long cardletId) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_BACKIMAGE_TMP, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_BACKIMAGE);
    }

    /**
     * upload logo for link to AWS
     * @param file gotten file
     * @return JSONObjectd with new path
     */
    public JSONObject uploadLinkLogo(MultipartFile file, Long cardletId, int index) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_LINKLOGO_TMP + index, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_LINKLOGO);
    }

    /**
     * generate path begin in bucket
     * @param cardlet
     * @return path
     */
    private String getStartFolder(Cardlet cardlet) {
        return "accounts/" + cardlet.getUser().getId() + "/" + cardlet.getId() + "/";
    }

    /**
     * get full name for file
     * @param name - file name to save
     * @param file uploaded file
     * @param cardlet
     * @return path
     */
    private String getFileNameInBucket(String name, MultipartFile file, Cardlet cardlet) {
        name = getStartFolder(cardlet) + name;
        if (file!=null){
            String originalFilename = file.getOriginalFilename();
            if (originalFilename.contains(".")) {
                name += originalFilename.substring(originalFilename.lastIndexOf("."));
            }
        }
        return name;
    }

    /**
     * validate cardlet
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
     * @param cardlet
     */
    public void delete(Cardlet cardlet) {
        if (cardlet.getCardletHeader() != null) {
            deleteFile(cardlet.getCardletHeader().getLogo());
            deleteFile(cardlet.getCardletHeader().getPhoto());
        }
        if (cardlet.getCardletBackground() != null) {
            deleteFile(cardlet.getCardletBackground().getImage());
        }
        if (cardlet.getCardletFooter() != null) {
            for (CardletFooter ent :
                cardlet.getCardletFooter()) {
                deleteFile(ent.getUrl());
            }
        }

    }

    /**
     * delete file
     * @param path
     */
    private void deleteFile(String path) {
        awss3BucketService.deleteFile(path);
    }

    /**
     * get backgrounds list in resource
     * @return files list
     */
    public Collection<String> getBackground(){
        return getFilePaths(pathToBannerImages);
    }

    /**
     * get images list in resource
     * @return files list
     */
    public Collection<String> getLinkImages(){
        return getFilePaths(pathToLinkImages);
    }

    /**
     *  get files list
     * @param sourcePath resource path
     * @return files list
     */
    private List<String> getFilePaths(String sourcePath){
        List<String> files = new ArrayList<>();
        Resource resource = applicationContext.getResource(sourcePath);
        if (resource.exists()) {
            try {
                File fileRoot= resource.getFile();
                if (fileRoot.isDirectory()) {
                    //add files only from root
                    for (File f:fileRoot.listFiles()) {
                        if (!f.isDirectory()){
                            files.add(sourcePath+"\\"+f.getName());
                        }
                    }
                } else {
                    files.add(fileRoot.getPath());
                }
            } catch (IOException e) {
                log.error("Cant't read resources path",e);
            }
        }
        return files;
    }


}
