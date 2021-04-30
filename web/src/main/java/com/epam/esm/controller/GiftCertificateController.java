package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller provides CRUD operations on GiftCertificate entity.
 */
@RestController
public class GiftCertificateController {

    private final GiftCertificateService service;

    @Autowired
    public GiftCertificateController(GiftCertificateService service) {
        this.service = service;
    }

    /**
     * Finds all GiftCertificates
     *
     * @return List of GiftCertificates
     */
    @GetMapping("/giftCertificates")
    public List<GiftCertificate> getAll() {
        return service.getAll();
    }

    /**
     * Finds GiftCertificate by id
     *
     * @param id the id of GiftCertificate
     * @return the GiftCertificate entity
     */
    @GetMapping("/giftCertificates/{id}")
    public GiftCertificate getGiftCertificate(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Saves  GiftCertificate
     *
     * @param dto GiftCertificateDto entity contains GiftCertificate and list of tags
     * @return GiftCertificate id
     */
    @PostMapping("giftCertificates")
    public Long save(@RequestBody GiftCertificateDto dto) {
        return service.save(dto);
    }

    /**
     * Finds all  GiftCertificates by part name or description or Tag name
     *
     * @param tagName        Tag name
     * @param partNameOrDesc part name or description
     * @param nameSort       is used to sort the list in ascending or descending order
     *                       by name, can be empty or ASC or DESC
     * @param dateSort       is used to sort the list in ascending or descending order
     *                       by name, can be empty or ASC or DESC
     * @return list of GiftCertificatesDto
     */
    @GetMapping("/giftCertificates/search")
    public List<GiftCertificateDto>
    find(@RequestParam(required = false, defaultValue = "") String tagName,
         @RequestParam(required = false, defaultValue = "") String partNameOrDesc,
         @RequestParam(required = false, defaultValue = "") String nameSort,
         @RequestParam(required = false, defaultValue = "") String dateSort) {
        return service.findByParameters(tagName, partNameOrDesc, nameSort, dateSort);
    }

    /**
     * Updates  GiftCertificate
     *
     * @param dto GiftCertificateDto entity contains GiftCertificate and list of tags
     * @return GiftCertificate
     */
    @PatchMapping("/giftCertificates")
    public GiftCertificate update(@RequestBody GiftCertificateDto dto) {
        return service.update(dto);
    }

    /**
     * Removes  GiftCertificate
     *
     * @param id GiftCertificate id
     */
    @DeleteMapping("/giftCertificates/{id}")
    public void deleteGiftCertificate(@PathVariable Long id) {
        service.delete(id);
    }
}