package ru.skypro.homework.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Маппинг объявления между сущностью и DTO.
 */
@Mapper(config = MapStructConfig.class)
public interface AdMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", qualifiedByName = "toImageUrl")
    Ad toAd(AdEntity entity);

    List<Ad> toAdList(List<AdEntity> entities);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", qualifiedByName = "toImageUrl")
    ExtendedAd toExtendedAd(AdEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(CreateOrUpdateAd createOrUpdateAd, @MappingTarget AdEntity entity);

    default Ads toAds(List<AdEntity> entities) {
        List<Ad> results = toAdList(entities);
        return new Ads(results.size(), results);
    }
}
