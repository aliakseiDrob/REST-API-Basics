package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for managing GiftCertificate entities
 */
public interface GiftCertificateDao {

    /**
     * Finds all GiftCertificates  from database
     *
     * @return list of GiftCertificate from database
     */
    List<GiftCertificate> getAll();

    /**
     * Finds GiftCertificate from database by id
     *
     * @param id GiftCertificate id
     * @return GiftCertificate entity
     */
    GiftCertificate getById(Long id);

    /**
     * Finds GiftCertificate from database by name
     *
     * @param name GiftCertificate name
     * @return list of GiftCertificates
     */
    List<GiftCertificate> getByName(String name);

    /**
     * Finds all  records GiftCertificate from database by Tag name
     *
     * @param tagName   Tag name
     * @param sortTypes is used to sort the result-set in ascending or descending order
     *                  by name and create date, can be empty
     * @return list of GiftCertificates from database
     */
    List<GiftCertificate> findByTagName(String tagName, List<String> sortTypes);

    /**
     * Finds all  records GiftCertificate from database by part name or description
     *
     * @param partNameOrDescription part name or description
     * @param sortTypes             is used to sort the result-set in ascending or descending order
     *                              by name and create date, can be empty
     * @return list of GiftCertificate from database
     */
    List<GiftCertificate> findByNameOrDescription(String partNameOrDescription, List<String> sortTypes);

    /**
     * Finds all  records GiftCertificate from database by part name or description or Tag name
     *
     * @param tagName           Tag name
     * @param nameOrDescription part name or description
     * @param sortTypes         is used to sort the result-set in ascending or descending order
     *                          by name and create date, can be empty
     * @return list of GiftCertificate from database
     */
    List<GiftCertificate> findByTagNameOrNameOrDescription(String tagName, String nameOrDescription, List<String> sortTypes);

    /**
     * Saves  GiftCertificate in database
     *
     * @param giftCertificate GiftCertificate entity
     * @return GiftCertificate id
     */
    Long save(GiftCertificate giftCertificate);

    /**
     * Saves  references between GiftCertificate and Tags
     *
     * @param certificateId id of GiftCertificate entity
     * @param tags          set of Tags
     */
    void saveReferencesBetweenCertificateAndTags(Long certificateId, Set<Tag> tags);

    /**
     * Updates  GiftCertificate in database
     *
     * @param parameters parameters of GiftCertificate entity for update
     * @return GiftCertificate id
     */

    Long update(Map<String, Object> parameters);

    /**
     * Updates  references between GiftCertificate and Tags
     *
     * @param certificateId id of GiftCertificate entity
     * @param tags          set of Tags
     */
    void updateReferencesBetweenCertificateAndTags(Long certificateId, Set<Tag> tags);

    /**
     * Removes  GiftCertificate from database
     *
     * @param id GiftCertificate id
     */

    void delete(Long id);

}
