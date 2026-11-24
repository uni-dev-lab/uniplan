package org.unilab.uniplan.lector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.unilab.uniplan.common.dto.PageResponseDto;
import org.unilab.uniplan.exception.InvalidSortPropertyException;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@WebMvcTest(LectorController.class)
class LectorControllerTest {

    private static final String LECTORS_URL = "/lectors";
    private static final String SIZE_PARAM = "size";
    private static final String OVERSIZED_PAGE_SIZE = "5000";
    private static final String CREATED_FROM_PARAM = "createdFrom";
    private static final String INVALID_DATE = "2026-13-45";
    private static final String FIRST_NAME = "firstName";
    private static final String SORT_PARAM = "sort";
    private static final String SORT_VALUE = "password,asc";
    private static final String ID = "id";
    private static final String INVALID_FILTER_MESSAGE = "Invalid filter data!";
    private static final String INVALID_SORT_MESSAGE = "Invalid sort property!";
    private static final String MESSAGE_PATH = "$.message";
    private static final String INVALID_FIRST_NAME = "a".repeat(101);
    private static final String LATER_DATE = "2026-11-20";
    private static final String EARLIER_DATE = "2026-11-10";
    private static final String CREATED_TO_PARAM = "createdTo";
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final PageResponseDto<LectorResponseDto> EMPTY_PAGE =
        new PageResponseDto<>(0, MAX_PAGE_SIZE, 0, 0, List.of());


    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private LectorWebFacade lectorWebFacade;

    @Test
    void getAllLectors_shouldReturnBadRequest_whenSortPropertyNotAllowed() {
        when(lectorWebFacade.getAllLectors(any(), any()))
            .thenThrow(new InvalidSortPropertyException(""));

        final var result = mvc.get().uri(LECTORS_URL).param(SORT_PARAM, SORT_VALUE).exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
                          .bodyJson().extractingPath(MESSAGE_PATH).isEqualTo(INVALID_SORT_MESSAGE);
        verify(lectorWebFacade).getAllLectors(any(), any());
    }

    @Test
    void getAllLectors_shouldClampPageSize_whenSizeExceedsMax() {
        final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        when(lectorWebFacade.getAllLectors(any(), any())).thenReturn(EMPTY_PAGE);

        final var result =
            mvc.get().uri(LECTORS_URL).param(SIZE_PARAM, OVERSIZED_PAGE_SIZE).exchange();
        ;

        assertThat(result).hasStatusOk();
        verify(lectorWebFacade).getAllLectors(any(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(MAX_PAGE_SIZE);
    }

    @Test
    void getAllLectors_shouldReturnBadRequest_whenCreatedFromIsMalformed() {
        final var result =
            mvc.get().uri(LECTORS_URL).param(CREATED_FROM_PARAM, INVALID_DATE).exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
                          .bodyJson()
                          .extractingPath(MESSAGE_PATH)
                          .isEqualTo(INVALID_FILTER_MESSAGE);
        verifyNoInteractions(lectorWebFacade);
    }

    @Test
    void getAllLectors_shouldApplyDefaultPaging_whenNoParamsGiven() {
        final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        final var sort = Sort.by(Direction.ASC, FIRST_NAME, ID);
        when(lectorWebFacade.getAllLectors(any(), any())).thenReturn(EMPTY_PAGE);

        final var result = mvc.get().uri(LECTORS_URL).exchange();

        assertThat(result).hasStatusOk();
        verify(lectorWebFacade).getAllLectors(any(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(pageableCaptor.getValue().getSort()).isEqualTo(sort);
    }

    @Test
    void getAllLectors_shouldReturnBadRequest_whenFirstNameTooLong() {
        final var result =
            mvc.get().uri(LECTORS_URL).param(FIRST_NAME, INVALID_FIRST_NAME).exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
                          .bodyJson()
                          .extractingPath(MESSAGE_PATH)
                          .isEqualTo(INVALID_FILTER_MESSAGE);
        verifyNoInteractions(lectorWebFacade);
    }

    @Test
    void getAllLectors_shouldReturnBadRequest_whenCreatedFromIsAfterCreatedTo() {
        final var result = mvc.get().uri(LECTORS_URL)
                              .param(CREATED_FROM_PARAM, LATER_DATE)
                              .param(CREATED_TO_PARAM, EARLIER_DATE)
                              .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
                          .bodyJson()
                          .extractingPath(MESSAGE_PATH)
                          .isEqualTo(INVALID_FILTER_MESSAGE);
        verifyNoInteractions(lectorWebFacade);
    }
}