package com.epam.esm.service.dto;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Objects;

public class GiftCertificateDto {

    private GiftCertificate giftCertificate;
    private List<Tag> tags;

    public GiftCertificateDto(GiftCertificate giftCertificate, List<Tag> tags) {
        this.giftCertificate = giftCertificate;
        this.tags = tags;
    }

    public GiftCertificateDto(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public GiftCertificateDto() {
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiftCertificateDto that = (GiftCertificateDto) o;
        return Objects.equals(giftCertificate, that.giftCertificate) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giftCertificate, tags);
    }
}
