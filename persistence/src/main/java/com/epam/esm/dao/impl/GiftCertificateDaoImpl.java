package com.epam.esm.dao.impl;

import com.epam.esm.dao.exception.EntityNotFoundException;
import com.epam.esm.dao.exception.TypeErrorCode;
import com.epam.esm.dao.utils.DaoUtils;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static String GET_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
    private final static String GET_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificate WHERE id= :id";
    private final static String GET_CERTIFICATE_BY_NAME = "SELECT * FROM gift_certificate WHERE name= :name";
    private final static String DELETE_GIFT_CERTIFICATE = "DELETE FROM gift_certificate WHERE id= :id";
    private final static String SAVE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name,description,price,duration,create_date,last_update_date) " +
            "values (:name,:description,:price,:duration,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
    private final static String UPDATE_CERTIFICATE = "UPDATE gift_certificate " +
            "SET %s, last_update_date = CURRENT_TIMESTAMP()" +
            "WHERE id = :id;";
    private final static String DELETE_REFERENCES_BETWEEN_CERTIFICATES_AND_TAGS = "DELETE FROM gift_certificate_tag " +
            "WHERE gift_certificate_id= :id;";
    static String GET_CERTIFICATES_BY_TAG_NAME =
            "SELECT gc.id, gc.name, gc.description, gc.price, " +
                    "gc.create_date, gc.last_update_date, gc.duration " +
                    "FROM gift_certificate gc " +
                    "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
                    "JOIN tag ON tag.id = gct.tag_id " +
                    "WHERE tag.name= :param " +
                    "%s;";
    static String GET_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION =
            "SELECT * FROM gift_certificate gc WHERE name LIKE :param " +
                    "OR description LIKE :param " +
                    "%s;";
    static String GET_CERTIFICATE_BY_TAG_NAME_AND_PART_OF_NAME_OR_DESCRIPTION =
            "SELECT DISTINCT gc.id, gc.name, gc.description, gc.price, " +
                    "gc.create_date, gc.last_update_date, gc.duration " +
                    "FROM gift_certificate gc " +
                    "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
                    "JOIN tag ON tag.id = gct.tag_id " +
                    "WHERE tag.name= :param " +
                    "OR gc.name LIKE :text " +
                    "OR gc.description LIKE :text " +
                    "%s";

    private static final RowMapper<GiftCertificate> mapper = ((rs, rowNum) -> new GiftCertificate(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getInt("duration"),
            rs.getTimestamp("create_date").toLocalDateTime(),
            rs.getTimestamp("last_update_date").toLocalDateTime()));

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public GiftCertificateDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, SimpleJdbcInsert simpleJdbcInsert) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return namedParameterJdbcTemplate.query(GET_ALL_CERTIFICATES, mapper);
    }

    @Override
    public GiftCertificate getById(Long id) {
        try {
            return namedParameterJdbcTemplate.queryForObject(GET_CERTIFICATE_BY_ID, Collections.singletonMap("id", id), mapper);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(TypeErrorCode.CERTIFICATE_NOT_FOUND.getErrorCode());
        }
    }

    @Override
    public List<GiftCertificate> getByName(String name) {
            return namedParameterJdbcTemplate.query(GET_CERTIFICATE_BY_NAME, Collections.singletonMap("name", name), mapper);
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName, List<String> sortTypes) {
        return namedParameterJdbcTemplate.query(insertSqlQuerySubString(defineSortType(sortTypes),
                GET_CERTIFICATES_BY_TAG_NAME),
                getParameterMap(tagName, null), mapper);
    }

    private String defineSortType(List<String> sortTypes) {
        return isSortTypeExists(sortTypes) ? getTableFieldsForSorting(sortTypes) : "";
    }

    private boolean isSortTypeExists(List<String> sortTypes) {
        return CollectionUtils.isNotEmpty(sortTypes);
    }

    private String getTableFieldsForSorting(List<String> sortTypes) {
        return isSortTypeExists(sortTypes) ? produceParams(sortTypes) : "";
    }

    private String produceParams(List<String> params) {
        return "ORDER BY " + String.join(", ", params);
    }

    private Map<String, String> getParameterMap(String param, @Nullable String text) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param", param);
        parameters.put("text", text);
        return parameters;
    }

    @Override
    public List<GiftCertificate> findByNameOrDescription(String partNameOrDescription, List<String> sortTypes) {
        return namedParameterJdbcTemplate.query(insertSqlQuerySubString(defineSortType(sortTypes),
                GET_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION),
                getParameterMap(prepareParameterForInsertingToSqlScript(partNameOrDescription), null),
                mapper);
    }

    private String prepareParameterForInsertingToSqlScript(String partNameOrDescription) {
        return "%" + partNameOrDescription + "%";
    }

    @Override
    public List<GiftCertificate> findByTagNameOrNameOrDescription(String tagName, String nameOrDescription, List<String> sortTypes) {
        return namedParameterJdbcTemplate.query(
                insertSqlQuerySubString(defineSortType(sortTypes),
                        GET_CERTIFICATE_BY_TAG_NAME_AND_PART_OF_NAME_OR_DESCRIPTION),
                getParameterMap(tagName, prepareParameterForInsertingToSqlScript(nameOrDescription)), mapper);
    }

    @Override
    public Long save(GiftCertificate giftCertificate) {
        DaoUtils.validateEmptyCertificateName(giftCertificate.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SAVE_GIFT_CERTIFICATE, new BeanPropertySqlParameterSource(giftCertificate), keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void saveReferencesBetweenCertificateAndTags(Long certificateId, Set<Tag> tags) {
        tags.forEach(
                tag -> {
                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("tag_id", tag.getId());
                    parameter.put("gift_certificate_id", certificateId);
                    simpleJdbcInsert.execute(parameter);
                });
    }

    @Override
    public Long update(Map<String, Object> parameters) {
        DaoUtils.validateEmptyCertificateName(parameters);
        long certificateId = (long) parameters.get("id");
        String fields = createPartSqlForUpdate(parameters.keySet());
        namedParameterJdbcTemplate.update(insertSqlQuerySubString(fields, UPDATE_CERTIFICATE), parameters);
        return certificateId;
    }


    private String createPartSqlForUpdate(Set<String> fields) {
        StringBuilder stringBuilder = new StringBuilder();
        fields.stream()
                .filter(field -> !field.equals("id"))
                .forEach(field -> stringBuilder
                        .append(field)
                        .append("=")
                        .append(":")
                        .append(field)
                        .append(" "));
        return String.join(", ", stringBuilder.toString().split(" "));
    }

    private String insertSqlQuerySubString(String subString, String sqlQuery) {
        return String.format(sqlQuery, subString);
    }

    @Override
    public void updateReferencesBetweenCertificateAndTags(Long certificateId, Set<Tag> tags) {
        namedParameterJdbcTemplate.update(DELETE_REFERENCES_BETWEEN_CERTIFICATES_AND_TAGS, Collections.singletonMap("id", certificateId));
        saveReferencesBetweenCertificateAndTags(certificateId, tags);
    }

    @Override
    public void delete(Long id) {
        namedParameterJdbcTemplate.update(DELETE_GIFT_CERTIFICATE, Collections.singletonMap("id", id));
    }
}
