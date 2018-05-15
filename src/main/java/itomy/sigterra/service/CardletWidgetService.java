package itomy.sigterra.service;

import itomy.sigterra.domain.*;
import itomy.sigterra.repository.CardletContentLibraryWidgetRepository;
import itomy.sigterra.repository.CardletQuickBitesWidgetRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.CardletTestimonialWidgetRepository;
import itomy.sigterra.service.mapper.CardletWidgetMapper;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
import itomy.sigterra.web.rest.vm.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardletWidgetService {

    private final static String ENTITY_CONTENT_LIBRARY = "cardlet_content_library_widget";
    private final static Long LIMIT_SIZE = 50L * 1024L * 1024L; //50mb

    @Inject
    private CardletTestimonialWidgetRepository cardletTestimonialWidgetRepository;

    @Inject
    private CardletQuickBitesWidgetRepository cardletQuickBitesWidgetRepository;

    @Inject
    private CardletContentLibraryWidgetRepository cardletContentLibraryWidgetRepository;

    @Inject
    private AWSS3BucketService awss3BucketService;

    @Inject
    private CardletRepository cardletRepository;
    @Inject
    private UserService userService;

    public CardletWidgetResponseVM getCardletWidgetesByCardletId(Long cardletId) {
        Cardlet cardlet = cardletRepository.findOne(cardletId);
        if (cardlet == null) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "A new cardletTestimonialWidget has't cardlet ID");
        }

        List<CardletTestimonialWidget> testimonialWidget = cardletTestimonialWidgetRepository.findAllByCardletId(cardletId);
        List<CardletQuickBitesWidget> quickBitesWidgets = cardletQuickBitesWidgetRepository.findAllByCardletId(cardletId);
        List<CardletContentLibraryWidget> contentLibraryWidget = cardletContentLibraryWidgetRepository.findAllByCardletId(cardletId);

        CardletWidgetResponseVM responseVM = new CardletWidgetResponseVM();
        responseVM.setCardletId(cardletId);

        List<CardletTestimonialWidgetResponseVM> testimonialWidgetList = testimonialWidget
            .stream()
            .map(CardletTestimonialWidgetResponseVM::new)
            .collect(Collectors.toList());
        responseVM.setCardletTestimonialWidget(testimonialWidgetList);

        List<CardletQuickBitesWidgetResponseVM> quickBitesWidgetsList = quickBitesWidgets
            .stream()
            .map(CardletQuickBitesWidgetResponseVM::new)
            .collect(Collectors.toList());
        responseVM.setCardletQuickBitesWidget(quickBitesWidgetsList);

        List<CardletContentLibraryWidgetResponseVM> contentLibraryWidgetList = contentLibraryWidget
            .stream()
            .map(CardletContentLibraryWidgetResponseVM::new)
            .collect(Collectors.toList());
        responseVM.setCardletContentLibraryWidget(contentLibraryWidgetList);

        return responseVM;
    }

    @Transactional
    public CardletWidgetResponseVM saveCardletlWidgetes(CardletWidgetRequestVM widget) {
        Cardlet cardlet = cardletRepository.findOne(widget.getCardletId());
        if (cardlet == null) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "A new cardletTestimonialWidget has't cardlet ID");
        }
        widget.setCardletId(cardlet.getId());

        if (widget.getCardletTestimonialWidget() != null && !widget.getCardletTestimonialWidget().isEmpty()) {
            for (CardletTestimonialWidgetRequestVM testimonial : widget.getCardletTestimonialWidget()) {
                CardletTestimonialWidget cardletTestimonialWidget = CardletWidgetMapper.mapToEntity(testimonial, cardlet);

                saveCardletTestimonialWidget(cardletTestimonialWidget);
            }
        }

        if (widget.getCardletQuickBitesWidget() != null && !widget.getCardletQuickBitesWidget().isEmpty()) {
            for (CardletQuickBitesWidgetRequestVM quickBites : widget.getCardletQuickBitesWidget()) {
                CardletQuickBitesWidget cardletQuickBitesWidget = CardletWidgetMapper.mapToEntity(quickBites, cardlet);

                saveCardletQuickBitesWidget(cardletQuickBitesWidget);
            }
        }

        if (widget.getCardletContentLibraryWidget() != null && !widget.getCardletContentLibraryWidget().isEmpty()) {
            for (CardletContentLibraryWidgetRequestVM contentLibrary : widget.getCardletContentLibraryWidget()) {
                CardletContentLibraryWidget cardletContentLibraryWidget = CardletWidgetMapper.mapToEntity(contentLibrary, cardlet);

                saveCardletContentLibraryWidget(cardletContentLibraryWidget);
            }
        }


        return getCardletWidgetResponseVM(cardlet.getId());
    }

    public CardletWidgetResponseVM getCardletWidgetResponseVM(Long cardletId) {
        List<CardletTestimonialWidget> testimonialWidget = cardletTestimonialWidgetRepository.findAllByCardletId(cardletId);
        List<CardletQuickBitesWidget> quickBitesWidgets = cardletQuickBitesWidgetRepository.findAllByCardletId(cardletId);
        List<CardletContentLibraryWidget> contentLibraryWidget = cardletContentLibraryWidgetRepository.findAllByCardletId(cardletId);
        CardletWidgetResponseVM responseVM = new CardletWidgetResponseVM();
        responseVM.setCardletId(cardletId);

        List<CardletTestimonialWidgetResponseVM> testimonialWidgetList = testimonialWidget
            .stream()
            .map(CardletTestimonialWidgetResponseVM::new)
            .collect(Collectors.toList());
        responseVM.setCardletTestimonialWidget(testimonialWidgetList);

        List<CardletQuickBitesWidgetResponseVM> quickBitesWidgetsList = quickBitesWidgets
            .stream()
            .map(CardletQuickBitesWidgetResponseVM::new)
            .collect(Collectors.toList());
        responseVM.setCardletQuickBitesWidget(quickBitesWidgetsList);

        List<CardletContentLibraryWidgetResponseVM> contentLibraryWidgetList = contentLibraryWidget
            .stream()
            .map(CardletContentLibraryWidgetResponseVM::new)
            .collect(Collectors.toList());
        responseVM.setCardletContentLibraryWidget(contentLibraryWidgetList);

        return responseVM;
    }

    private CardletContentLibraryWidget saveCardletContentLibraryWidget(CardletContentLibraryWidget widget) {
        if (widget.getId() == null) {
            return cardletContentLibraryWidgetRepository.save(widget);
        }
        String coverImageUrl = widget.getCoverImageUrl();
        String uploadFileUrl = widget.getUploadFileUrl();

        if (!isWebLink(coverImageUrl)) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY,
                "Incorrect cover image link for content library widget: " + coverImageUrl);
        }

        if (uploadFileUrl != null && !isWebLink(uploadFileUrl)) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY,
                "Incorrect upload file link for content library widget: " + uploadFileUrl);
        }

        CardletContentLibraryWidget oldWidget = cardletContentLibraryWidgetRepository.findOne(widget.getId());

        if (oldWidget == null) {
            return cardletContentLibraryWidgetRepository.save(widget);
        }

        if (!widget.getTitle().equals(oldWidget.getTitle())) {
            oldWidget.setTitle(widget.getTitle());
        }
        if (!widget.getCoverImageUrl().equals(oldWidget.getCoverImageUrl())) {
            deleteCoverImage(oldWidget);
            oldWidget.setCoverImageUrl(widget.getCoverImageUrl());
        }
        if (!widget.getUploadFileUrl().equals(oldWidget.getUploadFileUrl())) {
            deleteWidgetUploadFile(oldWidget);
            oldWidget.setUploadFileUrl(widget.getUploadFileUrl());
        }

        return cardletContentLibraryWidgetRepository.save(oldWidget);
    }

    private CardletQuickBitesWidget saveCardletQuickBitesWidget(CardletQuickBitesWidget widget) {
        if (widget.getId() == null) {
            return cardletQuickBitesWidgetRepository.save(widget);
        }

        CardletQuickBitesWidget oldWidget = cardletQuickBitesWidgetRepository.findOne(widget.getId());

        if (oldWidget == null) {
            return cardletQuickBitesWidgetRepository.save(widget);
        }

        if (!widget.getTitle().equals(oldWidget.getTitle())) {
            oldWidget.setTitle(widget.getTitle());
        }
        if (!widget.getDescription().equals(oldWidget.getDescription())) {
            oldWidget.setDescription(widget.getDescription());
        }

        return cardletQuickBitesWidgetRepository.save(oldWidget);
    }

    private CardletTestimonialWidget saveCardletTestimonialWidget(CardletTestimonialWidget widget) {
        if (widget.getId() == null) {
            return cardletTestimonialWidgetRepository.save(widget);
        }

        CardletTestimonialWidget oldWidget = cardletTestimonialWidgetRepository.findOne(widget.getId());

        if (oldWidget == null) {
            return cardletTestimonialWidgetRepository.save(widget);
        }

        if (!widget.getName().equals(oldWidget.getName())) {
            oldWidget.setName(widget.getName());
        }
        if (!widget.getCoName().equals(oldWidget.getCoName())) {
            oldWidget.setCoName(widget.getCoName());
        }
        if (!widget.getDescription().equals(oldWidget.getDescription())) {
            oldWidget.setDescription(widget.getDescription());
        }
        if (!widget.getDesignation().equals(oldWidget.getDesignation())) {
            oldWidget.setDesignation(widget.getDesignation());
        }

        return cardletTestimonialWidgetRepository.save(oldWidget);
    }

    @Transactional
    public CardletImagesContentLibraryResponseVM uploadImages(CardletImagesContentLibraryResponseVM widget, Long cardletId,
                                                              MultipartFile coverImage, MultipartFile uploadFile) {


        URI coverUrl = uploadCoverImage(coverImage, cardletId);
        if (coverUrl == null) {
            widget.setCoverImageUrl(null);
        } else {
            widget.setCoverImageUrl(coverUrl.toString());
        }

        String uploadUrl = setUploadFile(widget, uploadFile, cardletId);
        widget.setUploadFileUrl(uploadUrl);
        return widget;
    }

    /**
     * Set upload file url to widget, field upload url
     *
     * @param contentLibraryWidget {@link CardletContentLibraryWidget}
     * @param uploadFile           {@link MultipartFile}
     * @param cardletId            {@link MultipartFile}
     * @return {@link String}
     */
    private String setUploadFile(CardletImagesContentLibraryResponseVM contentLibraryWidget,
                                 MultipartFile uploadFile, Long cardletId) {

        String uploadFileUrl = contentLibraryWidget.getUploadFileUrl();
        if (uploadFileUrl != null) {
            if (isWebLink(uploadFileUrl)) {
                return uploadFileUrl;
            } else {
                throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "Incorrect link");
            }
        }
        if (uploadFile != null) {
            URI uri = uploadContentLibraryFile(uploadFile, cardletId);
            return uri.toString();
        }
        return contentLibraryWidget.getUploadFileUrl();
    }

    /**
     * Upload file for widget content type, field coverImage
     *
     * @param coverImage {@link MultipartFile} upload file
     * @param cardletId  {@link Long} cardlet id
     * @return {@link URI}
     */
    private URI uploadCoverImage(MultipartFile coverImage, Long cardletId) {
        final List<String> coverImageMimeTypes = Arrays.asList("image/jpg", "image/jpeg", "image/png");

        if (coverImage == null) {
            return null;
        }

        checkUploadFile(coverImage, coverImageMimeTypes, LIMIT_SIZE);

        return awss3BucketService.uploadImage(coverImage, cardletId, "widget/image/cover/");
    }

    /**
     * Upload file for widget content type, field upload url
     *
     * @param file      {@link MultipartFile} upload file
     * @param cardletId {@link Long} cardlet id
     * @return {@link URI}
     */
    private URI uploadContentLibraryFile(MultipartFile file, Long cardletId) {
        final List<String> coverImageMimeTypes = Arrays.asList("image/png", "image/jpeg", "application/pdf");

        checkUploadFile(file, coverImageMimeTypes, LIMIT_SIZE);

        return awss3BucketService.uploadImage(file, cardletId, "widget/image/file/");
    }

    /**
     * Check upload file by size and HTTP mime type
     *
     * @param file                {@link MultipartFile}  upload file
     * @param coverImageMimeTypes {@link List<String>} collection mime types
     * @param limitSize           {@link Long} file size limit
     */
    private void checkUploadFile(MultipartFile file, List<String> coverImageMimeTypes, long limitSize) {
        if (file.getSize() > limitSize) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "File size is not available");
        }
        if (coverImageMimeTypes.stream().noneMatch(mimeType -> Objects.equals(mimeType, file.getContentType()))) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "Incorrect file type!");
        }
    }

    /**
     * Check link by regex. If link is correct, well be returned true
     *
     * @param url- {@link String}
     */
    public boolean isWebLink(String url) {
        String regex = "(www|http:|https:)+[^\\s]+[\\w]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    /**
     * Delete all data by contentLibraryId from awss3
     */
    public void deleteContentLibrary(Long contentLibraryId) {
        User currentUser = userService.getUserWithAuthorities();

        CardletContentLibraryWidget widget = cardletContentLibraryWidgetRepository.findOne(contentLibraryId);
        User cardletOwner = widget.getCardlet().getUser();

        if (currentUser.equals(cardletOwner)) {
            if (widget == null) {
                throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "Incorrect widget ID");
            }
            String coverImageUrl = widget.getCoverImageUrl();
            if (coverImageUrl != null) {
                deleteCoverImage(widget);
            }

            String uploadFileUrl = widget.getUploadFileUrl();
            if (uploadFileUrl != null) {
                deleteWidgetUploadFile(widget);
            }
            //} else {
            //   throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY,
            //       "Current user has't access for deleting content library widget with ID: " + contentLibraryId);
        }
    }

    private void deleteWidgetUploadFile(CardletContentLibraryWidget widget) {
        String uploadFileUrl = widget.getUploadFileUrl();
        if (uploadFileUrl != null) {
            try {
                awss3BucketService.deleteFile(uploadFileUrl.substring(uploadFileUrl.indexOf("widget"), uploadFileUrl.indexOf("?")));
            } catch (Exception e) {
                //NOP
            }
        }
    }

    private void deleteCoverImage(CardletContentLibraryWidget widget) {
        String coverImageUrl = widget.getCoverImageUrl();

        if (coverImageUrl != null) {
            try {
                awss3BucketService.deleteFile(coverImageUrl.substring(coverImageUrl.indexOf("widget"), coverImageUrl.indexOf("?")));
            } catch (Exception e) {
                //NOP
            }
        }
    }
}



