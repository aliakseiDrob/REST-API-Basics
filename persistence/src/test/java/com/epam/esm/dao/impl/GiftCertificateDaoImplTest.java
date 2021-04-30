package com.epam.esm.dao.impl;

import com.epam.esm.config.SpringJdbcConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.config.SpringJdbcConfigTest;
import com.epam.esm.dao.exception.EntityNotFoundException;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringJUnitWebConfig(SpringJdbcConfig.class)
@ContextConfiguration(
        classes = {SpringJdbcConfigTest.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class GiftCertificateDaoImplTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final GiftCertificate CERTIFICATE_INSTANCE = new GiftCertificate(1L, "first", "for men",
            new BigDecimal("128.01"), 11,
            LocalDateTime.parse("2021-03-21 20:11:10", DATE_TIME_FORMATTER),
            LocalDateTime.parse("2021-03-24 20:11:10", DATE_TIME_FORMATTER));

    private static final GiftCertificate CERTIFICATE_FOR_SAVE = new GiftCertificate(5L, "fourth", "for men",
            new BigDecimal("49.20"), 6,
            LocalDateTime.parse("2021-03-29 20:11:10", DATE_TIME_FORMATTER),
            LocalDateTime.parse("2021-03-29 20:11:10", DATE_TIME_FORMATTER));

    private static List<GiftCertificate> expectedCertificateListGetAll;
    private static List<GiftCertificate> expectedCertificateListGetByName;
    private static List<GiftCertificate> expectedCertificateListFindByTagOrDescriptionOrName;
    private static List<GiftCertificate> expectedCertificateListFindByTagNameOrNameOrDescriptionSortByNameAscByDateDesc;


    private static void initCertificateListGetAll() {
        expectedCertificateListGetAll = Arrays.asList(
                new GiftCertificate(1L, "first", "for men", new BigDecimal("128.01"), 11,
                        LocalDateTime.parse("2021-03-21 20:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-24 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(2L, "second", "children", new BigDecimal("250.20"), 7,
                        LocalDateTime.parse("2021-03-06 20:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-11 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(3L, "third", "everybody", new BigDecimal("48.50"), 3,
                        LocalDateTime.parse("2021-03-26 19:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-28 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(4L, "first", "children", new BigDecimal("48.50"), 3,
                        LocalDateTime.parse("2021-03-20 19:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-28 20:11:10", DATE_TIME_FORMATTER)));
    }

    private static void initCertificateListGetByName() {
        expectedCertificateListGetByName = Arrays.asList(
                new GiftCertificate(1L, "first", "for men", new BigDecimal("128.01"), 11,
                        LocalDateTime.parse("2021-03-21 20:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-24 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(4L, "first", "children", new BigDecimal("48.50"), 3,
                        LocalDateTime.parse("2021-03-20 19:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-28 20:11:10", DATE_TIME_FORMATTER)));
    }

    private static void initCertificateListFindByTagOrDescriptionOrName() {
        expectedCertificateListFindByTagOrDescriptionOrName = Arrays.asList(
                new GiftCertificate(1L, "first", "for men", new BigDecimal("128.01"), 11,
                        LocalDateTime.parse("2021-03-21 20:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-24 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(2L, "second", "children", new BigDecimal("250.20"), 7,
                        LocalDateTime.parse("2021-03-06 20:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-11 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(4L, "first", "children", new BigDecimal("48.50"), 3,
                        LocalDateTime.parse("2021-03-20 19:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-28 20:11:10", DATE_TIME_FORMATTER)));
    }

    private static void initCertificateListFindByTagNameOrNameOrDescriptionSortByNameAscByDateDesc() {
        expectedCertificateListFindByTagNameOrNameOrDescriptionSortByNameAscByDateDesc = Arrays.asList(
                new GiftCertificate(1L, "first", "for men", new BigDecimal("128.01"), 11,
                        LocalDateTime.parse("2021-03-21 20:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-24 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(4L, "first", "children", new BigDecimal("48.50"), 3,
                        LocalDateTime.parse("2021-03-20 19:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-28 20:11:10", DATE_TIME_FORMATTER)),
                new GiftCertificate(2L, "second", "children", new BigDecimal("250.20"), 7,
                        LocalDateTime.parse("2021-03-06 20:11:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("2021-03-11 20:11:10", DATE_TIME_FORMATTER)));
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GiftCertificateDao dao;

    @BeforeAll
    static void initAllFields() {
        initCertificateListGetAll();
        initCertificateListFindByTagOrDescriptionOrName();
        initCertificateListGetByName();
        initCertificateListFindByTagNameOrNameOrDescriptionSortByNameAscByDateDesc();
    }

    @Test
    @Rollback
    void testGetAllShouldReturnListCertificates() {
        assertEquals(dao.getAll(), expectedCertificateListGetAll);
    }

    @Test
    @Rollback
    public void testGetByIdShouldReturnCertificate() {
        assertEquals(dao.getById(1L), CERTIFICATE_INSTANCE);
    }

    @Test
    @Rollback
    public void testGetByIdShouldThrowExceptionWhenCertificateNotExist() {
        assertThrows(EntityNotFoundException.class, () -> dao.getById(7L));
    }

    @Test
    @Rollback
    public void testGetByNameShouldReturnCertificate() {
        assertEquals(dao.getByName("first"), expectedCertificateListGetByName);
    }

    @Test
    @Rollback
    public void testFindByTagNameShouldReturnListCertificates() {
        assertEquals(dao.findByTagName("first", new ArrayList<>()), expectedCertificateListFindByTagOrDescriptionOrName);
    }

    @Test
    @Rollback
    public void testFindByNameOrDescriptionShouldReturnListCertificates() {
        assertEquals(dao.findByNameOrDescription("en", new ArrayList<>()), expectedCertificateListFindByTagOrDescriptionOrName);
    }

    @Test
    @Rollback
    public void testFindByTagNameOrNameOrDescriptionShouldReturnListCertificates() {
        assertEquals(dao.findByTagNameOrNameOrDescription("first", "en", new ArrayList<>()),
                expectedCertificateListFindByTagOrDescriptionOrName);
    }

    @Test
    @Rollback
    public void testFindByTagNameOrNameOrDescriptionSortByNameAscByDateDescShouldReturnListCertificates() {
        assertEquals(dao.findByTagNameOrNameOrDescription("first", "en",
                new ArrayList<>(Arrays.asList("name ASC", "create_date DESC"))),
                expectedCertificateListFindByTagNameOrNameOrDescriptionSortByNameAscByDateDesc);
    }

    @Test
    @Rollback
    public void testSaveShouldSaveCertificateInDb() {
        Long savedCertificateId = dao.save(CERTIFICATE_FOR_SAVE);
        GiftCertificate savedCertificate = dao.getById(savedCertificateId);
        assertEquals(savedCertificate.getName(), CERTIFICATE_FOR_SAVE.getName());
        assertEquals(savedCertificate.getDescription(), CERTIFICATE_FOR_SAVE.getDescription());
        assertEquals(savedCertificate.getPrice(), CERTIFICATE_FOR_SAVE.getPrice());
        assertEquals(savedCertificate.getDuration(), CERTIFICATE_FOR_SAVE.getDuration());

    }

    @Test
    @Rollback
    public void testDeleteShouldDeleteCertificateFromDb() {
        dao.delete(3L);
        assertEquals(dao.getAll(), expectedCertificateListFindByTagOrDescriptionOrName);
    }
}