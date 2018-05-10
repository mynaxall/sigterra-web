package itomy.sigterra.service;


import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletQuickBitesWidget;
import itomy.sigterra.domain.CardletTestimonialWidget;
import itomy.sigterra.repository.CardletQuickBitesWidgetRepository;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.CardletTestimonialWidgetRepository;
import itomy.sigterra.service.mapper.CardletWidgetMapper;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletQuickBitesWidgetResponseVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetRequestVM;
import itomy.sigterra.web.rest.vm.CardletTestimonialWidgetResponseVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class CardletWidgetService {

    @Inject
    private CardletTestimonialWidgetRepository cardletTestimonialWidgetRepository;

    @Inject
    private CardletQuickBitesWidgetRepository cardletQuickBitesWidgetRepository;

    @Inject
    private CardletRepository cardletRepository;

    public CardletTestimonialWidgetResponseVM findTestimonialWidget(Long id, Long cardletId) {
        CardletTestimonialWidget cardletTestimonialWidget = cardletTestimonialWidgetRepository.findByIdAndCardletId(cardletId, cardletId);
        return new CardletTestimonialWidgetResponseVM(cardletTestimonialWidget);
    }

    public Page<CardletQuickBitesWidgetResponseVM> findAllCardletQuickBitesWidgetRepository(Pageable pageable) {
        return cardletQuickBitesWidgetRepository.findAll(pageable).map(CardletQuickBitesWidgetResponseVM::new);
    }

    @Transactional
    public CardletTestimonialWidgetResponseVM saveCardletTestimonialWidget(CardletTestimonialWidgetRequestVM testimonialWidgetRequestVM) {
        Cardlet cardlet = cardletRepository.findOne(testimonialWidgetRequestVM.getCardletId());
        if (cardlet == null) {
            throw new BadRequestAlertException("cardlet_testimonial_widget", "A new cardletTestimonialWidget has't cardlet ID");
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
            throw new BadRequestAlertException("cardlet_quick_bites", "A new cardletTestimonialWidget has't cardlet ID");
        }

        CardletQuickBitesWidget cardletTestimonialWidget = CardletWidgetMapper.mapToEntity(cardletQuickBitesWidgetRequestVM, cardlet);

        Long widgetId = cardletQuickBitesWidgetRequestVM.getId();
        CardletQuickBitesWidget cardletWidget = cardletQuickBitesWidgetRepository.findOne(widgetId);
        if (cardletWidget != null) {
            cardletTestimonialWidget.setId(widgetId);
        }

        return new CardletQuickBitesWidgetResponseVM(cardletQuickBitesWidgetRepository.save(cardletTestimonialWidget));
    }

}
