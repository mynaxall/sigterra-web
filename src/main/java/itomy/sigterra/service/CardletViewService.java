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
import itomy.sigterra.web.rest.errors.CustomParameterizedException;
import itomy.sigterra.web.rest.vm.CardletBackgroundVM;
import itomy.sigterra.web.rest.vm.CardletFooterVM;
import itomy.sigterra.web.rest.vm.CardletHeaderVM;
import itomy.sigterra.web.rest.vm.CardletViewRequestResponseVM;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
  work with Cartdlet views: header,background, footers
 */

@Service
@Transactional
public class CardletViewService {
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

    public CardletViewRequestResponseVM getCardletView(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(cardletId);
        CardletViewRequestResponseVM cardletView = null;
        if (cardlet != null) cardletView = createCardletViewVM(cardlet);
        return cardletView;
    }


    private CardletViewRequestResponseVM createCardletViewVM(Cardlet cardlet) {
        CardletViewRequestResponseVM cardletView = new CardletViewRequestResponseVM();
        cardletView.setCardletId(cardlet.getId());
        CardletHeaderVM cardletHeaderVM = null;
        CardletHeader cardletHeader = cardlet.getCardletHeader();
        if (cardletHeader != null) {
            cardletHeaderVM = new CardletHeaderVM(
                cardletHeader.getId(),
                cardletHeader.getCtaButtonColor(),
                cardletHeader.getCtaText(),
                cardletHeader.getLogo(),
                cardletHeader.getPhoto(),
                cardletHeader.getName(),
                cardletHeader.getTitle(),
                cardletHeader.getCompany(),
                cardletHeader.getPhone(),
                cardletHeader.getEmail()
            );
        }
        cardletView.setHeaders(cardletHeaderVM);

        CardletBackgroundVM cardletBackgroundVM = null;
        CardletBackground cardletBackground = cardlet.getCardletBackground();

        if (cardletBackground != null) {
            cardletBackgroundVM = new CardletBackgroundVM(
                cardletBackground.getId(),
                cardletBackground.getImage(),
                cardletBackground.isTextColor(),
                cardletBackground.getCaptionText()
            );
        }
        cardletView.setBackground(cardletBackgroundVM);

        List<CardletFooterVM> footers = new ArrayList<>();
        for (CardletFooter cardletFooter : cardlet.getCardletFooter()) {
            CardletFooterVM cardletFooterVM = new CardletFooterVM(
                cardletFooter.getId(),
                cardletFooter.getIndex(),
                cardletFooter.getName(),
                cardletFooter.getUrl(),
                cardletFooter.getLogo()
            );
            footers.add(cardletFooterVM);
        }
        cardletView.setFooters(footers);

        return cardletView;
    }

    @Transactional
    public CardletViewRequestResponseVM saveCardletView(CardletViewRequestResponseVM cardletView) {
        Cardlet cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(cardletView.getCardletId());
        if (cardlet == null) {
            throw new CustomParameterizedException("Cardlet ID not found for user");
        } else {
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
                cardletHeader.setCtaButtonColor(cardletHeaderVM.getCtaColor());
                cardletHeader.setCtaText(cardletHeaderVM.getText());
                String logoURL = renameIfTmp(cardlet,cardletHeaderVM.getLogoUrl(), FILE_NAME_LOGO_TMP, FILE_NAME_LOGO_PERSIST);
                cardletHeader.setLogo(logoURL);
                String photoURL = renameIfTmp(cardlet,cardletHeaderVM.getPhotoUrl(), FILE_NAME_LOGO_TMP, FILE_NAME_PHOTO_PERSIST);
                cardletHeader.setPhoto(photoURL);
                cardletHeader.setName(cardletHeaderVM.getName());
                cardletHeader.setTitle(cardletHeaderVM.getTitle());
                cardletHeader.setCompany(cardletHeaderVM.getCompany());
                cardletHeader.setPhone(cardletHeaderVM.getPhone());
                cardletHeader.setEmail(cardletHeaderVM.getEmail());
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
                String imageURL = renameIfTmp(cardlet,cardletBackgroundVM.getImageUrl(), FILE_NAME_BACKIMAGE_TMP, FILE_NAME_BACKIMAGE_PERSIST);
                cardletBackground.setImage(imageURL);
                cardletBackground.setCaptionText(cardletBackgroundVM.getText());
                cardletBackground.setTextColor(cardletBackgroundVM.isTextColor());
                cardletBackground = cardletBackgroundRepository.save(cardletBackground);
                cardlet.setCardletBackground(cardletBackground);
            }

            List<CardletFooterVM> cardletFooterVMList = cardletView.getFooters();
            if (cardletFooterVMList != null) {
                Set<CardletFooter> cardletFooters = new HashSet<>();
                for (CardletFooterVM cardletFoterVM : cardletFooterVMList) {
                    CardletFooter cardletFooter;
                    if (cardletFoterVM.getId() == null) {
                        cardletFooter = new CardletFooter();
                        cardletFooter.setCardlet(cardlet);
                    } else {
                        cardletFooter = cardletFooterRepository.findOne(cardletFoterVM.getId());
                        if (cardletFooter == null || !cardlet.equals(cardletFooter.getCardlet()))
                            throw new CustomParameterizedException("Bad cardlet header ID");
                    }
                    CardletFooterIndex index = cardletFoterVM.getIndex();
                    cardletFooter.setIndex(index);
                    String logoUrl = renameIfTmp(cardlet,cardletFoterVM.getLogoUrl(), FILE_NAME_LINKLOGO_TMP + index, FILE_NAME_LINKLOGO_PERSIST + index);
                    cardletFooter.setLogo(logoUrl);
                    cardletFooter.setName(cardletFoterVM.getName());
                    cardletFooter.setUrl(cardletFoterVM.getUrl());
                    cardletFooter = cardletFooterRepository.save(cardletFooter);
                    cardletFooters.add(cardletFooter);
                }
                cardlet.setCardletFooter(cardletFooters);
            }

        }
        return createCardletViewVM(cardlet);
    }

    private String renameIfTmp(Cardlet cardlet,String url, String from, String to) {
        String result = url;
        if (url.contains(from)) {
            String source = getFileNameInBucket(from,null,cardlet);
            String dest = getFileNameInBucket(to,null,cardlet)
                +source.substring(source.lastIndexOf("."));//save extension
            awss3BucketService.renameFile(url, dest);
            result = dest;
        }
        return result;
    }

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


    public JSONObject uploadLogo(MultipartFile file, Long cardletId) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_LOGO_TMP, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_LOGO);
    }

    public JSONObject uploadPhoto(MultipartFile file, Long cardletId) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_PHOTO_TMP, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_PHOTO);
    }

    public JSONObject uploadBackgroundImage(MultipartFile file, Long cardletId) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_BACKIMAGE_TMP, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_BACKIMAGE);
    }

    public JSONObject uploadLinkLogo(MultipartFile file, Long cardletId, int index) throws JSONException {
        Cardlet cardlet = validateAndGetCardlet(cardletId);
        String path = getFileNameInBucket(FILE_NAME_LINKLOGO_TMP + index, file, cardlet);
        return uploadFile(file, path, MAX_SIZE_LINKLOGO);
    }

    private String getStartFolder(Cardlet cardlet) {
        return "accounts/" + cardlet.getUser().getId() + "/" + cardlet.getId() + "/";
    }

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

    private Cardlet validateAndGetCardlet(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(cardletId);
        if (cardlet == null) throw new CustomParameterizedException("Bad cardlet ID");
        return cardlet;
    }


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

    private void deleteFile(String path) {
        awss3BucketService.deleteFile(path);
    }
}
