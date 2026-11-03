package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.security.AccessChecker;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

/**
 * Объявления хранятся в таблице ads, права на изменение проверяет {@link AccessChecker}.
 */
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;
    private final AccessChecker accessChecker;
    private final ImageService imageService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Ads getAllAds() {
        return adMapper.toAds(adRepository.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, String email) {
        AdEntity ad = adMapper.toEntity(createOrUpdateAd);
        ad.setAuthor(userService.getUserEntity(email));
        ad.setImage(imageService.save(image));
        return adMapper.toAd(adRepository.save(ad));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAd(Integer id) {
        return adMapper.toExtendedAd(getAdEntity(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeAd(Integer id, Authentication authentication) {
        AdEntity ad = getAdEntity(id);
        accessChecker.checkCanModify(ad.getAuthor(), authentication);
        adRepository.delete(ad);
        imageService.delete(ad.getImage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Ad updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd, Authentication authentication) {
        AdEntity ad = getAdEntity(id);
        accessChecker.checkCanModify(ad.getAuthor(), authentication);
        adMapper.updateEntity(createOrUpdateAd, ad);
        return adMapper.toAd(adRepository.save(ad));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Ads getMyAds(String email) {
        return adMapper.toAds(adRepository.findAllByAuthorEmail(email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public byte[] updateImage(Integer id, MultipartFile image, Authentication authentication) {
        AdEntity ad = getAdEntity(id);
        accessChecker.checkCanModify(ad.getAuthor(), authentication);
        ad.setImage(imageService.replace(image, ad.getImage()));
        adRepository.save(ad);
        return imageService.load(ad.getImage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdEntity getAdEntity(Integer id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));
    }
}
