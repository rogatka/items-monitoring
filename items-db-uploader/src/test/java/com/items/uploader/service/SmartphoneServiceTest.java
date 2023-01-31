package com.items.uploader.service;

import com.items.uploader.mapper.SmartphoneMapper;
import com.items.uploader.model.Smartphone;
import com.items.uploader.model.dto.SmartphoneDto;
import com.items.uploader.repository.SmartphoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmartphoneServiceTest {

    @Mock
    private SmartphoneRepository smartphoneRepository;

    @Mock
    private SmartphoneMapper smartphoneMapper;

    @InjectMocks
    private SmartphoneService smartphoneService;

    @Test
    void shouldSaveItem() {
        // given
        Smartphone smartphone = Smartphone.builder().build();
        when(smartphoneRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Smartphone actual = smartphoneService.save(smartphone);

        // then
        assertEquals(smartphone, actual);
        verify(smartphoneRepository).save(actual);
    }

    @Test
    void shouldSaveItemDto() {
        // given
        SmartphoneDto smartphoneDto = new SmartphoneDto();
        Smartphone smartphone = Smartphone.builder().build();
        when(smartphoneMapper.map(smartphoneDto)).thenReturn(smartphone);
        when(smartphoneRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Smartphone actual = smartphoneService.save(smartphoneDto);

        // then
        assertEquals(smartphone, actual);
        verify(smartphoneMapper).map(smartphoneDto);
        verify(smartphoneRepository).save(actual);
    }

    @Test
    void shouldFindFirstByCodeOrderByCreatedAtDesc() {
        // given
        Smartphone expected = Smartphone.builder().build();
        String code = "123";
        when(smartphoneRepository.findFirstByCodeOrderByCreatedAtDesc(code)).thenReturn(Optional.of(expected));

        // when
        Optional<Smartphone> actual = smartphoneService.findFirstByCodeOrderByCreatedAtDesc(code);

        // then
        assertThat(actual)
                .isNotEmpty()
                .isEqualTo(expected);
    }

    @Test
    void shouldNotFindFirstByCodeOrderByCreatedAtDesc() {
        // given
        String code = "123";
        when(smartphoneRepository.findFirstByCodeOrderByCreatedAtDesc(code)).thenReturn(Optional.empty());

        // when-then
        assertThat(smartphoneService.findFirstByCodeOrderByCreatedAtDesc(code)).isEmpty();
    }
}