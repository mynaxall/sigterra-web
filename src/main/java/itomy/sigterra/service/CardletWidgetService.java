package itomy.sigterra.service;


import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.domain.CardletTestimonialWidget;
import itomy.sigterra.repository.CardletRepository;
import itomy.sigterra.repository.CardletTestimonialWidgetRepository;
import itomy.sigterra.service.mapper.CardletWidgetMapper;
import itomy.sigterra.web.rest.errors.BadRequestAlertException;
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
    private CardletRepository cardletRepository;

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

}
