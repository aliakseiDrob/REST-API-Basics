package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.SortType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return giftCertificateDao.getAll();
    }

    @Override
    public GiftCertificate getById(Long id) {
        return giftCertificateDao.getById(id);
    }

    @Override
    public void delete(Long id) {
        giftCertificateDao.delete(id);
    }

    @Override
    @Transactional
    public Long save(GiftCertificateDto dto) {
        GiftCertificate giftCertificate = dto.getGiftCertificate();
        Set<Tag> tagsWithId = tagDao.saveTags(Set.copyOf(dto.getTags()));
        Long certificateId = giftCertificateDao.save(giftCertificate);
        giftCertificateDao.saveReferencesBetweenCertificateAndTags(certificateId, tagsWithId);
        return certificateId;
    }


    @Override
    public List<GiftCertificateDto> findByParameters(String tagName,
                                                     String partOfNameOrDesc,
                                                     String nameSort,
                                                     String dateSort) {
        List<String> fieldsForSearch = extractFieldsForSearch(nameSort, dateSort);
        List<GiftCertificateDto> certificates = getCertificatesDto(getCertificates(fieldsForSearch,
                tagName, partOfNameOrDesc));
        getAllCertificatesTags(certificates);
        return certificates;
    }

    private List<String> extractFieldsForSearch(String nameSort, String dateSort) {
        List<String> fieldsList = new ArrayList<>();
        fieldsList.add(isSortTypeValid(nameSort) ? "name " + nameSort : null);
        fieldsList.add(isSortTypeValid(dateSort) ? "create_date " + dateSort : null);
        return fieldsList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean isSortTypeValid(String sortType) {
        return getSortTypes().contains(sortType.trim().toUpperCase());
    }

    private List<String> getSortTypes() {
        return Stream.of(SortType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private List<GiftCertificateDto> getCertificatesDto(List<GiftCertificate> certificates) {
        return certificates.stream()
                .map(GiftCertificateDto::new)
                .collect(Collectors.toList());
    }

    private List<GiftCertificate> getCertificates(List<String> sortTypes,
                                                  String tagName,
                                                  String partOfName) {
        List<GiftCertificate> certificateList = new ArrayList<>();
        if (isParameterPassed(tagName) && isParameterPassed(partOfName)) {
            certificateList = giftCertificateDao.findByTagNameOrNameOrDescription(tagName, partOfName, sortTypes);
        } else {
            if (isParameterPassed(tagName)) {
                certificateList = giftCertificateDao.findByTagName(tagName, sortTypes);
            }
            if (isParameterPassed(partOfName)) {
                certificateList = giftCertificateDao.findByNameOrDescription(partOfName, sortTypes);
            }
        }
        return certificateList;
    }

    private boolean isParameterPassed(String param) {
        return StringUtils.isNoneBlank(param);
    }

    private void getAllCertificatesTags(List<GiftCertificateDto> certificates) {
        certificates.forEach(c -> c.setTags(tagDao.getAllGiftCertificateTags(c.getGiftCertificate())));
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificateDto giftCertificateDto) {
        Set<Tag> allTags = Set.copyOf(giftCertificateDto.getTags());
        Map<String, Object> fieldsMap = extractFieldsForUpdate(giftCertificateDto.getGiftCertificate());
        Set<Tag> tagsWithId = tagDao.saveTags(allTags);
        Long certificateId = giftCertificateDao.update(fieldsMap);
        giftCertificateDao.updateReferencesBetweenCertificateAndTags(certificateId, tagsWithId);
        return giftCertificateDao.getById(certificateId);
    }

    private Map<String, Object> extractFieldsForUpdate(GiftCertificate certificate) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("id", certificate.getId());
        fields.put("name", certificate.getName());
        fields.put("description", certificate.getDescription());
        fields.put("price", certificate.getPrice());
        fields.put("duration", certificate.getDuration());
        fields.values().removeIf(Objects::isNull);
        fields.values().removeIf(field -> field.equals(0));
        return fields;
    }
}