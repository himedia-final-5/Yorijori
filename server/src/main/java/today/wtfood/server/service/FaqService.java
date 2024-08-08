package today.wtfood.server.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.wtfood.server.dto.faq.FaqDetail;
import today.wtfood.server.entity.Faq;
import today.wtfood.server.repository.FaqRepository;

@Service
@Transactional
public class FaqService {

    private final FaqRepository fr;

    public FaqService(FaqRepository fr) {
        this.fr = fr;
    }

    public void deleteInquiry(long id) {
        fr.deleteById(id);
    }

    public Faq insertFaq(Faq entity) {
        return fr.save(entity);
    }

    public Page<Faq> getFaqList(Pageable pageable) {
        return fr.findAll(pageable);
    }


    public FaqDetail getFaqView(long id) {
        return (FaqDetail) fr.findDetailById(id)
                .orElseThrow(() -> new RuntimeException("FAQ 없다 이것들아."));
    }
}
