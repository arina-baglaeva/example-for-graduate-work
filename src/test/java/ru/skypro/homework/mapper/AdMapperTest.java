package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.skypro.homework.TestData;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {AdMapperImpl.class, ImageUrlMapper.class})
class AdMapperTest {

    @Autowired
    private AdMapper adMapper;

    @Test
    void toAdUsesAuthorIdAndImageUrl() {
        Ad ad = adMapper.toAd(TestData.ad(TestData.user()));

        assertThat(ad.getPk()).isEqualTo(10);
        assertThat(ad.getAuthor()).isEqualTo(1);
        assertThat(ad.getTitle()).isEqualTo("Bicycle");
        assertThat(ad.getPrice()).isEqualTo(5000);
        assertThat(ad.getImage()).isEqualTo("/images/ad.png");
    }

    @Test
    void toExtendedAdTakesContactsFromAuthor() {
        ExtendedAd extendedAd = adMapper.toExtendedAd(TestData.ad(TestData.user()));

        assertThat(extendedAd.getPk()).isEqualTo(10);
        assertThat(extendedAd.getAuthorFirstName()).isEqualTo("Ivan");
        assertThat(extendedAd.getAuthorLastName()).isEqualTo("Ivanov");
        assertThat(extendedAd.getEmail()).isEqualTo(TestData.USER_EMAIL);
        assertThat(extendedAd.getPhone()).isEqualTo("+7 (999) 123-45-67");
        assertThat(extendedAd.getDescription()).isEqualTo("Good old bicycle");
    }

    @Test
    void toAdsCountsResults() {
        AdEntity ad = TestData.ad(TestData.user());

        Ads ads = adMapper.toAds(List.of(ad, ad));

        assertThat(ads.getCount()).isEqualTo(2);
        assertThat(ads.getResults()).hasSize(2);
    }

    @Test
    void updateEntityKeepsAuthorAndImage() {
        AdEntity entity = TestData.ad(TestData.user());
        CreateOrUpdateAd update = new CreateOrUpdateAd();
        update.setTitle("Scooter");
        update.setPrice(100);

        adMapper.updateEntity(update, entity);

        assertThat(entity.getTitle()).isEqualTo("Scooter");
        assertThat(entity.getPrice()).isEqualTo(100);
        assertThat(entity.getDescription()).isEqualTo("Good old bicycle");
        assertThat(entity.getAuthor().getId()).isEqualTo(1);
        assertThat(entity.getImage()).isEqualTo("ad.png");
    }
}
