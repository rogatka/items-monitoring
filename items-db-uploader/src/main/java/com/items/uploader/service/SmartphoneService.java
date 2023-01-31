package com.items.uploader.service;

import com.items.uploader.mapper.SmartphoneMapper;
import com.items.uploader.model.Smartphone;
import com.items.uploader.model.dto.SmartphoneDto;
import com.items.uploader.repository.SmartphoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmartphoneService {

    private final SmartphoneRepository smartphoneRepository;

    private final SmartphoneMapper smartphoneMapper;

    public Optional<Smartphone> findFirstByCodeOrderByCreatedAtDesc(String code) {
        return smartphoneRepository.findFirstByCodeOrderByCreatedAtDesc(code);
    }

    @Transactional
    public Smartphone save(Smartphone smartphone) {
        log.debug("Saving item {}", smartphone);
        return smartphoneRepository.save(smartphone);
    }

    @Transactional
    public Smartphone save(SmartphoneDto smartphoneDto) {
        Smartphone mappedSmartphone = smartphoneMapper.map(smartphoneDto);
        log.debug("Saving item {}", mappedSmartphone);
        return smartphoneRepository.save(mappedSmartphone);
    }
}
