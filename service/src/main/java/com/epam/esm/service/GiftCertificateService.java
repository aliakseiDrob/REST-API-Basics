package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Interface for serving GiftCertificate objects according to the business logic of GiftCertificate
 */
public interface GiftCertificateService {

    /**
     * Returns all GiftCertificates
     *
     * @return list of GiftCertificates
     */
    List<GiftCertificate> getAll();

    /**
     * Returns GiftCertificate by id
     *
     * @param id GiftCertificate id
     * @return GiftCertificate object
     */
    GiftCertificate getById(Long id);

    /**
     * Removes  GiftCertificate
     *
     * @param id GiftCertificate id
     */
    void delete(Long id);

    /**
     * Saves  GiftCertificate
     *
     * @param dto GiftCertificateDto entity contains GiftCertificate and list of tags
     * @return GiftCertificate id
     */
    Long save(GiftCertificateDto dto);

    /**
     * Finds all  GiftCertificates by part name or description or Tag name
     *
     * @param tagName          Tag name
     * @param partOfNameOrDesc part name or description
     * @param nameSort         is used to sort the result-set in ascending or descending order
     *                         by name, can be empty or ASC or DESC
     * @param dateSort         is used to sort the result-set in ascending or descending order
     *                         by name, can be empty or ASC or DESC
     * @return list of GiftCertificatesDto
     */
    List<GiftCertificateDto> findByParameters(@Nullable String tagName,
                                              @Nullable String partOfNameOrDesc,
                                              @Nullable String nameSort,
                                              @Nullable String dateSort);

    /**
     * Updates  GiftCertificate in database
     *
     * @param dto GiftCertificateDto entity contains GiftCertificate and list of tags
     * @return GiftCertificate
     */
    GiftCertificate update(GiftCertificateDto dto);
}
