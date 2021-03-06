package itomy.sigterra.web.rest;

import io.swagger.annotations.ApiParam;
import itomy.sigterra.domain.Event;
import itomy.sigterra.domain.TopDomain;
import itomy.sigterra.service.AnalyticService;
import itomy.sigterra.service.dto.RecentDTO;
import itomy.sigterra.service.dto.TopDTO;
import itomy.sigterra.service.exception.ResponseErrorException;
import itomy.sigterra.web.rest.util.PaginationUtil;
import itomy.sigterra.web.rest.vm.AnalyticStatVM;
import itomy.sigterra.web.rest.vm.AnalyticVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static itomy.sigterra.web.rest.util.ResponseUtil.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api/analytic")
@SuppressWarnings("unused")
public class AnalyticResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticResource.class);

    private static final String PERIOD_IS_REQUIRED_MESSAGE = "Period is required";
    private static final String TYPE_IS_REQUIRED_MESSAGE = "Type is required";

    @Inject
    private AnalyticService analyticService;

    @GetMapping("item/pdf/stats/{itemId:\\d+}")
    public ResponseEntity getStatsForPdf(@PathVariable("itemId") Long itemId) {
        log.debug("REST request to get stat for pdf with itemId = {}", itemId);
        try {
            AnalyticStatVM pdfStats = new AnalyticStatVM(analyticService.getPdfStats(itemId));
            return ResponseEntity.ok(pdfStats);
        } catch (ResponseErrorException rex) {
            return errorResponse(rex.getHttpStatus(), rex.getMessage());
        }
    }

    @GetMapping("/stat/{cardletId:\\d+}")
    public ResponseEntity getStatsForCardlet(@PathVariable("cardletId") Long cardletId,
                                             @RequestParam String period) {
        log.debug("REST request to get stat for cardlets with id = {}, peroid = {}", cardletId, period);
        if (period == null || period.isEmpty()) {
            return errorResponse(BAD_REQUEST, PERIOD_IS_REQUIRED_MESSAGE);
        }
        try {
            AnalyticStatVM vm = new AnalyticStatVM(analyticService.getStats(cardletId, period));
            return ResponseEntity.ok(vm);
        } catch (ResponseErrorException rex) {
            return errorResponse(rex.getHttpStatus(), rex.getMessage());
        }
    }

    @GetMapping("/stat")
    public ResponseEntity getStatsForAllCardlets(@RequestParam String period) {
        log.debug("REST request to get stat for cardlets, peroid = {}", period);
        if (period == null || period.isEmpty()) {
            return errorResponse(BAD_REQUEST, PERIOD_IS_REQUIRED_MESSAGE);
        }
        AnalyticStatVM vm = new AnalyticStatVM(analyticService.getStats(period));
        return ResponseEntity.ok(vm);
    }

    @GetMapping("/top/{cardletId:\\d+}")
    public ResponseEntity<List<TopDTO>> getTopForCardlet(@PathVariable("cardletId") Long cardletId,
                                                         @RequestParam String period,
                                                         @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get top for cardlet with id = {}, peroid = {}", cardletId, period);

        if (period == null || period.isEmpty()) {
            return errorResponse(BAD_REQUEST, PERIOD_IS_REQUIRED_MESSAGE);
        }
        try {
            Page<TopDomain> domains = analyticService.getTop(cardletId, period, pageable);
            List<TopDTO> dtos = domains.getContent().stream().map(TopDTO::new).collect(Collectors.toList());
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(domains, "/api/analytic/top");

            return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
        } catch (ResponseErrorException rex) {
            return errorResponse(rex.getHttpStatus(), rex.getMessage());
        }
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopDTO>> getTopForAllCardlets(@RequestParam String period,
                                                             @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get top for cardlets, peroid = {}", period);

        if (period == null || period.isEmpty()) {
            return errorResponse(BAD_REQUEST, PERIOD_IS_REQUIRED_MESSAGE);
        }
        Page<TopDomain> domains = analyticService.getTop(period, pageable);
        List<TopDTO> dtos = domains.getContent().stream().map(TopDTO::new).collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(domains, "/api/analytic/top");

        return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
    }

    @GetMapping("/recent/{cardletId:\\d+}")
    public ResponseEntity<List<RecentDTO>> getRecentForCardlet(@PathVariable("cardletId") Long cardletId,
                                                               @RequestParam String type,
                                                               @RequestHeader(value = "X-TimeZone", required = false) String timeZine,
                                                               @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get recent for cardlet with id = {}, type = {}", cardletId, type);

        if (type == null || type.isEmpty()) {
            return errorResponse(BAD_REQUEST, TYPE_IS_REQUIRED_MESSAGE);
        }
        try {
            Page<Event> domains = analyticService.getRecent(cardletId, type, pageable);
            List<RecentDTO> dtos = domains.getContent().stream().map(e -> new RecentDTO(e, timeZine)).collect(Collectors.toList());
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(domains, "/api/analytic/recent");

            return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
        } catch (ResponseErrorException rex) {
            return errorResponse(rex.getHttpStatus(), rex.getMessage());
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<RecentDTO>> getRecentForAllCardlets(@RequestParam String type,
                                                                   @RequestHeader(value = "X-TimeZone", required = false) String timeZine,
                                                                   @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get recent for cardlets, type = {}", type);

        if (type == null || type.isEmpty()) {
            return errorResponse(BAD_REQUEST, TYPE_IS_REQUIRED_MESSAGE);
        }
        Page<Event> domains = analyticService.getRecent(type, pageable);
        List<RecentDTO> dtos = domains.getContent().stream().map(e -> new RecentDTO(e, timeZine)).collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(domains, "/api/analytic/recent");

        return new ResponseEntity<>(dtos, headers, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<AnalyticVM> getRecentForAllCardlets(@RequestHeader(value = "X-TimeZone", required = false) String timeZine) {
        log.debug("REST request to get analytic");

        AnalyticVM analyticVM = analyticService.getAnalytic(timeZine);
        return ResponseEntity.ok(analyticVM);
    }

    @GetMapping("/{cardletId:\\d+}")
    public ResponseEntity<AnalyticVM> getRecentForAllCardlets(@PathVariable("cardletId") Long cardletId,
                                                              @RequestHeader(value = "X-TimeZone", required = false) String timeZine) {
        log.debug("REST request to get analytic");
        try {
            AnalyticVM analyticVM = analyticService.getAnalytic(cardletId, timeZine);
            return ResponseEntity.ok(analyticVM);
        } catch (ResponseErrorException rex) {
            return errorResponse(rex.getHttpStatus(), rex.getMessage());
        }
    }
}
