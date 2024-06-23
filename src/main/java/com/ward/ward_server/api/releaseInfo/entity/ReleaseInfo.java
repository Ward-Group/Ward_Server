package com.ward.ward_server.api.releaseInfo.entity;

import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import com.ward.ward_server.global.Object.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.Object.Constants.DATE_STRING_FORMAT;

@Entity
@Getter
@Table(name = "release_info")
@NoArgsConstructor
public class ReleaseInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Long drawPlatformId;

    private String siteUrl;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    private LocalDateTime dueDate;

    private LocalDateTime presentationDate;

    private Integer releasePrice;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)")
    private CurrencyUnit currencyUnit;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)", nullable = false)
    private NotificationMethod notificationMethod;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)", nullable = false)
    private ReleaseMethod releaseMethod;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)", nullable = false)
    private DeliveryMethod deliveryMethod;

    @Builder
    public ReleaseInfo(long itemId, long drawPlatformId, String siteUrl,
                       String releaseDate, String dueDate, String presentationDate, Integer releasePrice, CurrencyUnit currencyUnit,
                       NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        this.itemId = itemId;
        this.drawPlatformId = drawPlatformId;
        this.siteUrl = siteUrl;
        this.releaseDate = LocalDateTime.parse(releaseDate, DATE_STRING_FORMAT);
        this.dueDate = LocalDateTime.parse(dueDate, DATE_STRING_FORMAT);
        this.presentationDate = LocalDateTime.parse(presentationDate, DATE_STRING_FORMAT);
        this.releasePrice = releasePrice == null ? 0 : releasePrice;
        this.currencyUnit = currencyUnit == null ? CurrencyUnit.KRW : currencyUnit;
        this.notificationMethod = notificationMethod;
        this.releaseMethod = releaseMethod;
        this.deliveryMethod = deliveryMethod;
    }

    public void updateItemId(long itemId) {
        this.itemId = itemId;
    }

    public void updateDrawPlatformId(long drawPlatformId) {
        this.drawPlatformId = drawPlatformId;
    }

    public void updateSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void updateReleaseDate(String releaseDate) {
        this.releaseDate = LocalDateTime.parse(releaseDate, DATE_STRING_FORMAT);
    }

    public void updateDueDate(String dueDate) {
        this.dueDate = LocalDateTime.parse(dueDate, DATE_STRING_FORMAT);
    }

    public void updatePresentationDate(String presentationDate) {
        this.presentationDate = LocalDateTime.parse(presentationDate, DATE_STRING_FORMAT);
    }

    public void updateReleasePrice(Integer releasePrice) {
        this.releasePrice = releasePrice;
    }

    public void updateCurrencyUnit(CurrencyUnit currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public void updateNotificationMethod(NotificationMethod notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    public void updateReleaseMethod(ReleaseMethod releaseMethod) {
        this.releaseMethod = releaseMethod;
    }

    public void updateDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getReleaseFormatDate() {
        return releaseDate.format(DATE_STRING_FORMAT);
    }

    public String getDueFormatDate() {
        return dueDate.format(DATE_STRING_FORMAT);
    }

    public String getPresentationFormatDate() {
        return presentationDate.format(DATE_STRING_FORMAT);
    }

    public LocalDateTime getReleaseLocalDate() {
        return releaseDate;
    }

    public LocalDateTime getDueLocalDate() {
        return dueDate;
    }

    public LocalDateTime getPresentationLocalDate() {
        return presentationDate;
    }
}
