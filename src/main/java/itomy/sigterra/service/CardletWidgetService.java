package itomy.sigterra.service;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletContentLibraryWidget;
import itomy.sigterra.domain.CardletQuickBitesWidget;
import itomy.sigterra.domain.CardletTestimonialWidget;
import itomy.sigterra.repository.CardletContentLibraryWidgetRepository;
import itomy.sigterra.repository.CardletQuickBitesWidgetRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.CardletTestimonialWidgetRepository;
import itomy.sigterra.service.mapper.CardletWidgetMapper;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
import itomy.sigterra.web.rest.vm.CardletContentLibraryWidgetResponseVM;
import itomy.sigterra.web.rest.vm.CardletImagesContentLibraryResponseVM;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetResponseVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetResponseVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public CardletTestimonialWidgetResponseVM findTestimonialWidget(Long id, Long cardletId) {
        CardletTestimonialWidget cardletTestimonialWidget = cardletTestimonialWidgetRepository.findByIdAndCardletId(id, cardletId);
        if (cardletTestimonialWidget == null) {
            throw new BadRequestAlertException("cardlet_testimonial_widget",
                "Curent cardletTestimonialWidget does not exist. Cardlet id: " + cardletId);
        }

        return new CardletTestimonialWidgetResponseVM(cardletTestimonialWidget);
    }

    public Page<CardletQuickBitesWidgetResponseVM> findAllCardletQuickBitesWidgetRepository(Long cardletId, Pageable pageable) {
        return cardletQuickBitesWidgetRepository.findAllByCardletId(cardletId, pageable).map(CardletQuickBitesWidgetResponseVM::new);
    }

    @Transactional
    public CardletTestimonialWidgetResponseVM saveCardletTestimonialWidget(CardletTestimonialWidgetRequestVM testimonialWidgetRequestVM) {
        Cardlet cardlet = cardletRepository.findOne(testimonialWidgetRequestVM.getCardletId());
        if (cardlet == null) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "A new cardletTestimonialWidget has't cardlet ID");
        }

        CardletTestimonialWidget cardletTestimonialWidget = CardletWidgetMapper.mapToEntity(testimonialWidgetRequestVM, cardlet);

        Long widgetId = testimonialWidgetRequestVM.getId();
        CardletTestimonialWidget cardletWidget = cardletTestimonialWidgetRepository.findOne(widgetId);
        if (cardletWidget != null) {
            cardletTestimonialWidget.setId(widgetId);
        }

        return new CardletTestimonialWidgetResponseVM(cardletTestimonialWidgetRepository.save(cardletTestimonialWidget));
    }

    public Page<CardletTestimonialWidgetResponseVM> findAllByCardletId(Long cardletId, Pageable pageable) {
        return cardletTestimonialWidgetRepository.findAllByCardletId(cardletId, pageable)
            .map(CardletTestimonialWidgetResponseVM::new);
    }

    @Transactional
    public CardletQuickBitesWidgetResponseVM saveQuickBitesWidget(CardletQuickBitesWidgetRequestVM cardletQuickBitesWidgetRequestVM) {
        Cardlet cardlet = cardletRepository.findOne(cardletQuickBitesWidgetRequestVM.getCardletId());
        if (cardlet == null) {
            throw new BadRequestAlertException("cardlet_quick_bites", "A new quickBitesWidget has't cardlet ID");
        }

        CardletQuickBitesWidget cardletQuickBitesWidget = CardletWidgetMapper.mapToEntity(cardletQuickBitesWidgetRequestVM, cardlet);

        Long widgetId = cardletQuickBitesWidgetRequestVM.getId();
        CardletQuickBitesWidget cardletWidget = cardletQuickBitesWidgetRepository.findOne(widgetId);
        if (cardletWidget != null) {
            cardletQuickBitesWidget.setId(widgetId);
        }

        return new CardletQuickBitesWidgetResponseVM(cardletQuickBitesWidgetRepository.save(cardletQuickBitesWidget));
    }

    public CardletQuickBitesWidgetResponseVM findCardletQuickBitesWidget(Long id, Long cardletId) {
        CardletQuickBitesWidget cardletQuickBitesWidget = cardletQuickBitesWidgetRepository.findByIdAndCardletId(id, cardletId);
        if (cardletQuickBitesWidget == null) {
            throw new BadRequestAlertException("cardlet_quick_bites_widget", "Curent cardletQuickBitesWidget does not exist. Cardlet id: " + cardletId);
        }

        return new CardletQuickBitesWidgetResponseVM(cardletQuickBitesWidget);
    }

    public Page<CardletContentLibraryWidgetResponseVM> findAllContentLibraryByCardletId(Long cardletId, Pageable pageable) {
        return cardletContentLibraryWidgetRepository.findAllByCardletId(cardletId, pageable)
            .map(CardletContentLibraryWidgetResponseVM::new);
    }

    public CardletContentLibraryWidget mergeCardletContentLibraryWidget(CardletContentLibraryWidget widget) {
        CardletContentLibraryWidget oldWidget = cardletContentLibraryWidgetRepository.findOne(widget.getId());
        if (oldWidget == null) {
            throw new BadRequestAlertException(ENTITY_CONTENT_LIBRARY, "Request has incorrect id");
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
        CardletContentLibraryWidget widget = cardletContentLibraryWidgetRepository.findOne(contentLibraryId);
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



