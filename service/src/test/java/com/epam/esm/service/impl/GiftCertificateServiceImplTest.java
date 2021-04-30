package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    private static final GiftCertificate certificateInstance = new GiftCertificate(1L, "example",
            "description", new BigDecimal("100"), 7, LocalDateTime.now(), LocalDateTime.now());
    private static final List<Tag> tagList = Arrays.asList(new Tag(1L, "first"), new Tag(2L, "second"));
    private static final List<GiftCertificateDto> EMPTY_CERTIFICATE_DTO_WITH_TAGS_LIST = new ArrayList<>();
    private static final long CERTIFICATE_ID_VALUE = 1L;
    private static final String EMPTY_STRING = "";
    private static final String TAG_NAME = "tagName";
    private static final String PART_NAME_OR_DESCRIPTION = "partNameOrDescription";

    private static List<GiftCertificate> certificateList;
    private static List<Tag> tagsList;
    private static Set<Tag> tagsSet;
    private static List<GiftCertificateDto> certificateDtoWithTagsList;
    private static GiftCertificateDto giftCertificateDto;

    @Mock
    private GiftCertificateDaoImpl giftCertificateDao;
    @Mock
    private TagDaoImpl tagDao;
    @InjectMocks
    GiftCertificateServiceImpl giftCertificateService;

    @BeforeAll
    static void setUp() {
        initTagsList();
        initTagsSet();
        initCertificatesList();
        initGiftCertificateDto();
        initGiftCertificateDtoWithTagsList();
    }

    private static void initTagsList() {
        tagsList = Arrays.asList(new Tag(1L, "first"),
                new Tag(2L, "second"));
    }
    private static void initTagsSet() {
        tagsSet = new HashSet<>();
        tagsSet.add(new Tag(1L, "first"));
        tagsSet.add(new Tag(2L, "second"));
    }

    private static void initCertificatesList() {
        certificateList = Arrays.asList(new GiftCertificate(1L, "first", "description",
                        new BigDecimal("100"), 7, LocalDateTime.now(), LocalDateTime.now()),
                new GiftCertificate(2L, "second", "description",
                        new BigDecimal("200"), 9, LocalDateTime.now(), LocalDateTime.now()));
    }

    private static void initGiftCertificateDto() {
        giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setGiftCertificate(certificateInstance);
        giftCertificateDto.setTags(tagsList);
    }

    private static void initGiftCertificateDtoWithTagsList() {
        certificateDtoWithTagsList = certificateList.stream()
                .map(GiftCertificateDto::new)
                .collect(Collectors.toList());
        certificateDtoWithTagsList.forEach(c -> c.setTags(tagList));
    }

    @Test
    public void testGetAllShouldReturnGiftCertificateList() {
        when(giftCertificateDao.getAll()).thenReturn(certificateList);
        assertEquals(giftCertificateService.getAll(), certificateList);
        verify(giftCertificateDao).getAll();
        verifyNoMoreInteractions(giftCertificateDao);
        verifyNoInteractions(tagDao);
    }

    @Test
    public void testGetByIdShouldReturnGiftCertificate() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(certificateInstance);
        assertEquals(giftCertificateService.getById(anyLong()), certificateInstance);
        verify(giftCertificateDao).getById(anyLong());
        verifyNoMoreInteractions(giftCertificateDao);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testDelete() {
        giftCertificateService.delete(anyLong());
        verify(giftCertificateDao).delete(anyLong());
        verifyNoMoreInteractions(giftCertificateDao);
        verifyNoInteractions(tagDao);
    }

    @Test
    public void testSaveShouldReturnCertificateId() {
        when(tagDao.saveTags(anySet())).thenReturn(tagsSet);
        when(giftCertificateDao.save(any(GiftCertificate.class))).thenReturn(CERTIFICATE_ID_VALUE);
        assertEquals(giftCertificateService.save(giftCertificateDto), CERTIFICATE_ID_VALUE);
        verify(tagDao).saveTags(anySet());
        verify(giftCertificateDao).save(any(GiftCertificate.class));
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testFindShouldReturnEmptyListWhenParametersNotPassed() {
        assertEquals(giftCertificateService.findByParameters(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING), EMPTY_CERTIFICATE_DTO_WITH_TAGS_LIST);
        verifyNoInteractions(giftCertificateDao);
        verifyNoInteractions(tagDao);
    }

    @Test
    public void testFindShouldReturnListCertificateDtoWhenTagNamePassed() {
        when(giftCertificateDao.findByTagName(TAG_NAME, new ArrayList<>())).thenReturn(certificateList);
        when(tagDao.getAllGiftCertificateTags(any(GiftCertificate.class))).thenReturn(tagList);
        assertEquals(giftCertificateService.findByParameters(TAG_NAME, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING), certificateDtoWithTagsList);
        verifyNoMoreInteractions(giftCertificateDao);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testFindShouldReturnListCertificateDtoWhenPartNameOrDescriptionPassed() {
        when(giftCertificateDao.findByNameOrDescription(PART_NAME_OR_DESCRIPTION, new ArrayList<>())).thenReturn(certificateList);
        when(tagDao.getAllGiftCertificateTags(any(GiftCertificate.class))).thenReturn(tagList);
        assertEquals(giftCertificateService.findByParameters(EMPTY_STRING, PART_NAME_OR_DESCRIPTION, EMPTY_STRING, EMPTY_STRING), certificateDtoWithTagsList);
        verifyNoMoreInteractions(giftCertificateDao);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testFindShouldReturnListCertificateDtoWhenTagNameAndPartNameOrDescriptionPassed() {
        when(giftCertificateDao.findByTagNameOrNameOrDescription(TAG_NAME, PART_NAME_OR_DESCRIPTION, new ArrayList<>())).thenReturn(certificateList);
        when(tagDao.getAllGiftCertificateTags(any(GiftCertificate.class))).thenReturn(tagList);
        assertEquals(giftCertificateService.findByParameters(TAG_NAME, PART_NAME_OR_DESCRIPTION, EMPTY_STRING, EMPTY_STRING), certificateDtoWithTagsList);
        verifyNoMoreInteractions(giftCertificateDao);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void update() {
        when(tagDao.saveTags(anySet())).thenReturn(tagsSet);
        when(giftCertificateDao.save(certificateInstance)).thenReturn(CERTIFICATE_ID_VALUE);
        assertEquals(giftCertificateService.save(giftCertificateDto), CERTIFICATE_ID_VALUE);
        verifyNoMoreInteractions(tagDao);
    }
}
